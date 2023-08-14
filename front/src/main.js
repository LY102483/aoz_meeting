import { createApp } from 'vue'
import App from './App.vue'
const app = createApp(App)
import './registerServiceWorker'
import router from './router'
import store from './store'

// 引入element-plus
import ElementPlus from 'element-plus'  //引入element-plus库
import 'element-plus/dist/index.css'  //引入element-plus样式
//引入element-icon
import elementIcon from "./plugins/icons";
// 引入axios并配置
import axios from 'axios'
app.config.globalProperties.$axios = axios
window.axios=axios
app.config.productionTip = false


app.use(store).use(router).use(ElementPlus).use(elementIcon).mount('#app')
