<!--
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-27 18:40:40
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-06 14:49:48
-->
<template>
  <div id="app">
    <a-layout id="components-layout-demo-top-side">
      <a-layout-header class="header">
        <a-menu
          theme="dark"
          mode="horizontal"
          :default-selected-keys="tabNum"
          :style="{ lineHeight: '64px' }"
        >
          <a-menu-item key="2" v-if="!hasAuth"
            ><router-link to="/login">登录</router-link></a-menu-item
          >
          <a-menu-item
            key="3"
            v-if="hasAuth"
            id="text-loginout"
            @click="loginOut"
            >注销</a-menu-item
          >
        </a-menu>
        <div id="text-content">
          <span>广州航海学院学生网络管理中心</span>
        </div>
        <div id="text-name" v-if="hasAuth">
          <span id="text-name-span">欢迎您~ {{ managerName }}</span>
          <a-badge :count="IsCloseList.length">
            <a-icon type="notification" theme="twoTone" @click="showDrawer"/>
          </a-badge>
        </div>
      </a-layout-header>
      <a-layout-content id="my-content">
        <router-view></router-view>
      </a-layout-content>
      <a-layout-footer
        id="my-footer"
        style="textAlign: center; color: #fff; font-family: 微软雅黑"
      >
        System Design By Hinsane
        <my-icon type="icon-a-ziyuan107"></my-icon>
      </a-layout-footer>
    </a-layout>
    <a-drawer
      title="学生续费通知"
      placement="left"
      :visible="visible"
      @close="close"
    >
      <div id="info-box">
        <a-spin tip="Sending" :spinning="sending">
          <a-list bordered :data-source="IsCloseList">
            <a-list-item slot="renderItem" slot-scope="item, index">
              Id:{{ item.customer.customerId }}&nbsp;&nbsp;Name:{{
                item.customer.customerName
              }}<br> Phone:{{ item.customer.phoneNum }}
              &nbsp;&nbsp;
              <a-icon type="message" theme="twoTone" @click="sendMessage1" />
            </a-list-item>
          </a-list>
        </a-spin>
      </div>
    </a-drawer>
  </div>
</template>

<script>
// import moment from 'moment'
import { MyIcon } from "../src/assets/utils/icon.js";
import { mapState, mapMutations, mapActions } from "vuex";
export default {
  data() {
    return {
      tabNum: ["2"],
      timer: null,
      visible: false,
      sendTimer: null,
      sending:false,//是否在发送中
    };
  },
  components: {
    MyIcon,
  },
  computed: {
    ...mapState("user", ["hasAuth", "managerName", "IsCloseList"]),
  },
  methods: {
    ...mapMutations("user", ["setAuth"]),
    ...mapActions("manager", ["sendMessage",]),
    ...mapActions("user",["LoginOut"]),
    loginOut(e) {
      sessionStorage.clear();
      this.$message.warning("3秒后退出系统", 3);
      clearTimeout(this.timer);
      this.timer = setTimeout(async() => {
        await this.LoginOut();
        // this.setAuth(false); //退出后把权限设置为false,这样下次登录之后才可以继续访问后台权限
        // this.$router.push("/");
      }, 3000);
    },
    showDrawer(e) {
      this.visible = true;
    },
    close() {
      this.visible = false;
    },
    sendMessage1() {
      this.sending = true;
      clearTimeout(this.sendTimer);
      this.sendTimer = setTimeout(async () => {
        await this.sendMessage(); //调用后台发送短信
        this.sending = false;
      }, 800);
    },
  },
};
</script>

<style lang="less" scope>
body {
  box-sizing: border-box;
}

#app {
  height: 100vh;
}

#my {
  &-content {
    height: 100%;
  }
  &-footer {
    background: #001529;
  }
}
.header {
  display: flex;
  #text-content {
    padding-left: 40px;
    width: 80%;
    height: 100%;
    span {
      color: #fff;
      font-family: 微软雅黑;
      font-weight: bold;
      font-size: 24px;
    }
  }
  #text {
    &-name {
      width: 20%;
      &-span {
        margin: 0 10px;
        color: #fff;
        font-family: 微软雅黑;
        font-weight: bold;
        font-size: 12px;
      }
    }

    &-loginout {
      cursor: pointer;
    }
  }
}
.ant-badge {
  font-size: 26px !important;
}
#info {
  &-box {
    width: 100%;
    height: 100%;
  }
}
.ant-drawer-body {
  padding: 0 !important;
}
</style>
