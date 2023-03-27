package cn.zj.chatbot.api.domain.ai.service;

import cn.zj.chatbot.api.domain.ai.IOpenAI;
import cn.zj.chatbot.api.domain.ai.model.aggregates.AIAnswer;
import cn.zj.chatbot.api.domain.ai.model.vo.Choices;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class OpenAI implements IOpenAI {

    private Logger logger = LoggerFactory.getLogger(OpenAI.class);

    @Value("${chatbot-api.openAIkey}")
    private String openAIkey;

    @Override
    public String doChatGPT(String questions) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 代理地址；open.aiproxy.xyz、open2.aiproxy.xyz

        HttpPost post = new HttpPost("https://open2.aiproxy.xyz/v1/completions");

//        post.addHeader("user-agent", userAgent);
//        post.addHeader("Cookie", cookie);
//        post.addHeader("Connection", "keep-alive");
//        post.addHeader("date",);
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer "+openAIkey);

        String paramJson = "{\"model\": \"text-davinci-003\", \"prompt\": " +
                questions +
                "\"temperature\": 0, \"max_tokens\": 1024}";

        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity());
            AIAnswer aiAnswer = JSON.parseObject(jsonStr, AIAnswer.class);
            StringBuilder answers = new StringBuilder();
            List<Choices> choices =  aiAnswer.getChoices();
            for(Choices choice: choices){
                answers.append(choice.getText());
            }
            return answers.toString();
        } else {
            throw new RuntimeException("api.openai.com Err Code is " + response.getStatusLine().getStatusCode());
        }
    }
}
