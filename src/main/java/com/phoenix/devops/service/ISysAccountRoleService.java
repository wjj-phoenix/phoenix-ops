package com.phoenix.devops.service;

import com.mybatisflex.core.service.IService;
import com.phoenix.devops.entity.SysAccount;
import com.phoenix.devops.entity.SysAccountRole;
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
public interface ISysAccountRoleService extends IService<SysAccountRole> {

}
