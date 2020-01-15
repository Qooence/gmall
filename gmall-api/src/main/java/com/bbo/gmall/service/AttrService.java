package com.bbo.gmall.service;

import com.bbo.gmall.base.BaseService;
import com.bbo.gmall.bean.PmsBaseAttrInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface AttrService extends BaseService<PmsBaseAttrInfo> {
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    PageInfo<PmsBaseAttrInfo> attrInfoList1(Integer pageNum, Integer pageSize,String catalog3Id);
}
