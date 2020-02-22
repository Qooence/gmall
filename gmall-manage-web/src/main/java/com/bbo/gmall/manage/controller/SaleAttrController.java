package com.bbo.gmall.manage.controller;

import com.bbo.gmall.manage.bean.pms.PmsBaseSaleAttr;
import com.bbo.gmall.manage.response.Response;
import com.bbo.gmall.manage.service.pms.SaleAttrService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("saleAttr")
public class SaleAttrController {

    @Reference
    SaleAttrService saleAttrService;

    @GetMapping("list")
    public Response list(){
        List<PmsBaseSaleAttr> list = saleAttrService.selectAll();
        return Response.success(list);
    }


}
