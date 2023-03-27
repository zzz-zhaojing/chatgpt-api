package cn.zj.chatbot.api.domain.ai;

import java.io.IOException;

/**
 * ChatGPT Open AI接口
 *
 */
public interface IOpenAI {
    String doChatGPT(String questions) throws IOException;
}
