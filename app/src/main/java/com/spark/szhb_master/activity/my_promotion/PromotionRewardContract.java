package com.spark.szhb_master.activity.my_promotion;

import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.Promotion;
import com.spark.szhb_master.entity.PromotionRecord;
import com.spark.szhb_master.entity.PromotionReward;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public interface PromotionRewardContract {

    interface View extends Contract.BaseView<Presenter> {

        void getPromotionFail(Integer code, String toastMessage);

        void getPromotionSuccess(List<PromotionRecord> obj);

        void getPromotionRewardFail(Integer code, String toastMessage);

        void getPromotionRewardSuccess(List<PromotionReward> obj,Promotion objs, String wcnm);

    }


    interface Presenter extends Contract.BasePresenter {

        void getPromotionReward();

        void getPromotion();

    }

}
