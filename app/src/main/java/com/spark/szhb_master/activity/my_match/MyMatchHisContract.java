package com.spark.szhb_master.activity.my_match;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.MatchHis;

import java.util.List;

/**
 * Created by Administrator on 2018/4/18 0018.
 */

public interface MyMatchHisContract {

    interface View extends Contract.BaseView<Present>{
        void myMatchHisFail(Integer code, String toastMessage);

        void myMatchHisSuccess(List<MatchHis> obj);
    }

    interface Present extends Contract.BasePresenter{
        void myMatchHis(String token, String dataRange, int pageNo, int pageSize, int type);
    }
}
