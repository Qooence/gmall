package com.bbo.gmall.bean.pms;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class PmsBaseSaleAttr implements Serializable {

    private static final long serialVersionUID = -2844672866708565606L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private String id;
    @Column
    private String name;
}
