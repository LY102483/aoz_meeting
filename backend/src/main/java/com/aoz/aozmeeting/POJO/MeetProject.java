package com.aoz.aozmeeting.POJO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.apache.ibatis.annotations.Select;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class MeetProject implements Serializable {


    @TableId
    private Long meetId;

    /**
    * 会议标题
    */
    private String meetTitle;

    /**
    * 需要使用的会议室ID
    */
    private Long roomId;

    /**
    * 会议日期
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTime;

    /**
    * 会议开始时间Integer(半小时为1单位,如00:00则为0)
    */
    private Integer startTimeInteger;

    /**
    * 会议结束时间Integer(半小时为1单位,如00:30则为1)
    */
    private Integer endTimeInteger;
    /**
     * 会议开始时间(HH:mm)
     */
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    /**
     * 会议结束时间(HH:mm)
     */

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    /**
    * 会议室使用申请人ID
    */
    private Long userId;
    /**
     * 会议室使用申请人姓名
     */
    private String userName;

    /**
    * 会议描述
    */
    private String meetInfo;

    /**
    * 会议室申请状态(0表示审批中，1表示同意，2表示拒绝，3表示取消)
    */
    private Integer meetStatus;

    /**
    * 提交申请的时间
    */
    private LocalDateTime createTime;

    /**
    * 审批时间
    */
    private LocalDateTime updateTime;

    /**
    * 审批意见
    */
    private String approvalInfo;

    @TableLogic
    private Integer isDeleted;

}
