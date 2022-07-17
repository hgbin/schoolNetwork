package network.hgb.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/6/3 13:53
 */
@ControllerAdvice
public class ExpectionHanler {
    @ExceptionHandler(PhoneExistException.class)
    public void doException(PhoneExistException ex){
        System.err.println(ex.getMessage() + ex.getCustomerId());
    }

    @ExceptionHandler(OrderExistException.class)
    public void doException1(OrderExistException ex){
        System.err.println(ex.getMessage() + ex.getOrderId());
    }

    @ExceptionHandler(IOException.class)
    public void doIoException(IOException e){
        e.printStackTrace();
    }
}
