package api;

import com.alibaba.fastjson.JSON;
import domain.FileObj;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import javax.swing.text.Highlighter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ElasticOperation {

    private RestHighLevelClient client;

    /**
     * 有三种类型
     * 1.文件的名字
     * 2.文件type
     * 3.文件的data 64编码
     */
    public void upload(FileObj file) throws IOException {
        IndexRequest indexRequest = new IndexRequest("fileindex");
        //indexRequest.id(file.getId());

        //上传同时，使用attachment pipline进行提取文件
        indexRequest.source(JSON.toJSONString(file), XContentType.JSON);
        indexRequest.setPipeline("attatchment");
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

        System.out.println(indexResponse);
    }

    /**
     * 根据关键词，搜索对应的文件信息
     * 查询文件中的文本内容
     * @param keyword
     * @throws IOException
     */
    public void search(String keyword) throws IOException {
        SearchRequest searchRequest = new SearchRequest("fileindex");

        //默认会search出所有的东西来
        //SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        //使用lk分词器查询，会把插入的字段分词，然后进行处理
        SearchSourceBuilder srb = new SearchSourceBuilder();
        srb.query(QueryBuilders.matchQuery("attachment.content", keyword).analyzer("ik_smart"));

        //设置highlighting
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightContent = new HighlightBuilder.Field("attachment.content");
        highlightContent.highlighterType();
        highlightBuilder.field(highlightContent);
        highlightBuilder.preTags("<em>");
        highlightBuilder.postTags("</em>");

        //highlighting会自动返回匹配到的文本，所以就不需要再次返回文本了
        String[] includeFields = new String[]{"name"};
        String[] excludeFields = new String[]{"attachment.content"};
        srb.fetchSource(includeFields, excludeFields);

        //把刚才设置的值导入进去
        srb.highlighter(highlightBuilder);
        searchRequest.source(srb);
        SearchResponse res = client.search(searchRequest, RequestOptions.DEFAULT);

        //获取hits，这样就可以获取查询到的记录了
        SearchHits hits = res.getHits();

        //hits是一个迭代器，所以需要迭代返回每一个hits
        Iterator<SearchHit> iterator = hits.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            SearchHit hit = iterator.next();

            //获取返回的字段
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap.get("name"));

            //统计找到了几条
            count++;

            //这个就会把匹配到的文本返回，而且只返回匹配到的部分文本
            //Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            //System.out.println(highlightFields);

            //Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //System.out.println(sourceAsMap);
        }


        System.out.println("查询到" + count + "条记录");

    }


    public void setClient(RestHighLevelClient client) {
        this.client = client;
    }

    public RestHighLevelClient getClient() {
        return client;
    }


}
