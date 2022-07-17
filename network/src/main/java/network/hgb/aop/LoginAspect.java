package network.hgb.aop;

import network.hgb.pojo.Manager;
import network.hgb.service.LoginService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/29 15:51
 */
@Component
@Aspect
public class LoginAspect {
    @Autowired
    LoginService loginService;

    @Pointcut("execution(* network.hgb.service.LoginService.login(..))")
    public void Loginpc(){}

    @Before("Loginpc()")
    public void checkHasRight(JoinPoint joinPoint){
        //每次登录前需要做的事情----先判断该用户是否存在，是否为黑户，如果有一个成立则跳转到错误控制器上
        Object[] args= joinPoint.getArgs();
        System.out.println("登录前检测身份");
        Manager manager = null;
        for (Object o : args ){
//            这个获取的参数就是前端录入的用户登录信息
            loginService.checkManager(((Manager) o));
        }
    }
}
