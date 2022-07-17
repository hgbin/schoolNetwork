package network.hgb.service;

import com.alibaba.fastjson.JSONObject;
import network.hgb.exception.PhoneExistException;
import network.hgb.pojo.Customer;
import network.hgb.pojo.Manager;

import java.util.List;
import java.util.Map;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/31 13:32
 */
public interface CustomerService {
    public Map<String,Object> getCustomerList(JSONObject pagination, JSONObject customer);

    public boolean updateCustomer(Customer customer);

    public boolean deteleCustomer(Customer customer);

    public List<Integer> getCustomerIdList();

    public int AddCustomer(Customer customer) throws PhoneExistException;
}
