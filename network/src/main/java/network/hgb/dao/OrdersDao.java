package network.hgb.dao;

import network.hgb.pojo.Customer;
import network.hgb.pojo.Manager;
import network.hgb.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/31 23:47
 */
public interface OrdersDao {
    public List<Order> getOrdersList(@Param("current") int current,
                                        @Param("pageSize") int pageSize,
                                        @Param("order")Order order,
                                        @Param("customer") Customer customer,
                                        @Param("manager")Manager manager);

    public int getTotal(@Param("order") Order order,
                        @Param("customer") Customer customer,
                        @Param("manager") Manager manager);

    public int updateOrder(Order order);


    public int deleteOrder(Order order);


    public int AddOrder(Order order);

    public Order IshasOrder(int customerId);

    public List<Order> getStatisticsOrderList(@Param("current") int current,
                                              @Param("pageSize") int pageSize,
                                              @Param("type")String type);

    public int getStatisticsTotal(@Param("type")String type);

    public int getStatisticsPrice(@Param("type")String type);

    public List<Order> getAllExportList(@Param("type")String type);

    public List<Order> checkOrderIsFinshed();

    public int updateIsFinshed(List<Order> list);

    public List<Order> getCloseToFinshed();

    /**
     * 续费的时候需要更新订单中的endTime...
     * @param order
     * @return
     */
    public int UpdateChargeOrder(Order order);
}
