package com.bbo.gmall.manage.mapper;

import com.bbo.gmall.manage.bean.pms.PmsBaseAttrValue;
import com.bbo.gmall.manage.config.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PmsBaseAttrValueMapper extends BaseMapper<PmsBaseAttrValue> {

    List<PmsBaseAttrValue> findByAttrId(@Param("attrId") String attrId);

    List<PmsBaseAttrValue> findByAttrIds(@Param("attrIds") Collection<String> attrIds);
}
