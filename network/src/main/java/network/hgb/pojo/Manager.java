package network.hgb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/27 15:13
 */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Manager {
    int managerId;
    String managerName;
    String pwd;
    int managerIdentity;
    String managerStatus;
    List<Router> routerList;
}
