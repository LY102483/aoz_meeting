<template >
  <div class="common-layout" v-loading="quickLoginLoad">
    <div class="card">
      <el-container>
        <el-header>
          <img src="../assets/logoBig.png" height="50" alt="傲中科技">
          <h3>傲中科技会议室预约系统</h3>
        </el-header>
        <el-main>
          <p>姓名:</p>
          <el-input
              v-model.trim="username"
              type="text"
              placeholder=""
              clearable
              @keyup.enter.up="submitForm"
          />
          <p>密码:</p>
          <el-input
              v-model.trim="password"
              type="password"
              placeholder=""
              show-password clearable
              @keyup.enter.up="submitForm"
          />
          <el-button type="primary" @click="submitForm" >登陆</el-button>
        </el-main>
      </el-container>
    </div>
<!--    <div class="footer">我没有账号，现在去<a href="https://localhost">创建一个!</a></div>-->
  </div>
</template>

<script>



import {ElMessage} from "element-plus";
import userApi from "@/api/user";
import querystring from "querystring";
import {Loading} from "@element-plus/icons-vue";
import {mapState,mapActions,mapGetters,mapMutations} from "vuex";

export default {
  name: 'LoginView',
  data() {
    return {
      // 这里放数据
      username: "",
      password: "",
      resData:null,
      user:{
        token:null,
      },
      quickLoginLoad:false,//快捷登录蒙层
    }
  },
  methods: {
    // 登陆方法
    submitForm(){
      if(this.password==="" || this.username===""){
        ElMessage.error('手机号或密码不能为空！')
      }else {
        userApi.login({
          password:this.password,
          username:this.username}).then(res=>{
          this.resData=res.data;
          if(this.resData["code"]===0){
            ElMessage.error('账号或密码错误')
          }else{
            ElMessage({
              message: '登陆成功',
              type: 'success',
            })
            this.$store.dispatch('setLoginStatus',{isLoggedIn:true,user:res.data.data})
            this.$router.push('/home')
          }
        })
      }
    }
  },
  components: {Loading},
  mounted() {
    this.user.token=this.$route.params.token;
    if(this.user.token!=null&&this.user.token!==""){
      this.quickLoginLoad=true;
      ElMessage({
        message:'快捷登录中',
        type:'warning',
      })
      userApi.quickLogin({token:this.user.token}).then(res=>{
        this.quickLoginLoad=false;
        if(res.data["code"]===0){
          ElMessage.error(res.data.msg)
        }else if (res.data["code"]===1){
          ElMessage({
            message: '快捷登陆成功',
            type: 'success',
          })
          this.$store.dispatch('setLoginStatus',{isLoggedIn:true,user:res.data.data})
          this.$router.push('/home')
        }else{
          ElMessage({
            message: '无法连接到后端服务器',
            type: 'error',
          })
          this.quickLoginLoad=false;
        }
      })
    }
  }
}
</script>
<style scoped>
.card {
  border: 1px solid rgb(221, 225, 230);
  border-radius: 5px;
  padding: 20px 40px 40px 40px;
  width: 400px;
  background-color: white;
//margin: auto;
}
.el-input{
  margin-bottom: 20px;
}
p{
  text-align: left;
  color: #606266;
  font-size: 14px;
  padding-bottom: 5px;
}
.el-header {
  height: 100%;
  border-bottom: 1px solid rgb(221, 225, 230);
  padding-bottom: 10px;
}

form {

}

.el-main {
  margin-top: 10px;
}

.footer {
  color: rgb(102, 115, 130);
  font-size: 14px;
  margin-top: 10px;
}

button {
  width: 100%;
  background-color: rgb(32, 107, 196);
  margin-top: 30px;
}

.common-layout {
  background-color: #f4f6fa;
  height: 100vh;
  min-width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}


</style>
