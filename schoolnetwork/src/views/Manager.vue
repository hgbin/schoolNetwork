<!--
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-29 19:34:13
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-05-30 17:10:15
-->
<template>
  <div id="manager-header">
    <a-layout :style="{'height':'100%'}">
      <a-layout-header id="setting-box">
        <a-icon type="setting"/> 管理员信息控制面板
      </a-layout-header>
      <a-layout-content :style="{'height':'100%'}">
        <router-view/>
      </a-layout-content>
    </a-layout>
  </div>
</template>

<script>
export default {
  name: "Manager",
};
</script>

<style lang="less" scope>
#manager {
  &-header {
    margin: 0 0px !important;
    height: 100%;
  }
}


#setting{
  &-box{
    background: #fff !important;
    height: 25px;
    line-height: 25px;
    padding: 0 20px;
    font-size: 20px;
  }
}
</style>
