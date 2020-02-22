package com.bbo.gmall.manage.service.pms;

import com.bbo.gmall.manage.base.BaseService;
import com.bbo.gmall.manage.bean.pms.PmsProductInfo;
import com.bbo.gmall.manage.bean.pms.PmsProductSaleAttr;
import com.bbo.gmall.manage.response.Response;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductInfoService extends BaseService<PmsProductInfo> {

    PageInfo<PmsProductInfo> productInfoList(Integer pageNum, Integer pageSize, String catalog3Id);

    Response save(PmsProductInfo pmsProductInfo);

    PmsProductInfo detail(String id);

    Response deletes(String[] ids);

    List<PmsProductSaleAttr> getPmsProductSaleAttr(String productId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId);
}
