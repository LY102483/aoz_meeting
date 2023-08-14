<!--  Created by LiuYang On 2023/6/9 00:43  -->
<template>
  <!-- 模态框1 -->
    <el-dialog v-model="modal" title="会议室预定" width="40%" style="min-width: 600px;" destroy-on-close="true">
        <el-form label-width="100px" style="max-width: 660px;min-width: 574px;" center>
            <el-form-item label="会议室:">
                <el-input v-model="meetingInfo.roomName" disabled/>
            </el-form-item>
            <el-form-item label="预订人:">
                <el-input v-model="userInfo.name" disabled/>
            </el-form-item>
            <el-form-item label="会议标题:">
                <el-input v-model.trim="meetingInfo.meetTitle"/>
            </el-form-item>
            <el-form-item label="会议概述:">
                <el-input v-model.trim="meetingInfo.meetInfo" type="textarea" :rows="4" placeholder="可为空"/>
            </el-form-item>
            <el-form-item label="会议时间:">
                <el-space wrap>
                    <el-checkbox-button v-for="timeItem in timeList" :key="(selectDate, meetingInfo.roomId)"
                                        class="timeLi"
                                        :label="timeItem.startTimeInteger" :checked="false" @change="addTime()"
                                        :disabled="checkTimeDisable(modalData.projects, timeItem.startTimeInteger, timeItem.endTimeInteger)"
                                        :value="timeItem.startTimeInteger"
                                        style="display: flex;justify-content: space-around;">
                        {{ timeItem.time }}
                    </el-checkbox-button>
                </el-space>
            </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="modal = false">取消</el-button>
        <el-button type="primary" @click="submitForm">
          提交
        </el-button>
      </span>
        </template>
    </el-dialog>
  <!--  模态框2-->
    <el-dialog v-model="meetingInfoModal" title="预定详情" width="40%" center destroy-on-close="true">
        <el-row :span="24" style="margin-bottom: 10px"><span>会议详情:{{ theMeetingInfo.meetInfo }}</span></el-row>
        <el-row>
            <el-col :span="24">
                <el-steps align-center>
                    <el-step title="提交申请" :description="theMeetingInfo.createTime" status="success"/>
                    <el-step title="用户申请撤回" v-if="theMeetingInfo.status===3" status="error"
                             :description="theMeetingInfo.updateTime"/>
                    <el-step :title="theMeetingInfo.status===3?'已撤回':'管理员审批'"
                             :status="theMeetingInfo.status===3?'error':theMeetingInfo.status!==0?'success':'finish'"
                             :description="theMeetingInfo.status===3?'用户申请撤回':theMeetingInfo.status!==0?theMeetingInfo.updateTime:'待审批'"/>
                    <el-step title="待审核" v-if="theMeetingInfo.status===0"/>
                    <el-step title="同意申请" v-if="theMeetingInfo.status===1" status="success"
                             :description="'审批意见：'+theMeetingInfo.approvalInfo"/>
                    <el-step title="申请被拒绝" v-if="theMeetingInfo.status===2" status="error"
                             :description="'审批意见：'+theMeetingInfo.approvalInfo"/>
                </el-steps>
            </el-col>
        </el-row>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="meetingInfoModal = false">关闭</el-button>
      </span>
        </template>
    </el-dialog>
  <!--    模态框3：会议室详情-->
    <el-dialog v-model="tabInfo3.modal" title="会议室" width="50%" style="min-width: 800px" destroy-on-close="true">
        <el-form label-width="100px" style="max-width: 660px;min-width: 574px;" center>
            <el-form-item label="会议室名称:">
                <el-input v-model="tabInfo3.modalInfo.roomName"/>
            </el-form-item>
            <el-form-item label="会议室人数:">
                <el-input v-model="tabInfo3.modalInfo.userNum"/>
            </el-form-item>
            <el-form-item label="设备列表:">
            </el-form-item>
            <!--            tabInfo3.modalInfo.devices是会议室已有设备，tabInfo3.devicesList是所有可选的会议室设备-->
            <el-transfer v-model="tabInfo3.modalInfo.devices" :data="tabInfo3.devicesList"
                         :props="{ key: 'deviceId', label: 'deviceName' }"
                         :titles="['可选设备', '已选设备']"
                         :right-default-checked="[2]"
            />
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="tabInfo3.modal = false">取消</el-button>
            <el-button type="primary" @click="updateMeetRoom()">
              提交
            </el-button>
          </span>
        </template>
    </el-dialog>


    <el-tabs @tab-change="tabPaneChange">
        <el-tab-pane label="预定会议室">
            <el-container>
                <el-header class="date-select">
                    <div> 日期:
                        <el-date-picker v-model="selectDate" type="date" placeholder="点此预约会议" :size="'default'"
                                        :default-value="new Date()" :disabledDate="disabledDate"
                                        @change="selectMeetingOfDate"
                                        value-format="YYYY-MM-DD"/>
                    </div>
                </el-header>
                <el-main>
                    <ul style="width: 100%">
                        <el-empty description="这里空空如也" v-if="resData == null"/>
                        <el-row style="width: 100%;" :gutter="20" v-loading="selectDateLoad">
                            <el-col :span="12" v-for="item in resData">
                                <li class="roomLi">
                                    <el-row style="height: 100%;">
                                        <el-col :span="18">
                                            <h3 style="text-align: left;padding-bottom: 5px">{{ item.roomName }}</h3>
                                            <el-row style="margin-top: 10px;overflow: hidden;">
                                                <el-tag type="success">
                                                    <el-icon>
                                                        <User/>
                                                    </el-icon>
                                                    {{ item.userNum }}
                                                </el-tag>
                                            </el-row>
                                            <el-row style="margin-top: 10px;">
                                                <el-tag v-for="device in item.devices"
                                                        style="margin-right: 5px;margin-bottom: 5px;height: 29px;"
                                                        type="info">
                                                    {{ device.deviceName }}
                                                </el-tag>
                                            </el-row>
                                        </el-col>
                                        <el-col :span="6"
                                                style="height: 98px;align-items: center;display: flex;justify-content: center;">
                                            <el-button @click="openModal(item.projects, item.roomId, item.roomName)"
                                                       style="height: 54px;font-size: 18px;width: 108px;">
                                                预定
                                            </el-button>
                                        </el-col>
                                    </el-row>
                                </li>
                            </el-col>
                        </el-row>
                    </ul>
                </el-main>
            </el-container>
        </el-tab-pane>
        <el-tab-pane label="我的预定" v-loading="myMeetingProjectData == null ? true : false">
            <el-container>
                <el-main>
                    <el-empty description="这里空空如也" v-if="this.pageInfo.total === 0"/>
                    <el-table :data="myMeetingProjectData" :border="true" style="width: 100%"
                              v-if="this.pageInfo.total !== 0">
                        <el-table-column label="会议主题" prop="meetTitle" align="center"/>
                        <el-table-column label="会议时间" align="center">
                            <template #default="scope">
                <span style="display: flex;text-align: center;justify-content: center;">
                  {{ scope.row.dateTime }}
                  <div v-for="item in timeList"><span v-if="item.startTimeInteger==scope.row.startTimeInteger">&nbsp;&nbsp;{{
                      item.startTime
                      }}——</span><span v-if="item.endTimeInteger==scope.row.endTimeInteger">{{
                      item.endTime
                      }}</span></div>
                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="会议地点" align="center" width="100%">
                            <template #default="scope">
                                <div v-for="room in roomInfo">
                                    <span v-if="room.roomId===scope.row.roomId">{{ room.roomName }}</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="申请状态" align="center" width="130%">
                            <template #default="scope">
                                <el-tag :type="scope.row.meetStatus===0?'info':(scope.row.meetStatus === 1 ? 'success' : (scope.row.meetStatus === 2||scope.row.meetStatus === 3 ? 'danger':'info'))"
                                        disable-transitions>
                                    {{
                                    scope.row.meetStatus === 0 ? '待审核' : (scope.row.meetStatus === 1 ? '预定成功' : (scope.row.meetStatus === 2 ? '申请被拒绝' : '用户已撤回申请'))
                                    }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column align="center">
                            <template #default="scope">
                                <el-row>
                                    <el-button type="primary"
                                               @click="openMeetingInfoModal(scope.row.meetStatus,scope.row.approvalInfo,scope.row.createTime,scope.row.updateTime,scope.row.startTimeInteger,scope.row.endTimeInteger,scope.row.meetInfo)">
                                        详情
                                    </el-button>
                                    <el-button :disabled="scope.row.meetStatus<=1?false:true"
                                               @click="updateMeetingStatus(scope.row.meetId,3,'')" type="warning">
                                        撤回申请
                                    </el-button>
                                </el-row>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-main>
                <el-footer>
                    <el-row justify="space-between" v-if="this.pageInfo.total != 0">
                        <span style="text-align: center;line-height: 32px">每页10条数据,共计{{
                            pageInfo.total
                            }}条数据</span>
                        <el-pagination v-model:current-page="pageInfo.currentPage" :page-size="10" :background="true"
                                       layout="prev, pager, next, jumper" :total="pageInfo.total"
                                       @current-change="selectMyMeetingProjectData"/>
                    </el-row>
                </el-footer>
            </el-container>
        </el-tab-pane>
        <el-tab-pane label="会议室预约管理" v-loading="tabInfo2.MeetingProjectData == null ? true : false" v-if="userInfo.type!=='null'&&userInfo.type!==null">
            <el-container>
                <el-main>
                    <el-empty description="这里空空如也" v-if="this.tabInfo2.pageInfo.total === 0"/>
                    <el-table :data="tabInfo2.MeetingProjectData" :border="true" style="width: 100%"
                              v-if="this.tabInfo2.pageInfo.total !== 0">
                        <el-table-column label="会议主题" prop="meetTitle" align="center" width="200%"/>
                        <el-table-column label="申请人" prop="userName" align="center" width="100%"/>
                        <el-table-column label="会议时间" align="center">
                            <template #default="scope">
                <span style="display: flex;text-align: center;justify-content: center;">
                  {{ scope.row.dateTime }}
                  <div v-for="item in timeList"><span v-if="item.startTimeInteger==scope.row.startTimeInteger">&nbsp;&nbsp;{{
                      item.startTime
                      }}——</span><span v-if="item.endTimeInteger==scope.row.endTimeInteger">{{
                      item.endTime
                      }}</span></div>
                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="会议地点" align="center" width="100%">
                            <template #default="scope">
                                <div v-for="room in roomInfo">
                                    <span v-if="room.roomId===scope.row.roomId">{{ room.roomName }}</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="申请状态" align="center" width="130%">
                            <template #default="scope">
                                <el-tag :type="scope.row.meetStatus===0?'info':(scope.row.meetStatus === 1 ? 'success' : (scope.row.meetStatus === 2||scope.row.meetStatus === 3 ? 'danger':'info'))"
                                        disable-transitions>
                                    {{
                                    scope.row.meetStatus === 0 ? '待审核' : (scope.row.meetStatus === 1 ? '已同意' : (scope.row.meetStatus === 2 ? '已拒绝' : '用户已撤回申请'))
                                    }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" align="center">
                            <template #default="scope">
                                <el-row>
                                    <el-button type="primary"
                                               @click="openMeetingInfoModal(scope.row.meetStatus,scope.row.approvalInfo,scope.row.createTime,scope.row.updateTime,scope.row.startTimeInteger,scope.row.endTimeInteger,scope.row.meetInfo)">
                                        详情
                                    </el-button>
                                    <el-button :disabled="scope.row.meetStatus==0?false:true"
                                               @click="updateMeetingStatus(scope.row.meetId,1,'')" type="success">
                                        一键同意
                                    </el-button>
                                    <el-button :disabled="scope.row.meetStatus==0?false:true"
                                               @click="updateMeetingStatus(scope.row.meetId,2,'')" type="warning">
                                        一键拒绝
                                    </el-button>
                                </el-row>
                            </template>

                        </el-table-column>
                    </el-table>
                </el-main>
                <el-footer>
                    <el-row justify="space-between" v-if="this.pageInfo.total != 0">
                        <span style="text-align: center;line-height: 32px">每页10条数据,共计{{
                            tabInfo2.pageInfo.total
                            }}条数据</span>
                        <el-pagination v-model:current-page="tabInfo2.pageInfo.currentPage" :page-size="10"
                                       :background="true"
                                       layout="prev, pager, next, jumper" :total="tabInfo2.pageInfo.total"
                                       @current-change="selectMyMeetingProjectData"/>
                    </el-row>
                </el-footer>
            </el-container>
        </el-tab-pane>
        <el-tab-pane label="会议室管理" v-loading="tabInfo3.MeetingRoomData == null" v-if="userInfo.type!=='null'&&userInfo.type!==null">
            <el-container>
                <el-header>
                    <el-button @click="updateMeetRoom(null, 0,null, null,null)">新增会议室</el-button>
                </el-header>
                <el-main>
                    <el-empty description="这里空空如也" v-if="this.tabInfo3.MeetingRoomData ===null"/>
                    <el-table :data="tabInfo3.MeetingRoomData" :border="true" style="width: 100%">
                        <el-table-column label="会议室名称" prop="roomName" align="center" width="200%"/>
                        <el-table-column label="可容纳人数" prop="userNum" align="center" width="100%"/>
                        <el-table-column label="现有设备" align="center">
                            <template #default="scope">
                                <el-tag v-for="item in scope.row.devices"
                                        style="display: inline-flex;margin-right: 3px">{{ item.deviceName }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" align="center">
                            <template #default="scope">
                                <el-row>
                                    <el-button type="primary"
                                               @click="updateMeetRoom(scope.row.roomId, 0,scope.row. userNum, scope.row.roomName,scope.row.devices)">
                                        修改
                                    </el-button>
                                    <el-button type="warning" @click="updateMeetRoom(scope.row.roomId,1)">
                                        删除
                                    </el-button>
                                </el-row>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-main>
            </el-container>
        </el-tab-pane>
    </el-tabs>
</template>

<script>
import {ElMessage, ElMessageBox} from "element-plus";
import {defineComponent} from 'vue';
import meetingApi from "@/api/meeting";
import {User} from "@element-plus/icons-vue";
import querystring from "querystring";
import {mapGetters, mapState} from "vuex";
import meetingRoomApi from "@/api/meetingroom";

export default defineComponent({
    name: 'meetingRoomInfo',
    computed: {
        ...mapGetters(['isLogin']),
        userInfo() {
            console.log(this.isLogin)
            return this.isLogin;
        },
    },
    data() {
        return {
            tabInfo3: {
                MeetingRoomData: null,
                pageInfo: {
                    total: 0,
                    currentPage: 1,//当前页码
                },
                modal: false,
                modalInfo: {
                    roomName: null,
                    roomId: null,
                    devices: [],
                    userNum: null,
                },
                //设备清单
                devicesList: null,
            }
            ,
            tabInfo2: {
                MeetingProjectData: null,
                pageInfo: {
                    total: 0,
                    currentPage: 1,//当前页码
                }
            },
            pageInfo: {
                currentPage: 1,//当前页码
                total: 0,//总条目数
            },
            theMeetingInfo: {
                status: null,
                approvalInfo: null,
                createTime: null,
                updateTime: null,
                startTimeInteger: null,
                endTimeInteger: null,
                meetInfo: null
            },
            roomInfo: null,
            // 这里放数据

            myMeetingProjectData: null,//用户预定的会议室列表信息
            modal: null,//模态框1
            meetingInfoModal: null,//模态框2
            modalData: {time: [], projects: null},//模态框数据
            selectDate: null,
            disabledDate(time) {
                const today = new Date()
                const thirtyDaysLater = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 30)
                //限制日期选择器只能选择今天和未来30天的日期
                return time.getTime() < Date.now() - 8.64e7 || time.getTime() > thirtyDaysLater.getTime()
            },
            //通过日期查询会议安排的返回数据对象
            resData: null,
            selectDateLoad: false,//根据日期查询会议室时的加载动画
            // 需要渲染的时间列表 status：0表示未占用，1表示已占用，2表示被选中
            timeList: [
                {time: "07:00-07:30", startTimeInteger: 14, endTimeInteger: 15, startTime: "07:00", endTime: "07:30"},
                {time: "07:30-08:00", startTimeInteger: 15, endTimeInteger: 16, startTime: "07:30", endTime: "08:00"},
                {time: "08:00-08:30", startTimeInteger: 16, endTimeInteger: 17, startTime: "08:00", endTime: "08:30"},
                {time: "08:30-09:00", startTimeInteger: 17, endTimeInteger: 18, startTime: "08:30", endTime: "09:00"},
                {time: "09:00-09:30", startTimeInteger: 18, endTimeInteger: 19, startTime: "09:00", endTime: "09:30"},
                {time: "09:30-10:00", startTimeInteger: 19, endTimeInteger: 20, startTime: "09:30", endTime: "10:00"},
                {time: "10:00-10:30", startTimeInteger: 20, endTimeInteger: 21, startTime: "10:00", endTime: "10:30"},
                {time: "10:30-11:00", startTimeInteger: 21, endTimeInteger: 22, startTime: "10:30", endTime: "11:00"},
                {time: "11:00-11:30", startTimeInteger: 22, endTimeInteger: 23, startTime: "11:00", endTime: "11:30"},
                {time: "11:30-12:00", startTimeInteger: 23, endTimeInteger: 24, startTime: "11:30", endTime: "12:00"},
                {time: "12:00-12:30", startTimeInteger: 24, endTimeInteger: 25, startTime: "12:00", endTime: "12:30"},
                {time: "12:30-13:00", startTimeInteger: 25, endTimeInteger: 26, startTime: "12:30", endTime: "13:00"},
                {time: "13:00-13:30", startTimeInteger: 26, endTimeInteger: 27, startTime: "13:00", endTime: "13:30"},
                {time: "13:30-14:00", startTimeInteger: 27, endTimeInteger: 28, startTime: "13:30", endTime: "14:00"},
                {time: "14:00-14:30", startTimeInteger: 28, endTimeInteger: 29, startTime: "14:00", endTime: "14:30"},
                {time: "14:30-15:00", startTimeInteger: 29, endTimeInteger: 30, startTime: "14:30", endTime: "15:00"},
                {time: "15:00-15:30", startTimeInteger: 30, endTimeInteger: 31, startTime: "15:00", endTime: "15:30"},
                {time: "15:30-16:00", startTimeInteger: 31, endTimeInteger: 32, startTime: "15:30", endTime: "16:00"},
                {time: "16:00-16:30", startTimeInteger: 32, endTimeInteger: 33, startTime: "16:00", endTime: "16:30"},
                {time: "16:30-17:00", startTimeInteger: 33, endTimeInteger: 34, startTime: "16:30", endTime: "17:00"},
                {time: "17:00-17:30", startTimeInteger: 34, endTimeInteger: 35, startTime: "17:00", endTime: "17:30"},
                {time: "17:30-18:00", startTimeInteger: 35, endTimeInteger: 36, startTime: "17:30", endTime: "18:00"},
                {time: "18:00-18:30", startTimeInteger: 36, endTimeInteger: 37, startTime: "18:00", endTime: "18:30"},
                {time: "18:30-19:00", startTimeInteger: 37, endTimeInteger: 38, startTime: "18:30", endTime: "19:00"},
                {time: "19:00-19:30", startTimeInteger: 38, endTimeInteger: 39, startTime: "19:00", endTime: "19:30"},
                {time: "19:30-20:00", startTimeInteger: 39, endTimeInteger: 40, startTime: "19:30", endTime: "20:00"},
                {time: "20:00-20:30", startTimeInteger: 40, endTimeInteger: 41, startTime: "20:00", endTime: "20:30"},
                {time: "20:30-21:00", startTimeInteger: 41, endTimeInteger: 42, startTime: "20:30", endTime: "21:00"},
                {time: "21:00-21:30", startTimeInteger: 42, endTimeInteger: 43, startTime: "21:00", endTime: "21:30"},
                {time: "21:30-22:00", startTimeInteger: 43, endTimeInteger: 44, startTime: "21:30", endTime: "22:00"},
                {time: "22:00-22:30", startTimeInteger: 44, endTimeInteger: 45, startTime: "22:00", endTime: "22:30"},
                {time: "22:30-23:00", startTimeInteger: 45, endTimeInteger: 46, startTime: "22:30", endTime: "23:00"}
            ],
            //预定会议信息
            meetingInfo: {
                roomId: null,
                meetTitle: null,
                meetInfo: null,
                startTimeInteger: null,
                endTimeInteger: null,
                dateTime: this.selectDate,
                startTime: null,
                endTime: null
            },
        }
    },
    methods: {
        // 这里放方法
        //修改或删除会议室,因为后端用的是同一个接口方法，前端用的是同一个模态框
        updateMeetRoom(roomId, isDeleted, userNum, roomName, devices) {
            //删除会议室
            if (isDeleted === 1) {
                ElMessageBox.confirm(
                    '这将导致数据被永久删除且无法恢复，是否确认此次操作?',
                    '警告',
                    {
                        confirmButtonText: '确认',
                        cancelButtonText: '取消',
                        type: 'error',
                    }
                )
                    .then(() => {
                        meetingRoomApi.updateMeetingRoom({roomId: roomId, isDeleted: isDeleted}).then(res => {
                            if (res.data.code === 1) {
                                ElMessage.success("删除成功")
                                meetingApi.selectMeetingOfDate({
                                    selectDate: this.selectDate
                                }).then(res => {
                                    this.tabInfo3.MeetingRoomData = res.data.data;
                                })
                            } else {
                                ElMessage.error("删除失败")
                            }
                        })
                    })
            } else if (isDeleted === 0) {
                this.tabInfo3.modalInfo.devices=[];
                // 修改会议室模态框
                this.tabInfo3.modal = true;
                this.tabInfo3.modalInfo.roomId = roomId
                this.tabInfo3.modalInfo.roomName = roomName
                this.tabInfo3.modalInfo.userNum = userNum
                for (let i = 0; i < devices.length; i++) {
                    this.tabInfo3.modalInfo.devices.push(devices[i]["deviceId"])
                }
            } else {
                // 真实提交
                if(this.tabInfo3.modalInfo.roomName==null||this.tabInfo3.modalInfo.userNum==null){
                    ElMessage.error("会议室名称和人数不能为空")
                }else{
                    meetingRoomApi.updateMeetingRoom({
                        roomId: this.tabInfo3.modalInfo.roomId,
                        roomName: this.tabInfo3.modalInfo.roomName,
                        userNum: this.tabInfo3.modalInfo.userNum,
                        isDeleted:0,
                        deviceList: this.tabInfo3.modalInfo.devices,
                    }).then(res => {
                            if (res.data.code === 1) {
                                meetingApi.selectMeetingOfDate({
                                    selectDate: this.selectDate
                                }).then(res => {
                                    this.tabInfo3.MeetingRoomData = res.data.data;
                                })
                                //查询有哪些会议室设备
                                meetingRoomApi.getAllDevices().then(res => {
                                    this.tabInfo3.devicesList = res.data.data
                                })
                                this.tabInfo3.modal = false
                                ElMessage.success("提交成功")
                            } else {
                                ElMessage.error("提交失败")
                            }
                        }
                    )
                }

            }
        },

        //审核会议
        updateMeetingStatus(meetId, meetStatus, approvalInfo) {
            meetingApi.updateMeetingStatus({
                meetId: meetId,
                meetStatus: meetStatus,
                approvalInfo: approvalInfo
            }).then(res => {
                if (res.data.code === 1) {
                    ElMessage.success("操作成功")
                    meetingApi.selectMyMeetingProjectList({
                        page: this.pageInfo.currentPage,
                        type: 1,
                        orderBy: 1
                    }).then(res => {
                        this.tabInfo2.MeetingProjectData = res.data.data.records;
                        this.tabInfo2.pageInfo.total = res.data.data.total;
                    })
                    this.selectMyMeetingProjectData()
                } else {
                    ElMessage.error(res.data.msg)
                }
            })
        },
        //用户查看某个会议具体的信息
        openMeetingInfoModal(status, approvalInfo, createTime, updateTime, startTimeInteger, endTimeInteger, meetInfo) {
            this.meetingInfoModal = true;
            this.theMeetingInfo.status = status,
                this.theMeetingInfo.approvalInfo = approvalInfo,
                this.theMeetingInfo.createTime = createTime,
                this.theMeetingInfo.updateTime = updateTime,
                this.theMeetingInfo.startTimeInteger = startTimeInteger,
                this.theMeetingInfo.endTimeInteger = endTimeInteger
            this.theMeetingInfo.meetInfo = meetInfo
        },
        //面板被改变时
        tabPaneChange() {
            this.activeName = event.target.id;
            if (event.target.id === "tab-1") {
                console.log("切换到了tab1");
                this.pageInfo.currentPage = 1;
                this.pageInfo.total = 0;
                this.selectMyMeetingProjectData()
            } else if (event.target.id === "tab-2") {
                console.log("切换到了tab2")
                meetingApi.selectMyMeetingProjectList({
                    page: this.pageInfo.currentPage,
                    type: 1,
                    orderBy: 1
                }).then(res => {
                    this.tabInfo2.MeetingProjectData = res.data.data.records;
                    this.tabInfo2.pageInfo.total = res.data.data.total;
                })
            } else if (event.target.id === "tab-3") {
                meetingApi.selectMeetingOfDate({
                    selectDate: this.selectDate
                }).then(res => {
                    this.tabInfo3.MeetingRoomData = res.data.data;
                })
                //查询有哪些会议室设备
                meetingRoomApi.getAllDevices().then(res => {
                    this.tabInfo3.devicesList = res.data.data
                })
            } else if (event.target.id = "tab-0") {
                this.selectDate = null;
                this.resData = null;
            } else {

            }

        },
        //查询用户自己的预约申请状态
        selectMyMeetingProjectData() {
            meetingApi.selectMyMeetingProjectList({page: this.pageInfo.currentPage, type: 0, orderBy: 1}).then(res => {
                this.myMeetingProjectData = res.data.data.records;
                this.pageInfo.total = res.data.data.total;
            })
        },
        //提交预约申请
        submitForm() {
            //对选中的时间进行排序
            this.modalData.time.sort(function (a, b) {
                return parseInt(a) - parseInt(b);
            });
            //表单校验信号
            let checkOk = true;
            //检查时间段是否连续
            for (let i = 0; i < this.modalData.time.length - 1; i++) {
                if (parseInt(this.modalData.time[i + 1]) - parseInt(this.modalData.time[i]) != 1) {
                    ElMessage.error('你选择的时间范围不连续，请重新选择')
                    checkOk = false
                    return
                }
            }
            //检查会议标题栏是否为空
            if (this.meetingInfo.meetTitle == null || this.meetingInfo.meetTitle == "") {
                checkOk = false
                ElMessage.error('会议标题栏不能为空')
                return
            }

            //检查是否选择会议时间
            if (this.modalData.time.length === 0) {
                checkOk = false
                ElMessage.error('会议时间不能为空')
                return
            } else {
                this.meetingInfo.startTimeInteger = this.modalData.time[0]
                this.meetingInfo.endTimeInteger = parseInt(this.modalData.time[this.modalData.time.length - 1]) + 1
                for (let i = 0; i < this.timeList.length; i++) {
                    if (this.meetingInfo.startTimeInteger == this.timeList[i].startTimeInteger) {
                        this.meetingInfo.startTime = this.timeList[i].startTime;
                    }
                    if (this.meetingInfo.endTimeInteger == this.timeList[i].endTimeInteger) {
                        this.meetingInfo.endTime = this.timeList[i].endTime;
                    }
                }
            }


            //数据校验完毕，向接口发出请求
            if (checkOk) {
                meetingApi.createMeetingProject(this.meetingInfo).then(res => {
                    if (res.data.code === 1) {
                        ElMessage.success("预约成功，请等待管理员审核。")
                        this.modal = false
                        setTimeout(() => {
                            this.$router.go(0)
                        }, 1000);

                    } else {
                        ElMessage.error(res.data.msg)
                    }
                })
            }
        },
        //选中时间段
        addTime() {
            if (event.target.checked) {
                this.modalData.time.push(event.target.value)
                console.log(this.modalData.time);
            } else {
                this.modalData.time = this.modalData.time.filter(item => item != event.target.value)
                console.log(this.modalData.time);
            }
        },
        //打开模态框
        openModal(projects, roomId, roomName) {
            this.meetingInfo.roomId = roomId;
            this.meetingInfo.roomName = roomName;
            this.modalData.projects = projects;
            this.meetingInfo.meetTitle = null;
            this.meetingInfo.meetInfo = null;
            this.meetingInfo.dateTime = this.selectDate;
            this.modalData.time = [];
            this.modal = true;
        },
        //根据指定日期搜索会议室情况
        selectMeetingOfDate() {
            this.selectDateLoad = true;
            meetingApi.selectMeetingOfDate({
                selectDate: this.selectDate
            }).then(res => {
                this.resData = res.data.data
                this.selectDateLoad = false;
            })

        },
        //检查当前时间是否可用
        checkTimeDisable(projects, startTimeInteger, endTimeInteger) {
            let flag = false;
            for (let project of projects) {
                if (startTimeInteger >= project.startTimeInteger && endTimeInteger <= project.endTimeInteger) {
                    flag = true;
                    break
                }
            }
            return flag;
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
        //初始化会议室信息
        meetingApi.selectMeetingOfDate({
            selectDate: "1970-01-01"
        }).then(res => {
            this.roomInfo = res.data.data
        });

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
    }
});
</script>

<style scoped>
/* 这里放组件样式 */
.el-tag {
    display: flex;
    text-align: center;
    padding: 3px;
    height: 24px;
    line-height: 16px;
}

.date-select {
    display: flex;
    justify-content: left;
}

.demo-tabs > .el-tabs__content {
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
}

.el-tabs {
    height: 750px;
}

.roomLi {
    border: 1px solid #dcdfe6;
    padding: 10px;
    margin-top: 8px;
    border-radius: 5px;
    max-height: 98px;
    overflow: hidden;
    text-overflow: ellipsis;
}

.el-header {
    height: 100%;
    background-color: white;
    padding: 5px 0 5px 0;
    border-bottom: #dcdfe6 1px solid;
}

.el-main {
    min-height: 600px
}

ul {
    list-style: none;
}
</style>
