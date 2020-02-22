package com.bbo.gmall.manage.service;

import com.bbo.gmall.manage.bean.pms.PmsProductImage;
import com.bbo.gmall.manage.config.BaseServiceImpl;
import com.bbo.gmall.manage.mapper.PmsProductImageMapper;
import com.bbo.gmall.manage.service.pms.ProductImageService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ProductImageImpl extends BaseServiceImpl<PmsProductImage> implements ProductImageService {

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    @Override
    public List<PmsProductImage> listByProductId(String productId) {
        PmsProductImage image = new PmsProductImage();
        image.setProductId(productId);
        List<PmsProductImage> list = pmsProductImageMapper.select(image);
        return list;
    }
}
