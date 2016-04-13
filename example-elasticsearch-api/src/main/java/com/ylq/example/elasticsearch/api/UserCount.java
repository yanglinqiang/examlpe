package com.ylq.example.elasticsearch.api;

import com.zhe800.trade.common.elasticsearch.ESClient;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.Date;

/**
 * Class description goes here
 * Created by ylq on 16/1/9 下午1:29.
 */
public class UserCount {


    private ESClient esClient = new ESClient("eagleye_es", "172.16.10.18:9301,172.16.10.19:9301,172.16.10.20:9301,172.16.10.18:9302,172.16.10.19:9302,172.16.10.20:9302,172.16.10.18:9303,172.16.10.19:9303,172.16.10.20:9303");

    public void calculateGrayList() {
        Date end = new Date();
//        Date start = new Date(end.getTime() - 300 * 1000);
//        DateFormat dateFormatIndex = new SimpleDateFormat("yyyyMMdd");
//
//        Set<String> indexes = new HashSet<String>();
//        indexes.add("shield_index_" + dateFormatIndex.format(start));
//        indexes.add("shield_index_" + dateFormatIndex.format(end));

        SearchRequestBuilder searchRequestBuilder = esClient.getTransportClient()
                .prepareSearch("shield_index_20160109")
                .setTypes("passport")
                .setQuery(getBoolQueryBuilder())
                .addAggregation(getTermBuilder());
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        responseToList(searchResponse, end);

        System.out.println("calculateGrayList查询耗时:" + (new Date().getTime() - end.getTime()) + "ms");


    }


    private BoolQueryBuilder getBoolQueryBuilder() {
        Integer[] userIds = new Integer[]{2448176, 2111184, 63363693, 8181897, 4143157, 23882999, 5295273, 50264971, 46915043, 34972840, 2790094, 60307054, 40885019, 1005243, 19119686, 81344135, 51651101, 82216339, 3394258, 5253166, 60152511, 9307428, 4420188, 6931393, 51019379, 46915043, 64563474, 5002810, 50901170, 9153940, 2694809, 2769139, 7740044, 49786693, 4779548, 753459, 58985284, 4396623, 3140150, 49927533, 53834603, 46915043, 1739409, 49119461, 77720013, 46009103, 78613576, 8523889, 48293562, 6213217, 606510, 2955123, 74296246, 51754441, 49043089, 63525546, 7570565, 40863508, 41178707, 4028937, 26262138, 4843501, 16562394, 74216791, 60020710, 45244532, 51705557, 80648504, 1726188, 8050234, 45288431, 54662731, 20620952, 5019035, 5493326, 2873606, 7720058, 51549263, 82980051, 32648776, 52954497, 82854185, 39660273, 14383425, 41306425, 81669764, 46368500};
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("is_suc", 1))
//                .must(QueryBuilders.rangeQuery("time").from(start.getTime()))
                .must(QueryBuilders.termsQuery("u_id", userIds));
        return boolQueryBuilder;
    }

    private AggregationBuilder getTermBuilder() {
        AggregationBuilder aggregationBuilder = AggregationBuilders
                .terms("u_id_name")
                .field("u_id")
                .subAggregation(AggregationBuilders
                                .terms("ip_name")
                                .field("ip")
                );
        return aggregationBuilder;
    }

    private void responseToList(SearchResponse searchResponse, Date end) {
        Terms terms1 = searchResponse.getAggregations().get("u_id_name");
        for (Terms.Bucket term1 : terms1.getBuckets()) {
            Terms terms2 = term1.getAggregations().get("ip_name");

            for (Terms.Bucket term2 : terms2.getBuckets()) {
                Long num = term2.getDocCount();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(term1.getKey()).append("\t\t");
                stringBuilder.append(term2.getKey()).append("\t\t");
                stringBuilder.append(num).append("\t\t");
                System.out.println(stringBuilder);
            }
        }
    }

    public static void main(String[] args) {
        UserCount userCount = new UserCount();
        userCount.calculateGrayList();
    }
}
