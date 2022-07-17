# 1.项目介绍

本项目是基于Vue + JavaEE + antd 实现的客户关系管理系统，能够实现网络管理员对于学生客户的网络签约、续费进行控制管理。

# 2.项目运行前准备

(1).在navicat中导入数据库sql文件。
(2).修改Vue项目的vue.config.js的代理端口号
(3).修改ssm项目的db.properties数据库用户名以及密码
(4).启动Vue项目以及ssm项目

在Vue项目终端中，输入命令 

`npm install`

`npm run serve`

ssm项目中已经内置tomcat7服务器，只需要在idea中配置maven的启动整个项目即可。

# 3.项目运行备注

关于项目中的短信发送功能，因为具有个人隐私性，需要自己去阿里云短信服务平台利用短信测试模板来实现补充对应的accesskey。

