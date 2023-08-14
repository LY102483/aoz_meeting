package com.aoz.aozmeeting.controller;

import com.aoz.aozmeeting.POJO.MeetingDevice;
import com.aoz.aozmeeting.POJO.MeetingRoom;
import com.aoz.aozmeeting.POJO.RoomDevice;
import com.aoz.aozmeeting.common.R;
import com.aoz.aozmeeting.mapper.MeetingDeviceMapper;
import com.aoz.aozmeeting.mapper.MeetingRoomMapper;
import com.aoz.aozmeeting.mapper.RoomDeviceMapper;
import com.aoz.aozmeeting.service.MeetingRoomService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/meetingRoom")
@RestController
public class MeetingRoomController {
    @Autowired
    private MeetingRoomMapper meetingRoomMapper;
    @Autowired
    private MeetingRoomService meetingRoomService;
    @Autowired
    private RoomDeviceMapper roomDeviceMapper;

    /***
     * 修改或删除会议室
     * @param meetingRoom
     * @return
     */
    @RequestMapping("/admin/update")
    public R updateMeetingRoom(Long[] deviceList,MeetingRoom meetingRoom) {
        System.out.println(meetingRoom);
        // 只要不是新增会议室，就都会删除会议室所对应的设备
        if (meetingRoom.getRoomId() != null) {
            // 删除会议室对应设备
            QueryWrapper<RoomDevice> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("room_id", meetingRoom.getRoomId());
            roomDeviceMapper.delete(queryWrapper);
            // 删除会议室
            if (meetingRoom.getIsDeleted() == 1) {
                // 删除会议室
                int delete = meetingRoomMapper.deleteById(meetingRoom.getRoomId());
                if (delete == 0) {
                    return R.error("删除失败");
                } else {
                    return R.success("删除成功");
                }
            }
        }


        // 修改或新增会议室
        // 修改会议室基本信息
        // 1.保存会议室信息
        boolean b = meetingRoomService.saveOrUpdate(meetingRoom);
        // 2.修改会议室设备清单
        if(deviceList!=null){
            for (int i = 0; i < deviceList.length; i++) {
                System.out.println("会议室ID"+meetingRoom.getRoomId());
                System.out.println("设备ID"+deviceList[i]);
                RoomDevice roomDevice=new RoomDevice();
                roomDevice.setDeviceId(deviceList[i]);
                roomDevice.setRoomId(meetingRoom.getRoomId());
                roomDeviceMapper.insert(roomDevice);
            }
        }
        // 修改会议室拥有设备信息
        if (b) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }
}
