package com.bbo.gmall.manage.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bbo.gmall.bean.pms.PmsBaseAttrInfo;
import com.bbo.gmall.bean.pms.PmsBaseAttrValue;
import com.bbo.gmall.manage.config.BaseServiceImpl;
import com.bbo.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.bbo.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.bbo.gmall.service.pms.AttrService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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
            pmsBaseAttrInfos.forEach(item  ->{
                item.setAttrValueList(getAttrValueByAttrId(item.getId()));
            });
        }
        PageInfo<PmsBaseAttrInfo> pageInfo = new PageInfo<>(pmsBaseAttrInfos);

        return pageInfo;
    }

    @Override
    @Transactional
    public void saveAttrInfo(PmsBaseAttrInfo attrInfo) {
        if(StringUtils.isNotBlank(attrInfo.getId())){
            pmsBaseAttrInfoMapper.updateByPrimaryKeySelective(attrInfo);
            deleteAttrValueByAttrId(attrInfo.getId());
        }else{
            attrInfo.setIsEnabled("1");
            pmsBaseAttrInfoMapper.insertSelective(attrInfo);
        }

        if(CollectionUtil.isNotEmpty(attrInfo.getAttrValueList())){
            for(PmsBaseAttrValue attrValue: attrInfo.getAttrValueList()){
                attrValue.setIsEnabled("1");
                attrValue.setAttrId(attrInfo.getId());
                pmsBaseAttrValueMapper.insertSelective(attrValue);
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
        Example example = new Example(PmsBaseAttrValue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("attrId",attrId);
        List<PmsBaseAttrValue> list = pmsBaseAttrValueMapper.selectByExample(example);
        return list;
    }

    private void deleteAttrValueByAttrId(String attrId){
        Example example = new Example(PmsBaseAttrValue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("attrId",attrId);
        pmsBaseAttrValueMapper.deleteByExample(example);
    }

}
