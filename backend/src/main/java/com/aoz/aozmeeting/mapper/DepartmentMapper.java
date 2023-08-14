package com.aoz.aozmeeting.mapper;

import com.aoz.aozmeeting.POJO.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * Create by LiuYang on 2023/5/20 22:17
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

    @Update("UPDATE department set  is_deleted=0 WHERE department_id=#{departmentId}")
    int undoDelete(Long departmentId);
}
