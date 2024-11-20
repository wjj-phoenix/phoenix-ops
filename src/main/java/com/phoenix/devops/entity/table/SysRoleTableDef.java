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
public class SysRoleTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final SysRoleTableDef SYS_ROLE = new SysRoleTableDef();

    /**
     * 主键ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 角色编码
     */
    public final QueryColumn CODE = new QueryColumn(this, "code");

    /**
     * 角色名称
     */
    public final QueryColumn NAME = new QueryColumn(this, "name");

    /**
     * 角色排序
     */
    public final QueryColumn SORT = new QueryColumn(this, "sort");

    /**
     * 备注
     */
    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    /**
     * 角色状态
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 创建时间
     */
    public final QueryColumn CREATED_TIME = new QueryColumn(this, "created_time");

    /**
     * 更新时间
     */
    public final QueryColumn UPDATED_TIME = new QueryColumn(this, "updated_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, NAME, CODE, SORT, STATUS, REMARK, CREATED_TIME, UPDATED_TIME};

    public SysRoleTableDef() {
        super("", "sys_role");
    }

    private SysRoleTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysRoleTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysRoleTableDef("", "sys_role", alias));
    }

}
