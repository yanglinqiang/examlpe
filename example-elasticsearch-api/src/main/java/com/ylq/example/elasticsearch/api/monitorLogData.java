package com.ylq.example.elasticsearch.api;

import com.zhe800.elasticsearch.drive.ESClient;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class description goes here
 * Created by ylq on 16/3/10 上午10:29.
 */
public class monitorLogData {


    public monitorLogData() {

        String clustName = "eagleye_es";
        String hosts = "172.16.10.18:9308,172.16.10.19:9308,172.16.10.20:9308";
        ESClient esClient = new ESClient(clustName, hosts);


        Date end = new Date();

        SearchRequestBuilder searchRequestBuilder = esClient.getTransportClient()
                .prepareSearch("eagleye_bigindex_2016.03.09_long")
                .setTypes("monitorLog")
                .setQuery(getBoolQueryBuilder())
                .addAggregation(getTermBuilder());
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        responseToList(searchResponse, end);

        System.out.println("calculateGrayList查询耗时:" + (new Date().getTime() - end.getTime()) + "ms");
    }

    private BoolQueryBuilder getBoolQueryBuilder() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("type", "EAGLEYE_MONITOR_LOG_LOOM_CONSUMER_INVOKE_TIME"));
        return boolQueryBuilder;
    }

    private AggregationBuilder getTermBuilder() {
        AggregationBuilder aggregationBuilder = AggregationBuilders
                .terms("app")
                .field("app")
                .subAggregation(AggregationBuilders
                                .terms("ip")
                                .field("ip")
                                .subAggregation(AggregationBuilders.
                                                dateHistogram("mtime")
                                                .interval(DateHistogramInterval.MINUTE)
                                                .field("mtime")
                                                .subAggregation(AggregationBuilders.sum("tcount").field("tcount"))
                                )
                );
        return aggregationBuilder;
    }

    private void responseToList(SearchResponse searchResponse, Date end) {
        SimpleDateFormat simpleDateFormat=new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Terms terms1 = searchResponse.getAggregations().get("app");
        for (Terms.Bucket term1 : terms1.getBuckets()) {
            Terms terms2 = term1.getAggregations().get("ip");

            for (Terms.Bucket term2 : terms2.getBuckets()) {
                InternalHistogram dateHistogram = term2.getAggregations().get("mtime");

                for (Object bucket : dateHistogram.getBuckets()) {
                    InternalHistogram.Bucket bucketMinute = (InternalHistogram.Bucket) bucket;

                    // 这个日期是标准时间的字符串
                    Date date = new Date(Long.parseLong(bucketMinute.getKeyAsString()));
                    // 提取统计信息
                    Sum sum = bucketMinute.getAggregations().get("tcount");
                    if (sum.getValue() > 2000) {
                        System.out.println(term1.getKey() + "\t" + term2.getKey() + "\t" + simpleDateFormat.format(date) + "\t" + sum.getValueAsString());
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        monitorLogData monitorLogData = new monitorLogData();
    }
}
