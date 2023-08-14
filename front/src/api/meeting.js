import axios from "../utils/request";
import path from "./path"

const meetingApi={
    //根据日期查询会议室状况
    selectMeetingOfDate(data){
        return axios.post(path.baseURL+path.selectMeetingOfDate,data)
    },
    //新建会议室预约申请
    createMeetingProject(data){
        return axios.post(path.baseURL+path.createMeetingProject,data)
    },
    //用户端查询自己的会议预约
    selectMyMeetingProjectList(data){
        return axios.post(path.baseURL+path.selectMyMeetingProjectList,data)
    },
    //更改会议预约状态
    updateMeetingStatus(data){
        return axios.post(path.baseURL+path.updateMeetingStatus,data)
    }
}

export default meetingApi
