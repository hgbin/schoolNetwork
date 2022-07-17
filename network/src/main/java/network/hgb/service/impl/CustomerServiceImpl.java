package network.hgb.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import network.hgb.dao.CustomerDao;
import network.hgb.exception.PhoneExistException;
import network.hgb.pojo.Customer;
import network.hgb.pojo.Manager;
import network.hgb.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/31 13:32
 */
@Service
@Data
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerDao customerDao;

    Customer customer;


    @Override
    public Map<String, Object> getCustomerList(JSONObject pagination, JSONObject customerSearch) {
        //首先先获取分页的基本信息，每页页数，当前页数，而且需要在查询到总条数的时候，更新到前台...
        String current = pagination.getString("current");
        String pageSize = pagination.getString("pageSize");
        String total = pagination.getString("total");
        if(current == null || pageSize == null){
            return null;//如果传递的参数有误,则返回空值
        }
        //如果不为空，先判断一下
        int Intcurrent = Integer.parseInt(current);
        int IntpageSize = Integer.parseInt(pageSize);
        int Inttotal = Integer.parseInt(total);
        if(Integer.parseInt(current) <= 0){
            //恢复当前页数到第一页
            Intcurrent = 1;
        }
        if(Integer.parseInt(pageSize) <= 0){
            //恢复默认一页的页数为10条记录
            IntpageSize = 10;
        }
        //接下来找到参数customer
        String text = customerSearch.getString("customerName");
        String customerStatus = customerSearch.getString("customerStatus");
        String customerType = customerSearch.getString("customerType");
        //检测id or name
        customer = new Customer();
        checkIsID(text,customerStatus,customerType);

        //如果都校验成功后，则去后台查询分页数据.
        List<Customer> customerList =  customerDao.getCustomerList((Intcurrent-1) * IntpageSize,IntpageSize, customer);
        Map<String,Object> map = new HashMap<>();
        if(customerList != null && customerList.size() != 0){
            //如果分页查询的结果不为空，则返回查询总条数，以及当前分页的数据
            //调用查询条数的方法
            Inttotal = customerDao.getTotal(getCustomer());
            map.put("CustomerList",customerList);
            map.put("total",Inttotal);
            return map;
        }
        //如果查询记录为空，则直接返回null
        return null;
    }

    /**
     * 更新客户操作
     * @param customer
     * @return
     */
    @Override
    public boolean updateCustomer (Customer customer) {
        int count = customerDao.updateCustomer(customer);
        if(count > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean deteleCustomer(Customer customer) {
        int count = customerDao.deleteCustomer(customer);
        if(count > 0){
            return true;
        }else{
            return false;
        }
    }


    //检查查询的数据
    public void checkIsID(String text,String Status,String Type){
        if(text.trim().equals("")){
            //如果去首尾空格还是空字符串，则直接设置状态和类型
            customer.setCustomerStatus(Status);
            customer.setCustomerType(Type);
            return;
        }
        //对text进行判断，看看属于id还是名称，还是属于手机号码
        //先判断是否为手机...

        String Phonepattern = "^(13[0-9]|14[5|7]|15[0-9]|18[0-9]|19[4|5])\\d{8}$";
        Pattern r = Pattern.compile(Phonepattern);
        Matcher m = r.matcher(text);

        if(m.matches()){
            //如果为true则代表是手机号码...
            customer.setPhoneNum(text);
        }else{
            //如果已经验证不是手机类型了，那么只有可能为ID或者name
            String Idpattern = "^[0-9]*$";
            Pattern r1 = Pattern.compile(Idpattern);
            Matcher m1 = r1.matcher(text);
            if(m1.matches()){
                //为true代表为数字，则添加到id中
                customer.setCustomerId(Integer.parseInt(text));
            }else{
                //否则添加为name
                customer.setCustomerName(text);
            }
        }
        customer.setCustomerStatus(Status);
        customer.setCustomerType(Type);
    }

    @Override
    public List<Integer> getCustomerIdList() {
        return customerDao.getCustomerIdList();
    }

    @Transactional
    @Override
    public int AddCustomer(Customer customer) throws PhoneExistException{
        //先要判断传递进来的phone是否已经存在，因为phone为唯一索引，
        Customer hasPhoneCustomer = customerDao.checkPhoneNum(customer.getPhoneNum());
        if(hasPhoneCustomer != null){
            //证明手机号码已经存在..
            throw new PhoneExistException("该手机号已经存在,号主的客户Id为",hasPhoneCustomer.getCustomerId());
        }else{
            return customerDao.AddCustomer(customer);
        }
    }
}
