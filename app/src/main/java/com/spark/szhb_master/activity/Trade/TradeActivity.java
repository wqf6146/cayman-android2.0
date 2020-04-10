package com.spark.szhb_master.activity.Trade;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.entrust.CurrentTrustActivity;
import com.spark.szhb_master.activity.kline.KlineActivity;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.activity.main.TradeContract;
import com.spark.szhb_master.activity.main.presenter.TradePresenter;
import com.spark.szhb_master.activity.my_match.MatchActivity;
import com.spark.szhb_master.adapter.SellAdapter;
import com.spark.szhb_master.adapter.TrustAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.EntrustDialog;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.dialog.TradeBuyOrSellConfirmDialog;
import com.spark.szhb_master.entity.Currency;
import com.spark.szhb_master.entity.Entrust;
import com.spark.szhb_master.entity.Exchange;
import com.spark.szhb_master.entity.Favorite;
import com.spark.szhb_master.entity.Money;
import com.spark.szhb_master.entity.NewCurrency;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.SymbolStep;
import com.spark.szhb_master.entity.TextItems;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.factory.socket.NEWCMD;
import com.spark.szhb_master.serivce.SocketMessage;
import com.spark.szhb_master.serivce.SocketResponse;
import com.spark.szhb_master.utils.CommonUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.LogUtils;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.xw.repo.BubbleSeekBar;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * author: wuzongjie
 * time  : 2018/4/17 0017 15:20
 * desc  : 这是我重写的交易界面，在原来上我不知道怎么改了
 */

public class TradeActivity extends BaseActivity implements TradeContract.View {
    public static final String TAG = TradeActivity.class.getSimpleName();
    @BindView(R.id.ivOpen)
    ImageView ivOpen;
    @BindView(R.id.ivMarket)
    ImageView ivMarket;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.limitSpinner)
    NiceSpinner limitSpinner; // 下拉
    @BindView(R.id.recyclerSell)
    RecyclerView recyclerSell; // 卖出的
    @BindView(R.id.recyclerBuy)
    RecyclerView recyclerBuy; // 买进
    @BindView(R.id.recyclerCurrentEntrust)
    RecyclerView recyclerCurrentEntrust; // 当前委托
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.llMarketBuy)
    LinearLayout llMarketBuy; // 买入
    @BindView(R.id.llMarketSell)
    LinearLayout llMarketSell; // 卖出
    @BindView(R.id.tvBestPriceBuy)
    TextView tvBestPriceBuy; // 最优价
    @BindView(R.id.llBuyPrice)
    LinearLayout llBuyPrice;  // 输入价格
    @BindView(R.id.tvBestPriceSell)
    TextView tvBestPriceSell;
    @BindView(R.id.llSellPrice)
    LinearLayout llSellPrice;
    @BindView(R.id.etBuyPrice)
    EditText etBuyPrice;
    @BindView(R.id.tvBuyAdd)
    TextView tvBuyAdd;
    @BindView(R.id.tvBuyReduce)
    TextView tvBuyReduce;
    @BindView(R.id.etSellPrice)
    EditText etSellPrice;
    @BindView(R.id.tvSellAdd)
    TextView tvSellAdd;
    @BindView(R.id.tvSellReduce)
    TextView tvSellReduce;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvBuyCanUse)
    TextView tvBuyCanUse;
    @BindView(R.id.tvCanSell)
    TextView tvCanSell;
    @BindView(R.id.tvBuyRMB)
    TextView tvBuyRMB;
    @BindView(R.id.etCount)
    EditText etBuyCount;
    @BindView(R.id.etSellCount)
    EditText etSellCount;
    @BindView(R.id.tvSellRMB)
    TextView tvSellRMB;
    @BindView(R.id.btnBuy)
    Button btnBuy;
    @BindView(R.id.btnSell)
    Button btnSale;
    @BindView(R.id.tvBuyTradeCount)
    TextView tvBuyTradeCount; // 交易额
    @BindView(R.id.tvSellTradeCount)
    TextView tvSellTradeCount;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvBuySymbol)
    TextView tvBuySymbol;
    @BindView(R.id.tvSellSymbol)
    TextView tvSellSymbol;
    @BindView(R.id.llBuyTradeCount)
    LinearLayout llBuyTradeCount;
    @BindView(R.id.llSellTradeCount)
    LinearLayout llSellTradeCount;
    @BindView(R.id.buySeekBar)
    BubbleSeekBar buySeekBar;
    @BindView(R.id.sellSeekBar)
    BubbleSeekBar sellSeekBar;
    @BindView(R.id.ivCollect)
    ImageView ivCollect;
    @BindView(R.id.rbBuy)
    RadioButton rbBuy;
    @BindView(R.id.rbSell)
    RadioButton rbSell;
    @BindView(R.id.tvPriceTag)
    TextView tvPriceTag;
    @BindView(R.id.tvCountTag)
    TextView tvCountTag;
    @BindView(R.id.tvLatest)
    TextView tvLatest;
    private NewCurrency currency;
    private List<Exchange> sellExchangeList;
    private List<Exchange> buyExchangeList;
    private List<Entrust> entrustList;
    private SellAdapter sellAdapter; // 卖出适配器
    private SellAdapter buyAdapter; // 买入适配器
    private TrustAdapter trustAdapter; // 委托适配器
    private TradeContract.Presenter mPresenter;
    private int baseCoinScale = 2; // 价格
    private int coinScale = 2; // 数量
    private String oldSymbol; // 上个订阅的币种
    private boolean isFace = false;
    private double doubleBuyCount, doubleSellCount, doubleBuyPrice, doubleSellPrice;
    private EntrustDialog dialog;
    private String price; // 买入的价格
    private String amout; // 卖出的价格
    private String type = GlobalConstant.LIMIT_PRICE;
    private String orderId;
    private double usdeBalance = -1;
    private TradeBuyOrSellConfirmDialog tradeBuyOrSellConfirmDialog;
    private double sellCountBalance = 0; // 可卖币的多少
    private double sellCountBalance_two = 0; // 可卖币的多少
    private double buyCountBalance = 0; // 可用的币
    private double buyCountBalance_two = 0; // 可用的币
    private boolean isGetWallet;
    public static List<Favorite> mFavorte = new ArrayList<>();
    private List<Currency> currencyListAll = new ArrayList<>();
    private boolean isFirst = true;
    private boolean isLoginStateOld;

    /**
     * 主界面调用
     */
    public void tcpNotify() {
        if (currency != null) {
            tvPrice.setText(String.valueOf(currency.getClose()));
            tvPrice.setTextColor( Float.parseFloat(currency.getScale()) >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) :
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
            tvLatest.setTextColor(Float.parseFloat(currency.getScale()) >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) :
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
            if (GlobalConstant.CNY.equals(CommonUtils.getUnitBySymbol(currency.getSymbol()))) {
                tvMoney.setText(String.valueOf("≈" + MathUtils.getRundNumber(Float.parseFloat(currency.getClose()) * 1 * currency.getBaseUsdRate(),
                        2, null) + GlobalConstant.CNY));
            } else {
                tvMoney.setText(String.valueOf("≈" + MathUtils.getRundNumber(Float.parseFloat(currency.getClose()) * MainActivity.rate * currency.getBaseUsdRate(),
                        2, null) + GlobalConstant.CNY));
            }
        }
    }

    /**
     * 这个是从主界面来的，表示选择了某个币种
     */
    public void resetSymbol(NewCurrency currency) {
        this.currency = currency;
        if (this.currency != null) {
            etBuyCount.setText("");
            etSellCount.setText("");
            getExchangeAndSymbolInfo();
            if (MyApplication.getApp().isLogin()) {
                getWallet(1);
                if (usdeBalance != -1 && "USDT".equals(currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length()))) { // 这里请求USDT多少，但是不用每次都请求故取消了
                    String strCanUse = String.valueOf(MathUtils.getRundNumber(usdeBalance, 2, null) + currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length()));
                    tvBuyCanUse.setText(getString(R.string.text_can_used) + strCanUse);
                } else {
                    getWallet(2);
                }
                getCurrentEntrust();
            }
            setViewText();
            setTCPBySymbol();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }


    public interface OperateCallback {
        void GoneLine();

        void tohome();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop(currency.getSymbol(),currency.getType());
        EventBus.getDefault().unregister(this);
    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.frgment_trade;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        ivOpen.setVisibility(View.INVISIBLE);
        ivMarket.setVisibility(View.VISIBLE);
        ivCollect.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        isLoginStateOld = MyApplication.getApp().isLogin();
        limitSpinner.attachDataSource(new LinkedList<>(Arrays.asList(activity.getResources().getStringArray(R.array.text_type))));
        dialog = new EntrustDialog(activity);
        tradeBuyOrSellConfirmDialog = new TradeBuyOrSellConfirmDialog(activity);
        new TradePresenter(Injection.provideTasksRepository(activity.getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currency = (NewCurrency) bundle.getSerializable("currency");
            String comefrom = bundle.getString("comefrom");
            if (!StringUtils.isEmpty(comefrom)) {
                if (comefrom.equals("comefrom")) {
                    int type = bundle.getInt("type");
                    String symbol = bundle.getString("symbol");
//                    showPageFragment(symbol, type - 1); // 当type=1 交易fragment就显示买入，对应的page就是0 （即type -1），当type=2，同理
                    if ((type - 1) == 0) { // 买入
                        mRadioGroup.check(R.id.rbBuy);
                        llMarketBuy.setVisibility(View.VISIBLE);
                        llMarketSell.setVisibility(View.GONE);
                    } else { //卖出
                        mRadioGroup.check(R.id.rbSell);
                        llMarketBuy.setVisibility(View.GONE);
                        llMarketSell.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(0);
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
//                    mRadioGroup.check(R.id.rbBuy);
                    LogUtils.i("handler");
                    initSellAndBuyView();
                    initTrustView();
                    tvBuyTradeCount.setText(getString(R.string.text_entrust) + " --");
                    tvSellTradeCount.setText(getString(R.string.text_entrust) + " --");
                    setViewListener();
                    if (currency != null) {
                        getExchangeAndSymbolInfo();
                        setTCPBySymbol();
                        setViewText();
                        checkLogin();
                    }
                    break;
            }
        }
    };

    /**
     * 根据登录状态显示
     */
    private void checkLogin() {
        if (MyApplication.getApp().isLogin()) {
            getWallet(0);
            getCurrentEntrust();
            btnBuy.setText("买入" + strSymbol);
            btnSale.setText("卖出" + strSymbol);
        } else {
            btnBuy.setText("登录/注册");
            btnSale.setText("登录/注册");
//            tvLogin.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerCurrentEntrust.setVisibility(View.GONE);
        }
        isFirst = false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst && (isLoginStateOld != MyApplication.getApp().isLogin())) { // 从该界面跳转到登录页，回来刷新数据
            checkLogin();
        }
        isLoginStateOld = MyApplication.getApp().isLogin();
    }

    @Override
    protected void loadData() {
        LogUtils.i("loadData");
    }

    private String strSymbol;

    private void setViewText() {
        if (currency != null) {
            String symbol = currency.getSymbol();
            setTitle(symbol);
            tvPriceTag.setText(getString(R.string.text_price) + symbol);
            tvCountTag.setText(getString(R.string.text_number) + "(" + CommonUtils.getUnitBySymbol(symbol) + ")");
            isFace = addFace(symbol);
            ivCollect.setImageDrawable(isFace ? getResources().getDrawable(R.mipmap.icon_collect_hover) : getResources().getDrawable(R.mipmap.icon_collect_normal));
            String strClose = currency.getClose();
            etBuyPrice.setText(strClose);
            etSellPrice.setText(strClose);
            tvPrice.setText(strClose);
            tvPrice.setTextColor(Float.parseFloat(currency.getScale()) >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) :
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
            tvLatest.setTextColor(Float.parseFloat(currency.getScale()) >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) :
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
            strSymbol = symbol;
            tvBuySymbol.setText(strSymbol);
            tvSellSymbol.setText(strSymbol);
            btnBuy.setText(getString(R.string.text_buy_in) + strSymbol);
            btnSale.setText(getString(R.string.text_sale_out) + strSymbol);
            if (GlobalConstant.CNY.equals(CommonUtils.getUnitBySymbol(symbol))) {
                tvMoney.setText("≈" + MathUtils.getRundNumber(Float.parseFloat(currency.getClose()) * 1 * currency.getBaseUsdRate(),
                        2, null) + GlobalConstant.CNY);
            } else {
                tvMoney.setText("≈" + MathUtils.getRundNumber(Float.parseFloat(currency.getClose()) * MainActivity.rate * currency.getBaseUsdRate(),
                        2, null) + GlobalConstant.CNY);
            }
        }
    }

    /**
     * 初始化委托信息
     */
    private void initTrustView() {
        entrustList = new ArrayList<>();
        recyclerCurrentEntrust.setLayoutManager(new LinearLayoutManager(activity));
        trustAdapter = new TrustAdapter(entrustList);
        recyclerCurrentEntrust.setAdapter(trustAdapter);
    }

    /**
     * 初始化卖出的盘口信息和买入的盘口信息
     */
    private void initSellAndBuyView() {
        sellExchangeList = new ArrayList<>();
        buyExchangeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            sellExchangeList.add(new Exchange(5 - i, "--", "--"));
            buyExchangeList.add(new Exchange(i, "--", "--"));
        }
        recyclerSell.setLayoutManager(new LinearLayoutManager(activity));
        sellAdapter = new SellAdapter(sellExchangeList, 0);
        recyclerSell.setAdapter(sellAdapter);

        recyclerBuy.setLayoutManager(new LinearLayoutManager(activity));
        buyAdapter = new SellAdapter(buyExchangeList, 1);
        recyclerBuy.setAdapter(buyAdapter);
    }

    @OnClick({R.id.tvPrice, R.id.ivCollect, R.id.ivMarket, R.id.tvLogin, R.id.tvBuyAdd, R.id.tvBuyReduce, R.id.tvSellAdd, R.id.tvSellReduce, R.id.tvAll, R.id.btnBuy, R.id.btnSell})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
//            case R.id.ivOpen:
//                ((MainActivity) getActivity()).getDlRoot().openDrawer(Gravity.LEFT);
//                break;
            case R.id.tvPrice:
                if (rbBuy.isChecked()) {
                    etBuyPrice.setText(tvPrice.getText());
                } else {
                    etSellPrice.setText(tvPrice.getText());
                }
                break;
            case R.id.ivCollect:
                if (MyApplication.getApp().isLogin()) {
                    MainActivity.isAgain = true;
                    HashMap<String, String> map = new HashMap<>();
                    map.put("symbol", currency.getSymbol());
                    if (isFace) { // 已经收藏 则删除
                        mPresenter.deleteCollect(map);
                    } else {
//                    mPresenter.deleteCollect(map);
                        mPresenter.addCollect(map);
                    }
                } else {
                    showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
                }
                break;
            case R.id.ivMarket:
                Bundle bundle = new Bundle();
                bundle.putString("symbol", currency.getSymbol());
                bundle.putSerializable("currency", currency);
                showActivity(KlineActivity.class, bundle, 1);
                finish();
                break;
            case R.id.tvLogin: // 点击登录
                showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
                break;
            case R.id.tvBuyAdd:
            case R.id.tvBuyReduce:
                try {
                    double num = Double.valueOf(etBuyPrice.getText().toString());
                    if (v.getId() == R.id.tvBuyAdd) {
                        etBuyPrice.setText(String.valueOf(num + 0.01));
                    } else {
                        if ((num - 0.01) > 0) {
                            etBuyPrice.setText(String.valueOf(num - 0.01));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvSellAdd:
            case R.id.tvSellReduce:
                try {
                    double num1 = Double.valueOf(etSellPrice.getText().toString());
                    if (v.getId() == R.id.tvSellAdd) {
                        etSellPrice.setText(String.valueOf(num1 + 0.01));
                    } else {
                        if ((num1 - 0.01) > 0) {
                            etSellPrice.setText(String.valueOf(num1 - 0.01));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvAll: // 查看当前委托的全部
                if (MyApplication.getApp().isLogin() && currency != null) { // 如果登录了就可以进入
                    bundle = new Bundle();
                    bundle.putString("symbol", currency.getSymbol());
                    showActivity(CurrentTrustActivity.class, bundle);
                } else if (!MyApplication.getApp().isLogin()) {
                    showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
                }
                break;
            case R.id.btnBuy: // 买入
                if (MyApplication.getApp().isLogin()) {
                    from = 0;
                    mPresenter.safeSetting();
                } else {
                    showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
                }
                break;
            case R.id.btnSell: // 卖出
                if (MyApplication.getApp().isLogin()) {
                    from = 1;
                    mPresenter.safeSetting();
                } else {
                    showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
                }

                break;
        }
    }

    private int from = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    loadData();
                    break;
                case 1:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        int type = bundle.getInt("type");
                        String symbol = bundle.getString("symbol");
                        showPageFragment(symbol, type - 1); // 当type=1 交易fragment就显示买入，对应的page就是0 （即type -1），当type=2，同理
                    }
                    break;
            }

        }
    }

    /**
     * 盘口信息列表item的点击事件
     */
    BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
            Exchange e = (Exchange) baseQuickAdapter.getData().get(position);
            if (type.equals(GlobalConstant.LIMIT_PRICE)) {
                if (!"--".equals(e.getPrice())) {
                    if (rbBuy.isChecked()) {
                        etBuyPrice.setText(String.valueOf(MathUtils.getRundNumber(Double.valueOf(e.getPrice()), coinScale, null)));
                    } else {
                        etSellPrice.setText(String.valueOf(MathUtils.getRundNumber(Double.valueOf(e.getPrice()), coinScale, null)));
                    }
                }
            }
        }
    };

    @Override
    public void setPresenter(TradeContract.Presenter presenter) {
        mPresenter = presenter;
    }


    /**
     * 价格，数量监听
     */
    private class MyTextWatcher implements TextWatcher {
        private int intType;
        private double exchangeRate = 1;

        public MyTextWatcher(int intType) {
            this.intType = intType;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (currency == null) return;
            if (CommonUtils.getUnitBySymbol(currency.getSymbol()).equals(GlobalConstant.CNY)) {
                exchangeRate = 1;
                exchangeRate = 1 * MainActivity.rate * currency.getBaseUsdRate();
            }
            if (intType == 0) { // 卖出价格
                if (StringUtils.isNotEmpty(etBuyPrice.getText().toString().trim())) {
                    doubleBuyPrice = MathUtils.getDoubleTransferString(etBuyPrice.getText().toString().trim());
                } else {
                    doubleBuyPrice = 0.0;
                }
                if (type.equals(GlobalConstant.LIMIT_PRICE)) { // 限价
                    tvBuyRMB.setText(String.valueOf("≈" + MathUtils.getRundNumber(doubleBuyPrice * 1 * MainActivity.rate * currency.getBaseUsdRate(),
                            2, null) + GlobalConstant.CNY));
                    if (StringUtils.isNotEmpty(etBuyCount.getText().toString().trim())) {
                        tvBuyTradeCount.setText(getString(R.string.text_entrust) + " " + String.valueOf(MathUtils.getRundNumber(MathUtils.mul(doubleBuyPrice * doubleBuyCount, 1), 5, null)
                                + currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length())));
                    }
                }
            } else if (intType == 1) { // 买入价格
                if (StringUtils.isNotEmpty(etSellPrice.getText().toString().trim())) {
                    doubleSellPrice = MathUtils.getDoubleTransferString(etSellPrice.getText().toString().trim());
                } else {
                    doubleSellPrice = 0.0;
                }
                if (type.equals(GlobalConstant.LIMIT_PRICE)) {
//                    tvSellRMB.setText(String.valueOf("≈" + MathUtils.getRundNumber(doubleSellPrice * exchangeRate,
//                            2, null) + GlobalConstant.CNY));
                    tvSellRMB.setText(String.valueOf("≈" + MathUtils.getRundNumber(doubleSellPrice * 1 * MainActivity.rate * currency.getBaseUsdRate(),
                            2, null) + "CNY"));

                    if (StringUtils.isNotEmpty(etSellCount.getText().toString().trim())) {
                        tvSellTradeCount.setText(getString(R.string.text_entrust) + " " + String.valueOf(MathUtils.getRundNumber(MathUtils.mul(doubleSellCount * doubleSellPrice, 1), 5, null) +
                                currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length())));
                    }
                }
            } else if (intType == 2) { // 买入数量
                String strBuyCount = etBuyCount.getText().toString().trim();
                if (StringUtils.isNotEmpty(strBuyCount)) {
                    doubleBuyCount = MathUtils.getDoubleTransferString(strBuyCount);
                } else {
                    doubleBuyCount = 0.0;
                }
                if (type.equals(GlobalConstant.LIMIT_PRICE)) { // 限价
                    tvBuyTradeCount.setText(getString(R.string.text_entrust) + " " + String.valueOf(MathUtils.getRundNumber(MathUtils.mul(doubleBuyPrice * doubleBuyCount, 1), 5, null)
                            + currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length())));

                    if (buyCountBalance == 0) buyCountBalance = 1;
                    if (doubleBuyPrice == 0) doubleBuyPrice = 1;
                    double progress = doubleBuyPrice * doubleBuyCount / buyCountBalance;//
                    buySeekBar.setProgress(progress * 1000 >= 1000 ? 1000 : (float) progress * 1000);
                } else {
                    tvBuyTradeCount.setText(getString(R.string.text_entrust) + " " + String.valueOf("--"));
                    if (buyCountBalance == 0) buyCountBalance = 1;
                    double progress = doubleBuyCount / usdeBalance;
//                    buySeekBar.setProgress(Float.parseFloat(tvBuyCanUse+""));
//                    buySeekBar.setProgress((float)(Double.valueOf(MathUtils.getRundNumber(usdeBalance, 2, null)) * progress));
                    buySeekBar.setProgress(progress * 1000 >= 1000 ? 1000 : (float) progress * 1000);
                }

                int posDot = strBuyCount.indexOf(".");
                if (posDot <= 0) return;
                if (strBuyCount.length() - (posDot + 1) > coinScale) {
                    editable.delete(posDot + 1 + coinScale, posDot + 2 + coinScale);
                }
            } else if (intType == 3) { // 卖出数量
                String strSellCount = etSellCount.getText().toString().trim();
                if (StringUtils.isNotEmpty(strSellCount)) {
                    doubleSellCount = MathUtils.getDoubleTransferString(strSellCount);
                } else {
                    doubleSellCount = 0.0;
                }
                if (type.equals(GlobalConstant.LIMIT_PRICE)) {
                    tvSellTradeCount.setText(getString(R.string.text_entrust) + " " + String.valueOf(MathUtils.getRundNumber(MathUtils.mul(doubleSellCount * doubleSellPrice, 1), 5, null)
                            + currency.getSymbol()));
                    //新家的
                    if (sellCountBalance == 0) sellCountBalance = 1;
                    if (doubleSellPrice == 0) doubleSellPrice = 1;
//                    double progress = doubleSellPrice * doubleSellCount / sellCountBalance;
                    double progress = doubleSellCount / sellCountBalance;
                    sellSeekBar.setProgress(progress * 1000 >= 1000 ? 1000 : (float) progress * 1000);
                } else {
                    tvSellTradeCount.setText(getString(R.string.text_entrust) + " " + String.valueOf("--"));
//                    新家的
                    if (sellCountBalance == 0) sellCountBalance = 1;
//                    double progress = doubleSellPrice * doubleSellCount;//
//                    sellSeekBar.setProgress(progress * 1000 >= 1000 ? 1000 : (float) progress * 1000);//
                    double progress = doubleSellCount / sellCountBalance;
                    sellSeekBar.setProgress(progress * 1000 >= 1000 ? 1000 : (float) progress * 1000);
                }

                // 卖出的量变化的时候，下面的Bar也要跟着变化
//                if (sellCountBalance == 0) sellCountBalance = 1;
//                double progress = doubleSellCount / sellCountBalance;
//                sellSeekBar.setProgress(progress * 1000 >= 1000 ? 1000 : (float) progress * 1000);
                int posDot = strSellCount.indexOf(".");
                if (posDot <= 0) return;
                if (strSellCount.length() - (posDot + 1) > coinScale) {
                    editable.delete(posDot + 1 + coinScale, posDot + 2 + coinScale);
                }
            }
            if (intType == 0 || intType == 1) {
                String temp = editable.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - (posDot + 1) > baseCoinScale) {
                    editable.delete(posDot + 1 + baseCoinScale, posDot + 2 + baseCoinScale);
                }
            } else {
                String temp = editable.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - (posDot + 1) > coinScale) {
                    editable.delete(posDot + 1 + coinScale, posDot + 2 + coinScale);
                }
            }
        }
    }

    private void setViewListener() {
        tradeBuyOrSellConfirmDialog.getTvConfirm().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitEntrust();
                tradeBuyOrSellConfirmDialog.dismiss();
            }
        });
        dialog.setOnCancelOrder(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.cancelEntrust(orderId);
                dialog.dismiss();
            }
        });
        sellAdapter.setOnItemClickListener(onItemClickListener);
        buyAdapter.setOnItemClickListener(onItemClickListener);
        trustAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Entrust entrust = (Entrust) adapter.getItem(position);
                orderId = entrust.getOrderId();
                if (entrust != null) {
                    dialog.setEntrust(entrust);
                    dialog.show();
                }
            }
        });
        limitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setViewByType(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etBuyPrice.addTextChangedListener(new MyTextWatcher(0));
        etSellPrice.addTextChangedListener(new MyTextWatcher(1));
        etBuyCount.addTextChangedListener(new MyTextWatcher(2));
        etSellCount.addTextChangedListener(new MyTextWatcher(3));

        buySeekBar.setOnProgressChangedListener(new MyProgressChangedListener(0));
        sellSeekBar.setOnProgressChangedListener(new MyProgressChangedListener(1));


        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rbBuy: // 点击买入
                        llMarketBuy.setVisibility(View.VISIBLE);
                        llMarketSell.setVisibility(View.GONE);
                        break;
                    case R.id.rbSell: // 点击卖出
                        llMarketBuy.setVisibility(View.GONE);
                        llMarketSell.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    public class MyProgressChangedListener implements BubbleSeekBar.OnProgressChangedListener {
        private int intType; // 0-买，1-卖

        public MyProgressChangedListener(int intTyepe) {
            this.intType = intTyepe;
        }

        @Override
        public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i, float v) {

        }

        @Override
        public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float v) {
            if (currency == null)
                return;
            if (intType == 0) {
                if (type.equals("LIMIT_PRICE")) {
                    try {
                        double price = Double.valueOf(etBuyPrice.getText().toString());
                        if (price == 0) price = 1;
                        double d = buyCountBalance_two / price;
                        etBuyCount.setText(MathUtils.getRundNumber(d * progress / 1000, 4, null));
                    } catch (Exception e) {
                        etBuyCount.setText(MathUtils.getRundNumber(progress * progress / 1000, 4, null));
                    }
                } else {
                    etBuyCount.setText((Double.valueOf(MathUtils.getRundNumber(buyCountBalance, 2, null)) * progress) / 1000 + "");
//                    String.valueOf(MathUtils.getRundNumber(buyCountBalance, 2, null)
//                    etBuyCount.setText(MathUtils.getRundNumber(buyCountBalance * progress / 1000, 4, null));
//                    etBuyCount.setText(MathUtils.getRundNumber(Double.valueOf(usdeBalance) * progress / 1000, 4, null));
                }
            } else {
                if (type.equals("LIMIT_PRICE")) {
                    try {
                        double price = Double.valueOf(etSellPrice.getText().toString());
                        if (price == 0) price = 1;
//                        double d = sellCountBalance / price;
                        double d = sellCountBalance_two;
                        etSellCount.setText(MathUtils.getRundNumber(d * progress / 1000, 4, null));
                    } catch (Exception e) {
                        etSellCount.setText(MathUtils.getRundNumber(progress * progress / 1000, 4, null));
                    }
                } else {
//                    etSellCount.setText((int) (sellCountBalance * progress / 1000));
                    etSellCount.setText(MathUtils.getRundNumber(sellCountBalance * progress / 1000, 4, null));
                }
//                etSellCount.setText(MathUtils.getRundNumber(sellCountBalance * progress / 1000, 4, null));
            }
        }

        @Override
        public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int i, float v) {

        }
    }

    /**
     * 根据市价还是限价显示view
     */
    private void setViewByType(int intType) {
        type = (intType == 0 ? GlobalConstant.LIMIT_PRICE : GlobalConstant.MARKET_PRICE);
        llBuyPrice.setVisibility(intType == 0 ? View.VISIBLE : View.GONE);
        llSellPrice.setVisibility(intType == 0 ? View.VISIBLE : View.GONE);
        tvBestPriceSell.setVisibility(intType == 0 ? View.GONE : View.VISIBLE);
        tvBestPriceBuy.setVisibility(intType == 0 ? View.GONE : View.VISIBLE);
        llBuyTradeCount.setVisibility(intType == 0 ? View.VISIBLE : View.INVISIBLE);
        llSellTradeCount.setVisibility(intType == 0 ? View.VISIBLE : View.INVISIBLE);
        if (currency != null) {
            //String symbol = (intType == 0 ? currency.getSymbol().substring(0, currency.getSymbol().indexOf("/")) : currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length()));
            tvBuySymbol.setText(currency.getSymbol());
            tvSellSymbol.setText(currency.getSymbol());
        }
        etBuyCount.setHint(intType == 0 ? getString(R.string.text_number) : getString(R.string.text_entrust));
        tvBuyRMB.setVisibility(intType == 0 ? View.VISIBLE : View.INVISIBLE);
        tvSellRMB.setVisibility(intType == 0 ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 判断是否有收藏的数据
     *
     * @param symbol
     * @return
     */
    private boolean addFace(String symbol) {
        for (Favorite favorite : MainActivity.mFavorte) {
            if (symbol.equals(favorite.getSymbol())) return true;
        }
        return false;
    }

    /**
     * 获取当前委托
     */
    private void getCurrentEntrust() {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", "0");
        map.put("pageSize", "3");
        map.put("symbol", currency.getSymbol());
        mPresenter.getCurrentEntrust(map);
    }

    /**
     * 获取钱包
     *
     * @param intType 0-获取全部，1-获取strFirst，2-获取strSec
     */
    private void getWallet(int intType) {
        isGetWallet = true;
        String symbol = currency.getSymbol();
        String strFirst = symbol; //symbol.substring(0, currency.getSymbol().indexOf("/"));
        String strSec = CommonUtils.getUnitBySymbol(symbol);
        if (intType == 0) {
            mPresenter.getWallet(1, UrlFactory.getWalletUrl() + strFirst);
            mPresenter.getWallet(2, UrlFactory.getWalletUrl() + strSec);
        } else if (intType == 1) {
            mPresenter.getWallet(1, UrlFactory.getWalletUrl() + strFirst);
        } else {
            mPresenter.getWallet(2, UrlFactory.getWalletUrl() + strSec);
        }
    }

    /**
     * 获取盘口信息或精度
     */
    private void getExchangeAndSymbolInfo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", currency.getSymbol());
        mPresenter.getExchange(map);
        mPresenter.getSymbolInfo(map);
    }

    /**
     * 提交委托
     */
    private void commitEntrust() {
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", currency.getSymbol());
        map.put("price", price);
        map.put("amount", amout);
        String direction = (rbBuy.isChecked() ? GlobalConstant.BUY : GlobalConstant.SELL);
        map.put("direction", direction);
        map.put("type", type);
        mPresenter.commitEntrust(map);
    }

    @Override
    public void addCollectSuccess(String message) {
        ToastUtils.showToast(getString(R.string.text_add_success));
        isFace = true;
        ivCollect.setImageDrawable(getResources().getDrawable(R.mipmap.icon_collect_hover));
//        ((MainActivity) activity).find();
        setResult(RESULT_OK);
    }

    @Override
    public void deleteCollectSuccess(String message) {
        ToastUtils.showToast(getString(R.string.text_cancel_success));
        isFace = false;
        ivCollect.setImageDrawable(getResources().getDrawable(R.mipmap.icon_collect_normal));
//        ((MainActivity) activity).find();
        setResult(RESULT_OK);
    }

    private SafeSetting safeSetting;

    @Override
    public void safeSettingSuccess(SafeSetting obj) {
        if (obj == null)
            return;
        safeSetting = obj;
        if (safeSetting.getRealVerified() == 0) {
            ShiMingDialog shiMingDialog = new ShiMingDialog(this);
            String name = safeSetting.getRealNameRejectReason();
            if (safeSetting.getRealVerified() == 0) {
                if (safeSetting.getRealAuditing() == 1) {
                    shiMingDialog.setEntrust(7, name, 1);
                } else {
                    if (safeSetting.getRealNameRejectReason() != null)
                        shiMingDialog.setEntrust(8, name, 1);
                    else
                        shiMingDialog.setEntrust(9, name, 1);
                }
            } else {
                shiMingDialog.setEntrust(6, name, 1);
            }
            shiMingDialog.show();
        } else {
            buyOrSell(from);
        }
    }

    /**
     * 获取精度
     *
     * @param mCoinScale
     * @param mBaseCoinScale
     */
    @Override
    public void getSymbolInfoResult(final int mCoinScale, final int mBaseCoinScale) {
        coinScale = mCoinScale;// 数量
        baseCoinScale = mBaseCoinScale; // 价格
        if (sellAdapter == null || buyAdapter == null) return;
        sellAdapter.setText(new SellAdapter.myText() {
            @Override
            public int one() {
                return mCoinScale;
            }

            @Override
            public int two() {
                return mBaseCoinScale;
            }
        });
        sellAdapter.notifyDataSetChanged();
        buyAdapter.setText(new SellAdapter.myText() {
            @Override
            public int one() {
                return mCoinScale;
            }

            @Override
            public int two() {
                return mBaseCoinScale;
            }
        });
        buyAdapter.notifyDataSetChanged();
    }

    /**
     * 获取当前委托成功
     *
     * @param entrusts
     */
    @Override
    public void getCurrentEntrustSuccess(List<Entrust> entrusts) {
        tvLogin.setVisibility(View.GONE);
        if (entrusts != null && entrusts.size() > 0) {
            tvEmpty.setVisibility(View.GONE);
            recyclerCurrentEntrust.setVisibility(View.VISIBLE);
            this.entrustList.clear();
            this.entrustList.addAll(entrusts);
            trustAdapter.notifyDataSetChanged();
        } else {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerCurrentEntrust.setVisibility(View.GONE);
        }
    }


    private Money obj_money;
    /**
     * 获取钱包成功
     */
    @Override
    public void getWalletSuccess(Money obj, int type) {
        obj_money = obj;
        isGetWallet = false;
        String symbol = currency.getSymbol();
        switch (type) {
            case 1: // 可卖
                if (obj.getCode() == 0 && obj.getData() != null) {
                    sellCountBalance = obj.getData().getBalance();
                    sellCountBalance_two = obj.getData().getBalance();
                    tvCanSell.setText(getString(R.string.text_can_sell) + String.valueOf(MathUtils.getRundNumber(sellCountBalance, 2, null) + symbol));
                } else {
                    sellCountBalance = 0.0;
                    tvCanSell.setText(getString(R.string.text_can_sell) + String.valueOf(sellCountBalance +
                            symbol));
                }
                break;
            case 2: // 可用
                if (obj.getCode() == 0 && obj.getData() != null) {
                    if ("USDT".equals(CommonUtils.getUnitBySymbol(symbol))) {
                        usdeBalance = obj.getData().getBalance();
                    }
                    buyCountBalance = obj.getData().getBalance();
                    buyCountBalance_two = obj.getData().getBalance();
                    tvBuyCanUse.setText(getString(R.string.text_can_used) + String.valueOf(MathUtils.getRundNumber(buyCountBalance, 2, null) +
                            CommonUtils.getUnitBySymbol(symbol)));
                } else {
                    buyCountBalance = 0.0;
                    tvBuyCanUse.setText(getString(R.string.text_can_used) + String.valueOf(buyCountBalance + CommonUtils.getUnitBySymbol(symbol)));
                }
                break;
            case 3:
                buyCountBalance = 0.0;
                sellCountBalance = 0.0;
                tvCanSell.setText(getString(R.string.text_can_sell) + String.valueOf("0.0" +
                        symbol));
                tvBuyCanUse.setText(getString(R.string.text_can_used) + String.valueOf("0.0" + CommonUtils.getUnitBySymbol(symbol)));
                break;
        }
    }

    /**
     * 提交委托成功
     *
     * @param message
     */
    @Override
    public void commitEntrustSuccess(String message) {
        ToastUtils.showToast(message);
        etBuyCount.setText("");
        etSellCount.setText("");
        if (MyApplication.getApp().isLogin() && this.currency != null) {
            getCurrentEntrust();
            getWallet(0);
        }
    }

    /**
     * 盘口的数据
     */
    @Override
    public void getExchangeSuccess(List<Exchange> ask, List<Exchange> bid) {
        this.sellExchangeList.clear();
        this.buyExchangeList.clear();
        for (int i = 0; i < 5; i++) {
            sellExchangeList.add(new Exchange(5 - i, "--", "--"));
            buyExchangeList.add(new Exchange(i, "--", "--"));
        }
        if (ask.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                sellExchangeList.set(4 - i, new Exchange(i + 1, ask.get(i).getPrice(), ask.get(i).getAmount()));
            }
        } else {
            for (int i = 0; i < ask.size(); i++) {
                sellExchangeList.set(4 - i, new Exchange(i + 1, ask.get(i).getPrice(), ask.get(i).getAmount()));
            }
        }
        sellAdapter.notifyDataSetChanged();
        if (bid.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                buyExchangeList.set(i, new Exchange(i, bid.get(i).getPrice(), bid.get(i).getAmount()));
            }
        } else {
            for (int i = 0; i < bid.size(); i++) {
                buyExchangeList.set(i, new Exchange(i, bid.get(i).getPrice(), bid.get(i).getAmount()));
            }
        }
        buyAdapter.notifyDataSetChanged();
    }

    /**
     * 取消某个委托成功
     */
    @Override
    public void cancelSuccess(String success) {
        if (MyApplication.getApp().isLogin() && this.currency != null) {
            getCurrentEntrust();
            getWallet(0); // 应该还需要刷新下钱包的接口
        }
    }

    /**
     * 所有数据请求失败的地方
     *
     * @param e
     * @param msg
     */
    @Override
    public void doPostFail(int e, String msg) {
        NetCodeUtils.checkedErrorCode(this, e, msg);
        if (isGetWallet) {
            getWalletSuccess(null, 3);
        }
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
     * 这里我开始订阅某个币盘口的信息
     */
    private void startTCP(String symbol, String type) {
        String st = "market." + symbol + "_" + type + ".depth.step10";
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_DEPTH,
                buildGetBodyJson(st, "1").toString())); // 需要id
        oldSymbol = symbol;
    }

    /**
     * 这里我取消某个币盘口的信息
     */
    private void stop(String symbol, String type) {
//        EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.UNSUBSCRIBE_EXCHANGE_TRADE,
//                buildGetBodyJson(symbol, id).toString()));
        String st = "market." + symbol + "_" + type + ".depth.step10";
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_DEPTH,
                buildGetBodyJson(st, "0").toString())); // 需要id

    }

    public void showPageFragment(String symbol, int pageNo) {
        if (currency != null) {
            try {
                if (GlobalConstant.LIMIT_PRICE.equals(type))
                    tvBuySymbol.setText(symbol);
                tvSellSymbol.setText(symbol);
                btnBuy.setText(String.valueOf(getString(R.string.text_buy_in) + symbol));
                btnSale.setText(String.valueOf(getString(R.string.text_sale_out) + symbol));
                getExchangeAndSymbolInfo();
                setTCPBySymbol();
                if (pageNo == 0) { // 买入
                    mRadioGroup.check(R.id.rbBuy);
                } else { //卖出
                    mRadioGroup.check(R.id.rbSell);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 代表选择了哪个币种，需要重新订阅 应该先取消原来的再订阅现在的
     */
    private void setTCPBySymbol() {
        if (StringUtils.isNotEmpty(oldSymbol))
            stop(oldSymbol, currency.getType());
        startTCP(currency.getSymbol(), currency.getType());
    }

    /**
     * 买入或者卖出
     *
     * @param intType 0-买入，1-卖出
     */
    private void buyOrSell(int intType) {
        if (!MyApplication.getApp().isLogin()) {
            ToastUtils.showToast(getString(R.string.text_login_first));
            return;
        }
        if (currency == null) return;
        String total = "";
        String lastUnit = currency.getSymbol();
        String fistUnit = currency.getType();
        switch (type) {
            case GlobalConstant.LIMIT_PRICE:
                price = (intType == 0 ? etBuyPrice.getText().toString().trim() : etSellPrice.getText().toString().trim());
                amout = (intType == 0 ? etBuyCount.getText().toString().trim() : etSellCount.getText().toString().trim());
                total = (intType == 0 ? String.valueOf(tvBuyTradeCount.getText().toString()) : String.valueOf(tvSellTradeCount.getText().toString()));
                break;
            case GlobalConstant.MARKET_PRICE:
                price = "0.0";
                amout = (intType == 0 ? etBuyCount.getText().toString().trim() : etSellCount.getText().toString().trim());
                total = (intType == 0 ? etBuyCount.getText().toString().trim() + lastUnit : "--" + lastUnit);
                break;
        }
        if (StringUtils.isEmpty(amout, price)) {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        } else if (Double.parseDouble(amout) == 0) {
            ToastUtils.showToast("委托数量不能为0");
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("type", intType == 0 ? GlobalConstant.BUY : GlobalConstant.SELL);
            if (type.equals(GlobalConstant.LIMIT_PRICE)) {
                map.put("amount", amout + fistUnit);
                map.put("price", price + lastUnit);
            } else {
                map.put("price", getString(R.string.text_best_prices));
                map.put("amount", (intType == 0 ? "--" : etSellCount.getText().toString().trim()) + fistUnit);
            }
            map.put("total", total);
            tradeBuyOrSellConfirmDialog.setDataParams(map);
            tradeBuyOrSellConfirmDialog.show();
        }
    }

    /**
     * 盘口信息的返回 和 当前委托的返回
     *
     * @param response SocketResponse
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(SocketResponse response) {
        NEWCMD cmd = response.getCmd();
        if (cmd == null) return;

        if (cmd == NEWCMD.SUBSCRIBE_SYMBOL_DEPTH){
            LogUtils.i("盘口返回数据===" + response.getResponse());
            setResponse(response);
        }

        switch (response.getCmd()) {
//            case PUSH_EXCHANGE_PLATE:
//                try {

            //setResponse(response.getResponse());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case PUSH_EXCHANGE_ORDER_CANCELED:
//            case PUSH_EXCHANGE_ORDER_COMPLETED:
//            case PUSH_EXCHANGE_ORDER_TRADE:
//                if (MyApplication.getApp().isLogin() && this.currency != null) {
//                    getCurrentEntrust();
//                }
//                break;
//            default:
//                break;
        }
    }

    private void setResponse(SocketResponse response) {
        String res = response.getResponse();
        if (StringUtils.isEmpty(res)) return; // 如果返回为空不处理
        SymbolStep items = new Gson().fromJson(res, SymbolStep.class);
        if (items == null) return;
        if (!response.getRemark().equals(this.currency.getSymbol())) { // 这里加了层判断，如果推送来的币不是当前选择的币则不处理
            return;
        }

        this.sellExchangeList.clear();
        for (int i = 0; i < 5; i++) {
            sellExchangeList.add(new Exchange(5 - i, "--", "--"));
        }
        List<List<Double>> sells = items.getAsks();
        if (sells.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                sellExchangeList.set(4 - i, new Exchange(i + 1, sells.get(i).get(0), sells.get(i).get(1)));
            }
        } else {
            for (int i = 0; i < sells.size(); i++) {
                sellExchangeList.set(4 - i, new Exchange(i + 1, sells.get(i).get(0), sells.get(i).get(1)));
            }
        }
        sellAdapter.notifyDataSetChanged();

        this.buyExchangeList.clear();
        for (int i = 0; i < 5; i++) {
            buyExchangeList.add(new Exchange(5 - i, "--", "--"));
        }
        List<List<Double>> buys = items.getBids();
        if (buys.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                buyExchangeList.set(i, new Exchange(i, buys.get(i).get(0), buys.get(i).get(1)));
            }
        } else {
            for (int i = 0; i < buys.size(); i++) {
                buyExchangeList.set(i, new Exchange(i, buys.get(i).get(0), buys.get(i).get(1)));
            }
        }
        buyAdapter.notifyDataSetChanged();

//        if (GlobalConstant.SELL.equals(items.getSells())) { // 卖
//
//        } else { // 买
//
//        }

    }

}
