package com.phoenix.devops.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 *  表定义层。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
public class SysLoginLogTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final SysLoginLogTableDef SYS_LOGIN_LOG = new SysLoginLogTableDef();

    
    public final QueryColumn ID = new QueryColumn(this, "id");

    
    public final QueryColumn REASON = new QueryColumn(this, "reason");

    
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    
    public final QueryColumn USERNAME = new QueryColumn(this, "username");

    
    public final QueryColumn LOGIN_TIME = new QueryColumn(this, "login_time");

    
    public final QueryColumn USER_AGENT = new QueryColumn(this, "user_agent");

    
    public final QueryColumn CLIENT_ADDR = new QueryColumn(this, "client_addr");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, USERNAME, STATUS, CLIENT_ADDR, LOGIN_TIME, REASON, USER_AGENT};

    public SysLoginLogTableDef() {
        super("", "sys_login_log");
    }

    private SysLoginLogTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysLoginLogTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysLoginLogTableDef("", "sys_login_log", alias));
    }

}
