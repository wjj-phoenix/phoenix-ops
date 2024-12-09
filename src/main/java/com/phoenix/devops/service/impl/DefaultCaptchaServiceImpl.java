package com.phoenix.devops.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.phoenix.devops.fastmap.IFastMap;
import com.phoenix.devops.model.common.RepCodeEnum;
import com.phoenix.devops.model.common.ResponseModel;
import com.phoenix.devops.model.vo.CaptchaVO;
import com.phoenix.devops.model.vo.PointVO;
import com.phoenix.devops.service.ISysCaptchaService;
import com.phoenix.devops.utils.AESUtil;
import com.phoenix.devops.utils.ImageUtils;
import com.phoenix.devops.utils.JsonUtil;
import com.phoenix.devops.utils.RandomUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * @author wjj-phoenix
 */
@Slf4j
@Service
public class DefaultCaptchaServiceImpl implements ISysCaptchaService {
    @Resource
    private IFastMap<String, String> fastMap;
    protected static final String IMAGE_TYPE_PNG = "png";

    protected static int HAN_ZI_SIZE = 25;
    // check校验坐标
    protected static String REDIS_CAPTCHA_KEY = "RUNNING:CAPTCHA:%s";

    // 后台二次校验坐标
    protected static String REDIS_SECOND_CAPTCHA_KEY = "RUNNING:CAPTCHA:second-%s";

    protected static String waterMark = "PHOENIX";

    /**
     * 水印字体
     */
    protected Font waterMarkFont;

    protected static String slipOffset = "5";

    protected static Boolean captchaAesStatus = true;

    protected static int captchaInterferenceOptions = 1;

    @Override
    public CaptchaVO get() {
        // 原生图片
        BufferedImage originalImage = ImageUtils.fetchOriginalImage();

        Assert.notNull(originalImage, "原生图片未初始化成功，请检查路径");
        // 设置水印
        Graphics backgroundGraphics = originalImage.getGraphics();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        backgroundGraphics.setFont(waterMarkFont);
        backgroundGraphics.setColor(Color.white);
        backgroundGraphics.drawString(waterMark, width - getEnOrChLength(waterMark), height - (HAN_ZI_SIZE / 2) + 7);

        // 抠图图片
        String slidingBlockImageBase64 = ImageUtils.fetchSlidingBlockImage();
        BufferedImage slidingBlockImage = ImageUtils.base64StrToImage(slidingBlockImageBase64);
        Assert.notNull(slidingBlockImage, "滑动底图未初始化成功，请检查路径");

        return pictureTemplatesCut(originalImage, slidingBlockImage, slidingBlockImageBase64);
    }

    @Override
    public CaptchaVO check(CaptchaVO captchaVO) {
        if (StrUtil.isEmpty(captchaVO.getCaptchaType())) {
            // return RepCodeEnum.NULL_ERROR.parseError("类型");
            throw new IllegalArgumentException();
        }
        if (StrUtil.isEmpty(captchaVO.getToken())) {
            // return RepCodeEnum.NULL_ERROR.parseError("token");
            throw new IllegalArgumentException();
        }

        // 取坐标信息
        String codeKey = String.format(REDIS_CAPTCHA_KEY, captchaVO.getToken());
        String pointStr = fastMap.get(codeKey);
        if (StrUtil.isBlank(pointStr)) {
            // return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_INVALID);
            throw new IllegalArgumentException();
        }

        String pointJson;
        PointVO point = JsonUtil.parseObject(pointStr, PointVO.class);
        Assert.notNull(point, "坐标信息为空");
        try {
            // aes解密
            pointJson = decrypt(captchaVO.getPointJson(), point.getSecretKey());
        } catch (Exception e) {
            log.error("验证码坐标解析失败", e);
            // return ResponseModel.errorMsg(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
        PointVO point1 = JsonUtil.parseObject(pointJson, PointVO.class);
        Assert.notNull(point1, "坐标信息为空");
        boolean exists = point.x - Integer.parseInt(slipOffset) > point1.x || point1.x > point.x + Integer.parseInt(slipOffset) || point.y != point1.y;
        if (exists) {
            // return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR);
            throw new IllegalArgumentException(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getDesc());
        }
        // 校验成功，将信息存入缓存
        String secretKey = point.getSecretKey();
        String value;
        try {
            value = AESUtil.aesEncrypt(captchaVO.getToken().concat("---").concat(pointJson), secretKey);
        } catch (Exception e) {
            log.error("AES加密失败", e);
            throw new IllegalArgumentException(e.getMessage());
        }
        String secondKey = String.format(REDIS_SECOND_CAPTCHA_KEY, value);
        log.info("存入缓存 key：{}", secondKey);
        log.info("存入缓存 token：{}", captchaVO.getToken());
        // CaptchaServiceFactory.getCache(cacheType).set(secondKey, captchaVO.getToken(), EXPIRESIN_THREE);
        captchaVO.setResult(true);
        captchaVO.resetClientFlag();
        return captchaVO;
    }

    @Override
    public ResponseModel verification(CaptchaVO captchaVO) {
        if (captchaVO == null) {
            return RepCodeEnum.NULL_ERROR.parseError("captchaVO");
        }
        if (StrUtil.isEmpty(captchaVO.getCaptchaVerification())) {
            return RepCodeEnum.NULL_ERROR.parseError("二次校验参数");
        }
        try {
            String codeKey = String.format(REDIS_SECOND_CAPTCHA_KEY, captchaVO.getCaptchaVerification());
        } catch (Exception e) {
            log.error("验证码坐标解析失败", e);
            return ResponseModel.errorMsg(e.getMessage());
        }
        return ResponseModel.success();
    }

    protected void afterValidateFail(CaptchaVO data) {

    }

    /**
     * 解密前端坐标aes加密
     *
     * @param point 前端坐标
     * @param key   key
     * @return 前端坐标aes加密
     * @throws Exception E
     */
    public static String decrypt(String point, String key) throws Exception {
        return AESUtil.aesDecrypt(point, key);
    }

    /**
     * 根据模板切图
     *
     * @param originalImage           originalImage
     * @param slidingBlockImage       slidingBlockImage
     * @param slidingBlockImageBase64 jigsawImageBase64
     * @return CaptchaVO
     */
    public CaptchaVO pictureTemplatesCut(BufferedImage originalImage, BufferedImage slidingBlockImage, String slidingBlockImageBase64) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        int slidingBlockWidth = slidingBlockImage.getWidth();
        int slidingBlockHeight = slidingBlockImage.getHeight();

        // 随机生成拼图坐标
        PointVO point = generateSlidingBlockPoint(originalWidth, originalHeight, slidingBlockWidth, slidingBlockHeight);

        // 生成新的拼图图像
        BufferedImage newSlidingBlockImage = new BufferedImage(slidingBlockWidth, slidingBlockHeight, slidingBlockImage.getType());
        Graphics2D graphics = newSlidingBlockImage.createGraphics();

        // 如果需要生成RGB格式，需要做如下配置, Transparency 设置透明
        newSlidingBlockImage = graphics.getDeviceConfiguration().createCompatibleImage(slidingBlockWidth, slidingBlockHeight, Transparency.TRANSLUCENT);

        int x = point.getX();
        // 新建的图像根据模板颜色赋值,源图生成遮罩
        cutByTemplate(originalImage, slidingBlockImage, newSlidingBlockImage, x);
        if (captchaInterferenceOptions > 0) {
            int position;

            if (originalWidth - x - 5 > slidingBlockWidth * 2) {
                // 在原扣图右边插入干扰图
                position = RandomUtils.randomInt(x + slidingBlockWidth + 5, originalWidth - slidingBlockWidth);
            } else {
                // 在原扣图左边插入干扰图
                position = RandomUtils.randomInt(100, x - slidingBlockWidth - 5);
            }
            while (true) {
                String s = ImageUtils.fetchSlidingBlockImage();
                if (!slidingBlockImageBase64.equals(s)) {
                    interferenceByTemplate(originalImage, Objects.requireNonNull(ImageUtils.base64StrToImage(s)), position);
                    break;
                }
            }
        }
        if (captchaInterferenceOptions > 1) {
            while (true) {
                String s = ImageUtils.fetchSlidingBlockImage();
                if (!slidingBlockImageBase64.equals(s)) {
                    Integer randomInt = RandomUtils.randomInt(slidingBlockWidth, 100 - slidingBlockWidth);
                    interferenceByTemplate(originalImage, Objects.requireNonNull(ImageUtils.base64StrToImage(s)), randomInt);
                    break;
                }
            }
        }

        int bold = 5;
        // 设置“抗锯齿”的属性
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(new BasicStroke(bold, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        graphics.drawImage(newSlidingBlockImage, 0, 0, null);
        graphics.dispose();

        CaptchaVO dataVO = new CaptchaVO();
        try {
            // 新建流
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            // 利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流
            ImageIO.write(newSlidingBlockImage, IMAGE_TYPE_PNG, os);
            byte[] slidingBlockImages = os.toByteArray();

            // 新建流
            ByteArrayOutputStream oriImagesOs = new ByteArrayOutputStream();
            // 利用ImageIO类提供的write方法，将bi以jpg图片的数据模式写入流
            ImageIO.write(originalImage, IMAGE_TYPE_PNG, oriImagesOs);
            byte[] oriCopyImages = oriImagesOs.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            dataVO.setOriginalImageBase64(encoder.encodeToString(oriCopyImages).replaceAll("[\n" + "]", ""));
            // point信息不传到前端，只做后端check校验
            // dataVO.setPoint(point);
            dataVO.setJigsawImageBase64(encoder.encodeToString(slidingBlockImages).replaceAll("[\n" + "]", ""));
            dataVO.setToken(UUID.randomUUID().toString().replace("-", ""));
            dataVO.setSecretKey(point.getSecretKey());

            // 将坐标信息存入redis中
            String codeKey = String.format(REDIS_CAPTCHA_KEY, dataVO.getToken());
            log.info("codeKey: {}", codeKey);
            log.info("point: {}", JsonUtil.toJSONString(point));
            fastMap.put(codeKey, JSON.toJSONString(point));
            fastMap.expire(codeKey, 60 * 1_000L, (key, val) -> fastMap.remove(key));
            // CaptchaServiceFactory.getCache(cacheType).set(codeKey, JsonUtil.toJSONString(point), EXPIRESIN_SECONDS);
            log.debug("token：{}, point:{}", dataVO.getToken(), JsonUtil.toJSONString(point));
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return dataVO;
    }

    /**
     * 随机生成拼图坐标
     *
     * @param originalWidth      原图宽度
     * @param originalHeight     原图高度
     * @param slidingBlockWidth  滑块宽度
     * @param slidingBlockHeight 滑块高度
     * @return PointVO
     */
    private static PointVO generateSlidingBlockPoint(int originalWidth, int originalHeight, int slidingBlockWidth, int slidingBlockHeight) {
        int widthDifference = originalWidth - slidingBlockWidth;
        int heightDifference = originalHeight - slidingBlockHeight;

        Random random = new Random();
        int x = widthDifference <= 0 ? 5 : random.nextInt(originalWidth - slidingBlockWidth - 100) + 100;
        int y = heightDifference <= 0 ? 5 : random.nextInt(originalHeight - slidingBlockHeight) + 5;
        String key = null;
        if (captchaAesStatus) {
            key = AESUtil.fetchKey();
        }
        return PointVO.builder().x(x).y(y).secretKey(key).build();
    }

    /**
     * @param oriImage      原图
     * @param templateImage 模板图
     * @param newImage      新抠出的小图
     * @param x             随机扣取坐标X
     */
    private static void cutByTemplate(BufferedImage oriImage, BufferedImage templateImage, BufferedImage newImage, int x) {
        // 临时数组遍历用于高斯模糊存周边像素值
        int[][] martrix = new int[3][3];
        int[] values = new int[9];

        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();
        // 模板图像宽度
        for (int i = 0; i < xLength; i++) {
            // 模板图片高度
            for (int j = 0; j < yLength; j++) {
                // 如果模板图像当前像素点不是透明色 copy源文件信息到目标图片中
                int rgb = templateImage.getRGB(i, j);
                if (rgb < 0) {
                    newImage.setRGB(i, j, oriImage.getRGB(x + i, j));

                    // 抠图区域高斯模糊
                    readPixel(oriImage, x + i, j, values);
                    fillMatrix(martrix, values);
                    oriImage.setRGB(x + i, j, avgMatrix(martrix));
                }

                // 防止数组越界判断
                if (i == (xLength - 1) || j == (yLength - 1)) {
                    continue;
                }
                int rightRgb = templateImage.getRGB(i + 1, j);
                int downRgb = templateImage.getRGB(i, j + 1);
                // 描边处理，,取带像素和无像素的界点，判断该点是不是临界轮廓点,如果是设置该坐标像素是白色
                boolean exists = (rgb >= 0 && rightRgb < 0) ||
                        (rgb < 0 && rightRgb >= 0) ||
                        (rgb >= 0 && downRgb < 0) ||
                        (rgb < 0 && downRgb >= 0);
                if (exists) {
                    newImage.setRGB(i, j, Color.white.getRGB());
                    oriImage.setRGB(x + i, j, Color.white.getRGB());
                }
            }
        }

    }


    /**
     * 干扰抠图处理
     *
     * @param oriImage      原图
     * @param templateImage 模板图
     * @param x             随机扣取坐标X
     */
    private static void interferenceByTemplate(BufferedImage oriImage, BufferedImage templateImage, int x) {
        // 临时数组遍历用于高斯模糊存周边像素值
        int[][] martrix = new int[3][3];
        int[] values = new int[9];

        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();
        // 模板图像宽度
        for (int i = 0; i < xLength; i++) {
            // 模板图片高度
            for (int j = 0; j < yLength; j++) {
                // 如果模板图像当前像素点不是透明色 copy源文件信息到目标图片中
                int rgb = templateImage.getRGB(i, j);
                if (rgb < 0) {
                    // 抠图区域高斯模糊
                    readPixel(oriImage, x + i, j, values);
                    fillMatrix(martrix, values);
                    oriImage.setRGB(x + i, j, avgMatrix(martrix));
                }
                // 防止数组越界判断
                if (i == (xLength - 1) || j == (yLength - 1)) {
                    continue;
                }
                int rightRgb = templateImage.getRGB(i + 1, j);
                int downRgb = templateImage.getRGB(i, j + 1);
                // 描边处理，,取带像素和无像素的界点，判断该点是不是临界轮廓点,如果是设置该坐标像素是白色
                if ((rgb >= 0 && rightRgb < 0) || (rgb < 0 && rightRgb >= 0) || (rgb >= 0 && downRgb < 0) || (rgb < 0 && downRgb >= 0)) {
                    oriImage.setRGB(x + i, j, Color.white.getRGB());
                }
            }
        }

    }

    private static void readPixel(BufferedImage img, int x, int y, int[] pixels) {
        int xStart = x - 1;
        int yStart = y - 1;
        int current = 0;
        for (int i = xStart; i < 3 + xStart; i++) {
            for (int j = yStart; j < 3 + yStart; j++) {
                int tx = i;
                if (tx < 0) {
                    tx = -tx;

                } else if (tx >= img.getWidth()) {
                    tx = x;
                }
                int ty = j;
                if (ty < 0) {
                    ty = -ty;
                } else if (ty >= img.getHeight()) {
                    ty = y;
                }
                pixels[current++] = img.getRGB(tx, ty);

            }
        }
    }

    private static void fillMatrix(int[][] matrix, int[] values) {
        int filled = 0;
        for (int[] x : matrix) {
            for (int j = 0; j < x.length; j++) {
                x[j] = values[filled++];
            }
        }
    }

    private static int avgMatrix(int[][] matrix) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int[] x : matrix) {
            for (int j = 0; j < x.length; j++) {
                if (j == 1) {
                    continue;
                }
                Color c = new Color(x[j]);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        return new Color(r / 8, g / 8, b / 8).getRGB();
    }

    protected static int getEnOrChLength(String s) {
        int enCount = 0;
        int chCount = 0;
        for (int i = 0; i < s.length(); i++) {
            int length = String.valueOf(s.charAt(i)).getBytes(StandardCharsets.UTF_8).length;
            if (length > 1) {
                chCount++;
            } else {
                enCount++;
            }
        }
        int chOffset = (HAN_ZI_SIZE / 2) * chCount + 5;
        int enOffset = enCount * 8;
        return chOffset + enOffset;
    }

}
