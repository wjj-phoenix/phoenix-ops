package com.phoenix.devops.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.phoenix.devops.entity.SysRoleMenu;
import com.phoenix.devops.mapper.SysRoleMenuMapper;
import com.phoenix.devops.service.ISysRoleMenuService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {
}
