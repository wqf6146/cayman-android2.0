package com.spark.szhb_master.activity.kline;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.VolumeAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.entity.VolumeInfo;
import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.serivce.SocketMessage;
import com.spark.szhb_master.serivce.SocketResponse;
import com.spark.szhb_master.utils.LogUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import config.Injection;

/**
 * kline_成交
 * Created by daiyy on 2018/6/4 0004.
 */

public class VolumeFragment extends BaseFragment implements KlineContract.VolumeView {
    @BindView(R.id.tvNumberV)
    TextView tvNumberV;
    @BindView(R.id.tvPriceV)
    TextView tvPriceV;
    @BindView(R.id.rvVolume)
    RecyclerView recyclerView;
    @BindView(R.id.volumeBar)
    ProgressBar volumeBar;
    private String symbol;
    private ArrayList<VolumeInfo> objList;
    private VolumeAdapter adapter;
    private Activity activity;
    private Gson gson;
    private KlineContract.VolumePresenter presenter;


    public static VolumeFragment getInstance(String symbol) {
        VolumeFragment volumeFragment = new VolumeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("symbol", symbol);
        volumeFragment.setArguments(bundle);
        return volumeFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // 取消订阅
        //EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.UNSUBSCRIBE_EXCHANGE_TRADE, symbol));
        EventBus.getDefault().unregister(this);
    }

    private void startTCP() {
        // 开始订阅
        //EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.SUBSCRIBE_EXCHANGE_TRADE, symbol));
    }


    /**
     * socket 推送过来的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketMessage(SocketResponse response) {
//        if (response.getCmd() == ISocket.CMD.PUSH_EXCHANGE_TRADE) {
//            try {
//                LogUtils.i("成交 推送过来的信息==" + response.getResponse());
//                if (gson == null)
//                    gson = new Gson();
//                VolumeInfo volumeInfo = gson.fromJson(response.getResponse(), new TypeToken<VolumeInfo>() {
//                }.getType());
//                String strSymbol = volumeInfo.getSymbol();
//                if (strSymbol != null && strSymbol.equals(symbol)) {
//                    objList.add(0, volumeInfo);
//                    objList = initData(objList);
//                    adapter.setObjList(objList);
//                    adapter.notifyDataSetChanged();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_volume;
    }

    @Override
    protected void initView() {
        intLv();
        recyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 初始化列表数据
     */
    private void intLv() {
        activity = getActivity();
        objList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(false);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    protected void initData() {
        activity = getActivity();
        new VolumePresenterImpl(Injection.provideTasksRepository(activity), this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
            String[] strings = symbol.split("/");
            tvNumberV.setText(getResources().getString(R.string.number) + "(" + strings[0] + ")");
            tvPriceV.setText(getResources().getString(R.string.price) + "(" + strings[1] + ")");
            startTCP();
            getVolume();
        }
    }

    /**
     * 获取成交数据
     */
    private void getVolume() {
        if (volumeBar != null)
            volumeBar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("size", "20");
        presenter.getVolume(map);
    }

    /**
     * 填充数据
     */
    private ArrayList<VolumeInfo> initData(ArrayList<VolumeInfo> objList) {
        ArrayList<VolumeInfo> volumeInfos = new ArrayList<>();
        if (objList != null) {
            for (int i = 0; i < 20; i++) {
                if (objList.size() > i) { // list里有数据
                    volumeInfos.add(objList.get(i));
                } else {
                    VolumeInfo volumeInfo = new VolumeInfo();
                    volumeInfo.setTime(-1);
                    volumeInfo.setDirection(null);
                    volumeInfo.setPrice(-1);
                    volumeInfo.setAmount(-1);
                    volumeInfos.add(volumeInfo);
                }
            }
        } else {
            for (int i = 0; i < 20; i++) {
                VolumeInfo volumeInfo = new VolumeInfo();
                volumeInfo.setTime(-1);
                volumeInfo.setDirection(null);
                volumeInfo.setPrice(-1);
                volumeInfo.setAmount(-1);
                volumeInfos.add(volumeInfo);
            }
        }
        return volumeInfos;
    }

    @Override
    public void setPresenter(KlineContract.VolumePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getVolumeSuccess(ArrayList<VolumeInfo> list) {
        if (volumeBar != null)
            volumeBar.setVisibility(View.GONE);
        objList = initData(list);
        if (adapter == null) {
            adapter = new VolumeAdapter(activity, objList);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void dpPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }
}
