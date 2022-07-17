package network.hgb.controller;

import com.alibaba.fastjson.JSONObject;
import network.hgb.pojo.Manager;
import network.hgb.service.ManagerService;
import network.hgb.utils.Code;
import network.hgb.utils.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/30 13:27
 */
@Controller
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @PostMapping("/getManagerList")
    @ResponseBody
    public ResultObject<Map<String,Object>> getManagerList(@RequestParam Map<String, Object> params) {
        System.out.println(params.get("managerSearch"));
        if(params.get("pagination") == null || params.get("managerSearch") == null){
            return new ResultObject<>(null,Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
        Map<String,Object> map = managerService.getManagerList(JSONObject.parseObject(params.get("pagination").toString()),
                JSONObject.parseObject(params.get("managerSearch").toString()));
        if(map == null){
            return new ResultObject<>(null,Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
        return new ResultObject<>(map, Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
    }

    @PostMapping("/updateManager")
    @ResponseBody
    public ResultObject<Boolean> updateManager(@RequestBody Manager manager){
        Boolean b = managerService.updateManager(manager);
        if(b){
            return new ResultObject<>(true,Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
        }else {
            return new ResultObject<>(false,Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
    }

    @PostMapping("/deleteManager")
    @ResponseBody
    public ResultObject<Boolean> deleteManager(@RequestBody Manager manager){
        Boolean b = managerService.deteleMangaer(manager);
        if(b){
            return new ResultObject<>(true,Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
        }else {
            return new ResultObject<>(false,Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
    }


    @PostMapping("/AddManager")
    @ResponseBody
    public ResultObject<Manager> AddManager(@RequestBody Manager manager){
        System.out.println(manager);
        int count = managerService.AddManager(manager);
        if(count > 0){
            //如果大于0，则添加成功，返回添加成功的对象回去吧?
            return new ResultObject<>(manager,Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
        }else {
            return new ResultObject<>(manager,Code.FailCode.getCode(), Code.FailCode.getDesc());
        }
    }

    @PostMapping("/sendMessage")
    @ResponseBody
    public ResultObject<Boolean> sendMessage(){
       Boolean b = managerService.sendMessage();
       if(b){
           return new ResultObject<>(true,Code.SuccessCode.getCode(), Code.SuccessCode.getDesc());
       }else {
           return new ResultObject<>(false,Code.FailCode.getCode(), Code.FailCode.getDesc());
       }
    }
}
