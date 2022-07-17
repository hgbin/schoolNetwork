/*
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-29 10:38:04
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-06 13:08:13
 */
import qs from 'qs'
import router from '../router'
import axios from 'axios'
import { message } from 'ant-design-vue'
import store from '.'
export default {
    namespaced: true,
    state: {
        OrderList: [],
        total: 0,
        initCustomerIdList:[],
        initManagerIdList:[],
        managerNameList:[],
        StatisticsOrderList:[],//统计的列表
        StatisticsTotal:0,//统计的总数
        StatisticsPrice:"",//统计的总价格.
    },
    getters: {
    },
    mutations: {
        setOrderList(state, list) {
            state.OrderList = list
        },
        setTotal(state, total) {
            state.total = total
        },
        setinitCustomerIdList(state,list){
            state.initCustomerIdList = list
        },
        setinitManagerIdList(state,list){
            state.initManagerIdList = list
        },
        setmanagerNameList(state,list){
            state.managerNameList = list
        },
        setStatisticsOrderList(state,list){
            state.StatisticsOrderList = list
        },
        setStatisticsTotal(state,total){
            state.StatisticsTotal = total
        },
        setStatisticsPrice(state,price){
            state.StatisticsPrice = price
        }
    },
    actions: {
        async getOrderList({ state, commit }, search) {

            await axios({
                url: '/network/getOrderList',
                method: 'post',
                // data:{...pagination}
                data: qs.stringify({
                    pagination:JSON.stringify(search.pagination),
                    orderSearch:JSON.stringify(search.orderSearch)
                })
            }).then((res) => {
                if(res.data.code === '200'){
                    commit('setOrderList', res.data.data.OrderList)
                    commit('setTotal', res.data.data.total)
                }else{
                    let list = []
                    commit('setOrderList', list)
                    message.error('数据库空空如也',3)
                }
            }).catch((error) => {

            })
        },

        async getInitList({state,commit}){
            await axios({
                url:'/network/getInitList',
                method:'post'
            }).then((res)=>{
                if(res.data.data.initManagerIdList != null){
                    commit('setinitManagerIdList',res.data.data.initManagerIdList)
                }
                if(res.data.data.initCustomerIdList != null){
                    commit('setinitCustomerIdList',res.data.data.initCustomerIdList)
                }
            }).catch((err)=>{

            })
        },

        async getManagerNameList({state,commit}){
            await axios({
                url:'/network/getManagerNameList',
                method:'post'
            }).then((res)=>{
                if(res.data.data.managerList != null){
                    commit('setmanagerNameList',res.data.data.managerList)
                }
            }).catch((err)=>{

            })
        },

        async updateOrder({ state, commit }, order) {
            await axios({
                url: '/network/updateOrder',
                method: 'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(order)
            }).then((res)=>{
                if(res.data.code === '200'){
                    message.success('更新数据成功',3)
                }else{
                    message.error('更新数据失败',3)
                }
            }).catch((err)=>{

            })
        },


        async deleteOrder({state,commit},order){
            console.log('axios接收到的target' + JSON.stringify(order))
            await axios({
                url:'/network/deleteOrder',
                method:'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(order)
            }).then((res)=>{
                if(res.data.code === '200'){
                    message.success('删除数据成功',3)
                }else{
                    message.error('删除数据失败',3)
                }
            }).catch((err)=>{

            })
        },

        async AddOrder({commit,state},addOrder){
            await axios({
                url:'/network/AddOrder',
                method:'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(addOrder)
            }).then((res)=>{
                if(res.data.code === '200'){
                    message.success(`添加订单成功,订单Id为${res.data.data.orderId}`,3);
                }else{
                    message.error(`${res.data.message}`,3)
                }

            }).catch((err)=>{

            })
        },

        //统计获取
        async getStatisticsOrderList({ state, commit }, search) {
            await axios({
                url: '/network/getStatisticsOrderList',
                method: 'post',
                // data:{...pagination}
                data: qs.stringify({
                    pagination:JSON.stringify(search.pagination),
                    orderStatistics:JSON.stringify(search.orderStatistics),
                    tableName:search.tableName
                })
            }).then((res) => {
                if(res.data.code === '200'){
                    commit('setStatisticsOrderList', res.data.data.StatisticOrderList)
                    commit('setStatisticsTotal', res.data.data.StatisticsTotal)
                    commit('setStatisticsPrice', res.data.data.StatisticsPrice)
                }else{
                    let list = []
                    commit('setStatisticsOrderList', list)
                    commit('setStatisticsTotal', 0)
                    commit('setStatisticsPrice', 0)
                    message.error('数据库空空如也',3)
                }
            }).catch((error) => {

            })
        },
        
        //续费功能
       async AddRechargeOrder({commit,state},order){
            console.log(order);
           await axios({
                url:'/network/AddChargeOrder',
                method:'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                data:JSON.stringify(order)
            }).then((res)=>{
                if(res.data.code === '200'){
                    message.success('续费成功',3)
                }else{
                    message.error('续费失败',3)
                }
            }).catch((err)=>{

            })
        },


        //统计导出excel
        async getAllExportList({ state, commit }, search) {
            console.log(search.tableName)
            location.href = `/network/DownLoadExcel?type=${search.type}&tableName=${search.tableName}`;
        },

    }
}