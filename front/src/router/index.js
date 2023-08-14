import {createRouter, createWebHistory} from 'vue-router'
import loginView from "@/views/LoginView.vue";
import notFound from "@/views/NotFound.vue";

const routes = [
    {
        path: '/',
        name: 'login',
        meta: {
            title: '登陆'
        },
        component: loginView
    },
    {
        path: '/quickLogin/:token?',
        name: 'quickLogin',
        meta: {
            title: '登陆'
        },
        component: loginView
    },
    {
        path: '/home',
        name: 'home',
        meta: {
            title: '首页'
        },
        component: () => import(/* webpackChunkName: "about" */ '../views/HomeView.vue')
    },
  //所有路径放在404页面之前
  {
    path: '/404',
    name: 'NotFound',
    meta: {
      title: 'Page not found'
    },
    component: notFound
  },

  // 所有未定义路由，全部重定向到404页，必须放在最后
  {
    path: '/:pathMatch(.*)',

    redirect: '/404'
  }


]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})


//这个方法是为了每个页面添加属于自己的title
router.beforeEach((to,from,next)=>{
    if(to.meta.title){
        document.title=to.meta.title
    }
    next()
})


export default router

