package com.phoenix.devops.service;

import com.mybatisflex.core.service.IService;
import com.phoenix.devops.entity.SysRole;
import com.phoenix.devops.lang.IPage;
import com.phoenix.devops.model.vo.SysRoleVO;
import jakarta.validation.constraints.NotNull;

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
     * 根据条件分页查询角色信息
     *
     * @param page      页码
     * @param limit     每页大小
     * @param condition 条件
     * @return 分页数据
     */
    IPage<SysRole> fetchAllRolesByCondition(@NotNull(message = "页码不能为空!") Integer page, @NotNull(message = "每页大小不能为空!") Integer limit, String condition);

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
