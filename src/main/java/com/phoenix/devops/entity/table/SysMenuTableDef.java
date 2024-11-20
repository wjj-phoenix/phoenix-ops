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
public class SysMenuTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final SysMenuTableDef SYS_MENU = new SysMenuTableDef();

    /**
     * 主键ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 请求路径
     */
    public final QueryColumn URL = new QueryColumn(this, "url");

    /**
     * 权限编码
     */
    public final QueryColumn CODE = new QueryColumn(this, "code");

    /**
     * 图标
     */
    public final QueryColumn ICON = new QueryColumn(this, "icon");

    /**
     * 名称
     */
    public final QueryColumn NAME = new QueryColumn(this, "name");

    /**
     * 排序
     */
    public final QueryColumn SORT = new QueryColumn(this, "sort");

    /**
     * 类型
     */
    public final QueryColumn TYPE = new QueryColumn(this, "type");

    /**
     * 是否隐藏
     */
    public final QueryColumn HIDDEN = new QueryColumn(this, "hidden");

    /**
     * 请求方式
     */
    public final QueryColumn METHOD = new QueryColumn(this, "method");

    /**
     * 父ID
     */
    public final QueryColumn PARENT_ID = new QueryColumn(this, "parent_id");

    /**
     * 外链地址
     */
    public final QueryColumn REDIRECT = new QueryColumn(this, "redirect");

    /**
     * 创建时间
     */
    public final QueryColumn CREATED_TIME = new QueryColumn(this, "created_time");

    /**
     * 描述信息
     */
    public final QueryColumn DESCRIPTION = new QueryColumn(this, "description");

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
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, PARENT_ID, ICON, NAME, CODE, URL, METHOD, TYPE, SORT, REDIRECT, HIDDEN, DESCRIPTION, CREATED_TIME, UPDATED_TIME};

    public SysMenuTableDef() {
        super("", "sys_menu");
    }

    private SysMenuTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysMenuTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysMenuTableDef("", "sys_menu", alias));
    }

}
