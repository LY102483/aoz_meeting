package com.aoz.aozmeeting.controller;


import com.aoz.aozmeeting.POJO.MeetingDevice;
import com.aoz.aozmeeting.common.R;
import com.aoz.aozmeeting.mapper.MeetingDeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meetingDevice")
public class MeetingDeviceController {
    @Autowired
    private MeetingDeviceMapper meetingDeviceMapper;

    @RequestMapping("/getAllDevices")
    public R<List<MeetingDevice>> getAllDevices(){
        return R.success(meetingDeviceMapper.selectList(null));
    }
}
