package com.bbo.gmall.manage.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SysController {

    @RequestMapping("login")
    public JSONObject login(@RequestBody JSONObject jsonParam){
        JSONObject jsonObject = new JSONObject();
        if(jsonParam.getString("userName").equals("super_admin")){
            jsonObject.put("token","super_admin");
        }else{
            jsonObject.put("token","admin");
        }
        return jsonObject;
    }

    @RequestMapping("get_info")
    public JSONObject getUserInfo(String token){
        JSONObject jsonObject = new JSONObject();
        List<String> list = new ArrayList<>();
        list.add("admin");
        if(token.equals("super_admin")){
            jsonObject.put("name","super_admin");
            jsonObject.put("user_id","1");
            list.add("super_admin");
            jsonObject.put("access",list);
            jsonObject.put("token","super_admin");
            jsonObject.put("avatar","https://file.iviewui.com/dist/a0e88e83800f138b94d2414621bd9704.png");
        }else{
            jsonObject.put("name","admin");
            jsonObject.put("user_id","2");
            list.add("super_admin");
            jsonObject.put("access",list);
            jsonObject.put("token","admin");
            jsonObject.put("avatar","https://file.iviewui.com/dist/a0e88e83800f138b94d2414621bd9704.png");
        }
        return jsonObject;
    }

    @RequestMapping("logout")
    public String logout(){
        return null;
    }

    @RequestMapping("message/count")
    public String count(){
        return "3";
    }
    @RequestMapping("message/init")
    public String init(){
        return null;
    }
    @RequestMapping("message/content")
    public String content(){
        return null;
    }
    @RequestMapping("message/has_read")
    public String has_read(){
        return null;
    }

}
