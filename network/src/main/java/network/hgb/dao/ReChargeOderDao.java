package network.hgb.dao;

import network.hgb.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/6/5 21:49
 */
public interface ReChargeOderDao {
    public List <Order> getChargeOrder(int orderId);

    public int AddRechargeOrder(Order order);

    public List<Order> getStatisticsOrderList(@Param("current") int current,
                                              @Param("pageSize") int pageSize,
                                              @Param("type")String type);
    public int getStatisticsTotal(@Param("type")String type);

    public int getStatisticsPrice(@Param("type")String type);

    public List<Order> getAllExportList(@Param("type")String type);
}
