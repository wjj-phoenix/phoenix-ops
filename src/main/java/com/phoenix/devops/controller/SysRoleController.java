package com.phoenix.devops.controller;

import com.phoenix.devops.entity.SysRole;
import com.phoenix.devops.model.vo.SysRoleVO;
import com.phoenix.devops.service.ISysRoleService;
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
@RequestMapping("/role")
public class SysRoleController {
    @Resource
    private ISysRoleService service;

    @PostMapping("")
    public Long save(@Validated @RequestBody SysRoleVO roleVO) {
        return service.addSysRole(roleVO);
    }

    @DeleteMapping("")
    public boolean remove(@RequestBody Set<Long> ids) {
        return service.delSysRoles(ids);
    }


    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @Validated @RequestBody SysRoleVO roleVO) {
        return service.modSysRole(id, roleVO);
    }


    @GetMapping("")
    public List<SysRole> list() {
        return service.fetchAllSysRoles();
    }


    @GetMapping("/{id}")
    public SysRole getInfo(@PathVariable Long id) {
        return service.fetchSysRoleById(id);
    }
}
