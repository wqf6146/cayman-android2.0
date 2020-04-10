package com.spark.szhb_master.activity.my_promotion;

import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.Promotion;
import com.spark.szhb_master.entity.PromotionRecord;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public interface PromotionRecordContract {

    interface View extends Contract.BaseView<Presenter> {
        void getPromotionFail(Integer code, String toastMessage);

        void getPromotionSuccess(List<PromotionRecord> obj);


        void getPromotionRewardSuccess(Promotion obj,String wcnm);

        void getPromotionRewardFail(Integer code, String toastMessage);
    }


    interface Presenter extends Contract.BasePresenter {
        void getPromotion();

        void getPromotionReward();
    }

}
