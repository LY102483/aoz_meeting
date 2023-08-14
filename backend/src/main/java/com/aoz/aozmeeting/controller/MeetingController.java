package com.aoz.aozmeeting.controller;


import com.aoz.aozmeeting.POJO.MeetProject;
import com.aoz.aozmeeting.POJO.MeetingRoom;
import com.aoz.aozmeeting.common.R;
import com.aoz.aozmeeting.mapper.MeetProjectMapper;
import com.aoz.aozmeeting.mapper.MeetingRoomMapper;
import com.aoz.aozmeeting.service.MeetProjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import im.youdu.sdk.client.AppClient;
import im.youdu.sdk.entity.YDApp;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/meeting")
@EnableScheduling//开启定时
@EnableAsync//配置多线程
public class MeetingController {
    @Autowired
    private MeetingRoomMapper meetingRoomMapper;
    @Autowired
    private MeetProjectService meetProjectService;
    @Autowired
    private MeetProjectMapper meetProjectMapper;
    @Autowired
    private YDApp ydApp;
    @Value("${youdu.admin}")
    private String admin;

    /**
     * 查询某个指定日期的所有会议室信息与状况(会议室名称、可容纳人数、使用状态、有哪些可用设备)
     * @param selectDate 搜索的日期
     * @return 返回对应日期的会议室的信息和状况
     */
    @PostMapping("/selectMeetingOfDate")
    public R<List<MeetingRoom>> selectMeetingOfDate(String selectDate){
        return R.success(meetProjectService.selectMeetingOfDate(selectDate));
    }


    /**
     * 新建一个会议室预约申请
     * @param meetProject 前端封装的会议对象
     * @return 返回信息
     */
    @RequestMapping("/createMeetingProject")
    public R createMeetingProject(MeetProject meetProject, HttpSession session) throws ParamParserException, HttpRequestException, AESCryptoException {
        //设置申请会议室人的ID
        meetProject.setUserId((Long) session.getAttribute("userId"));
        //设置创建时间
        meetProject.setCreateTime(LocalDateTime.now());
        meetProject.setUserName((String) session.getAttribute("username"));
        boolean save = meetProjectService.save(meetProject);
        if(save){
            AppClient appClient=new AppClient(ydApp);
            appClient.sendTextMsg(admin,null,"您收到一条新的会议申请，请前往会议预约系统查看。");
            return R.success("预定成功");
        }else {
            return R.error("发生异常错误，请联系系统管理员处理");
        }
    }

    /***
     * 查询会议列表(管理员和用户共用一个接口)
     * @param page 页码
     * @param httpSession
     * @return
     */
    @RequestMapping("/selectMyMeetingProjectList")
    public R<Page<MeetProject>> selectMyMeetingProjectList(int page,int type,int orderBy,HttpSession httpSession){
        Page<MeetProject> myMeetingProject=new Page<>(page,10);
        QueryWrapper<MeetProject> queryWrapper=new QueryWrapper<>();
//        type=0查自己1查全部
//        用户查自己
        if(httpSession.getAttribute("type")==null){
             queryWrapper.eq("user_id",httpSession.getAttribute("userId"));
        } else if (httpSession.getAttribute("type")!=null&&type==0) {
            //管理员查自己
            queryWrapper.eq("user_id",httpSession.getAttribute("userId"));
        }

        if(orderBy==1){
            //根据会议申请时间进行排序
            queryWrapper.orderByDesc("create_time");
        }else{
            //根据会议时间进行排序
            queryWrapper.orderByDesc("date_time");
        }

        meetProjectService.page(myMeetingProject, queryWrapper);
        myMeetingProject.getTotal();//总数
        List<MeetProject> rs = myMeetingProject.getRecords();//结果
        return R.success(myMeetingProject);
    }

    @RequestMapping("/admin/test")
    public R test(){
        return R.success("管理员权限测试！");
    }

    /**
     * 更改会议申请状态(管理员和用户共用一个接口)
     */
    @RequestMapping("/updateMeetingStatus")
    public R updateUserMeetingProject(MeetProject meetProject,HttpSession httpSession) throws ParamParserException, HttpRequestException, AESCryptoException {
        AppClient appClient=new AppClient(ydApp);
        //设置审核时间
        meetProject.setUpdateTime(LocalDateTime.now());
        //假如是撤回申请
        if(meetProject.getMeetStatus()==3){
            //如果是撤回申请，需要判断是否为本人的撤回申请
            QueryWrapper<MeetProject> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("meet_id",meetProject.getMeetId());
            queryWrapper.eq("user_id",httpSession.getAttribute("userId"));
            MeetProject check = meetProjectMapper.selectOne(queryWrapper);
            if(check!=null){
                int i = meetProjectMapper.updateById(meetProject);
                if(i!=0){
                    return R.success("撤回会议申请成功");
                }
            }
            return R.error("修改状态失败");
        }
        //如果非撤回
        if(meetProject.getMeetStatus()!=3&&httpSession.getAttribute("type")==null){
                return R.error("非法请求");
        }else{
            int i = meetProjectMapper.updateById(meetProject);
            if(i!=0){
                meetProject = meetProjectMapper.selectById(meetProject);
                appClient.sendTextMsg(meetProject.getUserName(),null,"您有一条会议申请状态被改变，请前往会议预约系统查看。");
                return R.success("更改成功");
            }else{
                return R.error("修改失败");
            }
        }
    }

    @Scheduled(cron = "0 50 * * * *")
    @Scheduled(cron = "0 20 * * * *")
    public void informMeetingProject() throws ParamParserException, HttpRequestException, AESCryptoException {
        AppClient appClient=new AppClient(ydApp);
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime=localDateTime.plusMinutes(10);
        String time=localDateTime.getHour()+":"+localDateTime.getMinute();
        QueryWrapper<MeetProject> queryWrapper1=new QueryWrapper<>();
        queryWrapper1.eq("date_time",localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryWrapper1.eq("start_time", time);
        queryWrapper1.eq("meet_status","1");
        List<MeetProject> meetProjects1 = meetProjectMapper.selectList(queryWrapper1);
        if(meetProjects1!=null){
            if(meetProjects1.size()!=0){
                appClient.sendTextMsg(admin,null,"请注意，有会议即将开始。");
            }
        }
        QueryWrapper<MeetProject> queryWrapper2=new QueryWrapper<>();
        queryWrapper2.eq("end_time", time);
        queryWrapper2.eq("date_time",localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryWrapper2.eq("meet_status","1");
        List<MeetProject> meetProjects2 = meetProjectMapper.selectList(queryWrapper2);
        if(meetProjects2!=null){
            if(meetProjects2.size()!=0){
                appClient.sendTextMsg(admin,null,"请注意，有会议即将结束。");
            }
        }
    }
}