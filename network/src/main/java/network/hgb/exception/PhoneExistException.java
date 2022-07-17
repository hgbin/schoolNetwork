package network.hgb.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/6/3 13:20
 */
@Data
@AllArgsConstructor
public class PhoneExistException extends Exception{
    //异常信息
    String message;
    int customerId;
}
