package com.spark.szhb_master.activity.main.presenter;

import com.spark.szhb_master.activity.main.MainContract;
import com.spark.szhb_master.data.DataSource;

/**
 * Created by Administrator on 2018/2/24.
 */

public class HeyuePresenterImpl implements MainContract.HeyuePresenter {
    private MainContract.HeyueView view;
    private DataSource dataRepository;

    public HeyuePresenterImpl(DataSource dataRepository, MainContract.HeyueView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }


}
