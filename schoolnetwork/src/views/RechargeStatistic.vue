<!--
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-29 19:37:57
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-06 14:06:06
-->
<template>
  <div>
    <div id="order-Statistics-header">
      <a-layout>
        <a-layout-header :style="{ background: '#fff' }" class="search-header">
          <span style="font-size: 20px; margin: 0 20px">续费订单统计</span>
          <a-tooltip placement="top">
            <template slot="title">
              <span>请选择统计的时间段</span>
            </template>
            <a-select style="width: 200px" @change="handlerSearch">
              <a-icon slot="suffixIcon" type="meh" />
              <a-select-option value="1">今天</a-select-option>
              <a-select-option value="2">本周</a-select-option>
              <a-select-option value="3">本月</a-select-option>
              <a-select-option value="4">本季度</a-select-option>
              <a-select-option value="5">本年度</a-select-option>
              <a-select-option value="6">近7天</a-select-option>
              <a-select-option value="7">近30天</a-select-option>
              <a-select-option value="8">近半年</a-select-option>
            </a-select>
          </a-tooltip>
          <a-button
            type="primary"
            icon="download"
            :loading="iconLoading"
            @click="exportExcel"
          >
            导出为Excel
          </a-button>
        </a-layout-header>

        <a-layout-content class="search-table-box">
          <a-spin
            tip="Loading..."
            size="large"
            :spinning="isSearch"
            id="My-loading"
          >
            <a-layout-content :style="{ margin: '8px 8px 0 8px' }">
              <a-table
                :columns="columns"
                :pagination="search.pagination"
                :data-source="Mydata"
                :rowKey="
                  (record, index) => {
                    return index;
                  }
                "
                bordered
                @change="handleTableChange"
              >
              </a-table>
              <div id="show-info-box">
                <span v-if="StatisticsTotal !== 0">共有{{ StatisticsTotal }}条记录</span>
                <span v-if="StatisticsPrice!==''" >盈利{{ StatisticsPrice }}元</span>
              </div>
            </a-layout-content>
          </a-spin>
        </a-layout-content>
      </a-layout>
    </div>
  </div>
</template>

<script>
import { mapMutations, mapActions, mapState } from "vuex";
const columns = [
  {
    title: "订单Id",
    dataIndex: "orderId",
    width: "5%",
    align: "center",
  },
  {
    title: "经办人姓名",
    dataIndex: "manager.managerName",
    width: "8%",
    scopedSlots: { customRender: "manager.managerName" },
    align: "center",
  },
  {
    title: "客户姓名",
    dataIndex: "customer.customerName",
    width: "8%",
    scopedSlots: { customRender: "customer.customerName" },
    align: "center",
  },
  {
    title: "客户宿舍号",
    dataIndex: "customer.dormitoryNum",
    width: "5%",
    scopedSlots: { customRender: "customer.dormitoryNum" },
    align: "center",
  },
  {
    title: "订单类型",
    dataIndex: "orderType",
    width: "8%",
    scopedSlots: { customRender: "orderType" },
    align: "center",
  },
  {
    title: "订单续费时间",
    dataIndex: "beginTime",
    width: "8%",
    scopedSlots: { customRender: "beginTime" },
    align: "center",
  },
  {
    title: "订单生效时间",
    dataIndex: "createTime",
    width: "8%",
    scopedSlots: { customRender: "createTime" },
    align: "center",
  },
  {
    title: "订单过期时间",
    dataIndex: "endTime",
    width: "8%",
    scopedSlots: { customRender: "endTime" },
    align: "center",
  },
  {
    title: "订单价格(元)",
    dataIndex: "orderPrice",
    width: "5%",
    scopedSlots: { customRender: "orderPrice" },
    align: "center",
  },
];

export default {
  components: {
    name: "RechargeStatistic",
  },
  data() {
    return {
      Mydata: [], //用来展示页面的数据
      columns, //列名
      timer: null,
      isSearch: false, //是否在加载
      search: {
        //查询参数
        pagination: {
          current: 1,
          pageSize: 10,
          total: 0,
        },
        orderStatistics: {
          type: "", //统计类型
        },
         tableName:'rechargeorder',
      },
      paginationTimer: null, //点击调换分页的计时器
      iconLoading: false, //导出按钮延时...
      exportTimer: null, //导出按钮的延时计时器
    };
  },
  methods: {
    ...mapActions("order", ["getStatisticsOrderList","getAllExportList"]),
    ...mapMutations("order",["setStatisticsTotal","setStatisticsPrice"]),
    //搜索调用的函数
    handlerSearch(value) {
      clearTimeout(this.timer);
      this.isSearch = true;
      this.search.orderStatistics.type = value;
      this.timer = setTimeout(async () => {
        //去查询数据保存在表格中
        const pa = {
          current: 1,
          pageSize: 10,
          total: this.$store.state.order.StatisticsTotal, //为了保证在查询前后的记录条数都一样，避免前端页面显示的页数为1
        };
        //为了重头开始寻找，需要重置分页参数
        Object.assign(this.search.pagination, pa);
        await this.getStatisticsOrderList(this.search);
        this.Mydata = [...this.StatisticsOrderList]; //绑定显示数据
        this.$nextTick(() => {
          this.isSearch = false;
        });
      }, 800);
    },

    //当点击分页的时候触发的...,上一页,下一页
    handleTableChange(pagination, filters, sorter) {
      clearTimeout(this.paginationTimer);
      this.isSearch = true;
      this.paginationTimer = setTimeout(async () => {
        Object.assign(this.search.pagination, pagination);
        await this.getStatisticsOrderList(this.search); //传递正确的页面条数
        this.Mydata = [...this.StatisticsOrderList]; //绑定显示数据
        this.$nextTick(() => {
          this.isSearch = false;
        });
      }, 800);
    },
    //点击导出excel文件时
    exportExcel() {
      if (this.search.orderStatistics.type === "") {
        //如果还没有选择，则不允许导出
        this.$message.error("请先选择导出的时间段", 3);
      } else {
        this.iconLoading = true;
        clearTimeout(this.exportTimer);
        this.exportTimer = setTimeout(async() => {
          //访问后台导出文件...
          await this.getAllExportList({'type':this.search.orderStatistics.type,'tableName':'rechargeorder'})
          this.iconLoading = false;
        }, 800);
      }
    },
  },
  computed: {
    ...mapState("order", [
      "StatisticsOrderList",
      "StatisticsTotal",
      "StatisticsPrice",
    ]),
  },
  mounted() {
    //为了解决分页的页数刷新问题
    this.search.pagination.total = this.StatisticsTotal;
  },
  destroyed() {
    this.setStatisticsPrice("")
    this.setStatisticsTotal(0)
  },
  watch: {
    StatisticsTotal(newvalue, oldvalue) {
      this.search.pagination.total = newvalue;
      this.$message.success(`查询到${this.StatisticsTotal}条记录`, 2);
    },
    StatisticsOrderList(newvalue) {
      this.Mydata = newvalue;
    },
  },
};
</script>

<style lang="less" scope>
#order {
  &-Statistics-header {
    height: 100%;
    margin: 0 0px !important;
    display: flex;
  }
}

.search {
  &-header {
    padding: 0 20px;
    display: flex;
    align-items: center;
    border-bottom: 1px dashed gray;
    .ant-select {
      //   display: flex;
      //   align-items: center;
      margin-right: 20px;
    }
  }
  &-table {
    &-box {
      height: 100%;
    }
  }
}

#My {
  &-loadingg {
    text-align: center;
    z-index: 2;
  }
}

.editable-row-operations a {
  margin-right: 8px;
}

#show {
  &-info {
    &-box {
      width: 100%;
      height: 30px;
      display: flex;
      justify-content: flex-end;
      align-items: center;
      font-size: 12px;
      font-weight: bold;
      span {
        display: block;
        width: 80px;
        margin: 0 10px;
      }
    }
  }
}
</style>
