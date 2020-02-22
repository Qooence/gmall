package com.bbo.gmall.manage.service.pms;

import com.bbo.gmall.manage.base.BaseService;
import com.bbo.gmall.manage.bean.pms.PmsProductImage;

import java.util.List;

public interface ProductImageService extends BaseService<PmsProductImage> {

    List<PmsProductImage> listByProductId(String productId);

}
