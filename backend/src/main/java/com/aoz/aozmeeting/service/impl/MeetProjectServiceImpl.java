package com.aoz.aozmeeting.service.impl;

import com.aoz.aozmeeting.POJO.MeetingRoom;
import com.aoz.aozmeeting.mapper.MeetingRoomMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aoz.aozmeeting.POJO.MeetProject;
import com.aoz.aozmeeting.service.MeetProjectService;
import com.aoz.aozmeeting.mapper.MeetProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author LiuYang
* @description 针对表【meet_project】的数据库操作Service实现
* @createDate 2023-05-31 10:15:21
*/
@Service
public class MeetProjectServiceImpl extends ServiceImpl<MeetProjectMapper, MeetProject>
    implements MeetProjectService{

    @Autowired
    private MeetingRoomMapper meetingRoomMapper;

    @Override
    public List<MeetingRoom> selectMeetingOfDate(String selectDate) {
        //返回的结果集
        List<MeetingRoom> res=new ArrayList<>();
        //查询有哪些会议室
        List<MeetingRoom> meetingRooms = meetingRoomMapper.selectList(null);
        //查询对应的会议室设备和会议时间安排
        for (MeetingRoom meetingRoom : meetingRooms) {
            //查询当前会议室有哪些设备
            meetingRoom.setDevices(meetingRoomMapper.selectDevicesByRoomId(meetingRoom.getRoomId()));
            //查询当前会议室的时间安排
            QueryWrapper<MeetProject> meetProjectQueryWrapper = new QueryWrapper<>();
            meetProjectQueryWrapper
                    .select("meet_status", "start_time", "end_time", "date_Time","start_time_integer","end_time_integer")
                    .eq("room_id", meetingRoom.getRoomId())
                    .eq("meet_status", 1)
                    .eq("date_Time", selectDate);
            List<MeetProject> meetProjectList = this.list(meetProjectQueryWrapper);
            meetingRoom.setProjects(meetProjectList);
            meetingRoom.setSelectDate(selectDate);
            res.add(meetingRoom);
        }
        return res;
    }
}




