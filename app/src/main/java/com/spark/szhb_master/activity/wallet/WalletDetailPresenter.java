package com.spark.szhb_master.activity.wallet;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.WalletDetail;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class WalletDetailPresenter implements WalletDetailContract.Presenter {
    private final DataSource dataRepository;
    private final WalletDetailContract.View view;

    public WalletDetailPresenter(DataSource dataRepository, WalletDetailContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void allTransaction(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getAllTransactionUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    List<WalletDetail> objs = new Gson().fromJson(object.getJSONArray("content").toString(), new TypeToken<List<WalletDetail>>() {
                    }.getType());
                    view.allTransactionSuccess(objs);
                } catch (JSONException e) {
                    view.allTransactionFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.allTransactionFail(code, toastMessage);

            }
        });
    }

}
