<!--  Created by LiuYang On 2023/6/27 23:21  -->
<template>

</template>

<script>
import {defineComponent} from 'vue';

export default defineComponent({
  name: 'AdminPage',
  computed: {
  },
  data() {
    return {
      // 会议室相关数据
      meetRooms:{

      },
      //设备相关数据
      devices:{

      },
      // 用户相关数据
      users:{

      }
    }
  },
  methods: {
    // 这里放方法
    tabPaneChange() {
      if (event.target.id === "tab-1") {
        console.log("切换到了tab1");
        this.pageInfo.currentPage = 1;
        this.pageInfo.total = 0;
        this.selectMyMeetingProjectData()
      } else {
        console.log(event.target.id)
        this.myMeetingProjectData = null;
      }
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
    // 组件实例已经被挂载到DOM上
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

<style scoped>
/* 这里放组件样式 */
</style>
