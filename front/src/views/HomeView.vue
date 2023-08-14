<!--  Created by LiuYang On 2023/5/27 01:30  -->
<template>
  <!--                          修改密码模态框-->
    <el-dialog
            v-model="resetPasswordModal.modal"
            title="修改登陆密码"
            width="20%" style="min-width:400px;"
    >
        <el-form label-width="100px"  center>
            <el-form-item label="密码:">
                <el-input v-model.trim="resetPasswordModal.password"
                          type="password" placeholder="此密码非有度密码"
                          show-password clearable/>
            </el-form-item>
            <el-form-item label="确认密码:">
                <el-input v-model.trim="resetPasswordModal.rePassword"
                          type="password" placeholder="此密码非有度密码"
                          show-password clearable/>
            </el-form-item>
        </el-form>
        <template #footer>
                              <span class="dialog-footer">
                                <el-button @click="resetPasswordModal.modal = false">取消</el-button>
                                <el-button type="primary" @click="resetPassword">
                                  提交
                                </el-button>
                              </span>
        </template>
    </el-dialog>
  <!--                          修改密码模态框-->
    <div class="common-layout">
        <el-row justify="center" style="background-color: white">
            <el-col :span="18">
                <el-header>
                    <el-row>
                        <div><img src="../assets/logoTitle.jpg" height="40"></div>
                        <div>

                            <el-row>
                                <el-avatar shape="square" :src="squareUrl" :size="40"/>
                                <div>
                                    <el-row>
                                        <span>{{ userInfo.name }}</span>
                                        <el-dropdown placement="top">
                                  <span class="el-dropdown-link" style="display: flex;align-items: center">
                                      <el-icon class="el-icon--right">
                                        <arrow-down/>
                                      </el-icon>
                                  </span>
                                            <template #dropdown>
                                                <el-dropdown-menu>
                                                    <el-dropdown-item @click="updateUserInfo">
                                                        <el-icon color="rgb(64,158,255)">
                                                            <User/>
                                                        </el-icon>
                                                        同步个人信息
                                                    </el-dropdown-item>
                                                    <el-dropdown-item @click="updateDepartmentInfo" v-if="userInfo.type!=='null'&&userInfo.type!==null">
                                                        <el-icon color="rgb(64,158,255)">
                                                            <User/>
                                                        </el-icon>
                                                        同步有度组织架构
                                                    </el-dropdown-item>
                                                    <el-dropdown-item @click="resetPasswordModal.modal=true">
                                                        <el-icon color="red">
                                                            <Lock/>
                                                        </el-icon>
                                                        修改登陆密码
                                                    </el-dropdown-item>
                                                    <el-dropdown-item @click="logout">
                                                        <el-icon color="red">
                                                            <SwitchButton/>
                                                        </el-icon>
                                                        退出登陆
                                                    </el-dropdown-item>
                                                </el-dropdown-menu>
                                            </template>
                                        </el-dropdown>

                                    </el-row>
                                    <el-row><span>{{ departmentName }}</span></el-row>
                                </div>
                            </el-row>
                        </div>
                    </el-row>
                </el-header>
            </el-col>
        </el-row>
        <el-row justify="center">
            <el-col :span="20">
                <el-container>
                    <el-main>
                        <el-tabs type="border-card">
                            <UserPage></UserPage>
                        </el-tabs>
                    </el-main>
                </el-container>
                <el-footer>
                    <div> @傲中科技 2023</div>
                </el-footer>
            </el-col>
        </el-row>
    </div>
</template>

<style scoped>
/* 这里放组件样式 */

.el-main {
    padding-top: 20px;
}

.common-layout {
    background-color: #f4f6fa;
    height: 100vh;
    min-width: 470px;
}

.el-tabs {
    height: 770px;
}


.el-header span {
    color: #182433;
    font-size: 13px;
    margin: 2px;
}

.el-header {
    height: 100%;
    background-color: white;
    padding: 5px 0 5px 0;
    border-bottom: #dcdfe6 1px solid;
}

.el-header > .el-row {
    justify-content: space-between;
}

</style>

<script>
import {ElMessage} from "element-plus";
import {defineComponent} from 'vue';
import querystring from "querystring";
import {User} from "@element-plus/icons-vue";
import UserPage from "@/components/UserPage.vue";
import {mapGetters, mapState} from "vuex";
import departmentApi from "@/api/department";
import userApi from "@/api/user";

export default defineComponent({
    name: 'HomeView',
    components: {UserPage, User},
    data() {
        return {
            user: '',
            loginInfo: null,
            departmentList: null,
            departmentName: "",
            squareUrl: 'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png',
            resetPasswordModal: {
                modal: false, //模态框开关
                password: null,
                rePassword: null,
            }
        }
    },
    methods: {
        updateUserInfo() {
            userApi.updateUserInfo().then(res => {
                if (res.data.code == 1) {
                    this.$store.dispatch('logOut')
                    userApi.logOut().then(res => {
                        this.$router.push('/')
                    })
                    ElMessage.success("同步成功,请重新登陆")
                } else {
                    ElMessage.error(res.data.msg)
                }
            })
        },
        logout() {
            this.$store.dispatch('logOut')
            userApi.logOut().then(res => {
                ElMessage.success("已退出登陆")
                this.$router.push('/')
            })
        },
        resetPassword() {
            if (this.resetPasswordModal.rePassword !== this.resetPasswordModal.password) {
                ElMessage.error("两个密码不一致,请重试！")
            } else {
                console.log(this.resetPasswordModal.password)
                console.log(this.userInfo.id)
                userApi.resetPassword({
                    password: this.resetPasswordModal.password,
                    userId: this.userInfo.id
                }).then(res => {

                    if (res.data.code === 0) {
                        ElMessage.error(res.data.msg)
                    } else {
                        ElMessage.success("修改成功")
                        this.resetPasswordModal.modal = false;
                    }
                })
            }
        },
        updateDepartmentInfo(){
          departmentApi.updateAllDepartment().then(res => {
              if (res.data.code === 0) {
                  ElMessage.error(res.data.msg)
              } else {
                  ElMessage.success("同步成功，下次登陆后生效!")
              }
          })
        }
    },
    computed: {
        ...mapGetters(['isLogin']),
        userInfo() {
            return this.isLogin;
        },

    },
    beforeCreate() {
        // 组件实例被创建之前
    },
    created() {
        // 组件实例已经被创建，但DOM尚未生成
    },
    beforeMount() {
        // 组件实例被挂载之前
    },
    mounted() {
        // 检查用户是否登录
        if (this.userInfo.name === null) {
            ElMessage({
                message: '您还未登录！！！',
                type: 'error',
            })
            this.$router.push('/')
        } else {
            //获取部门列表
            departmentApi.getDepartmentList().then(res => {
                try {
                    this.departmentList = res.data.data
                    for (let i = 0; i < this.departmentList.length; i++) {
                        if (this.departmentList[i]["departmentId"] === this.userInfo.dept) {
                            this.departmentName = this.departmentList[i].departmentName
                            break
                        }
                    }
                } catch (e) {
                    ElMessage.error("登陆已失效");
                    this.$store.dispatch('logOut')
                    this.$router.push('/')
                }
            })
            //获取头像
            userApi.getAvatar().then(response => {
                const blob = new Blob([response.data], {type: 'image/jpeg'}); // 将返回的数据转为Blob对象
                const url = URL.createObjectURL(blob); // 生成Blob对象的URL
                this.squareUrl = url;
            }).catch(error => {
                ElMessage.error("无法获取到头像数据")
            });
        }
    },
    beforeUpdate() {
        // 组件实例在数据更新之前
    },
    updated() {
        // 组件实例已经更新完成
    },
    beforeUnmount() {
        // 组件实例被卸载之前
    },
    unmounted() {
        // 组件实例已经被卸载
    },
});
</script>
