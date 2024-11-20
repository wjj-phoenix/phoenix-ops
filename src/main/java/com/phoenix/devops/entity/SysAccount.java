package com.phoenix.devops.entity;

import com.mybatisflex.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 实体类。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("sys_account")
public class SysAccount implements Serializable /*, UserDetails */ {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户是否被锁
     */
    private Integer lock;

    /**
     * 用户是否可用
     */
    private Integer enable;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 最新登录时间
     */
    private LocalDateTime latestLoginTime;

    /**
     * 创建用户
     */
    private Long createdUser;

    @Column(ignore = true)
    private String createdUserName;

    @RelationManyToMany(
            selfField = "id",
            joinTable = "sys_account_role", joinSelfColumn = "account_id", joinTargetColumn = "role_id",
            targetTable = "sys_role", targetField = "id"
    )
    private List<SysRole> roles;

    /**
     * 用户所具有的权限
     */
   /*  @Column(ignore = true)
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities == null ? null : authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable == 1;
    } */
}
