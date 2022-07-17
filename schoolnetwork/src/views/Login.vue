<!--
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-27 21:57:35
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-08 21:37:37
-->
<template>
  <a-layout id="login-layout">
    <a-layout>
      <a-layout-content id="login-content">
        <div id="login-box">
          <a-tabs default-active-key="1">
            <a-tab-pane key="1" size="large">
              <span slot="tab"> <a-icon type="lock" /> 账号登录 </span>
              <div id="tab1">
                <a-form-model
                  ref="ruleForm"
                  :model="ruleForm"
                  :rules="rules"
                  v-bind="layout"
                >
                  <a-form-model-item
                    has-feedback
                    label="用户名"
                    prop="managerName"
                  >
                    <a-input v-model="ruleForm.managerName" autocomplete="off" />
                  </a-form-model-item>
                  <a-form-model-item has-feedback label="密码" prop="pwd">
                    <a-input-password
                      v-model="ruleForm.pwd"
                      autocomplete="off"
                    />
                  </a-form-model-item>
                  <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
                    <a-button type="primary" @click="submitForm('ruleForm')">
                      Login
                    </a-button>
                    <a-button
                      style="margin-left: 10px"
                      @click="resetForm('ruleForm')"
                    >
                      重置
                    </a-button>
                  </a-form-model-item>
                </a-form-model>
              </div>
            </a-tab-pane>
          </a-tabs>
        </div>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script>
import { MyIcon } from "../assets/utils/icon";
import { mapActions, mapState, mapMutations } from "vuex";
export default {
  data() {
    return {
      ruleForm: {
        managerName: "admin",
        pwd: "123456",
      },
      rules: {
        managerName: [
          {
            required: true,
            pattern: "^[A-Za-z0-9]+$",
            message: "不能输入非法符号",
            trigger: "change",
            whitespace: true,
          },
        ],
        pwd: [
          {
            required: true,
            pattern: "^[A-Za-z0-9._]+$",
            message: "不能输入非法符号",
            trigger: "change",
            whitespace: true,
          },
        ],
      },
      layout: {
        labelCol: { span: 4 },
        wrapperCol: { span: 14 },
      },
    };
  },
  methods: {
    ...mapMutations("user", ["login"]),
    ...mapActions("user",["LoginPost","getIsCloseFinshed"]),
    submitForm(formName) {
      this.$refs[formName].validate(async(valid) => {
        if (valid) {
        await this.LoginPost(this.ruleForm)//如果验证成功则进行登录
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
  comments: {
    MyIcon,
  },
  computed: {},
};
</script>

<style lang="less">
#login {
  &-layout {
    height: 86vh;
    margin: 0;
    background: #f0f2f5;
  }
  &-content {
    background: #ffffff;
    display: flex;
    align-items: center;
    justify-content: center;
    background-image: url("../assets/image/登录背景5.webp");
    background-size: cover;
    background-repeat: no-repeat;
  }
  &-box {
    width: 600px;
    height: 400px;
    border-radius: 10px 0 20px 0;
    box-shadow: 4px 4px 10px 3px #03506e;
    background-color: #00000068;
    opacity: 0.9;
  }
}

.ant-tabs {
  color: #fff;
  font-family: 微软雅黑;
  font-size: 20px;
  height: 100%;
  &-nav-scroll {
    display: flex;
  }
  &-nav {
    flex-grow: 1;
    display: flex;
    justify-content: center;
    margin: 0 30px;
    font-weight: bold;
    margin-left: 0;
  }
  &-tab {
    width: 500px;
    text-align: center;
  }
  &-nav-container {
    font-size: 20px;
  }
  &-tabpane {
    flex-grow: 1;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  &-content {
    height: 80%;
  }
  &-ink-bar {
    width: 600px !important;
  }
}

#tab {
  &1 {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 500px;
    height: 300px;
  }
  &2 {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 500px;
    height: 300px;
  }
}
.ant-form-item {
  display: flex;
  justify-content: center;
}
.ant-form {
  width: 95%;
  &-item-label {
    
    label{
      color: #eeeeee !important;
      font-weight: bold;
      font-size: 12px;
    }
  }
}
</style>
