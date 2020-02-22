package com.bbo.gmall.manage.controller;

import com.bbo.gmall.manage.bean.pms.PmsProductInfo;
import com.bbo.gmall.manage.bean.pms.PmsProductSaleAttr;
import com.bbo.gmall.manage.service.pms.ProductInfoService;
import com.bbo.gmall.manage.util.PmsUploadUtil;
import com.bbo.gmall.manage.response.Response;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("product")
public class ProductController {
    @Value("${img.product.host}")
    private String host;
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

    @CrossOrigin
    @RequestMapping("fileUpload")
    public Response fileUpload(@RequestParam("file") MultipartFile multipartFile){
        // 将图片或者音视频上传到分布式的文件存储系统
        // 将图片的存储路径返回给页面
        String imgUrl = PmsUploadUtil.uploadImage(host, multipartFile);
        return Response.success("上传成功",imgUrl);
    }
}
