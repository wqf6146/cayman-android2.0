package com.spark.szhb_master.activity.chat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.ChatEntity;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class ChatPresenter implements ChatContact.Presenter {
    private final DataSource dataRepository;
    private final ChatContact.View view;

    public ChatPresenter(DataSource dataRepository, ChatContact.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getHistoryMessage(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getHistoryMessageUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    List<ChatEntity> objs = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<ChatEntity>>() {
                    }.getType());
                    view.getHistoryMessageSuccess(objs);
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.getHistoryMessageFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getHistoryMessageFail(code, toastMessage);
            }
        });
    }

}
