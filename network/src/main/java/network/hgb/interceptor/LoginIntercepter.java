package network.hgb.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/6/6 14:22
 */
public class LoginIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return HandlerInterceptor.super.preHandle(request, response, handler);
        HttpSession session = request.getSession();
        if(session.getAttribute("name") != null){
            //证明登录了，可以放行
            return true;
        }else{
            //转发到控制器...?
            System.err.println("还没登录..");
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request,response);
            return false;
        }
    }
}
