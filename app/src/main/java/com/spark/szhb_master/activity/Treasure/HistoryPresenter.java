package com.spark.szhb_master.activity.Treasure;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.History;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class HistoryPresenter implements HistoryContract.Presenter {


    private final DataSource dataRepository;
    private final HistoryContract.View view;

    public HistoryPresenter(DataSource dataRepository,HistoryContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getVoteHistory(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getVoteHistory(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {


                        List<History>list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<History>>() {
                        }.getType());
                        view.getVoteHistorySuccess(list);
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
    public void getUserHistory(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getUserHistory(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {

                        List<History>list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<History>>() {
                        }.getType());
                        view.getUserHistorySuccess(list);
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
    public void getJoinHistory(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getJoinHistory(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<History>list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<History>>() {
                        }.getType());
                        view.getJoinHistorySuccess(list);
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
