package com.phoenix.devops.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.phoenix.devops.entity.SysOperationLog;
import com.phoenix.devops.mapper.SysOperationLogMapper;
import com.phoenix.devops.service.ISysOperationLogService;
import org.springframework.stereotype.Service;

/**
 * 用户操作日志表 服务层实现。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements ISysOperationLogService {

}
