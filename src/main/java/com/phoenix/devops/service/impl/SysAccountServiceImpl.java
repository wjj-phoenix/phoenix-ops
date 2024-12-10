package com.phoenix.devops.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.phoenix.devops.common.SelectCommon;
import com.phoenix.devops.entity.SysAccount;
import com.phoenix.devops.entity.SysAccountRole;
import com.phoenix.devops.lang.Constant;
import com.phoenix.devops.lang.IPage;
import com.phoenix.devops.mapper.SysAccountMapper;
import com.phoenix.devops.model.vo.PasswordVO;
import com.phoenix.devops.model.vo.SysAccountVO;
import com.phoenix.devops.service.ISysAccountRoleService;
import com.phoenix.devops.service.ISysAccountService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

import static com.phoenix.devops.entity.table.SysAccountRoleTableDef.SYS_ACCOUNT_ROLE;
import static com.phoenix.devops.entity.table.SysAccountTableDef.SYS_ACCOUNT;

/**
 * 服务层实现。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Service
@CacheConfig(cacheNames = "account")
public class SysAccountServiceImpl extends ServiceImpl<SysAccountMapper, SysAccount> implements ISysAccountService {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private ISysAccountRoleService accountRoleService;
    @Override
    public List<SysAccount> fetchAllAccounts() {
        return this.list(QueryWrapper.create().select(SYS_ACCOUNT.NOT_PASSWORD_COLUMNS).from(SYS_ACCOUNT));
    }

    @Override
    public SysAccount fetchSysAccountWithRelationsByUsername(String username){
        return mapper.selectOneWithRelationsByQuery(QueryWrapper.create().select(SYS_ACCOUNT.NOT_PASSWORD_COLUMNS).from(SYS_ACCOUNT).where(SYS_ACCOUNT.USERNAME.eq(username)));
    }

    @Override
    public SysAccount fetchAccountByUsername(String username) {
        return this.getOne(QueryWrapper.create().select(SYS_ACCOUNT.DEFAULT_COLUMNS).from(SYS_ACCOUNT).where(SYS_ACCOUNT.USERNAME.eq(username)));
    }

    @Override
    public IPage<SysAccount> fetchAllAccountsByCondition(Integer page, Integer limit, String condition) {
        QueryWrapper wrapper = QueryWrapper.create().select(SYS_ACCOUNT.NOT_PASSWORD_COLUMNS).from(SYS_ACCOUNT);
        return new IPage<>(new SelectCommon<SysAccount>().findAll(page, limit, condition, mapper, wrapper));
    }

    @Override
    public Long addSysAccount(SysAccountVO accountVO) {
        SysAccount account = BeanUtil.copyProperties(accountVO, SysAccount.class);
        account.setPassword(passwordEncoder.encode(Constant.PASSWORD));
        if (!this.save(account)) {
            throw new IllegalArgumentException("添加账户信息失败");
        }

        List<SysAccountRole> accountRoles = accountVO.getRoleIds().stream().map(roleId -> SysAccountRole.builder().accountId(account.getId()).roleId(roleId).build()).toList();
        accountRoleService.saveBatch(accountRoles);
        return account.getId();
    }

    @Override
    public Boolean modSysAccount(Long id, SysAccountVO accountVO) {
        SysAccount account = this.getById(id);
        Assert.notNull(account, "该账户不存在");

        BeanUtil.copyProperties(accountVO, account);
        if (!this.updateById(account)) {
            throw new IllegalArgumentException("修改账户信息失败");
        }
        if (CollUtil.isNotEmpty(accountVO.getRoleIds())) {
            accountRoleService.remove(QueryWrapper.create().where(SYS_ACCOUNT_ROLE.ACCOUNT_ID.eq(id)));
            List<SysAccountRole> accountRoles = accountVO.getRoleIds().stream().map(roleId -> SysAccountRole.builder().accountId(account.getId()).roleId(roleId).build()).toList();
            accountRoleService.saveBatch(accountRoles);
        }
        return true;
    }

    @Override
    public Boolean delSysAccount(Set<Long> ids) {
        if (!this.removeByIds(ids)) {
            throw new IllegalArgumentException("删除账户信息失败");
        }
        accountRoleService.remove(QueryWrapper.create().where(SYS_ACCOUNT_ROLE.ACCOUNT_ID.in(ids)));

        return true;
    }

    @Override
    public Boolean modSysAccountPassword(Long id, PasswordVO passwordVO) {
        SysAccount account = this.getById(id);
        Assert.notNull(account, "该账户不存在");
        if (!passwordEncoder.matches(passwordVO.getOldPassword(), account.getPassword())) {
            throw new IllegalArgumentException("旧密码不正确");
        }
        if (!passwordVO.getNewPassword().equals(passwordVO.getConfirmPassword())) {
            throw new IllegalArgumentException("两次输入的密码不一致");
        }
        if (!this.updateById(SysAccount.builder().id(id).password(passwordEncoder.encode(passwordVO.getNewPassword())).build())) {
            throw new IllegalArgumentException("修改密码失败");
        }
        return true;
    }

    @Override
    public void updateAccountLogin(Long userId, String address) {

    }
}
