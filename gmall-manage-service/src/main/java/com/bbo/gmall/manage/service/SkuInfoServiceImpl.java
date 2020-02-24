package com.bbo.gmall.manage.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.bbo.gmall.manage.config.BaseServiceImpl;
import com.bbo.gmall.manage.bean.pms.PmsSkuAttrValue;
import com.bbo.gmall.manage.bean.pms.PmsSkuImage;
import com.bbo.gmall.manage.bean.pms.PmsSkuInfo;
import com.bbo.gmall.manage.bean.pms.PmsSkuSaleAttrValue;
import com.bbo.gmall.manage.mapper.PmsSkuImageMapper;
import com.bbo.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.bbo.gmall.manage.response.Response;
import com.bbo.gmall.manage.service.pms.SkuInfoService;
import com.bbo.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.bbo.gmall.manage.mapper.PmsSkuInfoMapper;
import com.bbo.gmall.util.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

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

    @Autowired
    RedisUtil redisUtil;

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

        PmsSkuInfo info = new PmsSkuInfo();
        Jedis jedis = null;
        try {
            // 连接缓存
            jedis = redisUtil.getJedis();
            // 查询缓存
            String skyKey = "sku:" + skuId + ":info";
            String skuJson = jedis.get(skyKey);
            if(StringUtils.isNotBlank(skuJson)){
                info = JSON.parseObject(skuJson,PmsSkuInfo.class);
            }else {
                // 如果缓存中没有，查询mySql
                info = getSkuByIdFromDb(skuId);
                // 查询结果存入redis
                if(info != null){
                    jedis.set("sku:" + skuId + ":info",JSON.toJSONString(info));
                }else{
                    // 数据库中不存在该sku
                    // 为了防止缓存穿透，将null或者空字符串存入redis中
                    jedis.setex("sku:" + skuId + ":info",3*60, "");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return info;
    }

    /**
     * 加入redis自带分布式锁 防止缓存击穿
     * @param skuId
     * @param ip
     * @return
     */
    @Override
    public PmsSkuInfo getSkuById(String skuId, String ip){
        System.out.println("ip==>  "+ip+"的同学,Thread==>  "+Thread.currentThread().getName()+"进入的商品详情的请求");
        PmsSkuInfo info = new PmsSkuInfo();
        Jedis jedis = null;
        try {
            // 连接缓存
            jedis = redisUtil.getJedis();
            // 查询缓存
            String skuKey = "sku:" + skuId + ":info";
            String skuJson = jedis.get(skuKey);
            if(StringUtils.isNotBlank(skuJson)){
                System.out.println("ip==>  "+ip+"的同学,Thread==>  "+Thread.currentThread().getName()+"从缓存中获取到商品详情信息");
                info = JSON.parseObject(skuJson,PmsSkuInfo.class);
            }else {
                String lock = "sku:" + skuId + ":lock";
                System.out.println("ip==>  "+ip+"的同学,Thread==>  "+Thread.currentThread().getName()+"发现缓存中没有，申请缓存的分布式锁：" + lock);

                // 分布式锁的value,设置唯一值，做唯一值判断，防止在归还锁之前缓存过期导致删除了其他线程的锁
                String token = UUID.randomUUID().toString();

                // 设置分布式锁
                String OK = jedis.set(lock,token,"nx","px",10*1000);

                if(StringUtils.isNotBlank(OK) && OK.equals("OK")){
                    // 设置成功，有权在10秒的过期时间内访问数据库
                    System.out.println("ip==>  "+ip+"的同学,Thread==>  "+Thread.currentThread().getName() +  "有权在10秒的过期时间内访问数据库:" + lock);
                    // 如果缓存中没有，查询mySql
                    info = getSkuByIdFromDb(skuId);

                    if(info != null){
                        // 查询结果存入redis
//                        Thread.sleep(3*1000);//测试使用加入的
                        System.out.println("ip==>  "+ip+"的同学,Thread==>  "+Thread.currentThread().getName() +"睡眠结束,开始讲数据加入缓存");
                        jedis.set("sku:" + skuId + ":info",JSON.toJSONString(info));
                    }else{
                        // 数据库中不存在该sku
                        // 为了防止缓存穿透，将null或者空字符串存入redis中
                        jedis.setex("sku:" + skuId + ":info",3*60, JSON.toJSONString(""));
                    }

                    // 在访问mysql后，将mysql的分布锁释放
                    System.out.println("ip==>  "+ip+"的同学,Thread==>  " + Thread.currentThread().getName()+"使用完毕，将锁归还："+lock);
                    String lockToken = jedis.get("sku:" + skuId + ":lock");
                    if(StringUtils.isNotBlank(lockToken)&&lockToken.equals(token)){
                        jedis.del("sku:" + skuId + ":lock");// 用token确认删除的是自己的sku的锁
                    }

                    // 假设在做分布式锁value唯一值判断时，分布式锁过期失效，删除锁时就会删除其他线程的锁，这个问题这么处理
                    // 可利用lua脚本，在查询到key的同时删除该key，防止高并发下的意外的发生
                    // String script ="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                    // jedis.eval(script, Collections.singletonList(lock),Collections.singletonList(token));

                }else{
                    System.out.println("ip==>  "+ip+"的同学,Thread==>  " + Thread.currentThread().getName() + "的同学没有获取到锁,开始自旋");
                    Thread.sleep(1*1000);
                    return getSkuById(skuId,ip);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return info;
    }

    public PmsSkuInfo getSkuByIdFromDb(String skuId){
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

    @Override
    public List<PmsSkuInfo> getAllSku(){
        List<PmsSkuInfo> skuInfos = skuInfoMapper.selectAll();
        for (PmsSkuInfo skuInfo : skuInfos) {
            String skuId = skuInfo.getId();

            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> pmsSkuAttrValues = attrValueMapper.select(pmsSkuAttrValue);
            skuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        }
        return skuInfos;
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
