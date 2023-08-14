import axios from "../utils/request";
import path from "./path"

const meetingRoomApi={
    //删除或更新某个会议室
    updateMeetingRoom(data){
        return axios.post(path.baseURL+path.updateMeetingRoom,data)
    },

    //获取会议设备 列表
    getAllDevices(){
        return axios.post(path.baseURL+path.getAllDevices)
    }

}

export default meetingRoomApi
