package network.hgb.dao;

import network.hgb.pojo.Customer;
import network.hgb.pojo.Manager;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/31 13:35
 */
public interface CustomerDao {
    public List<Customer> getCustomerList(@Param("current") int current,
                                         @Param("pageSize") int pageSize,
                                         @Param("customer") Customer customer);

    public int getTotal(Customer customer);

    public int updateCustomer(Customer customer);


    public int deleteCustomer(Customer customer);

    public List<Integer> getCustomerIdList();

    public int AddCustomer(Customer customer);

    public Customer checkPhoneNum(String phoneNum);
}
