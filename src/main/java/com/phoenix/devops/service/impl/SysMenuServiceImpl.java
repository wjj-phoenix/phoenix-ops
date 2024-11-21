package com.phoenix.devops.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.phoenix.devops.entity.SysMenu;
import com.phoenix.devops.mapper.SysMenuMapper;
import com.phoenix.devops.model.vo.SysMenuVO;
import com.phoenix.devops.service.ISysMenuService;
import com.phoenix.devops.utils.TreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

import static com.phoenix.devops.entity.table.SysAccountRoleTableDef.SYS_ACCOUNT_ROLE;
import static com.phoenix.devops.entity.table.SysAccountTableDef.SYS_ACCOUNT;
import static com.phoenix.devops.entity.table.SysMenuTableDef.SYS_MENU;
import static com.phoenix.devops.entity.table.SysRoleMenuTableDef.SYS_ROLE_MENU;
import static com.phoenix.devops.entity.table.SysRoleTableDef.SYS_ROLE;

/**
 * 服务层实现。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "menu")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<SysMenu> fetchAllSysMenus() {
        List<SysMenu> menus = this.list(QueryWrapper.create().select(SYS_MENU.DEFAULT_COLUMNS).from(SYS_MENU));
        return TreeUtils.generateTrees(menus);
    }

    @Override
    public List<SysMenu> fetchSysMenusByPId(Long pid) {
        return this.list(QueryWrapper.create().select(SYS_MENU.DEFAULT_COLUMNS).from(SYS_MENU).where(SYS_MENU.PARENT_ID.eq(pid)));
    }

    @Override
    public List<SysMenu> fetchMenusByAccountId(Long accountId) {
        return this.list(
                QueryWrapper.create()
                        .select(QueryMethods.distinct(SYS_MENU.DEFAULT_COLUMNS)).from(SYS_MENU)
                        .leftJoin(SYS_ROLE_MENU).on(SYS_ROLE_MENU.MENU_ID.eq(SYS_MENU.ID))
                        .leftJoin(SYS_ACCOUNT_ROLE).on(SYS_ACCOUNT_ROLE.ROLE_ID.eq(SYS_ROLE_MENU.ROLE_ID))
                        .where(SYS_ACCOUNT_ROLE.ACCOUNT_ID.eq(accountId))
        );
    }

    @Override
    public List<SysMenu> fetchMenusByUsername(String username) {
        return this.list(
                QueryWrapper.create()
                        .select(QueryMethods.distinct(SYS_MENU.DEFAULT_COLUMNS)).from(SYS_MENU)
                        .leftJoin(SYS_ROLE_MENU).on(SYS_ROLE_MENU.MENU_ID.eq(SYS_MENU.ID))
                        .leftJoin(SYS_ACCOUNT_ROLE).on(SYS_ACCOUNT_ROLE.ROLE_ID.eq(SYS_ROLE_MENU.ROLE_ID))
                        .leftJoin(SYS_ACCOUNT).on(SYS_ACCOUNT_ROLE.ACCOUNT_ID.eq(SYS_ACCOUNT.ID))
                        .where(SYS_ACCOUNT.USERNAME.eq(username))
        );
    }

    @Override
    public List<SysMenu> fetchMenusByRoleId(Long roleId) {
        return this.list(
                QueryWrapper.create()
                        .select(QueryMethods.distinct(SYS_MENU.DEFAULT_COLUMNS)).from(SYS_MENU)
                        .leftJoin(SYS_ROLE_MENU).on(SYS_ROLE_MENU.MENU_ID.eq(SYS_MENU.ID))
                        .where(SYS_ROLE_MENU.ROLE_ID.eq(roleId))
        );
    }

    @Override
    public List<SysMenu> fetchMenusByRoleName(String roleName) {
        return this.list(
                QueryWrapper.create()
                        .select(QueryMethods.distinct(SYS_MENU.DEFAULT_COLUMNS)).from(SYS_MENU)
                        .leftJoin(SYS_ROLE_MENU).on(SYS_ROLE_MENU.MENU_ID.eq(SYS_MENU.ID))
                        .leftJoin(SYS_ROLE).on(SYS_ROLE_MENU.ROLE_ID.eq(SYS_ROLE.ID))
                        .where(SYS_ROLE.NAME.eq(roleName))
        );
    }

    @Override
    public SysMenu fetchSysMenuById(Long id) {
        return this.getById(id);
    }

    @Override
    public Long addSysMenu(SysMenuVO menuVO) {
        SysMenu menu = BeanUtil.toBean(menuVO, SysMenu.class);
        if (!this.save(menu)) {
            throw new IllegalArgumentException("添加菜单项失败");
        }
        return menu.getId();
    }

    @Override
    public Boolean modSysMenu(Long id, SysMenuVO menuVO) {
        SysMenu menu = this.getById(id);
        Assert.notNull(menu, "指定的菜单项不存在");

        BeanUtil.copyProperties(menuVO, menu);
        if (!this.updateById(menu)) {
            throw new IllegalArgumentException("修改菜单项失败");
        }
        return true;
    }

    @Override
    public Boolean delSysMenu(Set<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            if (!this.removeByIds(ids)) {
                throw new IllegalStateException("删除菜单项失败");
            }
        }
        return true;
    }
}
