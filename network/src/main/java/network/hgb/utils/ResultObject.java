package network.hgb.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/29 12:51
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResultObject<T> {
    private T data;
    private String code;
    private String message;
}
