<!--
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-29 19:34:34
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-04 10:52:12
-->
<template>
  <div id="manager-operate-header">
    <a-layout>
      <a-layout-header :style="{ background: '#fff' }" class="search-header">
        <a-tooltip placement="top">
          <template slot="title">
            <span>请选择待查询管理员的状态</span>
          </template>
          <a-select
            style="width: 250px"
            placeholder="请选择待查询管理员的状态"
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
            <span>请输入待查询管理员名字/Id</span>
          </template>
          <a-input-search
            size="large"
            placeholder="请输入待查询管理员名字/Id"
            :style="{ width: '350px' }"
            v-model="search.managerSearch.managerName"
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
                v-for="col in ['managerName', 'pwd']"
                :slot="col"
                slot-scope="text, record, index"
              >
                <div :key="col">
                  <a-input
                    v-if="record.editable"
                    style="margin: -5px 0"
                    :value="text"
                    @change="
                      (e) => handleChange(e.target.value, record.managerId, col)
                    "
                  />
                  <template v-else>
                    {{ text }}
                  </template>
                </div>
              </template>
              <template slot="managerIdentity" slot-scope="text, record, index">
                <template>
                  <a-select
                    v-if="record.editable"
                    :default-value="text"
                    style="width: 120px"
                    @change="handleChangeIdentity"
                  >
                    <a-select-option value="1">系统管理员</a-select-option>
                    <a-select-option value="2">业务管理员</a-select-option>
                  </a-select>
                  <template v-else>{{ text }}</template>
                </template>
              </template>
              <template slot="managerStatus" slot-scope="text, record, index">
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
              <template slot="operation" slot-scope="text, record, index">
                <div class="editable-row-operations">
                  <span v-if="record.editable">
                    <a-popconfirm
                      title="Sure to Save?"
                      @confirm="() => save(record.managerId)"
                    >
                      <a>Save</a>
                    </a-popconfirm>
                    <a-popconfirm
                      title="Sure to cancel?"
                      @confirm="() => cancel(record.managerId)"
                    >
                      <a>Cancel</a>
                    </a-popconfirm>
                  </span>
                  <span v-else>
                    <a-space>
                      <a
                        :disabled="
                          editingKey !== '' ||
                          record.managerName == $store.state.user.managerName
                        "
                        @click="() => edit(record.managerId)"
                      >
                        <a-icon type="edit" />修改
                      </a>
                      <a
                        :disabled="
                          editingKey !== '' ||
                          record.managerName == $store.state.user.managerName
                        "
                        @click="() => deleteInfo(record.managerId, index)"
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
                <span>管理员Id:{{ deleteTarget.managerId }}</span>
                <span>管理员名称:{{ deleteTarget.managerName }}</span>
                <span>管理员密码:{{ deleteTarget.pwd }}</span>
                <span>管理员状态:{{ deleteTarget.managerStatus }}</span>
                <span>管理员身份:{{ deleteTarget.managerIdentity }}</span>
                <a-divider />
              </div>
            </a-modal>
          </a-layout-content>
        </a-spin>
      </a-layout-content>
    </a-layout>
  </div>
</template>

<script>
import { mapMutations, mapActions, mapState } from "vuex";
const columns = [
  {
    title: "管理员Id",
    dataIndex: "managerId",
    width: "10%",
    align: "center",
  },
  {
    title: "管理员名称",
    dataIndex: "managerName",
    width: "15%",
    scopedSlots: { customRender: "managerName" },
    align: "center",
  },
  {
    title: "管理员密码",
    dataIndex: "pwd",
    width: "10%",
    scopedSlots: { customRender: "pwd" },
    align: "center",
  },
  {
    title: "管理员身份(1为系统管理员,2为业务管理员)",
    dataIndex: "managerIdentity",
    width: "20%",
    scopedSlots: { customRender: "managerIdentity" },
    align: "center",
  },
  {
    title: "管理员状态",
    dataIndex: "managerStatus",
    width: "10%",
    scopedSlots: { customRender: "managerStatus" },
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
    name: "Moperate",
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
        managerSearch: {
          managerName: "",
          managerStatus: "",
        },
      },
      inputTimer: null,
      paginationTimer: null,
      deleteTip: false,
      deleteTarget: {},
      deleteTimer: null,
      disabledInput:false,//禁用查询
    };
  },
  methods: {
    ...mapActions("manager", [
      "getManagerList",
      "updateManager",
      "deleteManager",
    ]),
    ...mapMutations("manager", ["setManagerList"]),
    //搜索调用的函数
    onSearch(value) {
      clearTimeout(this.timer);
      this.isSearch = true;
      this.timer = setTimeout(async () => {
        //去查询数据保存在表格中
        const pa = {
          current: 1,
          pageSize: 10,
          total: this.$store.state.manager.total, //为了保证在查询前后的记录条数都一样，避免前端页面显示的页数为1
        };
        //为了重头开始寻找，需要重置分页参数
        Object.assign(this.search.pagination, pa);
        await this.getManagerList(this.search);
        this.Mydata = [...this.managerList]; //绑定显示数据
        this.cacheData = this.Mydata.map((item) => ({ ...item }));
        this.$nextTick(() => {
          this.isSearch = false;
        });
      }, 800);
    },
    async initData() {
      await this.getManagerList(this.search);
      this.Mydata = [...this.managerList]; //绑定显示数据
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
        await this.getManagerList(this.search); //传递正确的页面条数
        this.Mydata = [...this.managerList]; //绑定显示数据
        this.cacheData = this.Mydata.map((item) => ({ ...item }));
        this.$nextTick(() => {
          this.isSearch = false;
        });
      }, 800);
    },

    //修改列表的时候触发
    handleChange(value, key, column) {
      clearTimeout(this.inputTimer);
      this.inputTimer = setTimeout(() => {
        if (value.trim() === "") {
          this.$message.error("不要输入空字符", 1);
        }
        const regex = /(;drop)+|(;DROP)+/g;
        const newData = [...this.Mydata];
        //先找到数据表中对应的那一项
        const target = newData.find((item) => key === item.managerId);
        if (regex.test(value)) {
          //如果存在非法字符，提示错误,
          this.$message.error("不要输入非法字符", 2);
          if (target) {
            delete target.editable;
            this.editingKey = ""; //释放修改状态
            this.Mydata = this.cacheData;
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
      const target = newData.find((item) => key === item.managerId);
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
      const target = newData.find((item) => key === item.managerId);
      const targetCache = newCacheData.find((item) => key === item.managerId);
      //先找到目标列
      if (target && targetCache) {
        //访问后台，将目标列传递到后端进行修改，在修改完成后，才进行更新页面
        await this.updateManager(target);
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
      const target = newData.find((item) => key === item.managerId);
      this.editingKey = ""; //释放修改状态
      this.disabledInput = false;
      if (target) {
        Object.assign(
          target,
          this.cacheData.find((item) => {
            return key === item.managerId;
          })
        );
        delete target.editable; //恢复到显示状态，即显示文本
        this.Mydata = newData;
      }
    },

    //选择管理员权限的下拉框
    handleChangeIdentity(value) {
      const newData = [...this.Mydata];
      //先找到数据表中对应的那一项
      const target = newData.find((item) => this.editingKey === item.managerId);
      if (target) {
        //如果找到了目标，则更新数据..，然后更新vuex中的列表数据
        target.managerIdentity = value;
        this.Mydata = newData;
      }
    },

    //选择管理员的状态的下拉框
    handleChangeStatus(value) {
      const newData = [...this.Mydata];
      //先找到数据表中对应的那一项
      const target = newData.find((item) => this.editingKey === item.managerId);
      if (target) {
        //如果找到了目标，则更新数据..，然后更新vuex中的列表数据
        target.managerStatus = value;
        this.Mydata = newData;
      }
    },

    //选择查询管理员的状态的下拉框
    handlerSearch(value) {
      if (value === "不限") {
        this.search.managerSearch.managerStatus = "";
        return;
      }
      this.search.managerSearch.managerStatus = value;
    },

    //点击删除的时候触发
    deleteInfo(key, index) {
      this.deleteTip = true;
      const newData = [...this.Mydata];
      const newCacheData = [...this.cacheData];
      const target = newData.find((item) => key === item.managerId);
      const targetCache = newCacheData.find((item) => key === item.managerId);
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
        await this.deleteManager(this.deleteTarget);
        //重新访问一次后端的数据
        await this.getManagerList(this.search);
        this.cacheData = this.Mydata; //删除之后更新缓存数组(!!!!)
        console.log("删除之后的缓存", this.cacheData);
        this.isSearch = false;
        this.deleteTip = false;
      }, 800);
    },
  },
  created() {
    this.initData();
  },
  computed: {
    ...mapState("manager", ["managerList", "total"]),
    ...mapState("user", ["managerName"]),
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
    managerList(newvalue) {
      this.Mydata = newvalue;
    },
  },
};
</script>

<style lang="less" scope>
#manager {
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
