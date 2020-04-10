package com.spark.szhb_master.activity.setting;

import com.spark.szhb_master.data.DataSource;

/**
 * Created by Administrator on 2018/4/24 0024.
 */

public class SettingPresent implements SettingContact.Presenter {

    private final DataSource dataRepository;
    private final SettingContact.View view;

    public SettingPresent(DataSource dataRepository, SettingContact.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

}
