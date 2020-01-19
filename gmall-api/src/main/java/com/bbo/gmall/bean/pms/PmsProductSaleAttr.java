package com.bbo.gmall.bean.pms;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
public class PmsProductSaleAttr implements Serializable {

    private static final long serialVersionUID = -7207039994007913520L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    private String productId;

    @Column
    private String saleAttrId;

    @Column
    private String saleAttrName;

    @Transient
    private List<PmsProductSaleAttrValue> saleAttrValues;
}
