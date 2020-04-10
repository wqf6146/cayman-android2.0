package com.spark.szhb_master.activity.entrust;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.EntrustHistory;
import com.spark.szhb_master.entity.MarketSymbol;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * author: wuzongjie
 * time  : 2018/4/18 0018 15:26
 * desc  :
 */

public class AllTrustImpl implements IAllTrustContract.Presenter {
    private IAllTrustContract.View view;
    private final DataSource dataRepository;

    public AllTrustImpl(DataSource dataRepository, IAllTrustContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    /**
     * 获取历史的订单
     */
    @Override
    public void getAllEntrust(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getHistoryEntrus(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    List<EntrustHistory> objs = new Gson().fromJson(object.getJSONArray("content").toString(), new TypeToken<List<EntrustHistory>>() {
                    }.getType());
                    view.getAllSuccess(objs);
                } catch (JSONException e) {
                    view.errorMes(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.errorMes(code, toastMessage);
            }
        });
    }


    @Override
    public void getSymbol() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getSymbolUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length() != 0) {
                        List<MarketSymbol> objs = new Gson().fromJson(array.toString(), new TypeToken<List<MarketSymbol>>() {
                        }.getType());
                        view.getSymbolSucccess(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(code, toastMessage);
            }
        });
    }
}
