package com.spark.szhb_master.activity.main.presenter;

import com.spark.szhb_master.activity.main.MainContract;
import com.spark.szhb_master.data.DataSource;

/**
 * Created by Administrator on 2018/2/24.
 */

public class MarketPresenterImpl implements MainContract.MarketPresenter {
    private MainContract.MarketView view;
    private DataSource dataRepository;

    public MarketPresenterImpl(DataSource dataRepository, MainContract.MarketView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }


}
