package com.bbo.gmall.manage.bean.pms;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public class PmsBaseCatalog3 implements Serializable {

    private static final long serialVersionUID = 9042448639938811023L;
    @Id
    private String id;
    private String name;
    private String catalog2Id;

}
