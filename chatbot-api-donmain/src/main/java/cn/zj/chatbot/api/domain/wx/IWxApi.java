package cn.zj.chatbot.api.domain.wx;

import cn.zj.chatbot.api.domain.wx.model.aggregates.UnAnsweredQuestionsAggregates;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * 微信api接口
 */
public interface IWxApi {
    /**
     * 查询未回答的问题
     * @throws IOException
     */
    UnAnsweredQuestionsAggregates queryUnAnsweredQuestions(String cookie) throws IOException;

    boolean answer(String cookie,String text) throws  IOException;
}
