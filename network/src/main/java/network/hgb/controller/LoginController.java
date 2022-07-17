package network.hgb.controller;

import network.hgb.pojo.Manager;
import network.hgb.pojo.Order;
import network.hgb.pojo.Router;
import network.hgb.service.LoginService;
import network.hgb.utils.Code;
import network.hgb.utils.ResultObject;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/29 12:50
 */
@Controller
public class LoginController {

    @Autowired
    LoginService loginService;



    @PostMapping("/login")
    @ResponseBody
    public ResultObject<List<Router>> login(@RequestBody Manager manager, HttpSession session){
        System.out.println(manager);
        List<Router> routerList =loginService.login(manager);
        if(routerList == null){
            return new ResultObject<>(null,Code.FailCode.getCode(),"登录失败");
        }
        session.setAttribute("name",manager.getManagerName());
        return new ResultObject<>(routerList,Code.SuccessCode.getCode(),Code.SuccessCode.getDesc());
    }

    @PostMapping("/LoginOut")
    @ResponseBody
    public ResultObject<Boolean> LoginOut(HttpSession session){
        if(session.getAttribute("name")!= null){
            session.invalidate();
            return new ResultObject<>(true,Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
        }else{
            return new ResultObject<>(false,Code.FailCode.getCode(),Code.FailCode.getDesc());
        }
    }

    @PostMapping("/getIsCloseFinshed")
    @ResponseBody
    public ResultObject<List<Order>> getIsCloseFinshed(){
        List<Order> orderList = loginService.getCloseToFinshed();
        if(orderList == null){
            return new ResultObject<>(null,Code.FailCode.getCode(),"获取失败");
        }
        return new ResultObject<>(orderList,Code.SuccessCode.getCode(),Code.SuccessCode.getDesc());
    }

}
