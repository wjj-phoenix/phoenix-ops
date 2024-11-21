package com.phoenix.devops.service;

import com.mybatisflex.core.service.IService;
import com.phoenix.devops.entity.SysMenu;
import com.phoenix.devops.lang.IPage;
import com.phoenix.devops.model.vo.SysMenuVO;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

/**
 * 服务层。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
public interface ISysMenuService extends IService<SysMenu> {
    /**
     * 查询所有菜单项
     *
     * @return 菜单项列表
     */
    List<SysMenu> fetchAllSysMenus();

    /**
     * 根据条件分页查询菜单信息
     *
     * @param page      页码
     * @param limit     每页大小
     * @param condition 条件
     * @return 分页数据
     */
    IPage<SysMenu> fetchAllRolesByCondition(@NotNull(message = "页码不能为空!") Integer page, @NotNull(message = "每页大小不能为空!") Integer limit, String condition);

    /**
     * 根据父ID查询其子菜单项
     *
     * @param pid 父ID
     * @return 子菜单项列表
     */
    List<SysMenu> fetchSysMenusByPId(@NotNull(message = "父ID不能为空!") Long pid);

    /**
     * 根据用户ID查询菜单列表
     *
     * @param accountId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> fetchMenusByAccountId(@NotNull(message = "用户ID不能为空!") Long accountId);

    /**
     * 根据用户名查询菜单信息
     *
     * @param username 用户名
     * @return 菜单列表
     */
    List<SysMenu> fetchMenusByUsername(@NotNull(message = "用户名不能为空!") String username);

    /**
     * 根据角色ID查询菜单项
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<SysMenu> fetchMenusByRoleId(@NotNull(message = "角色ID不能为空!") Long roleId);

    /**
     * 根据角色名查询菜单项
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<SysMenu> fetchMenusByRoleName(@NotNull(message = "角色名不能为空!") String roleName);

    /**
     * 根据主键ID查询菜单项
     *
     * @param id 主键ID
     * @return 菜单项
     */
    SysMenu fetchSysMenuById(@NotNull(message = "主键ID不能为空!") Long id);

    /**
     * 添加菜单项
     *
     * @param menuVO 菜单项信息
     * @return 主键ID
     */
    Long addSysMenu(@NotNull(message = "菜单信息不能为空!") SysMenuVO menuVO);

    /**
     * 根据主键ID修改菜单信息
     *
     * @param id     主键ID
     * @param menuVO 菜单信息
     * @return true|false
     */
    Boolean modSysMenu(@NotNull(message = "菜单主键ID不能为空!") Long id, @NotNull(message = "菜单信息不能为空!") SysMenuVO menuVO);

    /**
     * 根据主键ID批量删除菜单项
     *
     * @param ids 主键ID列表
     * @return true|false
     */
    Boolean delSysMenu(@NotNull(message = "菜单主键ID列表不能为空!") Set<Long> ids);
}
