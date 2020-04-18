package com.spark.szhb_master.activity.kline;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
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
import com.spark.szhb_master.entity.DepthListInfo;
import com.spark.szhb_master.entity.DepthResult;
import com.spark.szhb_master.entity.Exchange;
import com.spark.szhb_master.entity.SymbolStep;
import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.factory.socket.NEWCMD;
import com.spark.szhb_master.serivce.SocketMessage;
import com.spark.szhb_master.serivce.SocketResponse;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.LogUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private String symbolType;
    private DepthAdapter adapter;
    private ArrayList<DepthListInfo> objBuyList;
    private ArrayList<DepthListInfo> objSellList;
    private Activity activity;
    private KlineContract.DepthPresenter presenter;

    private boolean tcpStatus = false;

    private Handler mHandler;
    private boolean mRunning = true;


    public static DepthFragment getInstance(String symbol,String symbolType) {
        DepthFragment depthFragment = new DepthFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("symbol", symbol);
        bundle.putSerializable("symbolType", symbolType);
        depthFragment.setArguments(bundle);
        return depthFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mRunning = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        stopTcp();
        EventBus.getDefault().unregister(this);
        mRunning = false;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible){
            if (!tcpStatus)
                startTCP();
        }else{
            stopTcp();
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
    }

    private void stopTcp(){
        tcpStatus = false;
        String depth = "market." + symbol + "_" + symbolType + ".depth.step10";
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_DEPTH,
                buildGetBodyJson(depth, "0").toString())); // 需要id
    }

    private void startTCP() {
        tcpStatus = true;
        String depth = "market." + symbol + "_" + symbolType + ".depth.step10";
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_DEPTH,
                buildGetBodyJson(depth, "1").toString())); // 需要id
    }

    private JSONObject buildGetBodyJson(String value, String type) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("value", value);
            obj.put("type", type);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * socket 推送过来的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketMessage(SocketResponse response) {
        if (response.getCmd() == NEWCMD.SUBSCRIBE_SYMBOL_DEPTH) {
            try {
                if (rcvEnable) {
                    rcvEnable = false;
                    setResponse(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean rcvEnable = false;

    private void setResponse(SocketResponse response) {
        String res = response.getResponse();
        if (StringUtils.isEmpty(res)) return; // 如果返回为空不处理
        SymbolStep items = new Gson().fromJson(res, SymbolStep.class);
        if (items == null) return;
        if (!response.getRemark().equals(symbol)) { // 这里加了层判断，如果推送来的币不是当前选择的币则不处理
            return;
        }

        objSellList.removeAll(objSellList);
        objSellList = initDepthData(items.getAsks());
        adapter.setObjSellList(objSellList);
        adapter.notifyDataSetChanged();


        objBuyList.removeAll(objBuyList);
        objBuyList = initDepthData(items.getBids());
        adapter.setObjBuyList(objBuyList);
        adapter.notifyDataSetChanged();

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
            symbolType = bundle.getString("symbolType");
//            String[] strings = symbol.split("/");
            tvNumber.setText(getResources().getString(R.string.number) + symbol);
            tvNumberR.setText(getResources().getString(R.string.number) + symbol);
            tvPrice.setText(getResources().getString(R.string.price) + "(USDT)");

            DisplayMetrics dm = getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            adapter = new DepthAdapter(activity);
            adapter.setObjSellList(initDepthData(null));
            adapter.setObjBuyList(initDepthData(null));
            adapter.setWidth(width / 2);
            LogUtils.i("屏幕宽度==" + (width / 2));
            recyclerView.setAdapter(adapter);
        }

        HandlerThread thread = new HandlerThread("MyHandlerThread");
        thread.start();//创建一个HandlerThread并启动它
        mHandler = new Handler(thread.getLooper());//使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程
        mHandler.post(mBackgroundRunnable);//将线程post到Handler中

        startTCP();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mBackgroundRunnable);
    }

    Runnable mBackgroundRunnable = new Runnable() {

        @Override
        public void run() {
            while(mRunning){
                try {
                    rcvEnable = true;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

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
    private ArrayList<DepthListInfo> initDepthData(List<List<Double>> objList) {
        ArrayList<DepthListInfo> depthListInfos = new ArrayList<>();
        if (objList != null) {
            for (int i = 0; i < 20; i++) {
                if (objList.size() > i) { // list里有数据
                    depthListInfos.add(new DepthListInfo(objList.get(i).get(0),objList.get(i).get(1)));
                } else {
                    DepthListInfo depthListInfo = new DepthListInfo();
                    depthListInfo.setAmount(-1);
                    depthListInfo.setPrice(-1);
                    depthListInfos.add(depthListInfo);
                }
            }
        } else {
            for (int i = 0; i < 20; i++) {
                DepthListInfo depthListInfo = new DepthListInfo();
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
//        ArrayList<DepthResult.DepthListInfo> askItems = null;
//        ArrayList<DepthResult.DepthListInfo> bidItems = null;
//        if (depthBar != null)
//            depthBar.setVisibility(View.GONE);
//        try {
//            if (result != null) {
//                DepthResult.DepthList ask = result.getAsk();
//                askItems = ask.getItems();
//                DepthResult.DepthList bid = result.getBid();
//                bidItems = bid.getItems();
//            }
//            objSellList = initDepthData(askItems);
//            objBuyList = initData(bidItems);
//            if (adapter == null) {
//                DisplayMetrics dm = getResources().getDisplayMetrics();
//                int width = dm.widthPixels;
//                adapter = new DepthAdapter(activity);
//                adapter.setObjSellList(objSellList);
//                adapter.setObjBuyList(objBuyList);
//                adapter.setWidth(width / 2);
//                LogUtils.i("屏幕宽度==" + (width / 2));
//                recyclerView.setAdapter(adapter);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (isAdded())
//                LogUtils.i(getResources().getString(R.string.parse_error));
//        }
    }

    @Override
    public void dpPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }
}
