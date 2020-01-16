package com.bbo.gmall.manage.service.impl;

import com.bbo.gmall.bean.PmsBaseAttrInfo;
import com.bbo.gmall.manage.config.BaseServiceImpl;
import com.bbo.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.bbo.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.bbo.gmall.service.AttrService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AttrServiceImpl extends BaseServiceImpl<PmsBaseAttrInfo> implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;
    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Override
    public PageInfo<PmsBaseAttrInfo> attrInfoList(Integer pageNum, Integer pageSize,String catalog3Id) {

        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);

        if (null != pageNum && null != pageSize && pageSize.intValue() > 0) {
            PageHelper.startPage(pageNum.intValue(), pageSize.intValue());
        }else {
            PageHelper.startPage(1, 25);
        }
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        PageInfo<PmsBaseAttrInfo> pageInfo = new PageInfo<>(pmsBaseAttrInfos);

        return pageInfo;
    }
}
