package com.nowcoder.wenda.service;

import com.nowcoder.wenda.model.Question;
import org.apache.ibatis.annotations.Update;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jhc on 2019/5/7
 */
@Service
public class SearchService {
   private static final String URL = "http://localhost:8983/solr/wenda1/";
   private HttpSolrClient client = new HttpSolrClient.Builder(URL).build();



    private static final String QUESTION_TITLE_FIELD = "questionTitle";
    private static final String QUESTION_CONTENT_FIELD = "questionContent";

    /**
     * 搜索问题的时候
     * 首先是创建了一个solr的查询语句
     * 通过配置里面的内容，比如前缀和后缀分别加什么内容，以及查询的总数和偏移量
     * 得到的内容以后，解析里面的字段，
     * 通过关键字搜索内容
     * @param keyword
     * @param offset
     * @param count
     * @param hlPre
     * @param hlAfter
     * @return
     * @throws Exception
     */
    public List<Question> searchQuestion(String keyword,int offset,int count,String hlPre,String hlAfter)throws  Exception{
        List<Question> questions = new ArrayList<Question>();
        SolrQuery query = new SolrQuery();
        query.setQuery(keyword);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.set("df",QUESTION_CONTENT_FIELD);
        query.setHighlightSimplePre(hlPre);
        query.setHighlightSimplePost(hlAfter);
        query.set("hl.fl",QUESTION_TITLE_FIELD+","+QUESTION_CONTENT_FIELD);
        QueryResponse response = client.query(query);

        //返回的数据结构为 （1，{title,""
        // content,""}）
        for(Map.Entry<String,Map<String,List<String>>>entry:response.getHighlighting().entrySet()){
            Question q = new Question();
            q.setId(Integer.parseInt(entry.getKey()));
            if(entry.getValue().containsKey(QUESTION_TITLE_FIELD)){
                List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
                if(titleList.size() > 0 ){
                    q.setTitle(titleList.get(0));
                }
            }
            if(entry.getValue().containsKey(QUESTION_CONTENT_FIELD)){
                List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if(contentList.size() > 0){
                    q.setContent(contentList.get(0));
                }
            }
            questions.add(q);
        }
        System.out.println("quesiton size"+questions.size());
        return questions;
    }
//public List<Question> searchQuestion(String keyword, int offset, int count,
//                                     String hlPre, String hlPos) throws Exception {
//    List<Question> questionList = new ArrayList<>();
//    SolrQuery query = new SolrQuery(keyword);
//    query.setRows(count);
//    query.setStart(offset);
//    query.setHighlight(true);
//    query.setHighlightSimplePre(hlPre);
//    query.setHighlightSimplePost(hlPos);
//    query.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);
//
//    QueryResponse response = client.query(query);
//    for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
//        Question q = new Question();
//        q.setId(Integer.parseInt(entry.getKey()));
//        if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
//            List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
//            if (contentList.size() > 0) {
//                q.setContent(contentList.get(0));
//            }
//        }
//        if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
//            List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
//            if (titleList.size() > 0) {
//                q.setTitle(titleList.get(0));
//            }
//        }
//        questionList.add(q);
//    }
//    return questionList;
//}

    /**
     * 再添加问题的时候增加索引
     * 通过doc去提交到solr的数据里面去
     * @param qid
     * @param title
     * @param content
     * @return
     * @throws Exception
     */
    public boolean indexQuestion(int qid,String title,String content)throws  Exception{
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id",qid);
        doc.setField(QUESTION_TITLE_FIELD,title);
        doc.setField(QUESTION_CONTENT_FIELD,content);
        UpdateResponse response = client.add(doc,1000);
        return response!=null && response.getStatus() == 0;
    }
}
