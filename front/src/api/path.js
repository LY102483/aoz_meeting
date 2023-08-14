// 网络请求封装
const base ={
    //公共网址
    // 例如有一个请求的完整地址为：http://iwenwiki.com/api/blueberrypai/getChengpinDetails.php
    // baseURL:"http://172.168.1.91:8082/api/v1/",
    // VScode调试端口
    // baseURL:"https://jc.ly1024.top:86/proxy/8081/api/v1/",
    //华阳调试端口
    // baseURL:"http://jc.ly1024.top:8091/api/v1/",
    //本地调试端口
    baseURL:"http://meet.aoz.com:8082/api/v1/",

    login:"user/login",
    selectMeetingOfDate:"meeting/selectMeetingOfDate",
    createMeetingProject:"meeting/createMeetingProject",
    selectMyMeetingProjectList:"meeting/selectMyMeetingProjectList",
    quickLogin:"user/quickLogin",
    getDepartmentList:"department/getDepartmentList",
    updateAllDepartment:"department/updateAllDepartment",
    updateMeetingStatus:"meeting/updateMeetingStatus",
    updateUserInfo:"user/updateUserInfo",
    logOut:"user/logOut",
    getAvatar:"common/download",
    resetPassword:"user/resetPassword",
    updateMeetingRoom:"/meetingRoom/admin/update",
    getAllDevices:"/meetingDevice/getAllDevices"
}

export default base;
