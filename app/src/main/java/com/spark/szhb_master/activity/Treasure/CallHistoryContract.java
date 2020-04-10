package com.spark.szhb_master.activity.Treasure;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.CallHistory;
import com.spark.szhb_master.entity.TreasureInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface CallHistoryContract {

    interface View extends Contract.BaseView<Presenter> {

        void  getVoteHistorySuccess(List<CallHistory> list);

        void getTreaInfoSuccess(TreasureInfo treasureInfo);

        void getSigninSuccess();

        void getPowerRecordSuccess(List<WaterBallBean>list);

        void powerCollectSuccess();

        void getSigninFiled(Integer code, String toastMessage);




        void doFiled(Integer code, String toastMessage);



    }

    interface Presenter extends Contract.BasePresenter {

        void getVoteHistory(HashMap<String, String> params);

        void getTreaInfo();

        void getSignin();

        void getPowerRecord();

        void powerCollect(HashMap<String, String> params);


    }
}
