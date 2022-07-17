package network.hgb.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/6/3 17:11
 */
@Data
@AllArgsConstructor
public class OrderExistException extends Exception{
    //异常信息
    String message;
    int orderId;
}
