package com.phoenix.devops.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.phoenix.devops.common.SelectCommon;
import com.phoenix.devops.entity.SysRole;
import com.phoenix.devops.entity.SysRoleMenu;
import com.phoenix.devops.lang.IPage;
import com.phoenix.devops.mapper.SysRoleMapper;
import com.phoenix.devops.model.vo.SysRoleVO;
import com.phoenix.devops.service.ISysRoleMenuService;
import com.phoenix.devops.service.ISysRoleService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.phoenix.devops.entity.table.SysAccountRoleTableDef.SYS_ACCOUNT_ROLE;
import static com.phoenix.devops.entity.table.SysAccountTableDef.SYS_ACCOUNT;
import static com.phoenix.devops.entity.table.SysRoleMenuTableDef.SYS_ROLE_MENU;
import static com.phoenix.devops.entity.table.SysRoleTableDef.SYS_ROLE;

/**
 * 服务层实现。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Service
@CacheConfig(cacheNames = "role")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Resource
    private ISysRoleMenuService roleMenuService;

    @Override
    public List<SysRole> fetchAllSysRoles() {
        return this.list(QueryWrapper.create().select(SYS_ROLE.DEFAULT_COLUMNS).from(SYS_ROLE));
    }

    @Override
    public List<SysRole> fetchSysRolesByAccountId(Long accountId) {
        return this.list(
                QueryWrapper.create()
                        .select(SYS_ROLE.DEFAULT_COLUMNS).from(SYS_ROLE)
                        .leftJoin(SYS_ACCOUNT_ROLE).on(SYS_ACCOUNT_ROLE.ROLE_ID.eq(SYS_ROLE.ID))
                        .where(SYS_ACCOUNT_ROLE.ACCOUNT_ID.eq(accountId))
        );
    }

    @Override
    public IPage<SysRole> fetchAllRolesByCondition(Integer page, Integer limit, String condition) {
        QueryWrapper wrapper = QueryWrapper.create().select(SYS_ROLE.DEFAULT_COLUMNS).from(SYS_ROLE);
        return new IPage<>(new SelectCommon<SysRole>().findAll(page, limit, condition, mapper, wrapper));
    }

    @Override
    public List<SysRole> fetchSysRolesByUsername(String username) {
        return this.list(
                QueryWrapper.create()
                        .select(SYS_ROLE.DEFAULT_COLUMNS).from(SYS_ROLE)
                        .leftJoin(SYS_ACCOUNT_ROLE).on(SYS_ACCOUNT_ROLE.ROLE_ID.eq(SYS_ROLE.ID))
                        .leftJoin(SYS_ACCOUNT).on(SYS_ACCOUNT_ROLE.ACCOUNT_ID.eq(SYS_ACCOUNT.ID))
                        .where(SYS_ACCOUNT.USERNAME.eq(username))
        );
    }

    @Override
    public SysRole fetchSysRoleById(Long id) {
        return this.getById(id);
    }

    @Override
    public Long addSysRole(SysRoleVO roleVO) {
        SysRole role = BeanUtil.toBean(roleVO, SysRole.class);
        if (!this.save(role)) {
            throw new IllegalArgumentException("添加角色信息失败！");
        }
        List<SysRoleMenu> sysRoleMenus = roleVO.getMenuIds().stream().map(menuId -> SysRoleMenu.builder().roleId(role.getId()).menuId(menuId).build()).toList();
        roleMenuService.saveBatch(sysRoleMenus);
        return role.getId();
    }

    @Override
    public Boolean modSysRole(Long id, SysRoleVO roleVO) {
        SysRole role = this.getById(id);
        BeanUtil.copyProperties(roleVO, role);
        if (!this.updateById(role)) {
            throw new IllegalArgumentException("修改角色信息失败！");
        }
        if (CollUtil.isNotEmpty(roleVO.getMenuIds())) {
            roleMenuService.remove(QueryWrapper.create().where(SYS_ROLE_MENU.ROLE_ID.eq(id)));
            List<SysRoleMenu> sysRoleMenus = roleVO.getMenuIds().stream().map(menuId -> SysRoleMenu.builder().roleId(role.getId()).menuId(menuId).build()).toList();
            roleMenuService.saveBatch(sysRoleMenus);
        }
        return true;
    }

    @Override
    public Boolean delSysRoles(Set<Long> ids) {
        if (!this.removeByIds(ids)) {
            throw new IllegalStateException("删除角色信息失败");
        }
        roleMenuService.remove(QueryWrapper.create().where(SYS_ROLE_MENU.ROLE_ID.in(ids)));
        return true;
    }
}
