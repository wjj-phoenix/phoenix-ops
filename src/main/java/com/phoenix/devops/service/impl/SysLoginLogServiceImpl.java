package com.phoenix.devops.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.phoenix.devops.entity.SysLoginLog;
import com.phoenix.devops.mapper.SysLoginLogMapper;
import com.phoenix.devops.service.ISysLoginLogService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {
}
