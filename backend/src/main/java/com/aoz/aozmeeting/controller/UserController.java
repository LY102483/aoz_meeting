package com.aoz.aozmeeting.controller;


import com.aoz.aozmeeting.POJO.Department;
import com.aoz.aozmeeting.POJO.User;
import com.aoz.aozmeeting.common.R;
import com.aoz.aozmeeting.mapper.DepartmentMapper;
import com.aoz.aozmeeting.mapper.UserMapper;
import com.aoz.aozmeeting.service.DepartmentService;
import com.aoz.aozmeeting.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import im.youdu.sdk.client.IdentifyClient;
import im.youdu.sdk.client.OrgClient;
import im.youdu.sdk.entity.Const;
import im.youdu.sdk.entity.UserInfo;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Create by LiuYang on 2023/5/20 19:56
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdentifyClient identifyClient;
    @Autowired
    private YDApp ydApp;

    @Value("${aoz.avatarPath}")
    private String basePath;

    /**
     * 普通登录
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(User user, HttpSession session) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return R.error("账号或密码错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        queryWrapper.eq("password", DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        User loginUser = userService.getOne(queryWrapper);
        if (loginUser == null) {
            return R.error("账号或密码错误");
        } else if (loginUser.getStatus() == 1) {
            return R.error("账号已被禁用");
        } else {
            session.setAttribute("userId", loginUser.getUserId());
            session.setAttribute("type", loginUser.getType());
            session.setAttribute("username", loginUser.getUsername());
            return R.success(loginUser);
        }
    }

    /**
     * 基于有度的快捷登录
     *
     * @param user
     * @param session
     * @return
     * @throws ParamParserException
     * @throws ServiceException
     * @throws HttpRequestException
     * @throws AESCryptoException
     */
    @PostMapping("/quickLogin")
    public R<User> quickLogin(User user, HttpSession session) {
        // 首先判断是否为通过有度进行登录
        try {
            if (true) { // 临时设置
                // 有度返回的用户信息
                User idetifyUser = identifyClient.idetify(user.getToken());
                // 假如有度返回了用户信息
                if (idetifyUser != null) {
                    // 检查会议系统用户数据库是否有对应的用户
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    // 有度单点登录成功后只返回用户账号(姓名)
                    OrgClient orgClient = new OrgClient(ydApp);
                    UserInfo userInfo = orgClient.getUserInfo(idetifyUser.getUsername());// 有度返回的用户信息
                    User loginUser = userService.getOne(queryWrapper.eq("gid", idetifyUser.getGid()));
                    // 假设当前快捷登录用户不在数据库中，则进行保存
                    if (loginUser == null) {
                        loginUser = new User();
                        loginUser.setUsername(userInfo.getUserId());
                        loginUser.setDepartmentId((long) userInfo.getDept()[0]);
                        loginUser.setGid(userInfo.getGid());
                        // 先查询用户部门是否存在
                        Department department = departmentMapper.selectById(loginUser.getDepartmentId());
                        if (department == null) {
                            departmentService.updateAllDepartment();
                            return R.error("无法找到用户所对应的部门，请在稍后重试！");
                        } else {
                            boolean save = userService.save(loginUser);
                            if (!save) {
                                return R.error("用户注册失败");
                            }
                        }
                        // 下载头像
                        String path = orgClient.downloadUserAvatarAndSave(userInfo.getUserId(), Const.Avatar_Small, basePath);
                    } else if (loginUser.getStatus() == 1) {
                        return R.error("账号已被禁用");
                    }
                    // 保存登录信息
                    session.setAttribute("userId", loginUser.getUserId());
                    session.setAttribute("type", loginUser.getType());
                    session.setAttribute("username", loginUser.getUsername());
                    return R.success(loginUser);

                }
            }
        } catch (Exception ignored) {
            return R.error("快捷登陆失败:" + ignored.getMessage());
        }
        return R.error("非法快捷登陆");
    }


    @PostMapping("/updateUserInfo")
    public R<User> updateUserInfo(HttpSession session) throws ParamParserException, HttpRequestException, AESCryptoException, FileIOException {
        String username = (String) session.getAttribute("username");
        try {
            OrgClient orgClient = new OrgClient(ydApp);
            UserInfo userInfo = orgClient.getUserInfo(username);// 有度返回的用户信息
            User loginUser = new User();
            loginUser.setUsername(userInfo.getUserId());
            loginUser.setDepartmentId((long) userInfo.getDept()[0]);
            loginUser.setGid(userInfo.getGid());
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("gid", userInfo.getGid());
            int res = 0;
            try {
                res = userMapper.update(loginUser, queryWrapper);
                String path = orgClient.downloadUserAvatarAndSave(userInfo.getUserId(), Const.Avatar_Small, basePath);
            } catch (Exception e) {
                // 执行部门同步，方便下一次更新用户信息
                departmentService.updateAllDepartment();
                return R.error("同步到数据库失败，请重试");
            }
            if (res != 0) {
                return R.success(loginUser);
            } else {
                return R.error("同步到数据库失败，请重试");
            }
        } catch (HttpRequestException httpRequestException) {
            return R.error("从有度同步用户信息失败！");
        }
    }

    @PostMapping("/logOut")
    public R logout(HttpSession session) {
        session.removeAttribute("username");
        session.removeAttribute("type");
        session.removeAttribute("userId");
        return R.success("已退出登陆");
    }

    @PostMapping("/resetPassword")
    public R resetPassword(User user, HttpSession httpSession) {
        String password = user.getPassword();
        String userId = String.valueOf(user.getUserId());
        // 因为管理员和用户使用的同一个接口
        if (httpSession.getAttribute("type") == null && !httpSession.getAttribute("userId").equals(user.getUserId())) {
            return R.error("无修改权限");
        }
        if (StringUtils.isNotBlank(password) && password.length() >= 6 && StringUtils.isNotBlank(userId)) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            int i = userMapper.updateById(user);
            if (i != 0) {
                return R.success("修改成功");
            } else {
                return R.error("修改失败");
            }
        } else {
            return R.error("密码长度需大于6位！");
        }
    }
}
