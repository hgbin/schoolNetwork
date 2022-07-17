/*
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-27 18:40:40
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-05-30 00:56:07
 */
import Vue from 'vue'
import VueRouter from 'vue-router'

// 解决报错
const originalPush = VueRouter.prototype.push
const originalReplace = VueRouter.prototype.replace
// push
VueRouter.prototype.push = function push (location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}
// replace
VueRouter.prototype.replace = function push (location, onResolve, onReject) {
  if (onResolve || onReject) return originalReplace.call(this, location, onResolve, onReject)
  return originalReplace.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

const routes = [
  {
    path:'/',
    name:'Login',
    component:()=>import('../views/Login.vue')
  },
  {
    path:'/login',
    redirect: '/'
  }
]




const router = new VueRouter({
  routes
})

export default router
