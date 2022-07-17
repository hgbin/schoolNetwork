<!--
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-29 19:34:49
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-07 09:34:24
-->
<template>
  <div id="addManager">
    <a-layout>
      <a-layout-header style="background: #fff">
        <h3>添加网络管理员</h3>
      </a-layout-header>
      <a-layout-content id="addManager-content" style="height: 78vh">
        <div id="addManager-box">
          <a-spin tip="Loading..." size="large" :spinning="isAdding">
            <a-form-model
              id="addManager-form"
              ref="ruleForm"
              labelAlign="right"
              :model="ruleForm"
              :rules="rules"
              v-bind="layout"
            >
              <a-tooltip placement="top">
                <template slot="title">
                  <span>请输入管理员的名字</span>
                </template>
                <a-form-model-item
                  has-feedback
                  label="管理员名称"
                  prop="managerName"
                >
                  <a-input v-model="ruleForm.managerName" autocomplete="off" />
                </a-form-model-item>
              </a-tooltip>
              <a-tooltip placement="top">
                <template slot="title">
                  <span>请输入密码</span>
                </template>
                <a-form-model-item has-feedback label="管理员密码" prop="pwd">
                  <a-input-password v-model="ruleForm.pwd" />
                </a-form-model-item>
              </a-tooltip>

              <a-form-model-item has-feedback label="管理员身份">
                <a-tooltip placement="top">
                  <template slot="title">
                    <span>请选择待添加管理员的身份</span>
                  </template>
                  <a-select
                    default-value="1"
                    style="width: 100%"
                    v-model="ruleForm.managerIdentity"
                  >
                    <a-select-option value="1"> 系统管理员 </a-select-option>
                    <a-select-option value="2"> 业务管理员 </a-select-option>
                  </a-select>
                </a-tooltip>
              </a-form-model-item>
              <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
                <a-button type="primary" @click="submitForm('ruleForm')">
                  添加管理员
                </a-button>
                <a-button
                  style="margin-left: 10px"
                  @click="resetForm('ruleForm')"
                >
                  重置
                </a-button>
              </a-form-model-item>
            </a-form-model>
          </a-spin>
        </div>
      </a-layout-content>
    </a-layout>
  </div>
</template>

<script>
import { mapActions, mapState, mapMutations } from "vuex";
export default {
  name: "AddManager",
  data() {
    return {
      ruleForm: {
        managerName: "",
        pwd: "",
        managerIdentity: "",
      },
      rules: {
        managerName: [
          {
            required: true,
            pattern: "^[\u4e00-\u9fa5]{0,}|[A-Za-z0-9]+$",
            message: "输入不能有特殊符号或长度过长或输入不能为空",
            trigger: "change",
            max: 10,
            whitespace: true,
          },
        ],
        pwd: [
          {
            required: true,
            pattern: "^[A-Za-z0-9._]+$",
            message: "不能输入非法符号",
            trigger: "change",
            max: 20,
            whitespace: true,
          },
        ],
      },
      layout: {
        labelCol: { span: 4 },
        wrapperCol: { span: 14 },
      },
      isAdding: false, //添加管理员的时候的加载
      addTimer: null, //添加按钮的延时器
    };
  },
  methods: {
    ...mapActions("manager", ["addManager"]),
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.ruleForm.managerIdentity === "") {
            this.$message.error("请选择管理员类型", 3);
          } else {
            this.isAdding = true;
            clearTimeout(this.addTimer);
            this.addTimer = setTimeout(async () => {
              //如果验证成功，则提交待添加的管理员到后台去保存..
              await this.addManager(this.ruleForm);
              this.isAdding = false;
            }, 800);
          }
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
  computed: {},
  watch: {},
};
</script>

<style lang="less">
#addManager {
  &-box {
    width: 600px;
    height: 300px;
    border-radius: 20px;
    border: 1px solid rgb(216, 210, 210);
    box-shadow: 2px 3px 10px rgba(0, 0, 0, 0.64);
    display: flex;
    justify-content: center;
    padding: 20px 10px;
  }
  &-content {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  &-form {
    width: 600px;
  }
}
.ant {
  &-row {
    display: flex;
    justify-content: center;
  }
  &-col {
    margin: 0 10px;
  }
}

.ant-form {
  &-item-label {
    label {
      color: #131212 !important;
      font-weight: bold;
      font-size: 12px;
    }
  }
  &-item-control {
    text-align: center;
  }
}
</style>
