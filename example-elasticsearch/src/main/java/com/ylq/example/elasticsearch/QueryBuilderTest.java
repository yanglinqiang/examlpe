package com.ylq.example.elasticsearch;

import com.zhe800.elasticsearch.drive.ESClient;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * 此类测试QueryBuilder的should方法
 * Created by ylq on 16/3/17 下午4:52.
 */
public class QueryBuilderTest {

    public QueryBuilderTest() {
        String clustName = "eagleye_es";
        String hosts = "192.168.10.235:9300,192.168.10.236:9300,192.168.10.237:9300";
        ESClient esClient = new ESClient(clustName, hosts);
        SearchRequestBuilder searchRequestBuilder = esClient.getTransportClient().prepareSearch("packetbeat_2016.03.11")
                .setTypes("mysql")
                .setQuery(getErrorQueryBuilder());
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        System.out.println(searchResponse.getHits().totalHits());
    }

    private QueryBuilder getErrorQueryBuilder() {
        QueryBuilder termQueryBuilder =
                QueryBuilders.boolQuery()
                        .mustNot(QueryBuilders.termQuery("bytes_in", "21")
                        );
        return QueryBuilders
                .boolQuery()
                .should(termQueryBuilder)
                .should(QueryBuilders.termQuery("bytes_out", 11));
    }

    public static void main(String[] args) {
        new QueryBuilderTest();
    }

}