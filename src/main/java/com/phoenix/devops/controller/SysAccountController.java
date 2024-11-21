package com.phoenix.devops.controller;

import com.phoenix.devops.entity.SysAccount;
import com.phoenix.devops.lang.IPage;
import com.phoenix.devops.model.vo.SysAccountVO;
import com.phoenix.devops.service.ISysAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 控制层。
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@RestController
@RequestMapping("/account")
public class SysAccountController {

    @Resource
    private ISysAccountService service;

    @PostMapping()
    public Long save(@Validated @RequestBody SysAccountVO accountVO) {
        return service.addSysAccount(accountVO);
    }

    @DeleteMapping()
    public boolean remove(@RequestBody Set<Long> ids) {
        return service.delSysAccount(ids);
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @Validated @RequestBody SysAccountVO accountVO) {
        return service.modSysAccount(id, accountVO);
    }

    @GetMapping("/info")
    public SysAccount getInfo(@RequestParam String username) {
        return service.fetchAccountByUsername(username);
    }

    @GetMapping()
    @Operation(summary = "分页查询用户信息",
            description = "根据条件【条件可有可无】分页查询用户信息",
            parameters = {
                    @Parameter(name = "pageNum", description = "页码"),
                    @Parameter(name = "pageSize", description = "每页大小"),
                    @Parameter(name = "condition", description = "条件")
            }
    )
    public IPage<SysAccount> page(
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(value = "condition", defaultValue = "", required = false) String condition
    ) {
        return service.fetchAllAccountsByCondition(pageNum, pageSize, condition);
    }

}
