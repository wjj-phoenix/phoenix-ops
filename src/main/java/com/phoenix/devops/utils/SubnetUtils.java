package com.phoenix.devops.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author god-lamp
 * @since 2024-05-07
 */
public class SubnetUtils {
    // 用于子网摘要信息的便利容器。
    public final class SubnetInfo {
        // 掩码将无符号 int 转换为长整型（即保留 32 位）
        private static final long UNSIGNED_INT_MASK = 0x0FFFFFFFFL;

        private SubnetInfo() {
        }

        private long broadcastLong() {
            return broadcast & UNSIGNED_INT_MASK;
        }

        private int high() {
            return isInclusiveHostCount() ? broadcast : broadcastLong() - networkLong() > 1 ? broadcast - 1 : 0;
        }

        public boolean isInRange(final int address) {
            if (address == 0) { // 永远不能在范围内;现在拒绝可避免 CIDR/31,32 出现问题
                return false;
            }
            final long addLong = address & UNSIGNED_INT_MASK;
            final long lowLong = low() & UNSIGNED_INT_MASK;
            final long highLong = high() & UNSIGNED_INT_MASK;
            return addLong >= lowLong && addLong <= highLong;
        }

        // 判断传入的参数地址是否在指定网段
        public boolean isInRange(final String address) {
            return isInRange(toInteger(address));
        }

        private int low() {
            return isInclusiveHostCount() ? network : broadcastLong() - networkLong() > 1 ? network + 1 : 0;
        }

        /**
         * Long versions of the values (as unsigned int) which are more suitable for range checking.
         */
        private long networkLong() {
            return network & UNSIGNED_INT_MASK;
        }
    }

    private static final String IP_ADDRESS = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
    private static final String SLASH_FORMAT = IP_ADDRESS + "/(\\d{1,2})"; // 0 -> 32
    private static final Pattern ADDRESS_PATTERN = Pattern.compile(IP_ADDRESS);
    private static final Pattern CIDR_PATTERN = Pattern.compile(SLASH_FORMAT);

    private static final int NBITS = 32;

    private static final String PARSE_FAIL = "Could not parse [%s]";

    // 提取带点的十进制地址的分量，并使用正则表达式匹配打包成整数
    private static int matchAddress(final Matcher matcher) {
        int addr = 0;
        for (int i = 1; i <= 4; ++i) {
            final int n = rangeCheck(Integer.parseInt(matcher.group(i)), 255);
            addr |= (n & 0xff) << 8 * (4 - i);
        }
        return addr;
    }

    // 检查整数边界。检查值 x 是否在 [0，end] 范围内。如果 x 在范围内，则返回 x，否则引发异常
    private static int rangeCheck(final int value, final int end) {
        if (value >= 0 && value <= end) { // [0,end]
            return value;
        }
        throw new IllegalArgumentException("Value [" + value + "] not in range [" + 0 + "," + end + "]");
    }

    // 将带分的十进制格式地址转换为压缩的整数格式
    private static int toInteger(final String address) {
        final Matcher matcher = ADDRESS_PATTERN.matcher(address);
        // 判断传入的参数是否能匹配正则表达式
        if (matcher.matches()) {
            return matchAddress(matcher);
        }
        throw new IllegalArgumentException(String.format(PARSE_FAIL, address));
    }

    private final int network;

    private final int broadcast;

    /**
     * Whether the broadcast/network address are included in host count
     */
    @Getter
    @Setter
    private boolean inclusiveHostCount;

    public SubnetUtils(final String cidrNotation) {
        final Matcher matcher = CIDR_PATTERN.matcher(cidrNotation);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format(PARSE_FAIL, cidrNotation));
        }
        int address = matchAddress(matcher);

        // Create a binary netmask from the number of bits specification /x
        final int trailingZeroes = NBITS - rangeCheck(Integer.parseInt(matcher.group(5)), NBITS);

        //
        // IPv4 网络掩码由 32 位组成，这是一个连续的序列，由指定数量的 1 后跟所有零组成。
        // 因此，可以通过将无符号整数（32 位）向左移动尾随零的数量（32 - # 位规范）来获得它。
        // 请注意，没有无符号的左移运算符，因此我们必须使用 long 来确保最左边的位正确移出。
        //
        int netmask = (int) (0x0FFFFFFFFL << trailingZeroes);

        // Calculate base network address
        this.network = address & netmask;

        // Calculate broadcast address
        this.broadcast = network | ~netmask;
    }

    public final SubnetInfo getInfo() {
        return new SubnetInfo();
    }
}
