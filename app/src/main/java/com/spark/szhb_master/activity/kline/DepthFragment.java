package com.spark.szhb_master.activity.kline;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.DepthAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.entity.DepthResult;
import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.serivce.SocketMessage;
import com.spark.szhb_master.serivce.SocketResponse;
import com.spark.szhb_master.utils.GlobalConstant;
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
 * kline_深度
 * Created by daiyy on 2018/6/4 0004.
 */

public class DepthFragment extends BaseFragment implements KlineContract.DepthView {
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvNumberR)
    TextView tvNumberR;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.rvDepth)
    RecyclerView recyclerView;
    @BindView(R.id.depthBar)
    ProgressBar depthBar;
    private String symbol;
    private DepthAdapter adapter;
    private ArrayList<DepthResult.DepthListInfo> objBuyList;
    private ArrayList<DepthResult.DepthListInfo> objSellList;
    private Activity activity;
    private Gson gson;
    private KlineContract.DepthPresenter presenter;

    public static DepthFragment getInstance(String symbol) {
        DepthFragment depthFragment = new DepthFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("symbol", symbol);
        depthFragment.setArguments(bundle);
        return depthFragment;
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
//        if (response.getCmd() == ISocket.CMD.PUSH_EXCHANGE_DEPTH) {
//            try {
//                LogUtils.i("深度图 推送过来的信息==" + response.getResponse());
//                if (gson == null)
//                    gson = new Gson();
//                DepthResult.DepthList result = gson.fromJson(response.getResponse(), new TypeToken<DepthResult.DepthList>() {
//                }.getType());
//                String strSymbol = result.getSymbol();
//                if (strSymbol != null && strSymbol.equals(symbol)) {
//                    ArrayList<DepthResult.DepthListInfo> depthListInfos = result.getItems();
//                    String direct = result.getDirection();
//                    if (direct.equals(GlobalConstant.SELL)) {
//                        objSellList.removeAll(objSellList);
//                        objSellList = initData(depthListInfos);
//                        adapter.setObjSellList(objSellList);
//                        adapter.notifyDataSetChanged();
//                    } else {
//                        objBuyList.removeAll(objBuyList);
//                        objBuyList = initData(depthListInfos);
//                        adapter.setObjBuyList(objBuyList);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_depth;
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
        objBuyList = new ArrayList<>();
        objSellList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(false);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    protected void initData() {
        super.initData();
        new DepthPresenterImpl(Injection.provideTasksRepository(activity), this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
            String[] strings = symbol.split("/");
            tvNumber.setText(getResources().getString(R.string.number) + "(" + strings[0] + ")");
            tvNumberR.setText(getResources().getString(R.string.number) + "(" + strings[0] + ")");
            tvPrice.setText(getResources().getString(R.string.price) + "(" + strings[1] + ")");
            startTCP();
            getDepth();
        }
    }

    @Override
    protected void loadData() {
    }

    /**
     * 获取深度图数据
     */
    private void getDepth() {
        if (depthBar != null)
            depthBar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("size", "20");
        presenter.getDepth(map);
    }

    /**
     * 填充数据
     */
    private ArrayList<DepthResult.DepthListInfo> initData(ArrayList<DepthResult.DepthListInfo> objList) {
        ArrayList<DepthResult.DepthListInfo> depthListInfos = new ArrayList<>();
        if (objList != null) {
            for (int i = 0; i < 20; i++) {
                if (objList.size() > i) { // list里有数据
                    depthListInfos.add(objList.get(i));
                } else {
                    DepthResult.DepthListInfo depthListInfo = new DepthResult().new DepthListInfo();
                    depthListInfo.setAmount(-1);
                    depthListInfo.setPrice(-1);
                    depthListInfos.add(depthListInfo);
                }
            }
        } else {
            for (int i = 0; i < 20; i++) {
                DepthResult.DepthListInfo depthListInfo = new DepthResult().new DepthListInfo();
                depthListInfo.setAmount(-1);
                depthListInfo.setPrice(-1);
                depthListInfos.add(depthListInfo);
            }
        }
        return depthListInfos;
    }

    @Override
    public void setPresenter(KlineContract.DepthPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getDepthtSuccess(DepthResult result) {
        ArrayList<DepthResult.DepthListInfo> askItems = null;
        ArrayList<DepthResult.DepthListInfo> bidItems = null;
        if (depthBar != null)
            depthBar.setVisibility(View.GONE);
        try {
            if (result != null) {
                DepthResult.DepthList ask = result.getAsk();
                askItems = ask.getItems();
                DepthResult.DepthList bid = result.getBid();
                bidItems = bid.getItems();
            }
            objSellList = initData(askItems);
            objBuyList = initData(bidItems);
            if (adapter == null) {
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                adapter = new DepthAdapter(activity);
                adapter.setObjSellList(objSellList);
                adapter.setObjBuyList(objBuyList);
                adapter.setWidth(width / 2);
                LogUtils.i("屏幕宽度==" + (width / 2));
                recyclerView.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (isAdded())
                LogUtils.i(getResources().getString(R.string.parse_error));
        }
    }

    @Override
    public void dpPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }
}
