package com.phoenix.devops.model.vo;

import com.phoenix.devops.annotation.Unique;
import com.phoenix.devops.service.ISysRoleService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleVO {
    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    @Unique(field = "name", service = ISysRoleService.class, message = "角色名称已存在")
    private String name;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码")
    @Unique(field = "code", service = ISysRoleService.class, message = "角色编码已存在")
    private String code;

    /**
     * 角色排序
     */
    @Schema(description = "角色排序")
    private Integer sort;

    /**
     * 角色状态
     */
    @Schema(description = "角色状态")
    private Integer status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    @Schema(description = "菜单列表ID")
    private Set<Long> menuIds;
}
