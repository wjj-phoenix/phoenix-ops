package com.phoenix.devops.model.vo;

import com.phoenix.devops.annotation.UnionUnique;
import com.phoenix.devops.annotation.UnionUniques;
import com.phoenix.devops.annotation.Unique;
import com.phoenix.devops.service.ISysMenuService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Data
@Builder

@NoArgsConstructor
@AllArgsConstructor
@UnionUniques({
        @UnionUnique(service = ISysMenuService.class, fields = {"url", "method"}, message = "菜单请求地址和请求方式组合已存在"),
        @UnionUnique(service = ISysMenuService.class, fields = {"icon", "type"}, message = "菜单图标组合已存在")
})
public class SysMenuVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 父ID
     */
    @Schema(description = "父节点")
    private Long parentId;

    /**
     * 图标
     */
    @Schema(description = "图标")
    private String icon;

    /**
     * 名称
     */
    @Schema(description = "名称")
    @Unique(field = "name", service = ISysMenuService.class, message = "菜单名称已存在")
    private String name;

    /**
     * 权限编码
     */
    @Schema(description = "权限编码")
    @Unique(field = "code", service = ISysMenuService.class, message = "菜单编码已存在")
    private String code;

    /**
     * 请求路径
     */
    @Schema(description = "请求路径")
    private String url;

    /**
     * 请求方式
     */
    @Schema(description = "请求方式")
    private String method;

    /**
     * 类型
     */
    @Schema(description = "菜单类型")
    private Integer type = 1;

    /**
     * 排序
     */
    @Schema(description = "菜单排序")
    private Integer sort = 99;

    /**
     * 外链地址
     */
    @Schema(description = "外链地址")
    private String redirect = "";

    /**
     * 是否隐藏
     */
    @Schema(description = "是否隐藏")
    private Integer hidden = 1;

    /**
     * 描述信息
     */
    @Schema(description = "描述信息")
    private String description;
}
