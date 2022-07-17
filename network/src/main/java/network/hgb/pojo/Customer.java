package network.hgb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/27 15:11
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    int customerId;
    String customerName;
    String phoneNum;
    int dormitoryNum;
    String customerStatus;
    String customerType;
}
