import axios from "../utils/request";
import path from "./path"

const departmentApi={
    //获取所有部门信息
    getDepartmentList(){
        return axios.post(path.baseURL+path.getDepartmentList)
    },
    //从有度同步部门信息
    updateAllDepartment(){
        return axios.get(path.baseURL+path.updateAllDepartment)

    }
}

export default departmentApi
