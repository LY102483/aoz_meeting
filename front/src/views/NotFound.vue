<!--  Created by LiuYang On 2023/6/11 17:05  -->
<template>
  <div id="main">
    <h1>页面好像走丢了</h1>
    <h1>小狗到外太空也没有找到</h1>
    <h3><router-link to="/">返回登录界面</router-link></h3>
    <img src="../assets/405.png" style="margin-top: 40px">
  </div>

</template>

<script>
import {defineComponent} from 'vue';

export default defineComponent({
  name: '404',
  data() {
    return {
      // 这里放数据
    }
  },
  methods: {
    // 这里放方法
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
#main
{
  padding-top: 50px
}

/* 这里放组件样式 */
</style>