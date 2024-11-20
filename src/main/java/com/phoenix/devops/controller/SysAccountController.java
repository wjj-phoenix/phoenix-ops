package com.phoenix.devops.controller;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.phoenix.devops.entity.SysAccount;
import com.phoenix.devops.service.ISysAccountService;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 *  控制层。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@RestController
@RequestMapping("/sysAccount")
public class SysAccountController {

    @Autowired
    private ISysAccountService iSysAccountService;

    /**
     * 添加。
     *
     * @param sysAccount 
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody SysAccount sysAccount) {
        return iSysAccountService.save(sysAccount);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return iSysAccountService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param sysAccount 
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody SysAccount sysAccount) {
        return iSysAccountService.updateById(sysAccount);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<SysAccount> list() {
        return iSysAccountService.list();
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public SysAccount getInfo(@PathVariable Long id) {
        return iSysAccountService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<SysAccount> page(Page<SysAccount> page) {
        return iSysAccountService.page(page);
    }

}
