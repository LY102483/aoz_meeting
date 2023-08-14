package com.aoz.aozmeeting.service.impl;

import com.aoz.aozmeeting.POJO.Department;
import com.aoz.aozmeeting.common.R;
import com.aoz.aozmeeting.mapper.DepartmentMapper;
import com.aoz.aozmeeting.service.DepartmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import im.youdu.sdk.client.OrgClient;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by LiuYang on 2023/5/20 22:19
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    @Autowired
    private YDApp ydApp;
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    @Async
    public R<List<Department>> updateAllDepartment() throws ParamParserException, HttpRequestException, AESCryptoException {
        //逻辑删除掉现有的组织架构
        QueryWrapper<Department> queryWrapper=new QueryWrapper<>();
        queryWrapper.ne("department_id",0);
        this.remove(queryWrapper);
        //从有度获取组织架构
        OrgClient orgClient=new OrgClient(ydApp);
        List<Department> depts = orgClient.listDeptAllChildren(0);
        //遍历有度返回的组织架构
        for (Department dept : depts) {
            //首先对数据表中的数据取消逻辑删除
            int i = departmentMapper.undoDelete(dept.getDepartmentId());
            //假设不能进行逻辑删除，则说明这个部门是新的，则进行新增操作
            //有可能部门ID没有变动但是部门名称变动了，所以还是要再执行一次更新操作
            boolean b =this.saveOrUpdate(dept);
            if(!b){
                return R.error("同步发生错误");
            }
        }
        return R.success(depts);
    }
}
