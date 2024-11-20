package com.phoenix.devops.controller;

import com.mybatisflex.core.paginate.Page;
import com.phoenix.devops.entity.SysMenu;
import com.phoenix.devops.model.vo.SysMenuVO;
import com.phoenix.devops.service.ISysMenuService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  控制层。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController {

    @Resource
    private ISysMenuService service;

    /**
     * 添加。
     *
     * @param menuVO 菜单信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("")
    public Long save(@Validated @RequestBody SysMenuVO menuVO) {
        return service.addSysMenu(menuVO);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return service.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param sysMenu 
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody SysMenu sysMenu) {
        return service.updateById(sysMenu);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<SysMenu> list() {
        return service.list();
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public SysMenu getInfo(@PathVariable Long id) {
        return service.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<SysMenu> page(Page<SysMenu> page) {
        return service.page(page);
    }

}
