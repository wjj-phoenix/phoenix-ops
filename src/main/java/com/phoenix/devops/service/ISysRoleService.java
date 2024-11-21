package com.phoenix.devops.service;

import com.mybatisflex.core.service.IService;
import com.phoenix.devops.entity.SysRole;
import com.phoenix.devops.model.vo.SysRoleVO;

import java.util.List;
import java.util.Set;

/**
 * 服务层。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
public interface ISysRoleService extends IService<SysRole> {
    /**
     * 查询所有角色信息
     *
     * @return 角色信息列表
     */
    List<SysRole> fetchAllSysRoles();

    /**
     * 根据用户ID查询角色信息
     *
     * @param accountId 账户ID
     * @return 角色列表
     */
    List<SysRole> fetchSysRolesByAccountId(Long accountId);

    /**
     * 根据用户名查询角色信息
     *
     * @param username 用户名
     * @return 角色信息
     */
    List<SysRole> fetchSysRolesByUsername(String username);

    /**
     * 根据主键ID查询角色信息
     *
     * @param id 主键ID
     * @return 角色信息
     */
    SysRole fetchSysRoleById(Long id);

    /**
     * 添加角色信息
     *
     * @param roleVO 角色信息
     * @return 主键ID
     */
    Long addSysRole(SysRoleVO roleVO);

    /**
     * 根据主键ID修改角色信息
     *
     * @param id     主键ID
     * @param roleVO 角色信息
     * @return true|false
     */
    Boolean modSysRole(Long id, SysRoleVO roleVO);

    /**
     * 根据主键列表删除角色信息
     *
     * @param ids 主键列表
     * @return true|false
     */
    Boolean delSysRoles(Set<Long> ids);
}
