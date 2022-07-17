package network.hgb.service;

import com.alibaba.fastjson.JSONObject;
import network.hgb.exception.OrderExistException;
import network.hgb.pojo.Customer;
import network.hgb.pojo.Order;

import javax.servlet.ServletOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/31 23:49
 */
public interface OrdersService {
    public Map<String,Object> getOrderList(JSONObject pagination, JSONObject order);

    public boolean updateOrder(Order order);

    public boolean deleteOrder(Order order);

    public int AddOrder(Order order) throws OrderExistException;

    public Map<String,Object> getStatisticsOrderList(JSONObject pagination, JSONObject type,String tableName);

    public Boolean getAllExportList(String getType, String tableName,ServletOutputStream outputStream);

    public boolean AddChargeOrder(Order order);
}
