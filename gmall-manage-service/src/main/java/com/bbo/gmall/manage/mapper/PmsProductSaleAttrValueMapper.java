package com.bbo.gmall.manage.mapper;
import com.bbo.gmall.bean.pms.PmsProductSaleAttrValue;
import com.bbo.gmall.manage.config.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface PmsProductSaleAttrValueMapper extends BaseMapper<PmsProductSaleAttrValue>{

    List<PmsProductSaleAttrValue> findByAttrIds(@Param("attrIds") Collection<String> attrIds);
}
