package com.bbo.gmall.manage.service;

import com.bbo.gmall.bean.pms.PmsSkuInfo;
import com.bbo.gmall.manage.config.BaseServiceImpl;
import com.bbo.gmall.manage.mapper.PmsSkuInfoMapper;
import com.bbo.gmall.response.Response;
import com.bbo.gmall.service.pms.SkuInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SkuInfoServiceImpl extends BaseServiceImpl<PmsSkuInfo> implements SkuInfoService {

    @Autowired
    PmsSkuInfoMapper skuInfoMapper;

    @Override
    public PageInfo<PmsSkuInfo> skuInfo(Integer pageNum, Integer pageSize, String productId) {

        PmsSkuInfo skuInfo = new PmsSkuInfo();
        skuInfo.setProductId(productId);

        if (null != pageNum && null != pageSize && pageSize.intValue() > 0) {
            PageHelper.startPage(pageNum.intValue(), pageSize.intValue());
        }else {
            PageHelper.startPage(1, 25);
        }
        List<PmsSkuInfo> skuInfoList = skuInfoMapper.select(skuInfo);

        PageInfo<PmsSkuInfo> pageInfo = new PageInfo<>(skuInfoList);

        return pageInfo;
    }

    @Override
    public Response save(PmsSkuInfo pmsProductInfo) {
        return null;
    }

    @Override
    public PmsSkuInfo detail(String id) {
        return null;
    }

    @Override
    public Response deletes(String[] ids) {
        return null;
    }
}
