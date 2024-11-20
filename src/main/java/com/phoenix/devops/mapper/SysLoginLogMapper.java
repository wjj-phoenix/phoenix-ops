package com.phoenix.devops.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.mybatisflex.core.BaseMapper;
import com.phoenix.devops.entity.SysLoginLog;

/**
 *  映射层。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Mapper
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

}
