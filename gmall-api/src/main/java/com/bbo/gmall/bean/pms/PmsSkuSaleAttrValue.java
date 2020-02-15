package com.bbo.gmall.bean.pms;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class PmsSkuSaleAttrValue implements Serializable {
    private static final long serialVersionUID = 4264577410489302990L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id;

    @Column
    private String skuId;

    @Column
    private String saleAttrId;

    @Column
    private String saleAttrValueId;

    @Column
    private String saleAttrName;

    @Column
    private String saleAttrValueName;
}
