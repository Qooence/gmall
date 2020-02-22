package com.bbo.gmall.manage.controller;

import com.bbo.gmall.manage.bean.pms.PmsSkuInfo;
import com.bbo.gmall.manage.response.Response;
import com.bbo.gmall.manage.service.pms.SkuInfoService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("sku")
public class SkuInfoController {

    @Reference
    SkuInfoService skuInfoService;

    @RequestMapping("list")
    public Response productInfoList(@RequestParam(defaultValue = "1")Integer pageNum,
                                    @RequestParam(defaultValue = "10")Integer pageSize, String productId){

        PageInfo<PmsSkuInfo> pageInfo = skuInfoService.skuInfo(pageNum,pageSize,productId);

        return Response.success(pageInfo);
    }

    @PostMapping("save")
    public Response save(@RequestBody PmsSkuInfo skuInfo){
        return skuInfoService.save(skuInfo);
    }

    @RequestMapping("/detail/{id}")
    public Response detail(@PathVariable String id){
        PmsSkuInfo skuInfo = skuInfoService.detail(id);
        return Response.success("查询成功",skuInfo);
    }

    @DeleteMapping("deletes")
    public Response deletes(@RequestBody String[] ids){
        return skuInfoService.deletes(ids);
    }
}
