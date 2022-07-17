/*
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-29 10:38:04
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-04 18:35:31
 */
import qs from 'qs'
import router from '../router'
import axios from 'axios'
import { message } from 'ant-design-vue'
import store from '.'
export default {
    namespaced: true,
    state: {
        customerList: [],
        total: 0
    },
    getters: {
    },
    mutations: {
        setCustomerList(state, list) {
            state.customerList = list
        },
        setTotal(state, total) {
            state.total = total
        }
    },
    actions: {
        async getCustomerList({ state, commit }, search) {
            await axios({
                url: '/network/getCustomerList',
                method: 'post',
                // data:{...pagination}
                data: qs.stringify({
                    pagination:JSON.stringify(search.pagination),
                    customerSearch:JSON.stringify(search.customerSearch)
                })
            }).then((res) => {
                if(res.data.code === '200'){
                    commit('setCustomerList', res.data.data.CustomerList)
                    commit('setTotal', res.data.data.total)
                }else{
                    let list = []
                    commit('setCustomerList', list)
                    commit('setTotal', 0)
                    message.error('数据库空空如也',3)
                }
            }).catch((error) => {

            })
        },

        async updateCustomer({ state, commit }, customer) {
            await axios({
                url: '/network/updateCustomer',
                method: 'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(customer)
            }).then((res)=>{
                if(res.data.code === '200'){
                    message.success('更新数据成功',3)
                }else{
                    message.error('更新数据失败',3)
                }
            }).catch((err)=>{

            })
        },


        async deleteCustomer({state,commit},customer){
            console.log('axios接收到的target' + JSON.stringify(customer))
            await axios({
                url:'/network/deleteCustomer',
                method:'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(customer)
            }).then((res)=>{
                if(res.data.code === '200'){
                    message.success('删除数据成功',3)
                }else{
                    message.error('删除数据失败',3)
                }
            }).catch((err)=>{

            })
        },


        async AddCustomer({commit,state},addCustomer){
            console.log(addCustomer)
            await axios({
                url:'/network/AddCustomer',
                method:'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(addCustomer)
            }).then((res)=>{
                console.log(res.data)
                if(res.data.code === '200'){
                    message.success(`添加学生客户成功,客户Id为${res.data.data.customerId}`,3);
                }else{
                    message.error(`${res.data.message}`,3)
                }

            }).catch((err)=>{

            })
        }
    }
}