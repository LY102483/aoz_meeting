package com.aoz.aozmeeting.POJO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * Create by LiuYang on 2023/5/20 22:16
 */
@Data
public class Department {
    @TableId
    private Long departmentId;
    private String departmentName;
    private Integer departmentSortId;

    @TableLogic
    private Integer isDeleted;
}
