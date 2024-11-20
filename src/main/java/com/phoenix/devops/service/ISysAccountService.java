package com.phoenix.devops.service;

import com.mybatisflex.core.service.IService;
import com.phoenix.devops.entity.SysAccount;
import com.phoenix.devops.lang.IPage;
import com.phoenix.devops.model.vo.PasswordVO;
import com.phoenix.devops.model.vo.SysAccountVO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

/**
 *  服务层。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
public interface ISysAccountService extends IService<SysAccount> {
    /**
     * 查询所有账户信息
     *
     * @return 账户信息
     */
    List<SysAccount> fetchAllAccounts();

    /**
     * 更具用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysAccount fetchAccountByUsername(@NotBlank(message = "用户名不能为空!") String username);

    /**
     * 根据条件分页查询用户信息
     *
     * @param page      页码
     * @param limit     每页大小
     * @param condition 条件
     * @return 分页数据
     */
    IPage<SysAccount> fetchAllAccountsByCondition(@NotNull(message = "页码不能为空!") Integer page, @NotNull(message = "每页大小不能为空!") Integer limit, String condition);

    /**
     * 添加用户信息
     *
     * @param accountVO 账户信息
     * @return 账户主键ID
     */
    Long addSysAccount(@NotNull(message = "账户信息不能为空!") SysAccountVO accountVO);

    /**
     * 修改用户信息
     *
     * @param id        账户主键ID
     * @param accountVO 账户信息
     * @return true|false
     */
    Boolean modSysAccount(@NotNull(message = "账户主键ID不能为空") Long id, @NotNull(message = "账户信息不能为空!") SysAccountVO accountVO);


    /**
     * 删除用户信息
     *
     * @param ids 账户主键ID
     * @return true|false
     */
    Boolean delSysAccount(@NotNull(message = "账户主键ID不能为空") Set<Long> ids);

    /**
     * 删除用户信息
     *
     * @param id 账户主键ID
     * @return true|false
     */
    Boolean modSysAccountPassword(@NotNull(message = "账户主键ID不能为空") Long id, @NotNull(message = "密码信息不能为空") PasswordVO passwordVO);
}
