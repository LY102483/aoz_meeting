package com.aoz.aozmeeting.service.impl;

import com.aoz.aozmeeting.POJO.User;
import com.aoz.aozmeeting.mapper.UserMapper;
import com.aoz.aozmeeting.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Create by LiuYang on 2023/5/20 21:25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
