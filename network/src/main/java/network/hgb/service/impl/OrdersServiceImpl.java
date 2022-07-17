package network.hgb.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import network.hgb.dao.OrdersDao;
import network.hgb.dao.ReChargeOderDao;
import network.hgb.exception.OrderExistException;
import network.hgb.pojo.Customer;
import network.hgb.pojo.Manager;
import network.hgb.pojo.Order;
import network.hgb.service.OrdersService;
import network.hgb.utils.ExportExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/31 23:49
 */
@Service
@Data
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    OrdersDao ordersDao;

    @Autowired
    ReChargeOderDao reChargeOder;

    Order order;
    Customer customer;
    Manager manager;

    @Override
    public Map<String, Object> getOrderList(JSONObject pagination, JSONObject orderSearch) {
        //首先先获取分页的基本信息，每页页数，当前页数，而且需要在查询到总条数的时候，更新到前台...
        String current = pagination.getString("current");
        String pageSize = pagination.getString("pageSize");
        String total = pagination.getString("total");
        if (current == null || pageSize == null) {
            return null;//如果传递的参数有误,则返回空值
        }
        //如果不为空，先判断一下
        int Intcurrent = Integer.parseInt(current);
        int IntpageSize = Integer.parseInt(pageSize);
        int Inttotal = Integer.parseInt(total);
        if (Integer.parseInt(current) <= 0) {
            //恢复当前页数到第一页
            Intcurrent = 1;
        }
        if (Integer.parseInt(pageSize) <= 0) {
            //恢复默认一页的页数为10条记录
            IntpageSize = 10;
        }

        //接下来找到参数order
        String text = orderSearch.getString("orderName");
        String orderType = orderSearch.getString("orderType");
        String createTime = orderSearch.getString("createTime");
        String endTime = orderSearch.getString("endTime");
        String managerName = orderSearch.getString("managerName");


        //检测orderid or customerName
        customer = new Customer();
        manager = new Manager();
        order = new Order();
        checkIsID(text, orderType, createTime, endTime, managerName);


        //如果都校验成功后，则去后台查询分页数据.
        List<Order> orderList = ordersDao.getOrdersList((Intcurrent - 1) * IntpageSize, IntpageSize, order, customer, manager);
        Map<String, Object> map = new HashMap<>();
        if (orderList != null && orderList.size() != 0) {
            //如果分页查询的结果不为空，则返回查询总条数，以及当前分页的数据
            //调用查询条数的方法
            Inttotal = ordersDao.getTotal(getOrder(), getCustomer(), getManager());

            //需要遍历是否存在续费订单的情况
            for (Order temp : orderList) {
                List<Order> getChargeList = reChargeOder.getChargeOrder(temp.getOrderId());//通过orderId外键来寻找...
                if (getChargeList != null && getChargeList.size() != 0) {
                    //证明不为空
                    temp.setChargeOrderList(getChargeList);
                }
            }

            map.put("OrderList", orderList);
            map.put("total", Inttotal);
            return map;
        }
        //如果查询记录为空，则直接返回null
        return null;
    }

    @Override
    public boolean updateOrder(Order order) {
        int count = ordersDao.updateOrder(order);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteOrder(Order order) {
        int count = ordersDao.deleteOrder(order);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }


    //检查查询的数据
    public void checkIsID(String text, String Type, String createTime, String endTime, String managerName) {
        if (text.trim().equals("")) {
            //如果去首尾空格还是空字符串，则直接设置其他属性
            order.setOrderType(Type);
            order.setCreateTime(createTime);
            order.setEndTime(endTime);
            manager.setManagerName(managerName);
            return;
        }

        //只有可能为orderID或者customerName
        String Idpattern = "^[0-9]*$";
        Pattern r1 = Pattern.compile(Idpattern);
        Matcher m1 = r1.matcher(text);
        if (m1.matches()) {
            //为true代表为数字，则添加到orderId中
            order.setOrderId(Integer.parseInt(text));
        } else {
            //否则添加为name
            customer.setCustomerName(text);
        }
        order.setOrderType(Type);
        order.setCreateTime(createTime);
        order.setEndTime(endTime);
        manager.setManagerName(managerName);
    }

    @Transactional
    @Override
    public int AddOrder(Order order) throws OrderExistException {
        //需要先判断order的customerId是否为第一次下单，如果已经下单，则拒绝继续重新下单,
        //
        Order hasOrder = ordersDao.IshasOrder(order.getCustomerId());
        if (hasOrder != null) {
            //如果不为空，则证明已经存在订单...
            throw new OrderExistException("该客户已经存在一张订单了...,无法继续下单", hasOrder.getOrderId());
        } else {
            //不存在订单的时候，正常下单...
            return ordersDao.AddOrder(order);
        }
    }


    /**
     * 查询统计数据
     *
     * @param pagination
     * @param type       查询类型
     * @return
     */
    @Override
    public Map<String, Object> getStatisticsOrderList(JSONObject pagination, JSONObject type, String tableName) {
        //首先先获取分页的基本信息，每页页数，当前页数，而且需要在查询到总条数的时候，更新到前台...
        String current = pagination.getString("current");
        String pageSize = pagination.getString("pageSize");
        String total = pagination.getString("total");
        if (current == null || pageSize == null || current.equals("") || pageSize.equals("") || total == null || total.equals("")) {
            System.out.println("???");
            return null;//如果传递的参数有误,则返回空值
        }
        //如果不为空，先判断一下
        int Intcurrent = Integer.parseInt(current);
        int IntpageSize = Integer.parseInt(pageSize);
        int Inttotal = Integer.parseInt(total);
        if (Integer.parseInt(current) <= 0) {
            //恢复当前页数到第一页
            Intcurrent = 1;
        }
        if (Integer.parseInt(pageSize) <= 0) {
            //恢复默认一页的页数为10条记录
            IntpageSize = 10;
        }
        //接下来找到参数查询类型
        String searchType = type.getString("type");
        System.out.println(searchType);
        //如果都校验成功后，则去后台查询分页数据.
        List<Order> orderList = new ArrayList<>();
        if (tableName.equals("orders")) { //如果是查询order表的
            orderList = ordersDao.getStatisticsOrderList((Intcurrent - 1) * IntpageSize, IntpageSize, searchType);
        } else if (tableName.equals("rechargeorder")) {//这个是查询rechargeorder表的
            orderList = reChargeOder.getStatisticsOrderList((Intcurrent - 1) * IntpageSize, IntpageSize, searchType);
        }


        Map<String, Object> map = new HashMap<>();
        int StatisticPrice = 0;
        if (orderList != null && orderList.size() != 0) {
            //如果分页查询的结果不为空，则返回查询总条数，以及当前分页的数据,还有总价格
            //调用查询条数的方法
            if (tableName.equals("orders")) {//如果是查询order表
                Inttotal = ordersDao.getStatisticsTotal(searchType);
                StatisticPrice = ordersDao.getStatisticsPrice(searchType);
            } else if (tableName.equals("rechargeorder")) {//如果是查询rechargeorder表
                Inttotal = reChargeOder.getStatisticsTotal(searchType);
                StatisticPrice = reChargeOder.getStatisticsPrice(searchType);
            }
            map.put("StatisticOrderList", orderList);
            map.put("StatisticsTotal", Inttotal);
            map.put("StatisticsPrice", StatisticPrice);
            return map;
        }
        //如果查询记录为空，则直接返回null
        return null;
    }

    /**
     * 导出excel文件
     * 第一次尝试.
     *
     * @param getType
     * @return
     */
    @Override
    public Boolean getAllExportList(String getType, String tableName, ServletOutputStream outputStream) {
        //首先需要获取待导出excel文件的所有数据
        List<Order> getAllExcellist = new ArrayList<Order>();
        if (tableName.equals("orders")) {
            getAllExcellist = ordersDao.getAllExportList(getType);
        } else if (tableName.equals("rechargeorder")) {
            getAllExcellist = reChargeOder.getAllExportList(getType);
        }
        //需要对数据进行处理...,绑定成List<List<String>>data 类型的数据
        //先绑定头部
        List<String> header = new ArrayList<>();
        header.add("订单Id");
        header.add("经办人姓名");
        header.add("客户名称");
        header.add("客户宿舍号");
        header.add("订单类型");
        if (tableName.equals("rechargeorder")) {
            header.add("订单续费时间");
        }
        header.add("订单生效时间");
        header.add("订单过期时间");
        header.add("订单价格(元)");
        List<List<String>> body = new ArrayList<>();
        for (int index = 0; index < getAllExcellist.size(); index++) {
            //转化格式
            List<String> data = new ArrayList<>();
            data.add(String.valueOf(getAllExcellist.get(index).getOrderId()));
            data.add(getAllExcellist.get(index).getManager().getManagerName());
            data.add(getAllExcellist.get(index).getCustomer().getCustomerName());
            data.add(String.valueOf(getAllExcellist.get(index).getCustomer().getDormitoryNum()));
            data.add(getAllExcellist.get(index).getOrderType());
            if (tableName.equals("rechargeorder")) {
                data.add(getAllExcellist.get(index).getBeginTime());
            }
            data.add(getAllExcellist.get(index).getCreateTime());
            data.add(getAllExcellist.get(index).getEndTime());
            data.add(String.valueOf(getAllExcellist.get(index).getOrderPrice()));
            body.add(data);
        }
        //找对应的价格以及数量
        int total = 0;
        int price = 0;
        if (tableName.equals("orders")) {
            total = ordersDao.getStatisticsTotal(getType);
            price = ordersDao.getStatisticsPrice(getType);
        } else if (tableName.equals("rechargeorder")) {
            total = reChargeOder.getStatisticsTotal(getType);
            price = reChargeOder.getStatisticsPrice(getType);
        }
        //进行导出..
        Boolean b = false;
        try {
            b = ExportExcel.generateExcel("sheet1", header, body, total, price, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!b) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 续费订单...
     *
     * @param order
     * @return
     */
    @Transactional
    @Override
    public boolean AddChargeOrder(Order order) {
        //需要先去更新一下原始订单的endTime以及orderType
        int count1 = ordersDao.UpdateChargeOrder(order);
        //然后去添加续费表信息，orderId主外键联系
        int count2 = reChargeOder.AddRechargeOrder(order);

        if (count1 > 0 && count2 > 0) {
            //这样才证明续费成功...
            return true;
        } else {
            return false;
        }
    }
}
