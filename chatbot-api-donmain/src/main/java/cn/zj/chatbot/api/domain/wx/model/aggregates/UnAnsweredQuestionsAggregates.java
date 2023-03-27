package cn.zj.chatbot.api.domain.wx.model.aggregates;

import cn.zj.chatbot.api.domain.wx.model.res.ResData;
import cn.zj.chatbot.api.domain.wx.model.vo.Topics;

public class UnAnsweredQuestionsAggregates {

    private boolean succeeded;

    private ResData res_data;

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public ResData getRes_data() {
        return res_data;
    }

    public void setRes_data(ResData res_data) {
        this.res_data = res_data;
    }
}
