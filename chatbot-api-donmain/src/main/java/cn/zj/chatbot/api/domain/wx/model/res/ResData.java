package cn.zj.chatbot.api.domain.wx.model.res;

import cn.zj.chatbot.api.domain.wx.model.vo.Topics;

import java.util.List;

/**
 * 结果数据
 */
public class ResData {

    private List<Topics> topics;

    public List<Topics> getTopics() {
        return topics;
    }

    public void setTopics(List<Topics> topics) {
        this.topics = topics;
    }
}
