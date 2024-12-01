package com.phoenix.devops.service;

import com.phoenix.devops.model.common.ResponseModel;
import com.phoenix.devops.model.vo.CaptchaVO;

/**
 * @author wjj-phoenix
 * @since 2024-12-01
 */
public interface ISysCaptchaService {
    CaptchaVO get();

    ResponseModel check(CaptchaVO var1);

    ResponseModel verification(CaptchaVO captchaVO);
}
