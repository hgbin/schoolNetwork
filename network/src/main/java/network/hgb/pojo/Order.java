package network.hgb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/27 15:16
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    int orderId;
    int managerId;
    int customerId;
    int timeDifference;
    String orderType;
    String createTime; //订单生效时间
    String endTime;//订单过期时间
    String beginTime;//订单下单时间...
    double orderPrice;

    Customer customer;
    Manager manager;

    List<Order> chargeOrderList;


}
