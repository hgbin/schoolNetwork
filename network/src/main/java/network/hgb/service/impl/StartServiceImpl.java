package network.hgb.service.impl;

import network.hgb.dao.OrdersDao;
import network.hgb.pojo.Order;
import network.hgb.service.StartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/6/4 20:20
 */
@Service
public class StartServiceImpl implements StartService {

    @Autowired
    OrdersDao ordersDao;

    @PostConstruct //spring启动的时候自动执行
    @Override
    public void initService() {
        System.out.println("自动执行了...");
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
              //需要判断数据库中到期的用户，并且自动更新成禁用...
              checkOrderIsFinshed();
            }
        };

        //开启线程，在每天自动的24点执行
        Date date = new Date();
//        date.setHours(0);
//        date.setMinutes(0);
//        date.setSeconds(0);
        long oneDayDelay = 24 * 60 * 60 * 1000;
        timer.scheduleAtFixedRate(timerTask,date,oneDayDelay);
    }

    /**
     * 封装一个查询到期的用户函数...
     * @return
     */
    @Override
    public void checkOrderIsFinshed() {
        //调用dao层，...
        List<Order> getFinshedList = ordersDao.checkOrderIsFinshed();
        if(getFinshedList != null && getFinshedList.size() != 0){
            //如果不为空,则证明有新到期的用户，那么需要调用dao层去更新用户为禁用状态
            int count = ordersDao.updateIsFinshed(getFinshedList);
            if(count > 0 ){
                //更新成功
                System.out.println("更新成功");
            }else {
                //更新失败
                System.err.println("更新失败");
            }
        }
    }
}
