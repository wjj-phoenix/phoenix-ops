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
public class SysRoleMenuTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final SysRoleMenuTableDef SYS_ROLE_MENU = new SysRoleMenuTableDef();

    /**
     * 权限ID
     */
    public final QueryColumn MENU_ID = new QueryColumn(this, "menu_id");

    /**
     * 角色ID
     */
    public final QueryColumn ROLE_ID = new QueryColumn(this, "role_id");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ROLE_ID, MENU_ID};

    public SysRoleMenuTableDef() {
        super("", "sys_role_menu");
    }

    private SysRoleMenuTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysRoleMenuTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysRoleMenuTableDef("", "sys_role_menu", alias));
    }

}
