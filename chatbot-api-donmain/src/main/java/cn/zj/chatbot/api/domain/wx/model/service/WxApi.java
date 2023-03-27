package cn.zj.chatbot.api.domain.wx.model.service;

import cn.zj.chatbot.api.domain.wx.IWxApi;
import cn.zj.chatbot.api.domain.wx.model.aggregates.UnAnsweredQuestionsAggregates;
import cn.zj.chatbot.api.domain.wx.model.req.AnswerReq;
import cn.zj.chatbot.api.domain.wx.model.req.ReqData;
import cn.zj.chatbot.api.domain.wx.model.res.AnswerRes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WxApi implements IWxApi {

    private Logger logger = LoggerFactory.getLogger(WxApi.class);

    @Override
    public UnAnsweredQuestionsAggregates queryUnAnsweredQuestions(String cookie) throws IOException {
        // 获取未回答的问题逻辑
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet("");

        get.addHeader("cookie", cookie);
        get.addHeader("Content-Type", "application/json;charset=utf8");

        CloseableHttpResponse response = httpClient.execute(get);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity());
            logger.info("拉取提问数据。jsonStr: {}", jsonStr);
            return JSON.parseObject(jsonStr, UnAnsweredQuestionsAggregates.class);
        } else {
            throw new RuntimeException("queryUnAnsweredQuestions Err Code is " + response.getStatusLine().getStatusCode());
        }
    }

    @Override
    public boolean answer(String cookie, String text) throws IOException {
        // 回答问题逻辑

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost("");
        post.addHeader("cookie",cookie);
        post.addHeader("Content-Type","");
        post.addHeader("user-agent","application/json;charset=utf8");


        AnswerReq answerReq = new AnswerReq(new ReqData(text));
        String paramJson = JSONObject.toJSONString(answerReq);

        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity());
            logger.info("回答微信问题结果。jsonStr:{}", jsonStr);
            AnswerRes answerRes = JSON.parseObject(jsonStr, AnswerRes.class);
            return answerRes.isSucceeded();
        } else {
            throw new RuntimeException("answer Err Code is" + response.getStatusLine().getStatusCode());
        }


    }
}
