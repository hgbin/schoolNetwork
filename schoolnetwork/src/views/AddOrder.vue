<!--
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-29 19:37:29
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-07 11:23:05
-->
<template>
  <div id="addOrder">
    <a-layout>
      <a-layout-header style="background: #fff">
        <h3>添加订单</h3>
      </a-layout-header>
      <a-layout-content id="addOrder-content" style="height: 78vh">
        <div id="addOrder-box">
          <a-spin tip="Loading..." size="large" :spinning="isAdding">
            <a-form-model
              id="addOrder-form"
              ref="ruleForm"
              labelAlign="right"
              :model="ruleForm"
              :rules="rules"
              v-bind="layout"
            >
              <a-form-model-item has-feedback label="管理员Id">
                <a-tooltip placement="top">
                  <template slot="title">
                    <span>请选择待经办人Id</span>
                  </template>
                  <a-select style="width: 100%" v-model="ruleForm.managerId">
                    <template v-for="(item, index) of initManagerIdList">
                      <a-select-option :key="index" :value="item">{{
                        item
                      }}</a-select-option>
                    </template>
                  </a-select>
                </a-tooltip>
              </a-form-model-item>
              <a-form-model-item
                has-feedback
                label="客户Id"
                prop="customerId"
              >
                <a-input
                  placeholder="请输入客户Id"
                  v-model="ruleForm.customerId"
                  autocomplete="off"
                />
              </a-form-model-item>
              <a-form-model-item has-feedback label="订单类型">
                <a-tooltip placement="top">
                  <template slot="title">
                    <span>请选择业务的类型</span>
                  </template>
                  <a-select
                    default-value="包月"
                    style="width: 100%"
                    v-model="ruleForm.orderType"
                    @change="changeType"
                  >
                    <a-select-option value="包月"> 包月 </a-select-option>
                    <a-select-option value="包年"> 包年 </a-select-option>
                  </a-select>
                </a-tooltip>
              </a-form-model-item>

              <a-form-model-item has-feedback label="订单有效时间(月)">
                <a-tooltip placement="top">
                  <template slot="title">
                    <span>请选择订单的有效期（月）</span>
                  </template>
                  <a-select
                    style="width: 100%"
                    v-model="orderTimes"
                    @change="changeTimes"
                  >
                    <template v-if="ruleForm.orderType === '包月'">
                      <template v-for="(item, index) in 12">
                        <a-select-option :key="index" :value="item">{{
                          item
                        }}</a-select-option>
                      </template>
                    </template>
                    <template v-if="ruleForm.orderType === '包年'">
                      <template v-for="(item, index) in 24">
                        <a-select-option
                          v-if="item % 12 === 0"
                          :key="index"
                          :value="item"
                          >{{ item }}</a-select-option
                        >
                      </template>
                    </template>
                  </a-select>
                </a-tooltip>
              </a-form-model-item>

              <a-form-model-item has-feedback label="订单生效时间">
                <a-input
                  v-model="ruleForm.createTime"
                  :disabled="disabled"
                  autocomplete="off"
                />
              </a-form-model-item>

              <a-form-model-item has-feedback label="订单到期时间">
                <a-input
                  v-model="ruleForm.endTime"
                  :disabled="disabled"
                  autocomplete="off"
                />
              </a-form-model-item>

              <a-form-model-item has-feedback label="订单价格">
                <a-input
                  v-model="ruleForm.orderPrice"
                  :disabled="disabled"
                  autocomplete="off"
                />
              </a-form-model-item>

              <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
                <a-button type="primary" @click="submitForm('ruleForm')">
                  添加订单
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
  name: "AddOrder",
  data() {
    let validateCustomerIdTimer = null;
    let validateCustomerId = (rule, value, callback) => {
      //对输入的客户Id进行检测
      clearTimeout(validateCustomerIdTimer);
      validateCustomerIdTimer = setTimeout(() => {
        if (value.trim() === "") {
          callback(new Error("不能输入空格"));
        } else {
          //当输入不为空的时候,需要做是否在Id的操作...
          //如果输入不是数字的时候进行提示
          var pattern = /^[0-9]*$/g;
          if (pattern.test(value)) {
            var target = this.initCustomerIdList.find((item) => {
              // console.log(item,parseInt(value));
              return item === parseInt(value); //这里的数据类型不一样????
            });
            if (target) {
              //证明客户Id存在...
              callback();
            } else {
              callback(new Error("输入的客户Id不存在，请重新输入"));
            }
          } else {
            callback(new Error("请输入数字"));
          }
        }
      }, 800);
    };
    return {
      ruleForm: {
        managerId: "",
        customerId: "",
        orderType: "",
        createTime: "", //打开页面的时候生成
        endTime: "", //自动计算
        orderPrice: "", //自动计算
      },
      orderTimes: "",
      disabled: true, //先禁用选择期限...//只有在选择类型之后才解除...
      rules: {
        customerId: [
          {
            required: true,
            validator: validateCustomerId,
            trigger: "change",
            max: 5,
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
    ...mapActions("order", ["AddOrder", "getInitList"]),
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.ruleForm.managerId === "") {
            this.$message.error("请选择经办人", 3);
          } else if (this.ruleForm.orderType === "") {
            this.$message.error("请选择办理的类型", 3);
          } else if (this.orderTimes === "") {
            this.$message.error("请选择办理的有效期限", 3);
          } else {
            this.isAdding = true;
            clearTimeout(this.addTimer);
            this.addTimer = setTimeout(async () => {
              //如果验证成功，则提交待添加的管理员到后台去保存..
              await this.AddOrder(this.ruleForm);
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
    //改变订单的有效时间的时候触发
    changeTimes(value) {
      if (value !== "") {
        //只有时间不为空的时候才执行
        //需要分包月和包年进行计算
        if (this.ruleForm.orderType === "包月") {
          //如果是包月，我们需要进行计算到期时间，在生效时候开始进行计算
          //先计算到期时间，在计算价格
          this.ruleForm.endTime = this.moment(
            this.moment(this.ruleForm.createTime).add(value, "months")._d
          ).format("YYYY-MM-DD");
          this.ruleForm.orderPrice = value * 60.00;
        }else{
          //如果是包年
          //先计算到期时间，在计算价格
          this.ruleForm.endTime = this.moment(
            this.moment(this.ruleForm.createTime).add(value, "months")._d
          ).format("YYYY-MM-DD");

          this.ruleForm.orderPrice = (value/12) * 680.00;
        }
      }
    },
    //改变订单类型的时候触发
    changeType(value) {
      //改变类型的时候需要清空信息展示...
      this.orderTimes = "";
      this.ruleForm.endTime = "";
      this.ruleForm.orderPrice = "";
    },
  },
  computed: {
    ...mapState("order", ["initManagerIdList", "initCustomerIdList"]),
  },
  watch: {},
  created() {
    this.getInitList();
    this.ruleForm.createTime = this.moment().format("YYYY-MM-DD");
  },
};
</script>

<style lang="less">
#addOrder {
  &-box {
    width: 600px;
    height: 550px;
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
