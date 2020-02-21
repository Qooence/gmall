package com.bbo.gmall.manage.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bbo.gmall.bean.pms.PmsBaseAttrInfo;
import com.bbo.gmall.bean.pms.PmsBaseAttrValue;
import com.bbo.gmall.manage.config.BaseServiceImpl;
import com.bbo.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.bbo.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.bbo.gmall.manage.util.CompareListBeanUtil;
import com.bbo.gmall.service.pms.AttrService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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

        if(CollectionUtil.isNotEmpty(pmsBaseAttrInfos)){
            List<PmsBaseAttrValue> values = pmsBaseAttrValueMapper.findByAttrIds(pmsBaseAttrInfos.parallelStream().map(PmsBaseAttrInfo::getId).collect(Collectors.toList()));
            pmsBaseAttrInfos.forEach(item  ->{
                item.setAttrValueList(values.parallelStream().filter(value -> value.getAttrId().equals(item.getId())).collect(Collectors.toList()));
            });
        }
        PageInfo<PmsBaseAttrInfo> pageInfo = new PageInfo<>(pmsBaseAttrInfos);

        return pageInfo;
    }

    @Override
    @Transactional
    public void saveAttrInfo(PmsBaseAttrInfo attrInfo) {

        if(StringUtils.isNotBlank(attrInfo.getId())){ // 更新
            pmsBaseAttrInfoMapper.updateByPrimaryKey(attrInfo);

            // 查询出原来的PmsBaseAttrValue
            Map<String,List<PmsBaseAttrValue>> resultMap = CompareListBeanUtil.compare(pmsBaseAttrValueMapper.findByAttrId(attrInfo.getId()),attrInfo.getAttrValueList(),"valueName");

            if(resultMap.get("insert").size() > 0){
                // 新增操作
                resultMap.get("insert").parallelStream().filter(item -> {
                    item.setIsEnabled("1");
                    item.setAttrId(attrInfo.getId());
                    return true;
                }).collect(Collectors.toList());
                pmsBaseAttrValueMapper.insertList(resultMap.get("insert"));
            }

            if(resultMap.get("update").size() > 0){
                // 更新操作
                for (PmsBaseAttrValue attrValue : resultMap.get("update")) {
                    pmsBaseAttrValueMapper.updateByPrimaryKey(attrValue);
                }
            }

            if(resultMap.get("delete").size() > 0){
                // 删除操作
                List<String> ids = resultMap.get("delete").parallelStream().map(PmsBaseAttrValue:: getId).collect(Collectors.toList());
                pmsBaseAttrValueMapper.deleteByIds(StringUtils.join(ids,","));
            }

        }else{ // 新增
            attrInfo.setIsEnabled("1");
            pmsBaseAttrInfoMapper.insertSelective(attrInfo);

            if(CollectionUtil.isNotEmpty(attrInfo.getAttrValueList())){
                for(PmsBaseAttrValue attrValue: attrInfo.getAttrValueList()){
                    attrValue.setIsEnabled("1");
                    attrValue.setAttrId(attrInfo.getId());
                    pmsBaseAttrValueMapper.insertSelective(attrValue);
                }
            }
        }
    }

    @Override
    @Transactional
    public PmsBaseAttrInfo detail(String id) {
        PmsBaseAttrInfo attrInfo = pmsBaseAttrInfoMapper.selectByPrimaryKey(id);
        attrInfo.setAttrValueList(getAttrValueByAttrId(attrInfo.getId()));
        return attrInfo;
    }

    @Override
    @Transactional
    public void deletes(String[] ids) {
        for (String id : ids) {
            pmsBaseAttrInfoMapper.deleteByPrimaryKey(id);
            deleteAttrValueByAttrId(id);
        }
    }

    private List<PmsBaseAttrValue> getAttrValueByAttrId(String attrId){
        List<PmsBaseAttrValue> list = pmsBaseAttrValueMapper.findByAttrId(attrId);
        return list;
    }

    private void deleteAttrValueByAttrId(String attrId){
        Example example = new Example(PmsBaseAttrValue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("attrId",attrId);
        pmsBaseAttrValueMapper.deleteByExample(example);
    }

}
