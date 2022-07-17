<template>
  <div>
    <div id="order-operate-header">
      <a-layout>
        <a-layout-header :style="{ background: '#fff' }" class="search-header">
          <a-tooltip placement="top">
            <template slot="title">
              <span>请选择待查询订单的类型</span>
            </template>
            <a-select
              style="width: 200px"
              placeholder="请选择待查询订单的类型"
              @change="handlerSearch"
            >
              <a-icon slot="suffixIcon" type="meh" />
              <a-select-option value="不限">不限</a-select-option>
              <a-select-option value="包月">包月</a-select-option>
              <a-select-option value="包年">包年</a-select-option>
            </a-select>
          </a-tooltip>

          <a-tooltip placement="top">
            <template slot="title">
              <span>请选择待查询客户的姓名</span>
            </template>
            <a-select
              style="width: 200px"
              placeholder="请选择待查询客户的姓名"
              @change="handlerManagerName"
            >
              <a-icon slot="suffixIcon" type="meh" />
              <a-select-option value="不限">不限</a-select-option>
              <template v-for="(item, index) of managerNameList">
                <a-select-option :key="index" :value="item">{{
                  item
                }}</a-select-option>
              </template>
            </a-select>
          </a-tooltip>

          <a-date-picker
            placeholder="选择查询订单生效时间"
            allowClear
            @change="handlerCreateTime"
            style="width: 190px"
          />
          <a-date-picker
            placeholder="选择查询订单到期时间"
            allowClear
            @change="handlerEndTime"
            style="margin-left: 20px; width: 190px"
          />

          <a-tooltip placement="top">
            <template slot="title">
              <span>请输入待查询订单Id/客户姓名</span>
            </template>
            <a-input-search
              size="large"
              placeholder="请输入待查询订单Id/客户姓名"
              :style="{ width: '300px', margin: '0 0 0 20px' }"
              v-model="search.orderSearch.orderName"
              :disabled="disabledInput"
              @search="onSearch"
            >
              <a-icon slot="addonBefore" type="team" />
            </a-input-search>
          </a-tooltip>
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
                <template
                  slot="expandedRowRender"
                  slot-scope="record"
                  style="margin: 0"
                >
                  <h5>本订单续费详情&nbsp;:</h5>
                  <a-list bordered :data-source="record.chargeOrderList">
                    <a-list-item slot="renderItem" slot-scope="item, index">
                      <a-icon type="dollar" theme="twoTone" />
                      <span id="chargeSpan"
                        >经办人姓名:&nbsp;{{ item.manager.managerName }}</span
                      >
                      <span id="chargeSpan"
                        >续费类型:&nbsp;{{ item.orderType }}</span
                      >
                      <span id="chargeSpan"
                        >客户续费时间:&nbsp;{{ item.beginTime }}</span
                      >
                      <span id="chargeSpan"
                        >续费生效时间:&nbsp;{{ item.createTime }}</span
                      >
                      <span id="chargeSpan"
                        >续费过期时间:&nbsp;{{ item.endTime }}</span
                      >
                      <span id="chargeSpan"
                        >续费订单价格:&nbsp;{{ item.orderPrice }}&nbsp;元</span
                      >
                    </a-list-item>
                  </a-list>
                </template>

                <template
                  v-for="col in ['orderPrice']"
                  :slot="col"
                  slot-scope="text, record, index"
                >
                  <div :key="col">
                    <a-input
                      v-if="record.editable"
                      style="margin: -5px 0; width: 100px"
                      :value="text"
                      @change="
                        (e) => handleChange(e.target.value, record.orderId, col)
                      "
                    />
                    <template v-else>
                      {{ text }}
                    </template>
                  </div>
                </template>
                <template slot="orderType" slot-scope="text, record, index">
                  <template>
                    <a-select
                      v-if="record.editable"
                      :default-value="text"
                      style="width: 80px"
                      @change="handleChangeType"
                    >
                      <a-select-option value="包月">包月</a-select-option>
                      <a-select-option value="包年">包年</a-select-option>
                    </a-select>
                    <template v-else>{{ text }}</template>
                  </template>
                </template>

                <template slot="managerId" slot-scope="text, record, index">
                  <template>
                    <a-select
                      v-if="record.editable"
                      style="width: 60px"
                      :default-value="text"
                      @change="handleChangeManagerId"
                    >
                      <template v-for="(item, index) of initManagerIdList">
                        <a-select-option :key="index" :value="item">{{
                          item
                        }}</a-select-option>
                      </template>
                    </a-select>
                    <template v-else>{{ text }}</template>
                  </template>
                </template>
                <template
                  slot="timeDifference"
                  slot-scope="text, record, index"
                >
                  <span
                    style="color: red; font-family: 微软雅黑; font-weight: bold"
                    >{{ text }}天</span
                  >
                </template>

                <template slot="operation" slot-scope="text, record, index">
                  <div class="editable-row-operations">
                    <span v-if="record.editable">
                      <a-popconfirm
                        title="Sure to Save?"
                        @confirm="() => save(record.orderId)"
                      >
                        <a>Save</a>
                      </a-popconfirm>
                      <a-popconfirm
                        title="Sure to cancel?"
                        @confirm="() => cancel(record.orderId)"
                      >
                        <a>Cancel</a>
                      </a-popconfirm>
                    </span>
                    <span v-else>
                      <a-space>
                        <a
                          :disabled="editingKey !== ''"
                          @click="() => recharge(record.orderId)"
                        >
                          <a-icon type="dollar" />续费
                        </a>
                        <a
                          :disabled="editingKey !== ''"
                          @click="() => edit(record.orderId)"
                        >
                          <a-icon type="edit" />修改
                        </a>
                        <a
                          :disabled="editingKey !== ''"
                          @click="() => deleteInfo(record.orderId, index)"
                        >
                          <a-icon type="delete" />删除
                        </a>
                      </a-space>
                    </span>
                  </div>
                </template>
              </a-table>
              <a-modal
                v-model="deleteTip"
                title="删除提示"
                centered
                @ok="deleteConfirm"
              >
                <div id="delete-box">
                  <span>确定删除本订单嘛？</span>
                  <a-divider />
                  <span>订单Id:{{ deleteTarget.orderId }}</span>
                  <span>经办人Id:{{ deleteTarget.managerId }}</span>
                  <span>经办人姓名:{{ deleteTarget.manager.managerName }}</span>
                  <span>客户Id:{{ deleteTarget.customerId }}</span>
                  <span>客户姓名:{{ deleteTarget.customer.customerName }}</span>
                  <span
                    >客户宿舍号:{{ deleteTarget.customer.dormitoryNum }}</span
                  >
                  <span>订单类型:{{ deleteTarget.orderType }}</span>
                  <span>订单生效时间:{{ deleteTarget.createTime }}</span>
                  <span>订单过期时间:{{ deleteTarget.endTime }}</span>
                  <span>订单价格:{{ deleteTarget.orderPrice }}元</span>
                  <span
                    >本订单过期倒计时时间:{{
                      deleteTarget.timeDifference
                    }}天</span
                  >
                  <a-divider />
                </div>
              </a-modal>

              <a-modal
                v-model="rechargeTip"
                title="续费提示"
                centered
                @ok="rechargeConfirm"
                @cancel="cancelRecharge"
              >
                <div id="recharge-box">
                  <span>确定续费以下订单嘛？</span>
                  <a-divider />
                  <span>订单Id:{{ showRechargeTarget.orderId }}</span>
                  <span>经办人Id:{{ showRechargeTarget.managerId }}</span>
                  <span
                    >经办人姓名:{{
                      showRechargeTarget.manager.managerName
                    }}</span
                  >
                  <span>客户Id:{{ showRechargeTarget.customerId }}</span>
                  <span
                    >客户姓名:{{
                      showRechargeTarget.customer.customerName
                    }}</span
                  >
                  <span
                    >客户宿舍号:{{
                      showRechargeTarget.customer.dormitoryNum
                    }}</span
                  >
                  <span>订单类型:{{ showRechargeTarget.orderType }}</span>
                  <span>订单生效时间:{{ showRechargeTarget.createTime }}</span>
                  <span>订单过期时间:{{ showRechargeTarget.endTime }}</span>
                  <span>订单价格:{{ showRechargeTarget.orderPrice }}元</span>
                  <span
                    >订单过期倒计时:{{
                      showRechargeTarget.timeDifference
                    }}天</span
                  >
                  <a-divider />
                  <template v-if="rechargeTimes != ''">
                    <h4>续费信息</h4>
                    <span id="chargeSpan"
                      >续费办理人Id:&nbsp;{{
                        rechargeTarget.manager.managerId
                      }}&nbsp;</span
                    >
                    <span id="chargeSpan"
                      >订单续费类型:&nbsp;{{
                        rechargeTarget.orderType
                      }}&nbsp;</span
                    >
                    <span id="chargeSpan"
                      >续费生效时间:&nbsp;{{
                        rechargeTarget.createTime
                      }}&nbsp;</span
                    >
                    <span id="chargeSpan"
                      >续费时间:&nbsp;{{ rechargeTimes }}&nbsp;个月</span
                    >
                    <span id="chargeSpan"
                      >续费到期时间:&nbsp;{{
                        rechargeTarget.endTime
                      }}&nbsp;</span
                    >
                    <span id="chargeSpan"
                      >续费需缴金额:&nbsp;{{
                        rechargeTarget.orderPrice
                      }}&nbsp;元</span
                    >
                  </template>
                  <a-divider />
                  <a-space>
                    <a-tooltip placement="top">
                      <template slot="title">
                        <span>请选择经办人Id</span>
                      </template>
                      <a-select
                        style="width: 50px"
                        @change="reChargeManagerId"
                        v-model="rechargeId"
                      >
                        <template v-for="(item, index) in initManagerIdList">
                          <a-select-option :key="index" :value="item">{{
                            item
                          }}</a-select-option>
                        </template>
                      </a-select>
                    </a-tooltip>
                    <a-tooltip placement="top">
                      <template slot="title">
                        <span>请选择续费类型(包月/包年)</span>
                      </template>
                      <a-select
                        style="width: 190px"
                        @change="reChargeOrderType"
                        v-model="rechargeType"
                      >
                        <a-select-option value="包月">包月</a-select-option>
                        <a-select-option value="包年">包年</a-select-option>
                      </a-select>
                    </a-tooltip>
                    <a-tooltip placement="top">
                      <template slot="title">
                        <span>请选择续费期限(月)</span>
                      </template>
                      <a-select
                        style="width: 190px"
                        @change="reChargeOrderTimes"
                        v-model="rechargeTimes"
                      >
                        <template v-if="rechargeType === '包月'">
                          <template v-for="(item, index) in 12">
                            <a-select-option :key="index" :value="item">{{
                              item
                            }}</a-select-option>
                          </template>
                        </template>
                        <template v-if="rechargeType === '包年'">
                          <template v-for="(item, index) in 24">
                            <a-select-option
                              :key="index"
                              :value="item"
                              v-if="item % 12 == 0"
                              >{{ item }}</a-select-option
                            >
                          </template>
                        </template>
                      </a-select>
                    </a-tooltip>
                  </a-space>
                </div>
              </a-modal>
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
    title: "经办人Id",
    dataIndex: "managerId",
    width: "5%",
    scopedSlots: { customRender: "managerId" },
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
    title: "客户Id",
    dataIndex: "customerId",
    width: "5%",
    scopedSlots: { customRender: "customerId" },
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
    title: "订单生效时间",
    dataIndex: "createTime",
    width: "10%",
    scopedSlots: { customRender: "createTime" },
    align: "center",
  },
  {
    title: "订单过期时间",
    dataIndex: "endTime",
    width: "10%",
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
  {
    title: "本订单过期倒计时时间",
    dataIndex: "timeDifference",
    width: "5%",
    scopedSlots: { customRender: "timeDifference" },
    align: "center",
  },
  {
    title: "操作",
    dataIndex: "operation",
    scopedSlots: { customRender: "operation" },
    align: "center",
  },
];

export default {
  components: {
    name: "OrderOperate",
  },
  data() {
    return {
      Mydata: [], //用来展示页面的数据
      cacheData: [], //缓存数组，来实现cancel功能
      columns, //列名
      editingKey: "", //正在输入的key值，在本系统指的是id
      timer: null,
      isSearch: true, //是否在加载
      search: {
        //查询参数
        pagination: {
          current: 1,
          pageSize: 10,
          total: 0,
        },
        orderSearch: {
          orderName: "", //搜索框的内容，具体传递的内容给后端来判断
          orderType: "",
          createTime: "",
          endTime: "",
          managerName: "",
        },
      },
      inputTimer: null, //输入搜索框的时候的延时计时器
      paginationTimer: null, //点击调换分页的计时器
      deleteTip: false, //点击删除的时候出现的内容
      deleteTarget: {
        manager: {},
        customer: {},
      }, //删除目标
      deleteTimer: null, //删除函数的延时计时器

      rechargeTip: false, //点击续费的时候出现内容
      rechargeTarget: {
        manager: {},
        customer: {},
      }, //续费对象
      rechargeTimer: null, // 续费函数的延时计时器
      rechargeType: "", //这个是续费类型下拉框
      rechargeTimes: "", //这个是续费月份下拉框
      rechargeId: "", //这个是续费管理元Id
      showRechargeTarget: {
        manager: {},
        customer: {},
      }, //展示的续费对象，用于深拷贝....只做展示，不要修改它
      disabledInput: false, //禁用查询
    };
  },
  methods: {
    ...mapActions("order", [
      "getOrderList",
      "getInitList",
      "getManagerNameList",
      "updateOrder",
      "deleteOrder",
      "AddRechargeOrder",
    ]),
    ...mapMutations("order", ["setOrderList"]),
    //搜索调用的函数
    onSearch(value) {
      clearTimeout(this.timer);
      this.isSearch = true;
      this.timer = setTimeout(async () => {
        //去查询数据保存在表格中
        const pa = {
          current: 1,
          pageSize: 10,
          total: this.$store.state.order.total, //为了保证在查询前后的记录条数都一样，避免前端页面显示的页数为1
        };
        //为了重头开始寻找，需要重置分页参数
        Object.assign(this.search.pagination, pa);
        await this.getOrderList(this.search);
        this.Mydata = [...this.OrderList]; //绑定显示数据
        this.cacheData = this.Mydata.map((item) => ({ ...item }));
        this.$nextTick(() => {
          this.isSearch = false;
        });
      }, 800);
    },

    async initData() {
      await this.getOrderList(this.search);
      await this.getInitList(); //加载mangerId和customerId选项
      await this.getManagerNameList(); //加载manager姓名
      this.Mydata = [...this.OrderList]; //绑定显示数据
      this.cacheData = this.Mydata.map((item) => ({ ...item }));
      this.$nextTick(() => {
        this.isSearch = false;
      });
    },

    //当点击分页的时候触发的...,上一页,下一页
    handleTableChange(pagination, filters, sorter) {
      clearTimeout(this.paginationTimer);
      this.isSearch = true;
      this.paginationTimer = setTimeout(async () => {
        Object.assign(this.search.pagination, pagination);
        await this.getOrderList(this.search); //传递正确的页面条数
        this.Mydata = [...this.OrderList]; //绑定显示数据
        this.cacheData = this.Mydata.map((item) => ({ ...item }));
        this.$nextTick(() => {
          this.isSearch = false;
        });
      }, 800);
    },

    //修改列表的时候触发(Input插槽)
    handleChange(value, key, column) {
      clearTimeout(this.inputTimer);
      this.inputTimer = setTimeout(() => {
        if (value.trim() === "") {
          this.$message.error("不要输入空字符", 1);
        }
        const regex = /(;drop)+|(;DROP)+/g;
        const newData = [...this.Mydata];
        //先找到数据表中对应的那一项
        const target = newData.find((item) => key === item.orderId);
        if (regex.test(value)) {
          //如果存在非法字符，提示错误,
          this.$message.error("不要输入非法字符", 2);
          if (target) {
            //如果输入非法字符需要强制退出修改状态...
            delete target.editable;
            this.editingKey = ""; //释放修改状态
            this.Mydata = this.cacheData; //如果输入非法符号把缓冲数组赋值给Mydata
          }
        } else {
          if (target) {
            //如果找到了目标，则更新数据..，然后更新vuex中的列表数据
            target[column] = value;
            this.Mydata = newData;
          }
        }
      }, 800);
    },
    //点击编辑的时候触发
    edit(key) {
      this.disabledInput = true;
      const newData = [...this.Mydata];
      //先找到点击编辑的那个目标
      const target = newData.find((item) => key === item.orderId);
      this.editingKey = key; // 设置当前修改的行为编辑中，这样就可以保证显示出修改的save and cancel，同时其他行不能修改
      if (target) {
        target.editable = true;
        this.Mydata = newData;
      }
    },
    //点击保存的时候触发
    async save(key) {
      const newData = [...this.Mydata];
      const newCacheData = [...this.cacheData];
      const target = newData.find((item) => key === item.orderId);
      const targetCache = newCacheData.find((item) => key === item.orderId);
      this.isSearch = true;
      //先找到目标列
      if (target && targetCache) {
        //访问后台，将目标列传递到后端进行修改，在修改完成后，才进行更新页面
        await this.updateOrder(target);
        await this.getOrderList(this.search); //在订单中需要更新，因为更新id没有加载对应的姓名出来，所以必须重新请求一次
        delete target.editable;
        this.cacheData = this.Mydata.map((item) => ({ ...item }));
        // this.Mydata = newData; //更新显示数据
        // Object.assign(targetCache, target);
        // this.cacheData = newCacheData; //保存后更新缓冲数组
        this.isSearch = false;
      }
      this.editingKey = "";
      this.disabledInput = false;
    },
    //点击取消修改的时候触发
    cancel(key) {
      const newData = [...this.Mydata];
      const target = newData.find((item) => key === item.orderId);
      this.editingKey = ""; //释放修改状态
      if (target) {
        Object.assign(
          target,
          this.cacheData.find((item) => {
            return key === item.orderId;
          })
        );
        delete target.editable; //恢复到显示状态，即显示文本
        this.Mydata = newData;
        this.disabledInput = false;
      }
    },

    //选择订单的经办人Id的下拉框
    handleChangeManagerId(value) {
      const newData = [...this.Mydata];
      //先找到数据表中对应的那一项
      const target = newData.find((item) => this.editingKey === item.orderId);
      if (target) {
        //如果找到了目标，则更新数据..，然后更新vuex中的列表数据
        target.managerId = value;
        this.Mydata = newData;
      }
    },

    //选择订单的类型的下拉框
    handleChangeType(value) {
      const newData = [...this.Mydata];
      const newCacheData = [...this.cacheData];
      //先找到数据表中对应的那一项
      const target = newData.find((item) => this.editingKey === item.orderId);
      const newtarget = newCacheData.find(
        (item) => this.editingKey === item.orderId
      );
      if (target) {
        //如果找到了目标，则更新数据..，
        target.orderType = value;
        this.Mydata = newData;
      }
    },

    //选择查询订单的开始时间的下拉框
    handlerCreateTime(date) {
      if (date != null) {
        this.search.orderSearch.createTime = this.moment(date._d).format(
          "YYYY-MM-DD"
        );
      } else {
        this.search.orderSearch.createTime = ""; //如果为date为null，则设置为空
      }
    },

    //选择查询订单的到期时间的下拉框
    handlerEndTime(date) {
      if (date != null) {
        this.search.orderSearch.endTime = this.moment(date._d).format(
          "YYYY-MM-DD"
        );
      } else {
        this.search.orderSearch.endTime = ""; //如果为date为null，则设置为空
      }
    },

    //选择查询订单的类型的下拉框
    handlerSearch(value) {
      if (value === "不限") {
        this.search.orderSearch.orderType = "";
        return;
      }
      this.search.orderSearch.orderType = value;
    },

    //选择查询经办人的姓名的下拉框
    handlerManagerName(value) {
      if (value === "不限") {
        this.search.orderSearch.managerName = "";
        return;
      }
      this.search.orderSearch.managerName = value;
    },

    // !!!

    //点击删除的时候触发
    deleteInfo(key, index) {
      this.deleteTip = true;
      const newData = [...this.Mydata];
      const newCacheData = [...this.cacheData];
      const target = newData.find((item) => key === item.orderId);
      const targetCache = newCacheData.find((item) => key === item.orderId);
      //先找到目标列
      if (target && targetCache) {
        //访问后台，将目标列传递到后端进行修改，在修改完成后，才进行更新页面
        //先把deleteTarget绑定好...
        this.deleteTarget = target;
      }
    },
    //确认删除的时候触发....
    deleteConfirm(e) {
      //访问后端去删除该对象..
      clearTimeout(this.deleteTimer);
      this.isSearch = true; //删除前显示加载...
      this.deleteTimer = setTimeout(async () => {
        await this.deleteOrder(this.deleteTarget);
        //重新访问一次后端的数据
        await this.getOrderList(this.search);
        this.cacheData = this.Mydata; //删除之后更新缓存数组
        this.isSearch = false;
        this.deleteTip = false;
      }, 800);
    },

    //点击续费的时候触发
    recharge(key) {
      this.rechargeTip = true;
      const newData = [...this.Mydata];
      const newCacheData = [...this.cacheData];
      const target = newData.find((item) => key === item.orderId);
      const targetCache = newCacheData.find((item) => key === item.orderId);
      //先找到目标列
      if (target && targetCache) {
        //访问后台，将目标列传递到后端进行修改，在修改完成后，才进行更新页面
        //深拷贝一个展示对象
        this.showRechargeTarget = JSON.parse(JSON.stringify(target));
        this.rechargeTarget = JSON.parse(JSON.stringify(target));
        this.rechargeTarget.beginTime = this.moment().format("YYYY-MM-DD"); //续费时间
      }
    },

    //选择续费经办人..
    reChargeManagerId(value) {
      //更新续费对象的Id
      this.rechargeTarget.manager.managerId = value;
    },

    //选择续费期限
    reChargeOrderTimes(value) {
      const newCacheData = [...this.cacheData];
      const targetCache = newCacheData.find(
        (item) => this.rechargeTarget.orderId === item.orderId
      ); //用缓冲数据来进行添加修改
      //需要知道续费对象的生效时间，---》其实是原始订单的结束时间
      if (this.moment(targetCache.endTime).diff(this.moment(), "days") < 0) {
        this.rechargeTarget.createTime = this.moment().format("YYYY-MM-DD");
      } else {
        this.rechargeTarget.createTime = targetCache.endTime;
      }
      if (this.rechargeTarget.orderType === "包月") {
        //判断续费的类型是包月还是包年...
        //如果是包月，需要更新当前对象的到期时间，订单价格...，然后再更新到后台..然后再查询一次后台记录刷新页面
        if (this.moment(targetCache.endTime).diff(this.moment(), "days") < 0) {
          //需要判断是否当前订单的到期时候是否小于当前时间，如果是则需要更新订单生效时间
          this.rechargeTarget.endTime = this.moment(
            this.moment(this.moment()).add(value, "months")._d
          ).format("YYYY-MM-DD");
        } else {
          this.rechargeTarget.endTime = this.moment(
            this.moment(targetCache.endTime).add(value, "months")._d
          ).format("YYYY-MM-DD");
        }
        this.rechargeTarget.orderPrice = value * 60.0;
      } else {
        //如果是包年，也需要做对应的时间价格修改...
        if (this.moment(targetCache.endTime).diff(this.moment(), "days") < 0) {
          this.rechargeTarget.endTime = this.moment(
            this.moment(this.moment()).add(value, "months")._d
          ).format("YYYY-MM-DD");
        } else {
          this.rechargeTarget.endTime = this.moment(
            this.moment(targetCache.endTime).add(value, "months")._d
          ).format("YYYY-MM-DD");
        }
        this.rechargeTarget.orderPrice = ((value / 12) * 680.0).toFixed(2);
      }
    },

    //选择续费类型
    reChargeOrderType(value) {
      this.rechargeTimes = "";
      this.rechargeTarget.orderType = value; //先更新一下当前待续费对象的类型
      this.rechargeTarget.endTime = "";
      this.rechargeTarget.orderPrice = "";
    },

    //取消续费的时候进行的回调
    cancelRecharge() {
      this.rechargeTip = false;
      const newData = [...this.Mydata];
      const target = newData.find(
        (item) => this.rechargeTarget.orderId === item.orderId
      );
      if (target) {
        Object.assign(
          target,
          this.cacheData.find((item) => {
            return this.rechargeTarget.orderId === item.orderId;
          })
        );
        this.Mydata = newData;
        this.rechargeTimes = "";
        this.rechargeType = "";
      }
    },

    //点击确认续费的时候触发
    rechargeConfirm() {
      if (
        this.rechargeTimes === "" ||
        this.rechargeType === "" ||
        this.rechargeId === ""
      ) {
        this.$message.error("请完善续费信息", 3);
      } else {
        clearTimeout(this.rechargeTimer);
        this.isSearch = true; //续费前显示加载...
        this.rechargeTimer = setTimeout(async () => {
          await this.AddRechargeOrder(this.rechargeTarget);
          //重新访问一次后端的数据
          await this.getOrderList(this.search);
          this.cacheData = [...this.Mydata]; //续费之后更新缓存数组
          this.isSearch = false;
          this.rechargeTip = false;
          this.rechargeTimes = "";
          this.rechargeType = "";
          this.rechargeId = "";
        }, 800);
      }
    },
  },
  created() {
    this.initData();
  },
  computed: {
    ...mapState("order", [
      "OrderList",
      "total",
      "initCustomerIdList",
      "initManagerIdList",
      "managerNameList",
    ]),
  },
  mounted() {
    //为了解决分页的页数刷新问题
    this.search.pagination.total = this.total;
  },
  watch: {
    total(newvalue, oldvalue) {
      if (oldvalue === newvalue) {
        this.search.pagination.total = oldvalue;
        return;
      }
      this.search.pagination.total = newvalue;
    },
    OrderList(newvalue) {
      this.Mydata = newvalue;
    },
  },
};
</script>

<style lang="less" scope>
#order {
  &-operate-header {
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

#delete {
  &-box {
    width: 100%;
    height: 100%;
    // border: 1px solid red;
    padding: 0 20px;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    span {
      font-weight: bold;
      font-size: 16px;
      font-family: 微软雅黑;
    }
  }
}

#recharge {
  &-box {
    width: 100%;
    height: 100%;
    // border: 1px solid red;
    padding: 0 20px;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    span {
      font-weight: bold;
      font-size: 16px;
      font-family: 微软雅黑;
    }
  }
}

#chargeSpan {
  font-size: 11px;
  font-weight: bold;
  font-family: 微软雅黑;
}
.ant-list-item {
  padding: 2px;
}
</style>
