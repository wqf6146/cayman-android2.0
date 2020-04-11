package com.spark.szhb_master.activity.chat;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.order.OrderDetailActivity;
import com.spark.szhb_master.adapter.ChatAdapter;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.ChatEntity;
import com.spark.szhb_master.entity.ChatEvent;
import com.spark.szhb_master.entity.ChatTable;
import com.spark.szhb_master.entity.ChatTipEvent;
import com.spark.szhb_master.entity.OrderDetial;
import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.factory.socket.SocketFactory;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.DatabaseUtils;
import com.spark.szhb_master.utils.DateUtils;
import com.spark.szhb_master.utils.LogUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 聊天界面
 */
public class ChatActivity extends BaseActivity implements ISocket.TCPCallback, ChatContact.View {
    @BindView(R.id.rvMessage)
    RecyclerView rvMessage;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private OrderDetial orderDetial;
    private List<ChatEntity> chatEntities = new ArrayList<>();
    private Gson gson = new Gson();
    private ChatAdapter adapter;
    private ChatContact.Presenter presenter;
    private int pageNo = 0;
    private int pageSize = 20;
    private boolean hasNew = false;
    private DatabaseUtils databaseUtils = new DatabaseUtils();
    private List<ChatTable> findByOrderList;
    private List<ChatTable> list;
    private boolean isChatList;
    private JSONObject obj;

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        if (immersionBar != null)
            immersionBar.keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE).init();
        isNeedhide = false;
        tvGoto.setVisibility(View.INVISIBLE);
        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
    }

    @Override
    protected void initData() {
        super.initData();
        isNeedhide = false;
        new ChatPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderDetial = (OrderDetial) bundle.getSerializable("orderDetial");
            isChatList = bundle.getBoolean("isChatList");
            if (isChatList) {
                tvGoto.setVisibility(View.VISIBLE);
                tvGoto.setText(getString(R.string.orderdetail));
            }
            setTitle(orderDetial.getOtherSide());
        }
        initRvChat();
    }

    @OnClick({R.id.tvSend, R.id.tvGoto})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvSend:
                send();
                break;
            case R.id.tvGoto:
                Bundle bundle = new Bundle();
                bundle.putString("orderSn", orderDetial.getOrderSn());
                bundle.putBoolean("isChatList", isChatList);
                showActivity(OrderDetailActivity.class, bundle);
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getHistoyList(false);
            }
        });
    }

    /**
     * 获取历史聊天记录
     */
    private void getHistoyList(boolean isShow) {
        if (isShow)
            displayLoadingPopup();
        HashMap<String, String> map = new HashMap<>();
        map.put("orderId", orderDetial.getOrderSn());
        map.put("limit", pageSize + "");
        map.put("page", (++pageNo) + "");
        map.put("sortFiled", "sendTime");
        presenter.getHistoryMessage(map);

    }

    private void send() {
        String content = etContent.getText().toString();
        if (StringUtils.isEmpty(content)) return;
        obj = buildBodyJson(content);
        LogUtils.i("buildBodyJson" + obj.toString());
        SocketFactory.produceSocket(ISocket.C2C).sendRequest(ISocket.CMD.SEND_CHAT, obj.toString().getBytes(), this);
        addChatEntity(obj.toString(), ChatEntity.Type.RIGHT);
        etContent.setText("");
    }

    private void storageData(String response) {
        ChatEntity chatEntity = gson.fromJson(response, ChatEntity.class);
        LogUtils.i("chatEntity  " + chatEntity.toString());
        if (chatEntity == null) return;
        ChatTable table;
        hasNew = false;
        SharedPreferenceInstance.getInstance().saveHasNew(hasNew);
        list = databaseUtils.findAll();
        findByOrderList = databaseUtils.findByOrder(chatEntity.getOrderId());
        if (findByOrderList == null || findByOrderList.size() == 0) {
            LogUtils.i("初次建立");
            table = new ChatTable();
            table.setContent(chatEntity.getContent());
            table.setFromAvatar(chatEntity.getFromAvatar());
            table.setNameFrom(chatEntity.getNameTo());
            table.setNameTo(chatEntity.getNameFrom());
            table.setUidFrom(chatEntity.getUidTo());
            table.setUidTo(chatEntity.getUidFrom());
            table.setOrderId(chatEntity.getOrderId());
            table.setRead(true);
            table.setHasNew(false);
            table.setSendTimeStr(DateUtils.getDateTimeFromMillisecond(System.currentTimeMillis()));
            table.setSendTime(System.currentTimeMillis());
            databaseUtils.saveChat(table);
            ChatTipEvent tipEvent = new ChatTipEvent();
            tipEvent.setHasNew(hasNew);
            tipEvent.setOrderId(chatEntity.getOrderId());
            EventBus.getDefault().post(tipEvent);
            return;
        } else {
            table = findByOrderList.get(findByOrderList.size() - 1);
            table.setContent(chatEntity.getContent());
            table.setSendTimeStr(chatEntity.getSendTimeStr());
            table.setSendTime(chatEntity.getSendTime());
            databaseUtils.update(table);
            ChatTipEvent tipEvent = new ChatTipEvent();
            tipEvent.setHasNew(hasNew);
            tipEvent.setOrderId(chatEntity.getOrderId());
            EventBus.getDefault().post(tipEvent);
            LogUtils.i("再次进入");
            return;
        }
    }


    private void initRvChat() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(false);
        rvMessage.setLayoutManager(manager);
        adapter = new ChatAdapter(ChatActivity.this, chatEntities, String.valueOf(MyApplication.app.getCurrentUser().getId()));
        rvMessage.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        getHistoyList(true);
    }

    private JSONObject buildBodyJson(String content) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("orderId", orderDetial.getOrderSn());
            obj.put("uidFrom", MyApplication.getApp().getCurrentUser().getId());
            obj.put("uidTo", orderDetial.getHisId());
            obj.put("nameTo", orderDetial.getOtherSide());
            obj.put("nameFrom", MyApplication.getApp().getCurrentUser().getNick_name());
            obj.put("messageType", 1);
            obj.put("avatar", MyApplication.getApp().getCurrentUser().getAvatar());
            if (!StringUtils.isEmpty(content)) obj.put("content", content);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getChatEvent(ChatEvent chatEvent) {
        if (chatEvent.getCmd().equals(ISocket.CMD.PUSH_GROUP_CHAT)) {
            ChatEntity chatEntity = gson.fromJson(chatEvent.getResonpce(), ChatEntity.class);
            if (chatEntity.getOrderId().equals(orderDetial.getOrderSn())) {
                addChatEntity(chatEvent.getResonpce(), ChatEntity.Type.LEFT);
            }
        } else {
        }
    }

    @Override
    public void dataSuccess(ISocket.CMD cmd, String response) {
        switch (cmd) {
            case SEND_CHAT:
                LogUtils.i("-----" + response);
                storageData(obj.toString());
                break;
        }
    }

    private void addChatEntity(String response, ChatEntity.Type type) {
        try {
            ChatEntity chatEntity = gson.fromJson(response, ChatEntity.class);
            chatEntity.setType(type);
            chatEntities.add(chatEntity);
            adapter.notifyDataSetChanged();
            rvMessage.smoothScrollToPosition(chatEntities.size() - 1);
        } catch (Exception ex) {

        }
    }

    @Override
    public void dataFail(int code, ISocket.CMD cmd, String errorInfo) {
        switch (cmd) {
            case SEND_CHAT:
                LogUtils.i(errorInfo);
                break;
        }
    }


    @Override
    public void setPresenter(ChatContact.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getHistoryMessageSuccess(List<ChatEntity> entityList) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        if (entityList == null) return;
        if (pageNo == 0) chatEntities.clear();
        Collections.reverse(chatEntities);
        chatEntities.addAll(entityList);
        Collections.reverse(chatEntities);
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void getHistoryMessageFail(Integer code, String toastMessage) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }


}
