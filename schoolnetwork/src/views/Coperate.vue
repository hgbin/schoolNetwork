<template>
  <div>
    <div id="customer-operate-header">
      <a-layout>
        <a-layout-header :style="{ background: '#fff' }" class="search-header">
          <a-tooltip placement="top">
            <template slot="title">
              <span>请选择待查询客户的状态</span>
            </template>
            <a-select
              style="width: 250px"
              placeholder="请选择待查询客户的状态"
              @change="handlerSearch"
            >
              <a-icon slot="suffixIcon" type="meh" />
              <a-select-option value="不限">不限</a-select-option>
              <a-select-option value="正常">正常</a-select-option>
              <a-select-option value="禁用">禁用</a-select-option>
            </a-select>
          </a-tooltip>

          <a-tooltip placement="top">
            <template slot="title">
              <span>请选择待查询客户的类型</span>
            </template>
            <a-select
              style="width: 250px"
              placeholder="请选择待查询客户的类型"
              @change="handlerType"
            >
              <a-icon slot="suffixIcon" type="meh" />
              <a-select-option value="不限">不限</a-select-option>
              <a-select-option value="包月">包月</a-select-option>
              <a-select-option value="包年">包年</a-select-option>
            </a-select>
          </a-tooltip>

          <a-tooltip placement="top">
            <template slot="title">
              <span>请输入待查询客户名字/Id/PhoneNum</span>
            </template>
            <a-input-search
              size="large"
              placeholder="请输入待查询客户名字/Id/PhoneNum"
              :style="{ width: '350px' }"
              v-model="search.customerSearch.customerName"
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
                  v-for="col in ['customerName', 'phoneNum', 'dormitoryNum']"
                  :slot="col"
                  slot-scope="text, record, index"
                >
                  <div :key="col">
                    <a-input
                      v-if="record.editable"
                      style="margin: -5px 0"
                      :value="text"
                      @change="
                        (e) =>
                          handleChange(e.target.value, record.customerId, col)
                      "
                    />
                    <template v-else>
                      {{ text }}
                    </template>
                  </div>
                </template>
                <template
                  slot="customerStatus"
                  slot-scope="text, record, index"
                >
                  <template>
                    <a-select
                      v-if="record.editable"
                      default-value="正常"
                      style="width: 120px"
                      @change="handleChangeStatus"
                    >
                      <a-select-option value="正常">正常</a-select-option>
                      <a-select-option value="禁用">禁用</a-select-option>
                    </a-select>
                    <template v-else>{{ text }}</template>
                  </template>
                </template>
                <template slot="customerType" slot-scope="text, record, index">
                  <template>
                    <a-select
                      v-if="record.editable"
                      default-value="包月"
                      style="width: 120px"
                      @change="handleChangeType"
                    >
                      <a-select-option value="包月">包月</a-select-option>
                      <a-select-option value="包年">包年</a-select-option>
                    </a-select>
                    <template v-else>{{ text }}</template>
                  </template>
                </template>
                <template slot="operation" slot-scope="text, record, index">
                  <div class="editable-row-operations">
                    <span v-if="record.editable">
                      <a-popconfirm
                        title="Sure to Save?"
                        @confirm="() => save(record.customerId)"
                      >
                        <a>Save</a>
                      </a-popconfirm>
                      <a-popconfirm
                        title="Sure to cancel?"
                        @confirm="() => cancel(record.customerId)"
                      >
                        <a>Cancel</a>
                      </a-popconfirm>
                    </span>
                    <span v-else>
                      <a-space>
                        <a
                          :disabled="editingKey !== ''"
                          @click="() => edit(record.customerId)"
                        >
                          <a-icon type="edit" />修改
                        </a>
                        <a
                          :disabled="editingKey !== ''"
                          @click="() => deleteInfo(record.customerId, index)"
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
                  <span>确定删除本用户嘛？</span>
                  <a-divider />
                  <span>客户Id:{{ deleteTarget.customerId }}</span>
                  <span>客户名称:{{ deleteTarget.customerName }}</span>
                  <span>客户手机:{{ deleteTarget.phoneNum }}</span>
                  <span>客户状态:{{ deleteTarget.customerStatus }}</span>
                  <span>客户类型:{{ deleteTarget.customerType }}</span>
                  <span>客户宿舍号:{{ deleteTarget.dormitoryNum }}</span>
                  <a-divider />
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
    title: "客户Id",
    dataIndex: "customerId",
    width: "10%",
    align: "center",
  },
  {
    title: "客户名称",
    dataIndex: "customerName",
    width: "15%",
    scopedSlots: { customRender: "customerName" },
    align: "center",
  },
  {
    title: "客户手机号码",
    dataIndex: "phoneNum",
    width: "15%",
    scopedSlots: { customRender: "phoneNum" },
    align: "center",
  },
  {
    title: "客户状态",
    dataIndex: "customerStatus",
    width: "10%",
    scopedSlots: { customRender: "customerStatus" },
    align: "center",
  },
  {
    title: "客户类型",
    dataIndex: "customerType",
    width: "10%",
    scopedSlots: { customRender: "customerType" },
    align: "center",
  },
  {
    title: "客户宿舍号",
    dataIndex: "dormitoryNum",
    width: "10%",
    scopedSlots: { customRender: "dormitoryNum" },
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
    name: "Coperate",
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
        customerSearch: {
          customerName: "", //搜索框的内容，具体传递的内容给后端来判断
          customerStatus: "",
          customerType: "",
        },
      },
      inputTimer: null, //输入搜索框的时候的延时计时器
      paginationTimer: null, //点击调换分页的计时器
      deleteTip: false, //点击删除的时候出现的内容
      deleteTarget: {}, //删除目标
      deleteTimer: null, //删除函数的延时计时器
      disabledInput: false, //禁用查询
    };
  },
  methods: {
    ...mapActions("customer", [
      "getCustomerList",
      "updateCustomer",
      "deleteCustomer",
    ]),
    ...mapMutations("customer", ["setCustomerList"]),
    //搜索调用的函数
    onSearch(value) {
      clearTimeout(this.timer);
      this.isSearch = true;
      this.timer = setTimeout(async () => {
        //去查询数据保存在表格中
        const pa = {
          current: 1,
          pageSize: 10,
          total: this.$store.state.customer.total, //为了保证在查询前后的记录条数都一样，避免前端页面显示的页数为1
        };
        //为了重头开始寻找，需要重置分页参数
        Object.assign(this.search.pagination, pa);
        await this.getCustomerList(this.search);
        this.Mydata = [...this.customerList]; //绑定显示数据
        this.cacheData = this.Mydata.map((item) => ({ ...item }));
        this.$nextTick(() => {
          this.isSearch = false;
        });
      }, 800);
    },
    async initData() {
      await this.getCustomerList(this.search);
      this.Mydata = [...this.customerList]; //绑定显示数据
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
        await this.getCustomerList(this.search); //传递正确的页面条数
        this.Mydata = [...this.customerList]; //绑定显示数据
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
        const target = newData.find((item) => key === item.customerId);
        if (regex.test(value)) {
          //如果存在非法字符，提示错误,
          this.$message.error("不要输入非法字符", 2);
          if (target) {
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
      const target = newData.find((item) => key === item.customerId);
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
      const target = newData.find((item) => key === item.customerId);
      const targetCache = newCacheData.find((item) => key === item.customerId);
      //先找到目标列
      if (target && targetCache) {
        //访问后台，将目标列传递到后端进行修改，在修改完成后，才进行更新页面
        await this.updateCustomer(target);
        delete target.editable;
        this.Mydata = newData; //更新显示数据
        Object.assign(targetCache, target);
        this.cacheData = newCacheData; //保存后更新缓冲数组
      }
      this.editingKey = "";
      this.disabledInput = false;
    },
    //点击取消修改的时候触发
    cancel(key) {
      const newData = [...this.Mydata];
      const target = newData.find((item) => key === item.customerId);
      this.editingKey = ""; //释放修改状态
      if (target) {
        Object.assign(
          target,
          this.cacheData.find((item) => {
            return key === item.customerId;
          })
        );
        delete target.editable; //恢复到显示状态，即显示文本
        this.Mydata = newData;
        this.disabledInput = false;
      }
    },

    //选择客户的类型的下拉框
    handleChangeType(value) {
      const newData = [...this.Mydata];
      //先找到数据表中对应的那一项
      const target = newData.find(
        (item) => this.editingKey === item.customerId
      );
      if (target) {
        //如果找到了目标，则更新数据..，然后更新vuex中的列表数据
        target.customerType = value;
        this.Mydata = newData;
      }
    },

    //选择客户的状态的下拉框
    handleChangeStatus(value) {
      const newData = [...this.Mydata];
      //先找到数据表中对应的那一项
      const target = newData.find(
        (item) => this.editingKey === item.customerId
      );
      if (target) {
        //如果找到了目标，则更新数据..，然后更新vuex中的列表数据
        target.customerStatus = value;
        this.Mydata = newData;
      }
    },

    //选择查询客户的状态的下拉框
    handlerSearch(value) {
      if (value === "不限") {
        this.search.customerSearch.customerStatus = "";
        return;
      }
      this.search.customerSearch.customerStatus = value;
    },

    //选择查询客户的类型的下拉框
    handlerType(value) {
      if (value === "不限") {
        this.search.customerSearch.customerType = "";
        return;
      }
      this.search.customerSearch.customerType = value;
    },

    //点击删除的时候触发
    deleteInfo(key, index) {
      this.deleteTip = true;
      const newData = [...this.Mydata];
      const newCacheData = [...this.cacheData];
      const target = newData.find((item) => key === item.customerId);
      const targetCache = newCacheData.find((item) => key === item.customerId);
      //先找到目标列
      if (target && targetCache) {
        //访问后台，将目标列传递到后端进行修改，在修改完成后，才进行更新页面
        //先把deleteTarget绑定好...
        this.deleteTarget = target;
      }
    },
    //确认删除的时候触发....
    deleteConfirm(e) {
      console.log("待删除的对象", this.deleteTarget);
      //访问后端去删除该对象..
      clearTimeout(this.deleteTimer);
      this.isSearch = true; //删除前显示加载...
      this.deleteTimer = setTimeout(async () => {
        await this.deleteCustomer(this.deleteTarget);
        //重新访问一次后端的数据
        await this.getCustomerList(this.search);
        this.cacheData = this.Mydata; //删除之后更新缓存数组
        this.isSearch = false;
        this.deleteTip = false;
      }, 800);
    },
  },
  created() {
    this.initData();
  },
  computed: {
    ...mapState("customer", ["customerList", "total"]),
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
    customerList(newvalue) {
      this.Mydata = newvalue;
    },
  },
};
</script>

<style lang="less" scope>
#customer {
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
      margin-right: 50px;
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
      font-size: 20px;
      font-family: 微软雅黑;
    }
  }
}
</style>
