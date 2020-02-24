package com.bbo.gmall.manage.service.pms;

import com.bbo.gmall.manage.bean.pms.PmsSearchParam;
import com.bbo.gmall.manage.bean.pms.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {

    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
