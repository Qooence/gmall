package com.bbo.gmall.manage.bean.pms;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PmsSearchParam implements Serializable {

    private static final long serialVersionUID = -3207228553334161670L;

    private String catalog3Id;
    private String keyword;
    private List<PmsSkuAttrValue> skuAttrValueList;

}
