package com.spark.szhb_master.activity.my_match;



import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.MatchHis;

import java.util.List;

/**
 * Created by Administrator on 2018/7/11 0011.
 */

public class MatchHisPresent implements MyMatchHisContract.Present {

    private final DataSource dataRepository;
    private final MyMatchHisContract.View view;

    public MatchHisPresent(DataSource dataRepository, MyMatchHisContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void myMatchHis(String token,String dataRange,int pageNo,int pageSize,int type) {
        view.displayLoadingPopup();
        dataRepository.myMatchHis(token,dataRange,pageNo,pageSize,type, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.myMatchHisSuccess((List<MatchHis>)obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.myMatchHisFail(code, toastMessage);

            }
        });
    }
}
