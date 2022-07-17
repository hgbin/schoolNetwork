package network.hgb.service;

import network.hgb.pojo.Manager;
import network.hgb.pojo.Order;
import network.hgb.pojo.Router;

import java.util.List;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/29 13:51
 */
public interface LoginService {
    public List<Router> login(Manager manager);
    public Manager checkManager(Manager manager);

    public List<Order> getCloseToFinshed();
}
