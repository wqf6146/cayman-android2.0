package com.spark.szhb_master.activity.main.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.Treaget;
import com.spark.szhb_master.entity.TreasureInfo;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/9/13 0013.
 */

public class TreasurePresenterImpl implements TreasureContract.TreasurePresenter {

    private final DataSource dataRepository;
    private final TreasureContract.View view;

    public TreasurePresenterImpl(DataSource dataRepository, TreasureContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        view.setPresenter(this);
    }

    @Override
    public void getRewardList() {
        dataRepository.doStringPost(UrlFactory.getRewardList(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Treaget> treaget = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Treaget>>() {
                        }.getType());
                        view.getRewardListSuccess(treaget);
                    } else {
                        view.doFiled(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doFiled(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doFiled(code, toastMessage);
            }
        });
    }

    @Override
    public void getTreaInfo() {
        dataRepository.doStringPost(UrlFactory.getTreasureinfo(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        TreasureInfo treasureInfo = new Gson().fromJson(object.getJSONObject("data").toString(), new TypeToken<TreasureInfo>() {
                        }.getType());
                        view.getTreaInfoSuccess(treasureInfo);
                    } else {
                        view.doFiled(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doFiled(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doFiled(code, toastMessage);
            }
        });
    }
}
