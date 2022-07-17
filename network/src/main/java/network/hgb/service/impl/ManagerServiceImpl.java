package network.hgb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import lombok.Data;
import lombok.ToString;
import network.hgb.dao.ManagerDao;
import network.hgb.pojo.Manager;
import network.hgb.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/30 14:29
 */

@Service
@Data
@ToString
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    ManagerDao managerDao;

    Manager manager;

    @Override
    public Map<String,Object> getManagerList(JSONObject jsonObject,JSONObject managerSearch) {
        //首先先获取分页的基本信息，每页页数，当前页数，而且需要在查询到总条数的时候，更新到前台...
        String current = jsonObject.getString("current");
        String pageSize = jsonObject.getString("pageSize");
        String total = jsonObject.getString("total");
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
        //接下来找到参数manager
        String text = managerSearch.getString("managerName");
        String managerStatus = managerSearch.getString("managerStatus");
        //检测id or name
        manager = new Manager();
        checkIsID(text,managerStatus);

        //如果都校验成功后，则去后台查询分页数据.
        List<Manager> managerList =  managerDao.getManagerList((Intcurrent-1) * IntpageSize,IntpageSize, manager);
        Map<String,Object> map = new HashMap<>();
        if(managerList != null && managerList.size() != 0){
            //如果分页查询的结果不为空，则返回查询总条数，以及当前分页的数据
            //调用查询条数的方法
            Inttotal = managerDao.getTotal(getManager());
            map.put("ManagerList",managerList);
            map.put("total",Inttotal);
            return map;
        }
        //如果查询记录为空，则直接返回null
        return null;
    }

    @Override
    public boolean updateManager(Manager manager) {
        int count = managerDao.updateManager(manager);
        if(count > 0){
            return true;
        }else{
            return false;
        }
    }

    //检查查询的数据
    public void checkIsID(String text,String Status){
        if(text.trim().equals("")){
            //如果去首尾空格还是空字符串，则直接设置状态
            manager.setManagerStatus(Status);
            return;
        }
        //对text进行判断，看看属于id还是名称，
        String pattern = "^[0-9]*$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        if(m.matches()){
            //为true代表未数字，则添加到id中
            manager.setManagerId(Integer.parseInt(text));
        }else{
            //否则添加为name
            manager.setManagerName(text);
        }
        manager.setManagerStatus(Status);
    }

    /**
     * 删除管理员操作
     * @param manager
     * @return
     */
    @Override
    public boolean deteleMangaer(Manager manager) {
        int count = managerDao.deleteManager(manager);
        if(count > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<Integer> getManagerIdList() {
        return managerDao.getManagerIdList();
    }

    @Override
    public List<String> getManagerNameList() {
        return managerDao.getManagerNameList();
    }

    @Transactional
    @Override
    public int AddManager(Manager manager) {
        return managerDao.AddManager(manager);
    }


    @Override
    public boolean sendMessage() {
        DefaultProfile profile = DefaultProfile.getProfile("cn-beijing", "", "");
        /** use STS Token
         DefaultProfile profile = DefaultProfile.getProfile(
         "<your-region-id>",           // The region ID
         "<your-access-key-id>",       // The AccessKey ID of the RAM account
         "<your-access-key-secret>",   // The AccessKey Secret of the RAM account
         "<your-sts-token>");          // STS Token
         **/
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers("13060564888");//接收短信的手机号码
        request.setSignName("阿里云短信测试");//短信签名名称
        request.setTemplateCode("SMS_1549509xx");//短信模板CODE
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
        String code = "19230";
        request.setTemplateParam("{\"code\":\"" + code + "\"}");//短信模板变量对应的实际值
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            System.out.println(JSON.parseObject(new Gson().toJson(response)).getString("code"));
            if(JSON.parseObject(new Gson().toJson(response)).getString("code").equals("OK")){
                return true;
            }else{
                return false;
            }
        } catch (ServerException e) {
            e.printStackTrace();
            return false;
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
            return false;
        }
    }
}
