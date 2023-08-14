package com.aoz.aozmeeting.controller;

import com.aoz.aozmeeting.POJO.Department;
import com.aoz.aozmeeting.common.R;
import com.aoz.aozmeeting.mapper.DepartmentMapper;
import com.aoz.aozmeeting.service.DepartmentService;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Create by LiuYang on 2023/5/20 22:22
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {


    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentMapper departmentMapper;


    // 获取部门列表
    @RequestMapping("/getDepartmentList")
    public R getDepartmentList() {
        List<Department> departmentList = departmentMapper.selectList(null);
        return R.success(departmentList);
    }

    // 同步有度部门架构
    @GetMapping("/updateAllDepartment")
    public R<List<Department>> updateAllDepartment() throws ParamParserException, HttpRequestException, AESCryptoException {
        return departmentService.updateAllDepartment();
    }
}
