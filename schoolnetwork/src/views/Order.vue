<!--
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-29 19:37:07
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-01 15:40:35
-->
<template>
 <div>
    <div id="order-header">
      <a-layout :style="{ height: '100%' }">
        <a-layout-header id="setting-box">
          <a-icon type="setting" /> 订单信息控制面板
        </a-layout-header>
        <a-layout-content :style="{ height: '100%' }">
          <router-view />
        </a-layout-content>
      </a-layout>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      name: "Order",
    }
  },
};
</script>

<style lang="less" scope>
#order {
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

