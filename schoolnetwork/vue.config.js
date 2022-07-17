/*
 * @Description: 
 * @version: 
 * @Author: Hinsane
 * @Date: 2022-05-27 18:41:28
 * @LastEditors: Hinsane
 * @LastEditTime: 2022-06-06 16:40:38
 */
module.exports = {
    publicPath:'./',
    devServer:{
      proxy:{
          '/network':{
              target:'http://localhost:8099/network',
              changeOrigin:true,
              ws:true,
              pathRewrite: {
                '^/network': ''
            }
          }
      },
    }
}