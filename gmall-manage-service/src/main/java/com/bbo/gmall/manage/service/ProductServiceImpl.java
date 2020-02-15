package com.bbo.gmall.manage.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bbo.gmall.bean.pms.PmsProductInfo;
import com.bbo.gmall.bean.pms.PmsProductSaleAttr;
import com.bbo.gmall.bean.pms.PmsProductSaleAttrValue;
import com.bbo.gmall.manage.config.BaseServiceImpl;
import com.bbo.gmall.manage.mapper.PmsProductInfoMapper;
import com.bbo.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.bbo.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.bbo.gmall.response.Response;
import com.bbo.gmall.service.pms.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl extends BaseServiceImpl<PmsProductInfo> implements ProductInfoService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

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

        Set<String> productIds = pmsProductInfoList.parallelStream().map(item -> item.getId()).collect(Collectors.toSet());
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.findByProductIds(productIds);

        Set<String> saleAttrIds = pmsProductSaleAttrs.parallelStream().map(item -> item.getId()).collect(Collectors.toSet());
        List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.findByAttrIds(saleAttrIds);

        for (PmsProductInfo productInfo : pmsProductInfoList) {
            productInfo.setProductSaleAttrs(
                pmsProductSaleAttrs.parallelStream().filter(item -> {
                    item.setSaleAttrValues(pmsProductSaleAttrValues.parallelStream()
                            .filter(value -> value.getSaleAttrId().equals(item.getId()))
                            .collect(Collectors.toList()));
                    return item.getProductId().equals(productInfo.getId());
                }).collect(Collectors.toList())
            );

        }

        PageInfo<PmsProductInfo> pageInfo = new PageInfo<>(pmsProductInfoList);

        return pageInfo;
    }

    @Override
    @Transactional
    public Response save(PmsProductInfo pmsProductInfo) {
        if(StringUtils.isBlank(pmsProductInfo.getId())){// 新增
            // 1、保存spu基本信息
            pmsProductInfoMapper.insertSelective(pmsProductInfo);
            // 2、保存销售属性
            addAttrAndValue(pmsProductInfo,false,false);
            return Response.success("新增成功");
        }else{ // 修改
            // 1、修改spu基本信息
            pmsProductInfoMapper.updateByPrimaryKey(pmsProductInfo);
            // 2、修改销售属性
            addAttrAndValue(pmsProductInfo,true,false);
            return Response.success("修改成功");
        }
    }

    @Override
    public PmsProductInfo detail(String id) {
        PmsProductInfo info = pmsProductInfoMapper.selectByPrimaryKey(id);

        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.findByProductIds(new ArrayList<String>(){{add(info.getId());}});

        Set<String> saleAttrIds = pmsProductSaleAttrs.parallelStream().map(item -> item.getId()).collect(Collectors.toSet());
        List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.findByAttrIds(saleAttrIds);

        if(pmsProductSaleAttrs.size() > 0){
            for (PmsProductSaleAttr saleAttr : pmsProductSaleAttrs) {
                saleAttr.setSaleAttrValues(
                    pmsProductSaleAttrValues.parallelStream()
                        .filter(value -> value.getSaleAttrId().equals(saleAttr.getId()))
                        .collect(Collectors.toList())
                );
            }
            info.setProductSaleAttrs(pmsProductSaleAttrs);
        }

        return info;
    }

    @Override
    @Transactional
    public Response deletes(String[] ids) {
        for (String id : ids) {
            addAttrAndValue(detail(id),false,true);
            pmsProductInfoMapper.deleteByPrimaryKey(id);
        }
        return Response.success("删除成功");
    }

    @Override
    public List<PmsProductSaleAttr> getPmsProductSaleAttr(String productId) {
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.findByProductIds(new ArrayList<String>(){{add(productId);}});

        Set<String> saleAttrIds = pmsProductSaleAttrs.parallelStream().map(item -> item.getId()).collect(Collectors.toSet());
        List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.findByAttrIds(saleAttrIds);

        if(pmsProductSaleAttrs.size() > 0){
            for (PmsProductSaleAttr saleAttr : pmsProductSaleAttrs) {
                saleAttr.setSaleAttrValues(
                        pmsProductSaleAttrValues.parallelStream()
                                .filter(value -> value.getSaleAttrId().equals(saleAttr.getId()))
                                .collect(Collectors.toList())
                );
            }
        }

        return pmsProductSaleAttrs;
    }

    private void deleteSaleAttrByProductId(String productId){
        Example example = new Example(PmsProductSaleAttr.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",productId);
        pmsProductSaleAttrMapper.deleteByExample(example);
    }

    private void deleteSaleAttrValueByAttrId(String attrId){
        Example example = new Example(PmsProductSaleAttrValue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("saleAttrId",attrId);
        pmsProductSaleAttrValueMapper.deleteByExample(example);
    }

    private void addAttrAndValue(PmsProductInfo pmsProductInfo,Boolean isUpdate,Boolean isDelete){
        List<PmsProductSaleAttr> saleAttrs = pmsProductInfo.getProductSaleAttrs();
        if(isUpdate || isDelete) deleteSaleAttrByProductId(pmsProductInfo.getId());
        if(CollectionUtil.isNotEmpty(saleAttrs)){
            for (PmsProductSaleAttr saleAttr : saleAttrs){
                if(!isDelete){
                    saleAttr.setProductId(pmsProductInfo.getId());
                    pmsProductSaleAttrMapper.insertSelective(saleAttr);
                }
                // 3、保存销售属性值
                if(isUpdate || isDelete) deleteSaleAttrValueByAttrId(saleAttr.getId());
                if(!isDelete){
                    List<PmsProductSaleAttrValue> saleAttrValues = saleAttr.getSaleAttrValues();
                    if(CollectionUtil.isNotEmpty(saleAttrValues)){
                        for(PmsProductSaleAttrValue saleAttrValue : saleAttrValues) {
                            saleAttrValue.setProductId(pmsProductInfo.getId());
                            saleAttrValue.setSaleAttrId(saleAttr.getId());
                        }
                        pmsProductSaleAttrValueMapper.insertList(saleAttrValues);
                    }
                }
            }
        }
    }
}
