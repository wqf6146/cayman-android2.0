package com.spark.szhb_master.activity.chat;

import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.ChatEntity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public interface ChatContact {
    interface View extends Contract.BaseView<Presenter> {
        void getHistoryMessageSuccess(List<ChatEntity> entityList);

        void getHistoryMessageFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {
        void getHistoryMessage(HashMap<String, String> params);
    }
}
