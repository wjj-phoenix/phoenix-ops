package com.phoenix.devops.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  实体类。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("sys_account_role")
public class SysAccountRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id
    private Long accountId;

    /**
     * 角色ID
     */
    @Id
    private Long roleId;

}
