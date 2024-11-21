package com.phoenix.devops.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.phoenix.devops.entity.SysAccountRole;
import com.phoenix.devops.mapper.SysAccountRoleMapper;
import com.phoenix.devops.service.ISysAccountRoleService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Service
public class SysAccountRoleServiceImpl extends ServiceImpl<SysAccountRoleMapper, SysAccountRole> implements ISysAccountRoleService {
}
