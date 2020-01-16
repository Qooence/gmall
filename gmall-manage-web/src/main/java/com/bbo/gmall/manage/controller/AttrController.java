package com.bbo.gmall.manage.controller;

import com.bbo.gmall.bean.PmsBaseAttrInfo;
import com.bbo.gmall.response.Response;
import com.bbo.gmall.service.AttrService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AttrController  {

    @Reference
    AttrService attrService;

    @RequestMapping("saveAttrInfo")
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){

        return "success";
    }

    @RequestMapping("attrInfoList")
    public Response attrInfoList(@RequestParam(defaultValue = "1")Integer pageNum,
                                  @RequestParam(defaultValue = "5")Integer pageSize,String catalog3Id){
        PageInfo<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.attrInfoList(pageNum,pageSize,catalog3Id);
        return Response.success(pmsBaseAttrInfos);
    }
}
