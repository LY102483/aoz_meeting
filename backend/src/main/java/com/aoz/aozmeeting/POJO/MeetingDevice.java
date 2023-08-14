package com.aoz.aozmeeting.POJO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
public class MeetingDevice {
    @TableId
    private Long deviceId;
    private String deviceName;

    @TableLogic
    private Integer isDeleted;
}
