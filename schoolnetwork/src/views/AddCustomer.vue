<!--
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-29 19:37:01
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-07 09:34:51
-->
<template>
  <div id="addCustomer">
    <a-layout>
      <a-layout-header style="background: #fff">
        <h3>添加学生客户</h3>
      </a-layout-header>
      <a-layout-content id="addCustomer-content" style="height: 78vh">
        <div id="addCustomer-box">
          <a-spin tip="Loading..." size="large" :spinning="isAdding">
            <a-form-model
              id="addCustomer-form"
              ref="ruleForm"
              labelAlign="right"
              :model="ruleForm"
              :rules="rules"
              v-bind="layout"
            >
              <a-tooltip placement="top">
                <template slot="title">
                  <span>请输入客户名称</span>
                </template>
                <a-form-model-item
                  has-feedback
                  label="客户名称"
                  prop="customerName"
                >
                  <a-input v-model="ruleForm.customerName" autocomplete="off" />
                </a-form-model-item>
              </a-tooltip>
              <a-tooltip placement="top">
                <template slot="title">
                  <span>请输入客户的手机号码</span>
                </template>
                <a-form-model-item
                  has-feedback
                  label="客户电话号码"
                  prop="phoneNum"
                >
                  <a-input v-model="ruleForm.phoneNum" />
                </a-form-model-item>
              </a-tooltip>

              <a-tooltip placement="top">
                <template slot="title">
                  <span>请输入客户的宿舍号</span>
                </template>
                <a-form-model-item
                  has-feedback
                  label="宿舍号"
                  prop="dormitoryNum"
                >
                  <a-input v-model="ruleForm.dormitoryNum" />
                </a-form-model-item>
              </a-tooltip>

              <a-form-model-item has-feedback label="客户类型">
                <a-tooltip placement="top">
                  <template slot="title">
                    <span>请选择待添加客户的类型</span>
                  </template>
                  <a-select
                    default-value="包月"
                    style="width: 100%"
                    v-model="ruleForm.customerType"
                  >
                    <a-select-option value="包月"> 包月 </a-select-option>
                    <a-select-option value="包年"> 包年 </a-select-option>
                  </a-select>
                </a-tooltip>
              </a-form-model-item>
              <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
                <a-button type="primary" @click="submitForm('ruleForm')">
                  添加客户
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
  name: "AddCustomer",
  data() {
    let validatePhoneTimer = null;
    let validatePhone = (rule, value, callback) => {
      clearTimeout(validatePhoneTimer);
      validatePhoneTimer = setTimeout(() => {
        if (value.trim() === "") {
          callback(new Error("不能输入空格"));
        } else {
          var pattern = /^(13[0-9]|14[5|7]|15[0-9]|18[0-9]|19[4|5])\d{8}$/g;
          if (pattern.test(this.ruleForm.phoneNum)) {
            callback();
          } else {
            callback(new Error("请正确输入手机号码"));
          }
        }
      }, 800);
    };
    return {
      ruleForm: {
        customerName: "",
        phoneNum: "",
        customerType: "",
        dormitoryNum: "",
      },
      rules: {
        customerName: [
          {
            required: true,
            pattern: "^[\u4e00-\u9fa5]{0,}|[A-Za-z0-9]+$",
            message: "输入不能有特殊符号或长度过长或输入不能为空",
            trigger: "change",
            max: 10,
            whitespace: true,
          },
        ],
        phoneNum: [
          {
            required: true,
            validator: validatePhone,
            trigger: "change",
            max: 11,
            whitespace: true,
          },
        ],
        dormitoryNum: [
          {
            required: true,
            pattern: "^[0-9]*$",
            message: "只能输入数字...",
            trigger: "change",
            whitespace: true,
          },
        ],
      },
      layout: {
        labelCol: { span: 4 },
        wrapperCol: { span: 14 },
      },
      isAdding: false, //添加客户的时候的加载
      addTimer: null, //添加按钮的延时器
    };
  },
  methods: {
    ...mapActions("customer", ["AddCustomer"]),
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.ruleForm.customerType === "") {
            this.$message.error("请选择客户类型", 3);
          } else {
            this.isAdding = true;
            clearTimeout(this.addTimer);
            this.addTimer = setTimeout(async () => {
              //如果验证成功，则提交待添加的管理员到后台去保存..
              await this.AddCustomer(this.ruleForm);
              this.isAdding = false;
            }, 800);
          }
        } else {
          console.log("error submit!!");
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
#addCustomer {
  &-box {
    width: 600px;
    height: 350px;
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
