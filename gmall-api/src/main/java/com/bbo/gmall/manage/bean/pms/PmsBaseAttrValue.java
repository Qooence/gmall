package com.bbo.gmall.manage.bean.pms;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class PmsBaseAttrValue implements Serializable {
    private static final long serialVersionUID = -6397918056732413661L;
    @Id
    @Column
    private String id;
    @Column
    private String valueName;
    @Column
    private String attrId;
    @Column
    private String isEnabled;

    @Transient
    private String urlParam;

}
