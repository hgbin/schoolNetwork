package network.hgb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/27 15:19
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Router {
    int rid;
    int pid;
    String path;
    String name;
    String link;
    String title;
}
