package com.aoz.aozmeeting.mapper;

import com.aoz.aozmeeting.POJO.MeetingDevice;
import com.aoz.aozmeeting.POJO.MeetingRoom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MeetingRoomMapper extends BaseMapper<MeetingRoom> {


    /***
     * 多表联查，根据RoomID查询该会议室对应的设备
     * @param roomId
     * @return
     */
    @Select("SELECT md.device_id,md.device_name  FROM meeting_device md,room_device rd WHERE  rd.room_id=#{roomId} and rd.device_id=md.device_id")
    List<MeetingDevice> selectDevicesByRoomId(Long roomId);
}
