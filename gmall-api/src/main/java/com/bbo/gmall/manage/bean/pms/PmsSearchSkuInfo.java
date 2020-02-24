package com.bbo.gmall.manage.bean.pms;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PmsSearchSkuInfo implements Serializable {
    private static final long serialVersionUID = 4216093063810906471L;

    @Id
    private Long id;
    private String skuName;
    private String skuDesc;
    private String catalog3Id;
    private BigDecimal price;
    private String skuDefaultImg;
    private Double hotScore;
    private String productId;
    private List<PmsSkuAttrValue> skuAttrValueList;
}
