package com.bbo.gmall.manage.controller;

import com.bbo.gmall.bean.pms.PmsProductInfo;
import com.bbo.gmall.response.Response;
import com.bbo.gmall.service.pms.ProductInfoService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ProductController {

    @Reference
    ProductInfoService productInfoService;

    @RequestMapping("productInfoList")
    public Response productInfoList(@RequestParam(defaultValue = "1")Integer pageNum,
                                 @RequestParam(defaultValue = "10")Integer pageSize,String catalog3Id){

        PageInfo<PmsProductInfo> pageInfo = productInfoService.productInfoList(pageNum,pageSize,catalog3Id);

        return Response.success(pageInfo);
    }

}
