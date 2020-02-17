package com.bbo.gmall.bean.pms;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class PmsProductImage implements Serializable {

    private static final long serialVersionUID = -849401589205233646L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    private String productId;

    @Column
    private String imgName;

    @Column
    private String imgUrl;

}

