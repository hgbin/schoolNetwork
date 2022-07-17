package network.hgb.service.impl;

import network.hgb.dao.ManagerDao;
import network.hgb.dao.OrdersDao;
import network.hgb.pojo.Manager;
import network.hgb.pojo.Order;
import network.hgb.pojo.Router;
import network.hgb.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/29 13:54
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    ManagerDao managerDao;

    @Autowired
    OrdersDao ordersDao;

    private int Mid;

    public int getMid() {
        return Mid;
    }

    public void setMid(int mid) {
        Mid = mid;
    }

    @Override
    public List<Router> login(Manager manager){
        //Mid是在aop里面获取到的id
        List<Router> routerList = null;
        if(getMid()>0) { //如果大于0，证明aop调用check函数时候，找到了该用户且没被禁用
            routerList = managerDao.getRouterList(Mid);
        }
        return routerList;
    }

    //aop调用
    @Override
    public Manager checkManager(Manager manager) {
        Manager getmanager = managerDao.login(manager);
        setMid(0);//保证每次都能正确获取信息，不然会出现错误信息还能获取
        if(getmanager!= null){
            //如果不为空，则更新mid,不为0
            setMid(getmanager.getManagerId());
        }
        return getmanager;
    }

    /**
     * 用户登录之后访问执行....
     * @return
     */
    @Override
    public List<Order> getCloseToFinshed() {
        return ordersDao.getCloseToFinshed();
    }
}
