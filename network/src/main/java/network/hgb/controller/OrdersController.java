package network.hgb.controller;

import com.alibaba.fastjson.JSONObject;
import network.hgb.exception.OrderExistException;
import network.hgb.pojo.Customer;
import network.hgb.pojo.Manager;
import network.hgb.pojo.Order;
import network.hgb.service.CustomerService;
import network.hgb.service.ManagerService;
import network.hgb.service.OrdersService;
import network.hgb.utils.Code;
import network.hgb.utils.ResultObject;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/31 23:47
 */
@Controller
public class OrdersController {

    @Autowired
    OrdersService ordersService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ManagerService managerService;

    @PostMapping("/getOrderList")
    @ResponseBody
    public ResultObject<Map<String, Object>> getOrderList(@RequestParam Map<String, Object> params) {
        System.out.println(params.get("orderSearch"));
        if (params.get("pagination") == null || params.get("orderSearch") == null) {
            return new ResultObject<>(null, Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
        Map<String, Object> map = ordersService.getOrderList(JSONObject.parseObject(params.get("pagination").toString()),
                JSONObject.parseObject(params.get("orderSearch").toString()));
        if (map == null) {
            return new ResultObject<>(null, Code.FailCode.getCode(), Code.FailCode.getDesc());
        } else {
            //同时要返回对应的管理员id的列表，客户id的列表，提供给前端用户选择
            return new ResultObject<>(map, Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
        }
    }

    @PostMapping("/updateOrder")
    @ResponseBody
    public ResultObject<Boolean> updateOrder(@RequestBody Order order) {
        Boolean b = ordersService.updateOrder(order);
        if (b) {
            return new ResultObject<>(true, Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
        } else {
            return new ResultObject<>(false, Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
    }

    @PostMapping("/deleteOrder")
    @ResponseBody
    public ResultObject<Boolean> deleteOrder(@RequestBody Order order) {
        Boolean b = ordersService.deleteOrder(order);
        if (b) {
            return new ResultObject<>(true, Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
        } else {
            return new ResultObject<>(false, Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
    }

    @PostMapping("/getInitList")
    @ResponseBody
    public ResultObject<Map<String, Object>> getInitList() {
        Map<String, Object> initList = new HashMap<>();
        List<Integer> initCustomerIdList = customerService.getCustomerIdList();
        List<Integer> initManagerIdList = managerService.getManagerIdList();

        if (initCustomerIdList == null || initCustomerIdList.size() == 0) {
            initList.put("initCustomerIdList", null);
        } else {
            initList.put("initCustomerIdList", initCustomerIdList);
        }

        if (initManagerIdList == null || initManagerIdList.size() == 0) {
            initList.put("initManagerIdList", null);
        } else {
            initList.put("initManagerIdList", initManagerIdList);
        }
        return new ResultObject<>(initList, Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
    }

    @PostMapping("/getManagerNameList")
    @ResponseBody
    public ResultObject<Map<String, Object>> getManagerNameList() {
        Map<String, Object> nameList = new HashMap<>();
        List<String> managerNameList = managerService.getManagerNameList();
        if (managerNameList == null || managerNameList.size() == 0) {
            nameList.put("managerList", null);
        } else {
            nameList.put("managerList", managerNameList);
        }
        return new ResultObject<>(nameList, Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
    }


    @PostMapping("/AddOrder")
    @ResponseBody
    public ResultObject<Order> AddOrder(@RequestBody Order order) {
        System.out.println(order);
        int count = 0;
        try {
            count = ordersService.AddOrder(order);
        } catch (OrderExistException e) {
            System.err.println(e.getMessage() + e.getOrderId());
        } finally {
            if (count > 0) {
                //如果大于0，则添加成功，返回添加成功的对象回去吧?
                return new ResultObject<>(order, Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
            } else {
                return new ResultObject<>(order, Code.FailCode.getCode(), "本客户已经存在订单，不可以继续下单");
            }
        }
    }

    @PostMapping("/getStatisticsOrderList")
    @ResponseBody
    public ResultObject<Map<String, Object>> getStatisticsOrderList(@RequestParam Map<String, Object> params) {
        if (params.get("pagination") == null || params.get("orderStatistics") == null) {
            return new ResultObject<>(null, Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
        Map<String, Object> map = ordersService.getStatisticsOrderList(JSONObject.parseObject(params.get("pagination").toString()),
                JSONObject.parseObject(params.get("orderStatistics").toString()), params.get("tableName").toString());
        if (map == null) {
            return new ResultObject<>(null, Code.FailCode.getCode(), Code.FailCode.getDesc());
        } else {
            //同时要返回对应的统计数据
            return new ResultObject<>(map, Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
        }
    }

    @GetMapping("/DownLoadExcel")
    public void getAllExportList(String type, String tableName, HttpServletResponse response) {
        if (type == null || tableName == null) {
            return;
        }
        response.setContentType("application/binary;charset=UTF-8");
        try {
            ServletOutputStream out = response.getOutputStream();
            try {
                //设置文件头：最后一个参数是设置下载文件名
                if (tableName.equals("orders")) {
                    response.setHeader("Content-Disposition", "attachment;fileName=" +
                            URLEncoder.encode("新增订单统计表.xls", "UTF-8"));//生成表名
                } else if (tableName.equals("rechargeorder")) {
                    response.setHeader("Content-Disposition", "attachment;fileName=" +
                            URLEncoder.encode("续费订单统计表.xls", "UTF-8"));//生成表名
                }
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            Boolean b = ordersService.getAllExportList(type, tableName, out);
            if (!b) {
                return;
            } else {
                System.out.println("导出成功");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @PostMapping("/AddChargeOrder")
    @ResponseBody
    public ResultObject<Boolean> AddChargeOrder(@RequestBody Order order) {
        System.out.println(order);
        //需要调用添加续费表信息的函数
        Boolean b = ordersService.AddChargeOrder(order);
        if (b) {
            return new ResultObject<>(true, Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
        } else {
            return new ResultObject<>(false, Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
    }
}
