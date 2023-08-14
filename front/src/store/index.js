import { createStore } from 'vuex'

export default createStore({
  state: {
    // 登录状态为没登录
    isLogin: false,
    // 用户信息数据
    loginInfo:{
      name:null,
      type:null,
      dept:null,
      id:null
    },
  },
  getters: {
    isLogin(state) {
      if (!state.isLogin) {
        state.isLogin = sessionStorage.getItem('isLogin');   //从sessionStorage中读取状态
        state.loginInfo.name = sessionStorage.getItem('LoginName');
        state.loginInfo.type = sessionStorage.getItem('LoginType');
        state.loginInfo.dept = sessionStorage.getItem('LoginDept');
        state.loginInfo.id=sessionStorage.getItem('UserId');
      }
      return state.loginInfo
    }
  },
  mutations: {
    SET_LOGIN_STATUS(state, {isLoggedIn,user}) {
      // 登录
      // 先让登录状态变为登录了
      // 若只是改变state里的值，在强制刷新后会丢失，数据添加到sessionStorage里
      sessionStorage.setItem("isLogin",isLoggedIn)
      sessionStorage.setItem("LoginName", user["username"])
      sessionStorage.setItem("LoginType", user["type"])
      sessionStorage.setItem("LoginDept", user["departmentId"])
      sessionStorage.setItem("UserId", user["userId"])
      state.loginInfo.name=user["username"];
      state.loginInfo.type=user["type"];
      state.loginInfo.dept=user["departmentId"];
      state.loginInfo.id=user["userId"];
      state.isLogin=true;
    },

    // 登出
    LOGOUT(state) {
      // 这个同理
      sessionStorage.removeItem("isLogin")
      sessionStorage.removeItem("LoginName")
      sessionStorage.removeItem("LoginType")
      sessionStorage.removeItem("LoginDept")
      sessionStorage.removeItem("UserId")
      state.isLogin=false;
      state.loginInfo.name=null;
      state.loginInfo.dept=null;
      state.loginInfo.type=null;
      state.loginInfo.id=null;
    },
  },
  actions: {
    setLoginStatus({commit},{isLoggedIn,user}){
      commit('SET_LOGIN_STATUS',{isLoggedIn,user});
    },
    logOut({commit}){
      commit('LOGOUT');
    }
  },
  modules: {
  }
})
