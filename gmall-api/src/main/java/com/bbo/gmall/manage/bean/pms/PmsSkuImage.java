package com.bbo.gmall.manage.bean.pms;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class PmsSkuImage implements Serializable {


    private static final long serialVersionUID = 8433649277504486581L;
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    private String skuId;

    @Column
    private String imgName;

    @Column
    private String imgUrl;

    @Column
    private String productImgId;

    @Column
    private String isDefault;

}

