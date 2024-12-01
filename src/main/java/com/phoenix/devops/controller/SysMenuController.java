package com.phoenix.devops.controller;

import com.phoenix.devops.entity.SysMenu;
import com.phoenix.devops.model.vo.SysMenuVO;
import com.phoenix.devops.service.ISysMenuService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 控制层。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController {
    @Resource
    private ISysMenuService service;

    @PostMapping("")
    public Long save(@Validated @RequestBody SysMenuVO menuVO) {
        return service.addSysMenu(menuVO);
    }

    @DeleteMapping()
    public boolean remove(@RequestBody Set<Long> ids) {
        return service.delSysMenu(ids);
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @Validated @RequestBody SysMenuVO menuVO) {
        return service.modSysMenu(id, menuVO);
    }

    @GetMapping()
    public List<SysMenu> list(@RequestParam(defaultValue = "", required = false) String condition) {
        return service.fetchAllSysMenus();
    }

    @GetMapping("/{id}")
    public SysMenu getInfo(@PathVariable Long id) {
        return service.getById(id);
    }
}
