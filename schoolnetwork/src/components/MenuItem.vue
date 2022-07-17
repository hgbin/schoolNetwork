<!--
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-29 20:06:39
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-05-29 22:05:15
-->

<template>
  <div id="My-menu" v-if="!showMenu">
    <template v-if="MyMenuList.children && MyMenuList.children.length > 0">
      <div id="menu-box">
        <router-link :to="MyMenuList.link || MyMenuList.path">{{
          MyMenuList.title
        }}</router-link>
      </div>
      <div v-for="(c, index) in MyMenuList.children" :key="index">
        <ul>
          <MenuItem :MyMenuList="c" />
        </ul>
      </div>
    </template>
    <div v-else>
      <router-link :to="MyMenuList.link || MyMenuList.path">{{
        MyMenuList.title
      }}</router-link>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "MenuItem",
  props: {
    MyMenuList: Object,
  },
  computed: {
    ...mapState(["showMenu"]),
  },
};
</script>

<style lang="less" scoped>
#My {
  &-menu {
    // height: 400px;
    font-size: 16px;
    font-weight: bold;
    color: #fff !important;
  }
}

#menu {
  &-box {
    a {
    }
    div {
      display: flex;
      justify-content: center;
    }
  }
}
a {
  display: block;
  width: 100px;
  height: 22px;
  line-height: 22px;
  font-size: 13px;
  color: rgba(142, 185, 248, 0.671);
  margin:8px 15px;
}
</style>
