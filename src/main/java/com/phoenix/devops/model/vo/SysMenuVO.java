package com.phoenix.devops.model.vo;

import com.phoenix.devops.annotation.Unique;
import com.phoenix.devops.service.ISysMenuService;
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
public class SysMenuVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 名称
     */
    @Unique(field = "name", service = ISysMenuService.class, message = "菜单名称已存在")
    private String name;

    /**
     * 权限编码
     */
    @Unique(field = "code",service = ISysMenuService.class, message = "菜单编码已存在")
    private String code;

    /**
     * 请求路径
     */
    private String url;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 外链地址
     */
    private String redirect;

    /**
     * 是否隐藏
     */
    private Integer hidden;

    /**
     * 描述信息
     */
    private String description;
}
