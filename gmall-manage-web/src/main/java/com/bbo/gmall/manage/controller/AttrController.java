package com.bbo.gmall.manage.controller;

import com.bbo.gmall.bean.pms.PmsBaseAttrInfo;
import com.bbo.gmall.response.Response;
import com.bbo.gmall.response.ResponseCode;
import com.bbo.gmall.service.pms.AttrService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

@RestController
public class AttrController  {

    @Reference
    AttrService attrService;

    @RequestMapping("saveAttrInfo")
    public Response saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        if(null == pmsBaseAttrInfo){
            Response.error(ResponseCode.DATA_IS_EXIST,"数据异常");
        }
        attrService.saveAttrInfo(pmsBaseAttrInfo);
        return Response.success("保存成功");
    }

    @RequestMapping("attrInfoList")
    public Response attrInfoList(@RequestParam(defaultValue = "1")Integer pageNum,
                                  @RequestParam(defaultValue = "5")Integer pageSize,String catalog3Id){
        PageInfo<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.attrInfoList(pageNum,pageSize,catalog3Id);
        return Response.success(pmsBaseAttrInfos);
    }

    @RequestMapping("/detail/{id}")
    public Response detail(@PathVariable String id){
        PmsBaseAttrInfo attrInfo = attrService.detail(id);
        return Response.success("查询成功",attrInfo);
    }
}
