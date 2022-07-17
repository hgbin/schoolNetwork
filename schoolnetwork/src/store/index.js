/*
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-27 18:40:40
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-01 16:19:39
 */
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

import user from './user.js'
import manager from './manager.js'
import customer from './customer.js'
import order from './order.js'
export default new Vuex.Store({
  state: {
    showMenu:false
  },
  mutations: {
    setShowMenu(state,show){ //侧边栏展开菜单触发
      state.showMenu = show
    }
  },
  actions: {
  },
  modules: {
    user,
    manager,
    customer,
    order
  }
})
