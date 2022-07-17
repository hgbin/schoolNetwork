/*
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-29 10:38:04
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-06 14:47:45
 */
import qs from 'qs'
import router from '../router'
import axios from 'axios'
import { message,notification } from 'ant-design-vue'
import store from '.'
import { RouterListFormat, generateRouter } from '../libs/utils'
export default {
  namespaced: true,
  state: {
    managerName: '',//登录用户名
    pwd: '',//登录用户密码
    routerList: [],
    showList:[],//用来菜单展示的列表
    hasAuth: false,
    IsCloseList:[],//存放每次登录的时候后端返回快到期的用户的列表
  },
  getters: {
  },
  mutations: {
    setNameAndPwd(state, user) {
      state.managerName = user.managerName
      state.pwd = user.pwd
      console.log('登录信息保存成功')
    },
    setRouterList(state, rlist) {
      state.routerList = rlist
    },
    setAuth(state, auth) {
      state.hasAuth = auth
    },
    setshowList(state,list){
      state.showList = list
    },
    addRoutes(state, rlist) {
      const newRoutes = generateRouter(rlist)
      newRoutes.map((item) => {
        router.addRoute(item)
      })
      // console.log(router.options)
    },
    setIsCloseList(state,list){
      state.IsCloseList = list;
    }
  },
  actions: {
    async LoginPost({ commit, state,dispatch}, user) {
      await axios({
        url: '/network/login',
        method: 'post',
        headers: {
          'Content-Type': 'application/json'
        },
        data: JSON.stringify(user)
      }).then((res) => {
        if (res.data.code === '200') {//如果验证身份成功则保存信息
          sessionStorage.setItem('user', JSON.stringify({managerName:user.managerName,pwd:user.pwd}))
          commit('setNameAndPwd', user)
          commit('setRouterList', RouterListFormat(res.data.data))//把获取的菜单转化为树模型
          commit('setAuth', true)
          commit('addRoutes',state.routerList)
          setTimeout(()=>{
            dispatch('getIsCloseFinshed')
          },10000)
          message.success('登录成功', 3)
          router.push('/index')
        } else if (res.data.code === '500') {
          message.error('登录失败', 3)
        }
      }).catch((err) => {
        console.log(err.message)
      })
    },

    async LoginOut({commit,state}){
     await axios({
        url:'/network/LoginOut',
        method:'post',
      }).then((res)=>{
        if(res.data.code === '200'){
          commit('setAuth',false)
          router.push('/')
          message.success('成功退出系统',3)
        }else{
          message.error('退出失败',3)
        }
      }).catch((err)=>{

      })
    },

    async getIsCloseFinshed({commit,state}){
      await axios({
        url:'/network/getIsCloseFinshed',
        method:'post',
      }).then((res)=>{
        if(res.data.code === '200'){
          //如果有快到期的用户，则更新
          commit('setIsCloseList',res.data.data)
          notification.info({
            message: '学生用户到期通知',
            description:
              `有 ${state.IsCloseList.length} 位学生的网络快到期咯，快去通知他们...`,
            duration: 0,
            placement:'bottomRight'
          })
        }
      }).catch((err)=>{

      })
    },
  }
}