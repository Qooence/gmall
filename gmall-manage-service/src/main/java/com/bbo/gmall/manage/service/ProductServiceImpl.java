package com.bbo.gmall.manage.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bbo.gmall.bean.pms.*;
import com.bbo.gmall.manage.config.BaseServiceImpl;
import com.bbo.gmall.manage.mapper.PmsProductImageMapper;
import com.bbo.gmall.manage.mapper.PmsProductInfoMapper;
import com.bbo.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.bbo.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.bbo.gmall.manage.util.CompareListBeanUtil;
import com.bbo.gmall.response.Response;
import com.bbo.gmall.service.pms.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
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

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

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
            addAttrAndValue(pmsProductInfo,false);
            return Response.success("新增成功");
        }else{ // 修改
            // 1、修改spu基本信息
            pmsProductInfoMapper.updateByPrimaryKey(pmsProductInfo);
            Set<String> productIds = new HashSet<String>(){{add(pmsProductInfo.getId());}};

            // 2、修改销售属性 && 修改销售属性值
            List<PmsProductSaleAttr> pSaleAttrs = pmsProductSaleAttrMapper.findByProductIds(productIds);
            Map<String,List<PmsProductSaleAttr>> attrMap =  CompareListBeanUtil.compare(pSaleAttrs,pmsProductInfo.getProductSaleAttrs(),"saleAttrId");
            List<PmsProductSaleAttr> attrInsert =  attrMap.get("insert");
            List<PmsProductSaleAttr> attrUpdate =  attrMap.get("update");
            List<PmsProductSaleAttr> attrDelete =  attrMap.get("delete");
            if(attrInsert.size() > 0) {
                attrInsert.parallelStream().filter(item -> {
                    item.setProductId(pmsProductInfo.getId());
                    // 新增相应的销售属性值
                    if(CollectionUtil.isNotEmpty(item.getSaleAttrValues())){
                        for(PmsProductSaleAttrValue saleAttrValue : item.getSaleAttrValues()) {
                            saleAttrValue.setProductId(pmsProductInfo.getId());
                            saleAttrValue.setSaleAttrId(item.getId());
                        }
                        pmsProductSaleAttrValueMapper.insertList(item.getSaleAttrValues());
                    }
                    return true;
                }).collect(Collectors.toList());
                pmsProductSaleAttrMapper.insertList(attrInsert);
            }
            if(attrUpdate.size() > 0) {
                for (PmsProductSaleAttr attr : attrUpdate) {
                    // 修改销售属性
                    pmsProductSaleAttrMapper.updateByPrimaryKey(attr);

                    // 修改销售属性对应的属性值
                    List<PmsProductSaleAttrValue> dbList = pmsProductSaleAttrValueMapper.findByAttrIds(new ArrayList<String>(){{add(attr.getId());}});
                    Map<String,List<PmsProductSaleAttrValue>> valueMap = CompareListBeanUtil.compare(dbList,attr.getSaleAttrValues(),"saleAttrValueName");
                    List<PmsProductSaleAttrValue> valueInsert =  valueMap.get("insert");
                    List<PmsProductSaleAttrValue> valueUpdate =  valueMap.get("update");
                    List<PmsProductSaleAttrValue> valueDelete =  valueMap.get("delete");
                    if(valueInsert.size()> 0) {
                        valueInsert.parallelStream().filter(value -> {
                            value.setProductId(pmsProductInfo.getId());
                            value.setSaleAttrId(attr.getId());
                            return true;
                        }).collect(Collectors.toList());
                        pmsProductSaleAttrValueMapper.insertList(valueInsert);
                    }
                    if(valueUpdate.size()> 0) {
                        for (PmsProductSaleAttrValue attrValue : valueUpdate) {
                            pmsProductSaleAttrValueMapper.updateByPrimaryKey(attrValue);
                        }
                    }
                    if(valueDelete.size()> 0) {
                        List<String> ids = valueDelete.parallelStream().map(PmsProductSaleAttrValue:: getId).collect(Collectors.toList());
                        pmsProductSaleAttrValueMapper.deleteByIds(StringUtils.join(ids,","));
                    }
                }
            }

            if(attrDelete.size() > 0) {
                // 删除操作
                List<String> ids = attrDelete.parallelStream().map(item -> {
                    if(CollectionUtil.isNotEmpty(item.getSaleAttrValues())){
                        List<String> valueIds = item.getSaleAttrValues().parallelStream().map(PmsProductSaleAttrValue::getId).collect(Collectors.toList());
                        pmsProductSaleAttrValueMapper.deleteByIds(StringUtils.join(valueIds,","));
                    }
                    return item.getId();
                }).collect(Collectors.toList());
                pmsProductSaleAttrMapper.deleteByIds(StringUtils.join(ids,","));

            }

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

        PmsProductImage image = new PmsProductImage();
        image.setProductId(info.getId());
        List<PmsProductImage> images = pmsProductImageMapper.select(image);
        info.setProductImages(images);

        return info;
    }

    @Override
    @Transactional
    public Response deletes(String[] ids) {
        for (String id : ids) {
            addAttrAndValue(detail(id),true);
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

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId) {
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.selectSpuSaleAttrListCheckBySku(productId,skuId);
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

    private void deleteProductImageByProductId(String productId){
        Example example = new Example(PmsProductImage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",productId);
        pmsProductImageMapper.deleteByExample(example);
    }

    private void addAttrAndValue(PmsProductInfo pmsProductInfo,Boolean isDelete){
        List<PmsProductSaleAttr> saleAttrs = pmsProductInfo.getProductSaleAttrs();
        if(isDelete) deleteSaleAttrByProductId(pmsProductInfo.getId());
        if(CollectionUtil.isNotEmpty(saleAttrs)){
            for (PmsProductSaleAttr saleAttr : saleAttrs){
                if(!isDelete){
                    saleAttr.setProductId(pmsProductInfo.getId());
                    pmsProductSaleAttrMapper.insertSelective(saleAttr);
                }
                // 3、保存销售属性值
                if(isDelete) deleteSaleAttrValueByAttrId(saleAttr.getId());
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

        if(isDelete) deleteProductImageByProductId(pmsProductInfo.getId());
        if (!isDelete && CollectionUtil.isNotEmpty(pmsProductInfo.getProductImages())) {
            for (PmsProductImage productImage : pmsProductInfo.getProductImages()) {
                productImage.setProductId(pmsProductInfo.getId());
            }
            pmsProductImageMapper.insertList(pmsProductInfo.getProductImages());
        }
    }
}
