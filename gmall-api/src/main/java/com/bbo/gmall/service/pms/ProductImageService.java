package com.bbo.gmall.service.pms;

import com.bbo.gmall.base.BaseService;
import com.bbo.gmall.bean.pms.PmsProductImage;

import java.util.List;

public interface ProductImageService extends BaseService<PmsProductImage> {

    List<PmsProductImage> listByProductId(String productId);

}
