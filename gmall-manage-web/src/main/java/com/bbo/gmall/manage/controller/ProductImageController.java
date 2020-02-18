package com.bbo.gmall.manage.controller;


import com.bbo.gmall.bean.pms.PmsProductImage;
import com.bbo.gmall.response.Response;
import com.bbo.gmall.service.pms.ProductImageService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("productImage")
public class ProductImageController {

    @Reference
    ProductImageService productImageService;

    @GetMapping("list")
    public Response list(String productId){
        List<PmsProductImage> list = productImageService.listByProductId(productId);
        return Response.success(list);
    }
}
