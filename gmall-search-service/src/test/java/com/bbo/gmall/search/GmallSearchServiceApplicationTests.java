package com.bbo.gmall.search;

import com.bbo.gmall.manage.bean.pms.PmsSearchSkuInfo;
import com.bbo.gmall.manage.bean.pms.PmsSkuInfo;
import com.bbo.gmall.manage.service.pms.SkuInfoService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.dubbo.config.annotation.Reference;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GmallSearchServiceApplicationTests {

    @Autowired
    JestClient jestClient;

    @Reference
    SkuInfoService skuInfoService; // 查询Mysql

    @Test
    public void contextLoads() throws IOException {

        // jest的dsl工具
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();


        // filter
        // term
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId","43");
        boolQueryBuilder.filter(termQueryBuilder);

        //term
//        TermQueryBuilder termQueryBuilder1 = new TermQueryBuilder("","");
//        boolQueryBuilder.filter(termQueryBuilder1);
//
//        //terms
//        TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("","");
//        boolQueryBuilder.filter(termsQueryBuilder);

        // must
        // match
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName","华为");
        boolQueryBuilder.must(matchQueryBuilder);

        // query
        sourceBuilder.query(boolQueryBuilder);


        // form
        sourceBuilder.from(0);
        // size
        sourceBuilder.size(20);
        //highlight
        sourceBuilder.highlighter(null);

        String dsl = sourceBuilder.toString();
        System.out.println(dsl);

        // 用api执行复杂查询
        Search search = new Search.Builder(dsl)
                .addIndex("gmall").addType("PmsSkuInfo").build();

        SearchResult execute = jestClient.execute(search);

        List<SearchResult.Hit<PmsSearchSkuInfo,Void>> hits = execute.getHits(PmsSearchSkuInfo.class);
        List<PmsSearchSkuInfo> searchSkuInfos = new ArrayList<>();
        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo source = hit.source;
            searchSkuInfos.add(source);
        }

        System.out.println(searchSkuInfos.size());
    }

    @Test
    public void put() throws IOException {
        // 查询mysql数据
//        skuInfoService.
        List<PmsSkuInfo> pmsSkuInfoList = skuInfoService.getAllSku();

        // 转化为es数据
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
            pmsSearchSkuInfo.setId(Long.parseLong(pmsSkuInfo.getId()));
            pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
        }

        for (PmsSearchSkuInfo searchSkuInfo : pmsSearchSkuInfoList) {
            // 导入es
            Index put = new Index.Builder(searchSkuInfo).index("gmall").type("PmsSkuInfo")
                    .id(searchSkuInfo.getId() + "").build();
            JestResult jestResult = jestClient.execute(put);
            System.out.println(jestResult);
        }
    }
}
