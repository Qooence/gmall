package com.bbo.gmall.manage.mapper;
import com.bbo.gmall.manage.bean.pms.PmsProductSaleAttr;
import com.bbo.gmall.manage.config.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface PmsProductSaleAttrMapper extends BaseMapper<PmsProductSaleAttr>{

    List<PmsProductSaleAttr> findByProductIds(@Param("productIds") Collection<String> productIds);

    List<PmsProductSaleAttr> selectSpuSaleAttrListCheckBySku(@Param("productId") String productId, @Param("skuId") String skuId);

}
