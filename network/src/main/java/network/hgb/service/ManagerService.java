package network.hgb.service;

import com.alibaba.fastjson.JSONObject;
import network.hgb.pojo.Manager;

import java.util.List;
import java.util.Map;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/30 14:26
 */
public interface ManagerService {
    public Map<String,Object> getManagerList(JSONObject pagination,JSONObject manager);

    public boolean updateManager(Manager manager);

    public boolean deteleMangaer(Manager manager);

    public List<Integer> getManagerIdList();

    public List<String> getManagerNameList();

    public int AddManager(Manager manager);

    public boolean sendMessage();
}
