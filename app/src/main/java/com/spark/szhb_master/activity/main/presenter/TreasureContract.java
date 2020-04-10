package com.spark.szhb_master.activity.main.presenter;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.Treaget;
import com.spark.szhb_master.entity.TreasureInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface TreasureContract {
    interface View extends Contract.BaseView<TreasurePresenter> {

        void getRewardListSuccess(List<Treaget> messages);

        void getTreaInfoSuccess(TreasureInfo treasureInfo);

        void doFiled(Integer code, String toastMessage);
    }

    interface TreasurePresenter extends Contract.BasePresenter {


        void getRewardList();

        void getTreaInfo();

    }

    interface PreheatingView extends Contract.BaseView<TreasureContract.PreheatingPresenter> {

        void getVoteListSuccess(Object obj);
        void getVoteSuccess(int obj);

        void safeSettingSuccess(SafeSetting obj);

        void getCheckVoteSuccess(int min,int masx);

        void geCheckVoteFailed(Integer code, String toastMessage);


        void getCheckJoinSuccess(int min,int masx);

        void getJoinSuccess(Object obj);

        void doFiled(Integer code, String toastMessage);

    }

    interface PreheatingPresenter extends Contract.BasePresenter {

//        void getVoteList();
        //投票
        void getVote(HashMap<String, String> map);

        void getVoteList();
        void getCheckVote(HashMap<String, String> map);


        //挖宝
        void getCheckJoin(HashMap<String, String> map);
        void getJoin(HashMap<String, String> map);

        void safeSetting();

    }

    interface DiggingView extends Contract.BaseView<TreasureContract.DiggingPresenter> {

        void getUserListSuccess(Object obj);

        void getJoinSuccess(Object obj);

        void getCheckJoinSuccess(int min,int masx,int rate);

        void doFiled(Integer code, String toastMessage);
        void safeSettingSuccess(SafeSetting obj);
    }

    //我的挖宝列表
    interface DiggingPresenter extends Contract.BasePresenter {

        void getUserList();
        void getCheckJoin(HashMap<String, String> map);
        void getJoin(HashMap<String, String> map);
        void safeSetting();
    }


    interface MyDiggingView extends Contract.BaseView<TreasureContract.MyDiggingPresenter> {

        void getUserListmySuccess(Object obj);
        void doFiled(Integer code, String toastMessage);
    }

    //我的挖宝列表
    interface MyDiggingPresenter extends Contract.BasePresenter {
        void getUsermyList();
    }


}
