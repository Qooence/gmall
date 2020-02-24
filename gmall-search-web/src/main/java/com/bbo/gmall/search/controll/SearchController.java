package com.bbo.gmall.search.controll;

import com.bbo.gmall.manage.bean.pms.PmsSearchParam;
import com.bbo.gmall.manage.bean.pms.PmsSearchSkuInfo;
import com.bbo.gmall.manage.service.pms.SearchService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class SearchController {

    @Reference
    SearchService searchService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap){ //三级分类、关键字

        // 调用搜索服务，返回搜索结果
        List<PmsSearchSkuInfo> pmsSearchSkuInfos =  searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList",pmsSearchSkuInfos);

        return "list";
    }
}
