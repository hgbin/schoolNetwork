/*
 Navicat MySQL Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : network

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 12/06/2022 15:32:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `customer_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户名',
  `phone_num` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户手机，唯一索引',
  `dormitory_num` int(0) NOT NULL COMMENT '客户宿舍号',
  `customer_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '正常' COMMENT '客户状态，分别为“正常”与“禁用”状态',
  `customer_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户类别,有”月份客户”和”年份客户”',
  PRIMARY KEY (`customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (1, 'huang', '13133343333', 947, '正常', '包月客户');

-- ----------------------------
-- Table structure for manager
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager`  (
  `manager_id` int(0) NOT NULL AUTO_INCREMENT,
  `manager_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员名称',
  `pwd` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码（6 位以上）\r\n密码（6 位以上）',
  `manager_identity` int(0) NOT NULL DEFAULT 2 COMMENT '管理员身份，例如:”1”代表系统管理员，”2”为业务管理员',
  `manager_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '正常' COMMENT '管理员状态，分别为“正常”与“禁用”状态',
  PRIMARY KEY (`manager_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of manager
-- ----------------------------
INSERT INTO `manager` VALUES (1, 'admin', '123456', 1, '正常');
INSERT INTO `manager` VALUES (2, 'manager', '123456', 2, '正常');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `order_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `manager_id` int(0) NULL DEFAULT NULL COMMENT '经办人(管理员 id)，外键',
  `customer_id` int(0) NULL DEFAULT NULL COMMENT '客户 id，外键',
  `order_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单类别，有”按月缴费”和”按年缴费”',
  `create_time` date NOT NULL COMMENT '生效时间（”yyyy-mm-dd”）格式',
  `end_time` date NOT NULL COMMENT '过期时间（”yyyy-mm-dd”）格式',
  `order_price` double(10, 2) NOT NULL COMMENT '订单价格',
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `customerid`(`customer_id`) USING BTREE,
  INDEX `managerid`(`manager_id`) USING BTREE,
  CONSTRAINT `customerid` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `managerid` FOREIGN KEY (`manager_id`) REFERENCES `manager` (`manager_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 1, 1, '按月缴费', '2022-06-11', '2022-07-11', 30.00);

-- ----------------------------
-- Table structure for rechargeorder
-- ----------------------------
DROP TABLE IF EXISTS `rechargeorder`;
CREATE TABLE `rechargeorder`  (
  `recharge_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` int(0) NOT NULL COMMENT '订单 Id,外键',
  `manager_id` int(0) NOT NULL COMMENT '经办人 Id',
  `customer_id` int(0) NOT NULL COMMENT '客户 Id',
  `begin_time` date NOT NULL COMMENT '订单续费时间',
  `create_time` date NOT NULL COMMENT '订单续费生效时间',
  `end_time` date NOT NULL COMMENT '订单续费过期时间',
  `order_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单续费类型',
  `order_price` double(10, 2) NOT NULL COMMENT '订单续费续交费用',
  PRIMARY KEY (`recharge_id`) USING BTREE,
  INDEX `orderid`(`order_id`) USING BTREE,
  INDEX `cid`(`customer_id`) USING BTREE,
  INDEX `mid`(`manager_id`) USING BTREE,
  CONSTRAINT `cid` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `mid` FOREIGN KEY (`manager_id`) REFERENCES `manager` (`manager_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `orderid` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rechargeorder
-- ----------------------------
INSERT INTO `rechargeorder` VALUES (1, 1, 1, 1, '2022-06-12', '2022-07-11', '2022-08-11', '按月缴费', 30.00);

-- ----------------------------
-- Table structure for router
-- ----------------------------
DROP TABLE IF EXISTS `router`;
CREATE TABLE `router`  (
  `rid` int(0) NOT NULL AUTO_INCREMENT,
  `pid` int(0) NOT NULL,
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `link` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `title` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`rid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of router
-- ----------------------------
INSERT INTO `router` VALUES (1, 14, 'manager', 'Manager', '/index/manager', '宽带业务员管理');
INSERT INTO `router` VALUES (2, 1, 'moperate', 'Moperate', '/index/manager/moperate', '业务员控制管理');
INSERT INTO `router` VALUES (3, 1, 'addmanager', 'AddManager', '/index/manager/addmanager', '添加网络管理员');
INSERT INTO `router` VALUES (7, 14, 'customer', 'Customer', '/index/customer', '学生客户管理');
INSERT INTO `router` VALUES (8, 7, 'coperate', 'Coperate', '/index/customer/coperate', '学生控制管理');
INSERT INTO `router` VALUES (9, 7, 'addcustomer', 'AddCustomer', '/index/customer/addcustomer', '添加学生客户');
INSERT INTO `router` VALUES (10, 14, 'order', 'Order', '/index/order', '订单管理');
INSERT INTO `router` VALUES (11, 10, 'orderoperate', 'OrderOperate', '/index/order/orderoperate', '订单控制管理');
INSERT INTO `router` VALUES (12, 10, 'addorder', 'AddOrder', '/index/order/addorder', '添加订单');
INSERT INTO `router` VALUES (13, 10, 'statistics', 'OrderStatistics', '/index/order/statistics', '统计新增订单');
INSERT INTO `router` VALUES (14, 0, '/index', 'Index', '/index', '网络管理中心');
INSERT INTO `router` VALUES (15, 10, 'rechargestatistics', 'RechargeStatistic', '/index/order/rechargestatistics', '统计续费订单');

-- ----------------------------
-- Table structure for user_router
-- ----------------------------
DROP TABLE IF EXISTS `user_router`;
CREATE TABLE `user_router`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `identity_id` int(0) NOT NULL,
  `router_id` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_router
-- ----------------------------
INSERT INTO `user_router` VALUES (1, 1, 1);
INSERT INTO `user_router` VALUES (2, 1, 2);
INSERT INTO `user_router` VALUES (3, 1, 3);
INSERT INTO `user_router` VALUES (7, 1, 7);
INSERT INTO `user_router` VALUES (8, 1, 8);
INSERT INTO `user_router` VALUES (9, 1, 9);
INSERT INTO `user_router` VALUES (10, 1, 10);
INSERT INTO `user_router` VALUES (11, 1, 11);
INSERT INTO `user_router` VALUES (12, 1, 12);
INSERT INTO `user_router` VALUES (13, 1, 13);
INSERT INTO `user_router` VALUES (17, 2, 7);
INSERT INTO `user_router` VALUES (18, 2, 8);
INSERT INTO `user_router` VALUES (19, 2, 9);
INSERT INTO `user_router` VALUES (20, 2, 10);
INSERT INTO `user_router` VALUES (21, 2, 11);
INSERT INTO `user_router` VALUES (22, 2, 12);
INSERT INTO `user_router` VALUES (23, 2, 13);
INSERT INTO `user_router` VALUES (24, 1, 14);
INSERT INTO `user_router` VALUES (25, 2, 14);
INSERT INTO `user_router` VALUES (26, 1, 15);
INSERT INTO `user_router` VALUES (27, 2, 15);

SET FOREIGN_KEY_CHECKS = 1;
