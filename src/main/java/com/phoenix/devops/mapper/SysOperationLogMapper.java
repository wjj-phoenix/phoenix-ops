package com.phoenix.devops.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.phoenix.devops.entity.SysOperationLog;

/**
 * 用户操作日志表 映射层。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Mapper
public interface SysOperationLogMapper extends BaseMapper<SysOperationLog> {

}
