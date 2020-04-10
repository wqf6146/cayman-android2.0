package com.spark.szhb_master.activity.Treasure;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.History;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface HistoryContract {

    interface View extends Contract.BaseView<Presenter> {

        void  getVoteHistorySuccess(List<History> list);

        void  getJoinHistorySuccess(List<History> list);

        void  getUserHistorySuccess(List<History> list);

        void doFiled(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void getVoteHistory(HashMap<String, String> params);

        void getUserHistory(HashMap<String, String> params);

        void getJoinHistory(HashMap<String, String> params);
    }
}
