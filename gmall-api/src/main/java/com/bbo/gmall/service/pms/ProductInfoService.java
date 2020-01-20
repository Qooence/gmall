package com.bbo.gmall.service.pms;

import com.bbo.gmall.base.BaseService;
import com.bbo.gmall.bean.pms.PmsProductInfo;
import com.github.pagehelper.PageInfo;

public interface ProductInfoService extends BaseService<PmsProductInfo> {

    PageInfo<PmsProductInfo> productInfoList(Integer pageNum, Integer pageSize, String catalog3Id);
}
