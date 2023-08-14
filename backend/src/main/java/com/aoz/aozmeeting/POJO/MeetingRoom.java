package com.aoz.aozmeeting.POJO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.List;

@Data
public class MeetingRoom {
    @TableId
    private Long roomId;
    private String roomName;
    private Integer userNum;//可容纳的人数

    // @TableField(exist = false)：表示该属性不为数据库表字段，但又是必须使用的。
    @TableField(exist = false)
    private List<MeetingDevice> devices;//当前会议室有哪些设备

    @TableField(exist = false)
    private String selectDate;//选中的会议室日期

    @TableField(exist = false)
    private List<MeetProject> projects;//当前会议室有哪些会议

    @TableLogic
    private Integer isDeleted;

}
