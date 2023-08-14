package com.aoz.aozmeeting.mapper;

import com.aoz.aozmeeting.POJO.MeetProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author LiuYang
* @description 针对表【meet_project】的数据库操作Mapper
* @createDate 2023-05-31 10:15:21
* @Entity com.aoz.aozmeeting.POJO.MeetProject
*/
@Mapper
public interface MeetProjectMapper extends BaseMapper<MeetProject> {


}




