package com.spark.szhb_master.activity.Treasure;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.TreaDetail;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface TreaDetailContract {

    interface View extends Contract.BaseView<Presenter> {

        void  getVoteDetailSuccess(List<TreaDetail>list);

        void getCheckVoteSuccess(int min,int masx);

        void getVoteSuccess(int obj);

        void geCheckVoteFailed(Integer code, String toastMessage);

        void doFiled(Integer code, String toastMessage);

        void safeSettingSuccess(SafeSetting obj);
    }

    interface Presenter extends Contract.BasePresenter {

        void getVoteDetail(HashMap<String, String> params);
        void getCheckVote(HashMap<String, String> map);

        //投票
        void getVote(HashMap<String, String> map);

        void safeSetting();

    }
}
