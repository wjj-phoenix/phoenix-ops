package com.phoenix.devops.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 * 用户操作日志表 表定义层。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
public class SysOperationLogTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户操作日志表
     */
    public static final SysOperationLogTableDef SYS_OPERATION_LOG = new SysOperationLogTableDef();

    /**
     * 主键ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 操作详细描述
     */
    public final QueryColumn DESC = new QueryColumn(this, "desc");

    /**
     * 物理主键
     */
    public final QueryColumn UUID = new QueryColumn(this, "uuid");

    /**
     * 参数内容
     */
    public final QueryColumn PARAMS = new QueryColumn(this, "params");

    /**
     * 失败原因
     */
    public final QueryColumn REASON = new QueryColumn(this, "reason");

    /**
     * 请求URI
     */
    public final QueryColumn REQ_URI = new QueryColumn(this, "req_uri");

    /**
     * 请求结果
     */
    public final QueryColumn RESULT = new QueryColumn(this, "result");

    /**
     * 操作人
     */
    public final QueryColumn OPERATOR = new QueryColumn(this, "operator");

    /**
     * 耗时
     */
    public final QueryColumn RESP_TIME = new QueryColumn(this, "resp_time");

    /**
     * 方法描述
     */
    public final QueryColumn OPERATION = new QueryColumn(this, "operation");

    /**
     * 客户端User-Agent
     */
    public final QueryColumn USER_AGENT = new QueryColumn(this, "user_agent");

    /**
     * 客户端IP
     */
    public final QueryColumn CLIENT_ADDR = new QueryColumn(this, "client_addr");

    /**
     * 创建时间
     */
    public final QueryColumn OPERATED_TIME = new QueryColumn(this, "operated_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, UUID, OPERATION, REQ_URI, RESP_TIME, CLIENT_ADDR, RESULT, PARAMS, DESC, USER_AGENT, REASON, OPERATOR, OPERATED_TIME};

    public SysOperationLogTableDef() {
        super("", "sys_operation_log");
    }

    private SysOperationLogTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysOperationLogTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysOperationLogTableDef("", "sys_operation_log", alias));
    }

}
