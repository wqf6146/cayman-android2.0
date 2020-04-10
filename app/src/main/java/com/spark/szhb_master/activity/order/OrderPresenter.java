package com.spark.szhb_master.activity.order;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.Order;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class OrderPresenter implements OrderContract.Presenter {
    private final DataSource dataRepository;
    private final OrderContract.View view;

    public OrderPresenter(DataSource dataRepository, OrderContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void myOrder(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getMyOrderUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == 0) {
                        List<Order> objs = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<Order>>() {
                        }.getType());
                        view.myOrderSuccess(objs);
                    } else {
                        view.myOrderFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.myOrderFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.myOrderFail(code, toastMessage);
            }
        });
    }

}
