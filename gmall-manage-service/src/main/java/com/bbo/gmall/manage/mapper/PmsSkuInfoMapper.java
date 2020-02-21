package com.bbo.gmall.manage.mapper;

import com.bbo.gmall.bean.pms.PmsSkuInfo;
import com.bbo.gmall.manage.config.BaseMapper;

import java.util.List;

public interface PmsSkuInfoMapper extends BaseMapper<PmsSkuInfo> {

    List<PmsSkuInfo> selectSkuSaleAttrValueListBySpu(String productId);
}
