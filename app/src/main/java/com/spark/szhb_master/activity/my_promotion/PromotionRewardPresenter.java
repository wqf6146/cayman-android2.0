package com.spark.szhb_master.activity.my_promotion;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.Promotion;
import com.spark.szhb_master.entity.PromotionRecord;
import com.spark.szhb_master.entity.PromotionReward;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionRewardPresenter implements PromotionRewardContract.Presenter {

    private final DataSource dataRepository;
    private final PromotionRewardContract.View view;

    public PromotionRewardPresenter(DataSource dataRepository, PromotionRewardContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getPromotionReward() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getPromotionRewardUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<PromotionReward> objs = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<PromotionReward>>() {
                        }.getType());
//                        view.getPromotionRewardSuccess(objs);

                        Promotion coins = new Gson().fromJson(object.toString(), new TypeToken<Promotion>() {
                        }.getType());
                        String wcnm;
                        if ( object.getJSONObject("data").getJSONArray("content").toString().length() < 5){
                            wcnm = "";
                        }else {
                            wcnm= object.getJSONObject("data").getJSONObject("reward").toString();
                        }
                        view.getPromotionRewardSuccess(objs,coins,wcnm);

                    } else {
                        view.getPromotionRewardFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.getPromotionRewardFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getPromotionRewardFail(code, toastMessage);
            }
        });
    }

    @Override
    public void getPromotion() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getPromotionUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<PromotionRecord> objs = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<PromotionRecord>>() {
                        }.getType());
                        Promotion coins = new Gson().fromJson(object.toString(), new TypeToken<Promotion>() {
                        }.getType());
                        view.getPromotionSuccess(objs);
                    } else {
                        view.getPromotionFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.getPromotionFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getPromotionFail(code, toastMessage);
            }
        });
    }




}
