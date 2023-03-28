package cn.zj.chatbot.api.application.job;

import cn.zj.chatbot.api.domain.ai.IOpenAI;
import cn.zj.chatbot.api.domain.wx.IWxApi;
import cn.zj.chatbot.api.domain.wx.model.aggregates.UnAnsweredQuestionsAggregates;
import cn.zj.chatbot.api.domain.wx.model.vo.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.util.List;

/**
 * 问答任务
 */
@EnableScheduling
@Configuration
public class ChatBot implements Runnable{
    private Logger logger = LoggerFactory.getLogger(ChatBot.class);

    private String  cookie;

    private String userAgent;

    private String openAIkey;

    @Resource
    private IWxApi wxApi;

    @Resource
    private IOpenAI openAI;

    public ChatBot(String cookie, String openAIkey, IWxApi wxApi, IOpenAI openAI) {
        this.cookie = cookie;
        this.openAIkey = openAIkey;
        this.wxApi = wxApi;
        this.openAI = openAI;
    }

    public void run(){
        try {
            //1.检索问题
            UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = wxApi.queryUnAnsweredQuestions(cookie);
            List<Topics>  topics = unAnsweredQuestionsAggregates.getRes_data().getTopics();

            //2.AI回答
            if(null == topics || topics.isEmpty()){
                return;
            }
            String answer = openAI.doChatGPT(topics.get(0).getQuestion().getText().trim());
            //3.问题回复
            wxApi.answer(cookie, "");
        }catch (Exception e){
            logger.error("{} 自动回答问题异常", e);
        }
    }
}
