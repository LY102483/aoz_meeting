// 网络请求封装
import axios from "../utils/request";
import path from "./path"

const userApi={
    login(data){
        return axios.post(path.baseURL+path.login,data)
    },
    quickLogin(data){
        return axios.post(path.baseURL+path.quickLogin,data)
    },
    updateUserInfo(){
        return axios.post(path.baseURL+path.updateUserInfo)
    },
    logOut(){
        return axios.post(path.baseURL+path.logOut)
    },
    getAvatar(){
        return axios.get(path.baseURL+path.getAvatar,{responseType:'blob'})
    },
    resetPassword(data){
        return axios.post(path.baseURL+path.resetPassword,data)
    }
}

export default userApi
