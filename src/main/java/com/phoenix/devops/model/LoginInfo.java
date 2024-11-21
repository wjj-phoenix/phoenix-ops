package com.phoenix.devops.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空!")
    private String username;
    /**
     * 密码
     */
    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空!")
    private String password;
}
