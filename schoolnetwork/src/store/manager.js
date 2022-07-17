/*
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-29 10:38:04
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-05 02:13:50
 */
import qs from 'qs'
import router from '../router'
import axios from 'axios'
import { message } from 'ant-design-vue'
import store from '.'
import { RouterListFormat, generateRouter } from '../libs/utils'
export default {
    namespaced: true,
    state: {
        managerList: [],
        total: 0,
        addManager:{},
    },
    getters: {
    },
    mutations: {
        setManagerList(state, list) {
            state.managerList = list
        },
        setTotal(state, total) {
            state.total = total
        }
    },
    actions: {
        async getManagerList({ state, commit }, search) {
            await axios({
                url: '/network/getManagerList',
                method: 'post',
                // data:{...pagination}
                data: qs.stringify({
                    pagination: JSON.stringify(search.pagination),
                    managerSearch:JSON.stringify(search.managerSearch)
                })
            }).then((res) => {
                if(res.data.code === '200'){
                    commit('setManagerList', res.data.data.ManagerList)
                    commit('setTotal', res.data.data.total)
                }else{
                    let list = []
                    commit('setManagerList', list)
                    commit('setTotal',0)
                    message.error('数据库空空如也',3)
                }
            }).catch((error) => {

            })
        },

        async updateManager({ state, commit }, manager) {
            await axios({
                url: '/network/updateManager',
                method: 'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(manager)
            }).then((res)=>{
                if(res.data.code === '200'){
                    message.success('更新数据成功',3)
                }else{
                    message.error('更新数据失败',3)
                }
            }).catch((err)=>{

            })
        },


        async deleteManager({state,commit},manager){
            await axios({
                url:'/network/deleteManager',
                method:'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(manager)
            }).then((res)=>{
                if(res.data.code === '200'){
                    message.success('删除数据成功',3)
                }else{
                    message.error('删除数据失败',3)
                }
            }).catch((err)=>{

            })
        },


        async addManager({commit,state},addManager){
            console.log(addManager)
            await axios({
                url:'/network/AddManager',
                method:'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(addManager)
            }).then((res)=>{
                console.log(res.data)
                if(res.data.code === '200'){
                    message.success(`添加管理员成功,新管理员的Id为${res.data.data.managerId}`,3);
                }else{
                    message.error('添加管理员失败',3)
                }

            }).catch((err)=>{

            })
        },

        async sendMessage({commit,state}){
            await axios({
                url:'/network/sendMessage',
                method:'post',
            }).then((res)=>{
                if(res.data.code === '200'){
                    message.success('通知成功',3);
                }else{
                    message.error('通知失败',3);
                }
            }).catch((err)=>{

            })
        }
    }
}