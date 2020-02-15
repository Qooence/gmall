package com.bbo.gmall.manage.controller;

import com.bbo.gmall.bean.pms.PmsProductInfo;
import com.bbo.gmall.bean.pms.PmsProductSaleAttr;
import com.bbo.gmall.response.Response;
import com.bbo.gmall.service.pms.ProductInfoService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("product")
public class ProductController {

    @Reference
    ProductInfoService productInfoService;

    @RequestMapping("productInfoList")
    public Response productInfoList(@RequestParam(defaultValue = "1")Integer pageNum,
                                 @RequestParam(defaultValue = "10")Integer pageSize,String catalog3Id){

        PageInfo<PmsProductInfo> pageInfo = productInfoService.productInfoList(pageNum,pageSize,catalog3Id);

        return Response.success(pageInfo);
    }

    @PostMapping("save")
    public Response save(@RequestBody PmsProductInfo pmsProductInfo){
        return productInfoService.save(pmsProductInfo);
    }

    @RequestMapping("/detail/{id}")
    public Response detail(@PathVariable String id){
        PmsProductInfo productInfo = productInfoService.detail(id);
        return Response.success("查询成功",productInfo);
    }

    @DeleteMapping("deletes")
    public Response deletes(@RequestBody String[] ids){
        return productInfoService.deletes(ids);
    }

    @GetMapping("saleAttr")
    public Response getPmsProductSaleAttr(String productId){
        List<PmsProductSaleAttr> pmsProductSaleAttrs = productInfoService.getPmsProductSaleAttr(productId);
        return Response.success(pmsProductSaleAttrs);
    }
}
