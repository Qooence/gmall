package com.bbo.gmall.manage.user.controller;

import com.bbo.gmall.manage.bean.ums.UmsMemberReceiveAddress;
import com.bbo.gmall.manage.bean.ums.UmsMember;
import com.bbo.gmall.manage.service.ums.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("getReceiveAddressByMemberId")
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId){

        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = userService.getReceiveAddressByMemberId(memberId);

        return umsMemberReceiveAddresses;
    }


    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){

        List<UmsMember> umsMembers = userService.getAllUser();

        return umsMembers;
    }

    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return "hello user";
    }



}
