package com.bbo.gmall.manage.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bbo.gmall.bean.pms.*;
import com.bbo.gmall.manage.config.BaseServiceImpl;
import com.bbo.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.bbo.gmall.manage.mapper.PmsSkuImageMapper;
import com.bbo.gmall.manage.mapper.PmsSkuInfoMapper;
import com.bbo.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.bbo.gmall.response.Response;
import com.bbo.gmall.service.pms.SkuInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SkuInfoServiceImpl extends BaseServiceImpl<PmsSkuInfo> implements SkuInfoService {

    @Autowired
    PmsSkuInfoMapper skuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper attrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper saleAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

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
    @Transactional
    public Response save(PmsSkuInfo pmsProductInfo) {
        if (StringUtils.isNotBlank(pmsProductInfo.getId())) { //修改
            // 1、修改sku信息
            skuInfoMapper.updateByPrimaryKey(pmsProductInfo);
            insertAndDelete(pmsProductInfo, true, false);
            return Response.success("修改成功");
        } else { // 新增
            // 1、保存sku信息
            skuInfoMapper.insertSelective(pmsProductInfo);
            insertAndDelete(pmsProductInfo,false, false);
            return Response.success("新增成功");
        }
    }

    @Override
    public PmsSkuInfo detail(String id) {
        PmsSkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(id);
        if(null == skuInfo) return null;
        PmsSkuAttrValue attrValue = new PmsSkuAttrValue();
        attrValue.setSkuId(id);
        List<PmsSkuAttrValue>  attrValues = attrValueMapper.select(attrValue);
        if(CollectionUtil.isNotEmpty(attrValues)){
            for (PmsSkuAttrValue attr : attrValues) {
                skuInfo.getBaseAttr().add(attr.getAttrId() + "," + attr.getValueId());
            }
        }

        PmsSkuSaleAttrValue saleAttrValue = new PmsSkuSaleAttrValue();
        saleAttrValue.setSkuId(id);
        List<PmsSkuSaleAttrValue> saleAttrValues = saleAttrValueMapper.select(saleAttrValue);
        if(CollectionUtil.isNotEmpty(saleAttrValues)){
            for (PmsSkuSaleAttrValue saleAttr : saleAttrValues) {
                skuInfo.getSaleAttr().add(saleAttr.getSaleAttrId() + "," + saleAttr.getSaleAttrValueId());
            }
        }

        skuInfo.setSkuImages(getPmsSkuImagesBySkuId(skuInfo.getId()));
        return skuInfo;
    }

    @Override
    @Transactional
    public Response deletes(String[] ids) {
        for (String id : ids) {
            insertAndDelete(detail(id),false,true);
            skuInfoMapper.deleteByPrimaryKey(id);
        }
        return Response.success("删除成功");
    }

    @Override
    public PmsSkuInfo getSkuById(String skuId){
        // sku信息
        PmsSkuInfo info = skuInfoMapper.selectByPrimaryKey(skuId);

        // sku的图片集合
        info.setSkuImages(getPmsSkuImagesBySkuId(skuId));
        return info;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {

        List<PmsSkuInfo> pmsSkuInfos = skuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);

        return pmsSkuInfos;
    }


    private void insertAndDelete(PmsSkuInfo info,Boolean isUpdate,Boolean isDelete){

        // 2、保存pms_sku_attr_value 平台属性的值
        if(isUpdate || isDelete) deleteBaseAttrValueBySkuId(info.getId());
        List<String> baseAttrs = info.getBaseAttr();
        if (CollectionUtil.isNotEmpty(baseAttrs) && !isDelete) {
            List<PmsSkuAttrValue> skuAttrValues = new ArrayList<>();
            for (String baseAttr : baseAttrs) {
                List<String> list = Arrays.asList(baseAttr.split(","));
                PmsSkuAttrValue skuAttrValue = new PmsSkuAttrValue();
                skuAttrValue.setSkuId(info.getId());
                skuAttrValue.setAttrId(list.get(0));
                skuAttrValue.setValueId(list.get(1));
                skuAttrValues.add(skuAttrValue);
            }
            attrValueMapper.insertList(skuAttrValues);
        }

        // 3、保存pms_sku_sale_attr_value 库存单元（单件商品）的销售属性的值
        if(isUpdate || isDelete) deleteSaleAttrValueBySkuId(info.getId());
        List<String> saleAttrs = info.getSaleAttr();
        if (CollectionUtil.isNotEmpty(saleAttrs) && !isDelete) {
            List<PmsSkuSaleAttrValue> skuSaleAttrValues = new ArrayList<>();
            for (String saleAttr : saleAttrs) {
                List<String> list = Arrays.asList(saleAttr.split(","));
                PmsSkuSaleAttrValue skuSaleAttrValue = new PmsSkuSaleAttrValue();
                skuSaleAttrValue.setSkuId(info.getId());
                skuSaleAttrValue.setSaleAttrId(list.get(0));
                skuSaleAttrValue.setSaleAttrValueId(list.get(1));
                skuSaleAttrValues.add(skuSaleAttrValue);
            }
            saleAttrValueMapper.insertList(skuSaleAttrValues);
        }

        // 4、保存pms_sku_info 库存单元（单件商品）的图片信息
        if(isUpdate || isDelete) deleteSkuImageBySkuId(info.getId());
        List<PmsSkuImage> skuImageList = info.getSkuImages();
        if(CollectionUtil.isNotEmpty(skuImageList) && !isDelete){
            for (PmsSkuImage skuImage : skuImageList) {
                skuImage.setSkuId(info.getId());
                skuImage.setIsDefault(skuImage.getIsDefault() == null ? "0" : skuImage.getIsDefault());
            }
            pmsSkuImageMapper.insertList(skuImageList);
        }
    }

    private void deleteBaseAttrValueBySkuId(String skuId){
        Example example = new Example(PmsSkuAttrValue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("skuId",skuId);
        attrValueMapper.deleteByExample(example);
    }

    private void deleteSaleAttrValueBySkuId(String skuId){
        Example example = new Example(PmsSkuSaleAttrValue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("skuId",skuId);
        saleAttrValueMapper.deleteByExample(example);
    }

    private void deleteSkuImageBySkuId(String skuId){
        Example example = new Example(PmsSkuImage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("skuId",skuId);
        pmsSkuImageMapper.deleteByExample(example);
    }

    private List<PmsSkuImage> getPmsSkuImagesBySkuId(String skuId){
        PmsSkuImage image = new PmsSkuImage();
        image.setSkuId(skuId);
        return pmsSkuImageMapper.select(image);
    }

}
