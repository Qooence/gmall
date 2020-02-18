package com.bbo.gmall.bean.pms;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PmsSkuInfo implements Serializable {

    private static final long serialVersionUID = 772316092950595296L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id;

    @Column
    private String productId;

    @Column
    private BigDecimal price;

    @Column
    private String skuName;

    @Column
    private String skuDesc;

    @Column
    private BigDecimal weight;

    @Column
    private String tmId;

    @Column
    private String catalog3Id;

    @Column
    private String skuDefaultImg;

    @Transient
    private List<String> baseAttr = new ArrayList<String>();

    @Transient
    private List<String> saleAttr = new ArrayList<String>();

    @Transient
    private List<PmsSkuImage> skuImages = new ArrayList<PmsSkuImage>();
}
