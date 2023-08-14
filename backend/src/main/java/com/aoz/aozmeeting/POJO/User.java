package com.aoz.aozmeeting.POJO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * Create by LiuYang on 2023/5/20 20:10
 */
@Data
public class User {
    @TableId
    private Long userId;
    private Long gid;//有度的用户ID
    @TableField(exist = false)
    private String token;//有度单点登陆时提供的token
    private String phone;
    private String username;
    @TableField(select = false)
    private String password;//查询时不返回这个参数
    //部门ID
    private Long departmentId;
    //用户类型：0表示管理员，1表示用户
    private Integer type;
    // 用户是否被禁用：0表示正常，1表示被禁用
    private Integer status;
    //头像路径地址
    @TableField(exist = false)
    private String avatarUrl;

    @TableLogic
    private Integer isDeleted;
}
