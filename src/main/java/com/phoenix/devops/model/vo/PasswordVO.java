package com.phoenix.devops.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 旧密码
     */
    @Schema(description = "当前密码")
    @NotBlank(message = "当前密码不能为空!")
    private String oldPassword;
    /**
     * 新密码
     */
    @Schema(description = "新密码")
    @NotBlank(message = "新密码不能为空!")
    private String newPassword;
    /**
     * 确认密码
     */
    @Schema(description = "确认密码")
    @NotBlank(message = "确认密码不能为空!")
    private String confirmPassword;
}
