package com.bbo.gmall.service;

import com.bbo.gmall.bean.PmsBaseAttrInfo;

import java.util.List;


public interface AttrService {
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);
}
