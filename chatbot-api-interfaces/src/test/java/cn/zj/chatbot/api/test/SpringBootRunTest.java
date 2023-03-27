package cn.zj.chatbot.api.test;


import cn.zj.chatbot.api.domain.ai.IOpenAI;
import cn.zj.chatbot.api.domain.wx.IWxApi;
import cn.zj.chatbot.api.domain.wx.model.aggregates.UnAnsweredQuestionsAggregates;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRunTest {
    private Logger logger = LoggerFactory.getLogger(SpringBootRunTest.class);

    @Value("${chatbot-api.cookie}")
    private String  cookie;

    @Value("${chatbot-api.user-agent}")
    private String userAgent;

    @Resource
    private IWxApi wxApi;

    @Resource
    private IOpenAI openAI;

    @Test
    public void test_wxApi() throws IOException {
        UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = wxApi.queryUnAnsweredQuestions(cookie);

        logger.info("测试结果: {}", JSON.toJSONString(unAnsweredQuestionsAggregates));
    }


    @Test
    public void test_chatGPT() throws IOException{

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 代理地址；open.aiproxy.xyz、open2.aiproxy.xyz

        HttpPost post = new HttpPost("https://open2.aiproxy.xyz/v1/completions");

        post.addHeader("user-agent", userAgent);
//        post.addHeader("Cookie", cookie);
//        post.addHeader("Connection", "keep-alive");
//        post.addHeader("date",);
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer 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");

        String paramJson = "{\"model\": \"text-davinci-003\", \"prompt\": \" Say this is a test \", \"temperature\": 0, \"max_tokens\": 1024}";

        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        } else {
            throw new RuntimeException("api.openai.com Err Code is " + response.getStatusLine().getStatusCode());
        }
    }


    @Test
    public void test_openAi() throws IOException {
        String s = openAI.doChatGPT("帮我写一个冒泡排序");

        logger.info("测试结果", s);
    }

}
