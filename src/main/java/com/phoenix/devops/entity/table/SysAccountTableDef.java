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
public class SysAccountTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final SysAccountTableDef SYS_ACCOUNT = new SysAccountTableDef();

    /**
     * 主键ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 用户是否被锁
     */
    public final QueryColumn LOCK = new QueryColumn(this, "lock");

    /**
     * 邮箱
     */
    public final QueryColumn EMAIL = new QueryColumn(this, "email");

    /**
     * 头像
     */
    public final QueryColumn AVATAR = new QueryColumn(this, "avatar");

    /**
     * 用户是否可用
     */
    public final QueryColumn ENABLE = new QueryColumn(this, "enable");

    /**
     * 备注
     */
    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    /**
     * 密码
     */
    public final QueryColumn PASSWORD = new QueryColumn(this, "password");

    /**
     * 真实姓名
     */
    public final QueryColumn REAL_NAME = new QueryColumn(this, "real_name");

    /**
     * 用户名
     */
    public final QueryColumn USERNAME = new QueryColumn(this, "username");

    /**
     * 创建时间
     */
    public final QueryColumn CREATED_TIME = new QueryColumn(this, "created_time");

    /**
     * 创建用户
     */
    public final QueryColumn CREATED_USER = new QueryColumn(this, "created_user");

    /**
     * 更新时间
     */
    public final QueryColumn UPDATED_TIME = new QueryColumn(this, "updated_time");

    /**
     * 最新登录时间
     */
    public final QueryColumn LATEST_LOGIN_TIME = new QueryColumn(this, "latest_login_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, USERNAME, PASSWORD, AVATAR, EMAIL, REAL_NAME, REMARK, LOCK, ENABLE, CREATED_TIME, UPDATED_TIME, LATEST_LOGIN_TIME, CREATED_USER};

    public final QueryColumn[] NOT_PASSWORD_COLUMNS = new QueryColumn[]{ID, USERNAME, AVATAR, EMAIL, REAL_NAME, REMARK, LOCK, ENABLE, CREATED_TIME, UPDATED_TIME, LATEST_LOGIN_TIME, CREATED_USER};

    public SysAccountTableDef() {
        super("", "sys_account");
    }

    private SysAccountTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysAccountTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysAccountTableDef("", "sys_account", alias));
    }

}
