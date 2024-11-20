package com.phoenix.devops.model.vo;

import com.phoenix.devops.model.Add;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Set;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SysAccountVO implements Serializable {
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不允许为空", groups = {Add.class})
    private String username;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不允许为空", groups = {Add.class})
    private String email;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名")
    @NotBlank(message = "真实姓名不允许为空", groups = {Add.class})
    private String realName;

    /**
     * 备注
     */
    @Schema(description = "备注信息")
    private String remark;

    /**
     * 用户是否可用
     */
    @Schema(description = "是否可用")
    private Integer enable = 1;

    @Schema(description = "角色列表")
    @NotNull(message = "角色列表不允许为空", groups = {Add.class})
    private Set<Long> roleIds;
}
