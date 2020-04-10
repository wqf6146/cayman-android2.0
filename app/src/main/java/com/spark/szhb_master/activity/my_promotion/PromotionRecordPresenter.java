package com.spark.szhb_master.activity.my_promotion;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.Promotion;
import com.spark.szhb_master.entity.PromotionRecord;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionRecordPresenter implements PromotionRecordContract.Presenter {

    private final DataSource dataRepository;
    private final PromotionRecordContract.View view;

    public PromotionRecordPresenter(DataSource dataRepository, PromotionRecordContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
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
                        Promotion coins = new Gson().fromJson(object.toString(), new TypeToken<Promotion>() {
                        }.getType());
//                        String  totalElements = (object.getJSONObject("data").getString("totalElements").toString());
//                        List<PromotionReward> objs = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<PromotionReward>>() {
//                        }.getType());
//
//                        Gson gson=new Gson();
//                        Type type=new TypeToken<Map<String,String>>(){}.getType();
//                        Map<String,String> map=gson.fromJson(object.getJSONObject("data").getJSONObject("reward").toString(),type);
//                        for (Map.Entry<String,String> entry : map.entrySet()){
//                            Log.e("TAG", entry.getKey()+"|"+entry.getValue());
//                        }
                        String wcnm = object.getJSONObject("data").getJSONObject("reward").toString();

                        view.getPromotionRewardSuccess(coins,wcnm);
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

}
