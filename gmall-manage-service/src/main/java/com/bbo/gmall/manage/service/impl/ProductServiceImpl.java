package com.bbo.gmall.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bbo.gmall.bean.pms.PmsBaseAttrInfo;
import com.bbo.gmall.bean.pms.PmsProductInfo;
import com.bbo.gmall.manage.config.BaseServiceImpl;
import com.bbo.gmall.manage.mapper.PmsProductInfoMapper;
import com.bbo.gmall.service.pms.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ProductServiceImpl extends BaseServiceImpl<PmsProductInfo> implements ProductInfoService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Override
    public PageInfo<PmsProductInfo> productInfoList(Integer pageNum, Integer pageSize, String catalog3Id) {

        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);

        if (null != pageNum && null != pageSize && pageSize.intValue() > 0) {
            PageHelper.startPage(pageNum.intValue(), pageSize.intValue());
        }else {
            PageHelper.startPage(1, 25);
        }
        List<PmsProductInfo> pmsProductInfoList = pmsProductInfoMapper.select(pmsProductInfo);
//        if(CollectionUtil.isNotEmpty(pmsBaseAttrInfos)){
//            pmsBaseAttrInfos.forEach(item  ->{
//                item.setAttrValueList(getAttrValueByAttrId(item.getId()));
//            });
//        }
        PageInfo<PmsProductInfo> pageInfo = new PageInfo<>(pmsProductInfoList);

        return pageInfo;
    }

}
