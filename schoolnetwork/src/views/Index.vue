<!--
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-27 22:12:02
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-04 18:30:39
-->
<template>
  <a-layout id="components-layout-demo-custom-trigger">
    <a-layout-sider
      v-model="collapsed"
      :trigger="null"
      collapsible
      :collapsedWidth="174"
      :width="245"
      theme="dark"
    >
      <a-tooltip placement="right">
        <template slot="title">
          <span v-if="!collapsed">缩小侧边栏</span>
          <span v-if="collapsed">展开侧边栏</span>
        </template>
        <div class="mylogo">
          <img :src="background" style="cursor: pointer" @click="showAdise()" />
        </div>
      </a-tooltip>
      <template v-for="(item, index) of routerList">
        <div :key="index">
          <MenuItem :MyMenuList="item"></MenuItem>
        </div>
      </template>
    </a-layout-sider>
    <a-layout>
      <a-layout-content
        :style="{
          margin: '8px 8px 0 8px',
          padding: '0px',
          background: '#f0f2f5',
          minHeight: '280px',
        }"
      >
        <a-skeleton active v-if="!loading" />
        <transition name="myWelcome">
          <div id="welcome-box" v-if="welcome">
            <span>W&nbsp;E&nbsp;L&nbsp;C&nbsp;O&nbsp;M&nbsp;E</span>
          </div>
        </transition name="mytransition">
        <router-view v-if="loading" />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script>
import MenuItem from "../components/MenuItem.vue";
import { mapState, mapMutations } from "vuex";
import "animate.css";
export default {
  data() {
    return {
      time: this.moment().format("dddd"),
      background: require("../assets/image/background.png"),
      collapsed: false,
      loading: false,
      welcome: false,
    };
  },
  methods: {},
  name: "Index",
  components: {
    MenuItem,
  },
  computed: {
    ...mapState("user", ["routerList"]),
  },
  methods: {
    ...mapMutations(["setShowMenu"]),
    showAdise() {
      this.collapsed = !this.collapsed;
      this.$store.commit("setShowMenu", this.collapsed);
    },
    init() {
      setTimeout(() => {
        this.loading = true;
      }, 1000);
    },
    initWelcome(){
      setTimeout(() => {//为了展示效果，这个函数刷新或者刚打开才会执行...
      this.welcome = true;
      }, 200);
      if(this.$route.fullPath !== '/index'){
      setTimeout(()=>{
      this.welcome = false;
      },300)
      }
    }
  },
  created() {
    this.init();
    this.initWelcome()//分开加载
  },
  watch: {
    $route(newvalue) {
      if (newvalue.fullPath === "/index") {//如果跳转路由为主页，则进行展示welcome，否则不展示
        this.welcome = true;
      } else {
        this.welcome = false;
      }
      this.loading = false;
      this.init();//需要解除骨架
    },
  },
};
</script>

<style lang="less" scoped>
.mylogo {
  width: 120px;
  height: 120px;
  // overflow: hidden;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-around;
  img {
    width: 90%;
    height: 90%;
    border-radius: 50%;
  }
}

// 分界线

#components-layout-demo-custom-trigger {
  height: 90vh;
}

#welcome {
  &-box {
    width: 800px;
    height: 400px;
    position: absolute;
    top: 50%;
    left: 50%;
    margin-top: -200px;
    margin-left: -260px;
    display: flex;
    align-items: center;
    justify-content: center;
    span {
      color: powderblue; /*设置文字颜色*/
      font-size: 120px;
      text-shadow: 8px 6px 4px rgba(26, 25, 25,0.74); /*添加字体文字的阴影*/
    }
  }
}

 .myWelcome-enter-active{
    animation: zoomIn 2s
  }
 .myWelcome-leave-active{
    animation: zoomOut 2s;
 }
</style>
