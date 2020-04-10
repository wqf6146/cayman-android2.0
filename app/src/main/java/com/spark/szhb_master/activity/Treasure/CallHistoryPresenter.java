package com.spark.szhb_master.activity.Treasure;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.CallHistory;
import com.spark.szhb_master.entity.TreasureInfo;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class CallHistoryPresenter implements CallHistoryContract.Presenter {


    private final DataSource dataRepository;
    private final CallHistoryContract.View view;

    public CallHistoryPresenter(DataSource dataRepository, CallHistoryContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getVoteHistory(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getVoteCallHistory(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<CallHistory>list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<CallHistory>>() {
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

    @Override
    public void getSignin() {
        dataRepository.doStringPost(UrlFactory.getSignin(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.getSigninSuccess();
                    } else {
                        view.getSigninFiled(object.getInt("code"), object.optString("message"));
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
    public void getPowerRecord() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getPowerRecord(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<WaterBallBean>list = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<WaterBallBean>>() {
                        }.getType());
                        view.getPowerRecordSuccess(list);
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
    public void powerCollect(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.powerCollect(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.powerCollectSuccess();
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
