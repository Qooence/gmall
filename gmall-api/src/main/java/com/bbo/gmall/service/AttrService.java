package com.bbo.gmall.service;

import com.bbo.gmall.base.BaseService;
import com.bbo.gmall.bean.PmsBaseAttrInfo;
import com.github.pagehelper.PageInfo;


public interface AttrService extends BaseService<PmsBaseAttrInfo> {
    PageInfo<PmsBaseAttrInfo> attrInfoList(Integer pageNum, Integer pageSize,String catalog3Id);
}
