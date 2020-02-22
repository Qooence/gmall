package com.bbo.gmall.manage.bean.pms;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class PmsSkuAttrValue implements Serializable {

    private static final long serialVersionUID = 4439212918533971148L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id;

    @Column
    private String attrId;

    @Column
    private String valueId;

    @Column
    private String skuId;
}
