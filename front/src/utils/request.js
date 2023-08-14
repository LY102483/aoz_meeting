import axios from "axios";
import querystring from "querystring"
import { ElMessage } from "element-plus";
import {mapState,mapActions,mapGetters,mapMutations} from "vuex";

//axios每次请求都不会默认携带cookie,因为我们采用了session+cookie进行验证，所以需配置transpileDependencies
axios.defaults.withCredentials=true
// 参考文档：https://www.kancloud.cn/yunye/axios/234845
const errorHandle = (status, info) => {
    switch (status) {
        case 400:
            console.log("语义有误");
            ElMessage.error('参数有误')
            break;
        case 401:
            console.log("服务器认证失败");
            ElMessage.error('服务器认证失败')
            break;
        case 403:
            console.log("服务器拒绝访问");
            ElMessage.error('服务器拒绝访问')
            break;
        case 404:
            console.log("地址错误")
            ElMessage.error('找不到请求地址')
            break;
        case 500:
            console.log("服务器遇到意外，可能是请求参数不正确")
            ElMessage.error('服务器遇到意外')
            break;
        case 502:
            console.log("服务器无响应")
            ElMessage.error('服务器无响应')
            break;
        default:
            console.log(info)
            ElMessage.error(info)
            break;
    }
}

const instance = axios.create({
    //网络请求的公共配置
    timeout: 5000
})

// 拦截器最常用的。
//发送数据之前
instance.interceptors.request.use(
    config => {
        if (config.method === "post") {
            config.data = querystring.stringify(config.data)
        }
        //config：包含着网络请求的所有信息
        return config;
    },
    error => {
        return Promise.reject(error)
    }
)

//获取数据之前
instance.interceptors.response.use(
    response => {
        return response.status === 200 ? Promise.resolve(response) : Promise.reject(response)
    },
    error => {
        try{
            const { response } = error;
            console.log(error)
            //     //错误的处理才是我们需要关注的
            errorHandle(response.status,response.info)
        }catch (e) {
            ElMessage.error("请求无权限或服务器无响应");
        }
    }
)

export default instance;
