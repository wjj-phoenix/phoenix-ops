package com.phoenix.devops.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户操作日志表 实体类。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("sys_operation_log")
public class SysOperationLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 物理主键
     */
    private String uuid;

    /**
     * 方法描述
     */
    private String operation;

    /**
     * 请求URI
     */
    private String reqUri;

    /**
     * 耗时
     */
    private Long respTime;

    /**
     * 客户端IP
     */
    private String clientAddr;

    /**
     * 请求结果
     */
    private Integer result;

    /**
     * 参数内容
     */
    private String params;

    /**
     * 操作详细描述
     */
    private String desc;

    /**
     * 客户端User-Agent
     */
    private String userAgent;

    /**
     * 失败原因
     */
    private String reason;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 创建时间
     */
    @Column(onInsertValue = "now()")
    private LocalDateTime operatedTime;

}
