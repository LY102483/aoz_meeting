package com.aoz.aozmeeting.service;

import com.aoz.aozmeeting.POJO.MeetProject;
import com.aoz.aozmeeting.POJO.MeetingRoom;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author LiuYang
* @description 针对表【meet_project】的数据库操作Service
* @createDate 2023-05-31 10:15:21
*/
public interface MeetProjectService extends IService<MeetProject> {
    List<MeetingRoom> selectMeetingOfDate(String selectDate);
}
