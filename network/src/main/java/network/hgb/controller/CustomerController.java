package network.hgb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import network.hgb.exception.PhoneExistException;
import network.hgb.pojo.Customer;
import network.hgb.pojo.Manager;
import network.hgb.service.CustomerService;
import network.hgb.service.ManagerService;
import network.hgb.utils.Code;
import network.hgb.utils.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/31 13:15
 */
@Controller
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping("/getCustomerList")
    @ResponseBody
    public ResultObject<Map<String, Object>> getCustomerList(@RequestParam Map<String, Object> params) {
        System.out.println(params.get("customerSearch"));
        if (params.get("pagination") == null || params.get("customerSearch") == null) {
            return new ResultObject<>(null, Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
        Map<String, Object> map = customerService.getCustomerList(JSONObject.parseObject(params.get("pagination").toString()),
                JSONObject.parseObject(params.get("customerSearch").toString()));
        if (map == null) {
            return new ResultObject<>(null, Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
        return new ResultObject<>(map, Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
    }

    @PostMapping("/updateCustomer")
    @ResponseBody
    public ResultObject<Boolean> updateCustomer(@RequestBody Customer customer) {
        Boolean b = customerService.updateCustomer(customer);
        if (b) {
            return new ResultObject<>(true, Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
        } else {
            return new ResultObject<>(false, Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
    }

    @PostMapping("/deleteCustomer")
    @ResponseBody
    public ResultObject<Boolean> deleteCustomer(@RequestBody Customer customer) {
        Boolean b = customerService.deteleCustomer(customer);
        if (b) {
            return new ResultObject<>(true, Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
        } else {
            return new ResultObject<>(false, Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
    }

    @PostMapping("/AddCustomer")
    @ResponseBody
    public ResultObject<Customer> AddCustomer(@RequestBody Customer customer) throws PhoneExistException {
        System.out.println(customer);
        int count = 0;
        try{
           count =  customerService.AddCustomer(customer);
        }catch (PhoneExistException e){
            System.err.println(e.getMessage() + e.getCustomerId());
        }finally {
            if(count > 0){
                //如果大于0，则添加成功，返回添加成功的对象回去吧?
                return new ResultObject<>(customer,Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
            }else {
                return new ResultObject<>(customer,Code.FailCode.getCode(),"手机号码已经存在,添加客户失败");
            }
        }
    }
}
