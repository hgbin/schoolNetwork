package network.hgb.dao;

import network.hgb.pojo.Manager;
import network.hgb.pojo.Router;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/29 13:56
 */
public interface ManagerDao {
    public Manager login(Manager manager);

    public List<Router> getRouterList(int id);

    public List<Manager> getManagerList(@Param("current") int current,
                                        @Param("pageSize") int pageSize,
                                        @Param("manager") Manager manager);

    public int getTotal(Manager manager);

    public int updateManager(Manager manager);

    public int deleteManager(Manager manager);

    public List<Integer> getManagerIdList();

    public List<String> getManagerNameList();

    public int AddManager(Manager manager);
}
