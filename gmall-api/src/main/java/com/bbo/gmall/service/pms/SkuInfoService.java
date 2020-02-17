package com.bbo.gmall.service.pms;

import com.bbo.gmall.base.BaseService;
import com.bbo.gmall.bean.pms.PmsSkuInfo;
import com.bbo.gmall.response.Response;
import com.github.pagehelper.PageInfo;

public interface SkuInfoService extends BaseService<PmsSkuInfo> {

    PageInfo<PmsSkuInfo> skuInfo(Integer pageNum, Integer pageSize, String productId);

    Response save(PmsSkuInfo pmsProductInfo);

    PmsSkuInfo detail(String id);

    Response deletes(String[] ids);

    PmsSkuInfo getSkuById(String skuId);
}
