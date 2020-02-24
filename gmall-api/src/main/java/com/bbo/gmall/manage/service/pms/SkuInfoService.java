package com.bbo.gmall.manage.service.pms;

import com.bbo.gmall.manage.base.BaseService;
import com.bbo.gmall.manage.bean.pms.PmsSkuInfo;
import com.bbo.gmall.manage.response.Response;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SkuInfoService extends BaseService<PmsSkuInfo> {

    PageInfo<PmsSkuInfo> skuInfo(Integer pageNum, Integer pageSize, String productId);

    Response save(PmsSkuInfo pmsProductInfo);

    PmsSkuInfo detail(String id);

    Response deletes(String[] ids);

    PmsSkuInfo getSkuById(String skuId);

    PmsSkuInfo getSkuById(String skuId, String ip);

    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId);

    List<PmsSkuInfo> getAllSku();
}
