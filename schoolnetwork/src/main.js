/*
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-27 18:40:40
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-07 11:09:28
 */
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

Vue.config.productionTip = false
import qs from 'qs'
import axios from 'axios'
import 'ant-design-vue/dist/antd.css'
import Antd from 'ant-design-vue'
import moment from 'moment'
import { generateRouter } from './libs/utils.js'
import {message} from 'ant-design-vue'

Vue.prototype.axios = axios
Vue.prototype.qs = qs
Vue.prototype.moment = moment

Vue.use(Antd)

router.beforeEach(async(to,from,next) =>{
  // alert(to.path)
  if(to.path === '/' || to.path === '/login'){
    //证明是去登录页面
    next()//直接放行
  }
  else{
    //如果不是去登录页面，考虑登录情况，没有登录的先登录
    if(sessionStorage.getItem("user")){
      //证明登录了,同时需要更新state状态
       if(!store.state.user.hasAuth){
         //刷新时还没有获取到菜单，需要重新去后台获取一次
         await store.dispatch('user/LoginPost',JSON.parse(sessionStorage.getItem("user")))
        //  store.commit('user/setAuth', true)
         next({path:to.path}) //只有这样刷新才能到达指定路径的页面
        //  next() 
       }else{
         next()
       }
    }else{
      //提示先登录，拒绝放行
      message.error('请先登录',3)
      next('/')
    }
  }
})


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')


