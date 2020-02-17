package com.bbo.gmall.bean.pms;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
public class PmsProductInfo implements Serializable {

    private static final long serialVersionUID = 1117649407999707862L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    private String productName;

    @Column
    private String description;

    @Column
    private String catalog3Id;

    @Column
    private String tmId;

    @Transient
    private List<PmsProductSaleAttr> productSaleAttrs;

    @Transient
    private List<PmsProductImage> productImages;
}
