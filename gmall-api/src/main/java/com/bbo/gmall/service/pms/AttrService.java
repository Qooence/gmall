package com.bbo.gmall.service.pms;

import com.bbo.gmall.base.BaseService;
import com.bbo.gmall.bean.pms.PmsBaseAttrInfo;
import com.github.pagehelper.PageInfo;


public interface AttrService extends BaseService<PmsBaseAttrInfo> {
    PageInfo<PmsBaseAttrInfo> attrInfoList(Integer pageNum, Integer pageSize,String catalog3Id);

    void saveAttrInfo(PmsBaseAttrInfo attrInfo);

    PmsBaseAttrInfo detail(String id);

    void deletes(String[] ids);
}
