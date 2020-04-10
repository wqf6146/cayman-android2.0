package com.spark.szhb_master.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.entrust.CurrentTrustActivity;
import com.spark.szhb_master.activity.main.presenter.TradePresenter;
import com.spark.szhb_master.activity.kline.KlineActivity;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.adapter.SellAdapter;
import com.spark.szhb_master.adapter.TrustAdapter;
import com.spark.szhb_master.base.BaseTransFragment;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.dialog.TradeBuyOrSellConfirmDialog;
import com.spark.szhb_master.dialog.EntrustDialog;
import com.spark.szhb_master.entity.Currency;
import com.spark.szhb_master.entity.Entrust;
import com.spark.szhb_master.entity.Exchange;
import com.spark.szhb_master.entity.Favorite;
import com.spark.szhb_master.entity.Money;
import com.spark.szhb_master.entity.TextItems;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.serivce.SocketMessage;
import com.spark.szhb_master.serivce.SocketResponse;
import com.spark.szhb_master.utils.CommonUtils;
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
import butterknife.Unbinder;
import config.Injection;

import static android.app.Activity.RESULT_OK;

/**
 * author: wuzongjie
 * time  : 2018/4/17 0017 15:20
 * desc  : 这是我重写的交易界面，在原来上我不知道怎么改了
 */

public class TradeFragment extends BaseTransFragment implements TradeContract.View {
    public static final String TAG = TradeFragment.class.getSimpleName();
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivOpen)
    ImageView ivOpen;
    @BindView(R.id.ivMarket)
    ImageView ivMarket;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.llAll)
    LinearLayout llAll;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
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
    Unbinder unbinder;
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
    private List<Currency> currencies = new ArrayList<>();
    private Currency currency;
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
    private double buyCountBalance = 0; // 可用的币
    private boolean isGetWallet;

    /**
     * 主界面调用
     */
    public void tcpNotify() {
        if (currency != null) {
            tvPrice.setText(String.valueOf(currency.getClose()));
            tvPrice.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) :
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
            tvLatest.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) :
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
            if (GlobalConstant.CNY.equals(CommonUtils.getUnitBySymbol(currency.getSymbol()))) {
                tvMoney.setText(String.valueOf("≈" + MathUtils.getRundNumber(currency.getClose() * 1 * currency.getBaseUsdRate(),
                        2, null) + GlobalConstant.CNY));
            } else {
                tvMoney.setText(String.valueOf("≈" + MathUtils.getRundNumber(currency.getClose() * MainActivity.rate * currency.getBaseUsdRate(),
                        2, null) + GlobalConstant.CNY));
            }
        }
    }

    /**
     * 这个是从主界面来的，表示选择了某个币种
     */
    public void resetSymbol(Currency currency) {
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
    //10.19更改
//    public static boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//        }
//        return true;
//    }


    public interface OperateCallback {
        void GoneLine();

        void tohome();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        immersionBar.flymeOSStatusBarFontColor("#FFFFFF").init();
        if (!isSetTitle) {
            immersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frgment_trade;
    }

    @Override
    protected void initView() {
        super.initView();
        ivBack.setVisibility(View.VISIBLE);
        tvGoto.setVisibility(View.GONE);
        ivOpen.setVisibility(View.INVISIBLE);
        ivMarket.setVisibility(View.VISIBLE);
        ivCollect.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        new TradePresenter(Injection.provideTasksRepository(activity.getApplicationContext()), this);
        limitSpinner.attachDataSource(new LinkedList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.text_type))));
        dialog = new EntrustDialog(activity);
        tradeBuyOrSellConfirmDialog = new TradeBuyOrSellConfirmDialog(activity);
        isLoginStateOld = MyApplication.getApp().isLogin();
        initSellAndBuyView();
        initTrustView();
        tvBuyTradeCount.setText(getString(R.string.text_entrust) + " --");
        tvSellTradeCount.setText(getString(R.string.text_entrust) + " --");
    }
    private boolean isFirst = true;
    private boolean isLoginStateOld;

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst && (isLoginStateOld != MyApplication.getApp().isLogin())) { // 从该界面跳转到登录页，回来刷新数据
            checkLogin();
        }
        isLoginStateOld = MyApplication.getApp().isLogin();
//        if (!MyApplication.getApp().isLogin()) {
//            tvLogin.setVisibility(View.VISIBLE);
//            tvEmpty.setVisibility(View.INVISIBLE);
//        }
        if (currency != null) {
            String symbol = currency.getSymbol();
            setTitle(symbol);
            isFace = addFace(symbol);
            ivCollect.setImageDrawable(isFace ? getResources().getDrawable(R.mipmap.icon_collect_hover) : getResources().getDrawable(R.mipmap.icon_collect_normal));
        }
        llAll.setFocusable(true);
        llAll.setFocusableInTouchMode(true);
        llAll.requestFocus();
        llAll.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                getFocus();
                return false;
            }
        });
        etBuyCount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
//                    getFocus();
                }
                return false;
            }
        });
        etSellCount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
//                    getFocus();
                }
                return false;
            }
        });
        etSellPrice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
//                    getFocus();
                }
                return false;
            }
        });

        etBuyPrice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    //关闭软键盘
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(nickname.getWindowToken(), 0);
                    //使得根View重新获取焦点，以监听返回键
//                    getFocus();
                }
                return false;
            }
        });
    }

    private void checkLogin() {
        if (MyApplication.getApp().isLogin()) {
            getWallet(0);
            getCurrentEntrust();
        } else {
            tvLogin.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            recyclerCurrentEntrust.setVisibility(View.GONE);
        }
        isFirst = false;
    }

    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    ((OperateCallback) getActivity()).tohome();
                    return true;
                }
                return false;
            }
        });
    }

//    private boolean isfirst;
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (isfirst) {
//            loadData();
//        }
//    }

    public static List<Favorite> mFavorte = new ArrayList<>();
    private List<Currency> currencyListAll = new ArrayList<>();

    @Override
    protected void loadData() {
        isNeedLoad = false;
        if (currency != null && MyApplication.getApp().isLogin()) {
            setViewText();
            getWallet(0);
            getCurrentEntrust();
        }
    }


    private void setViewText() {
        if (currency != null) {

            String symbol = currency.getSymbol();
            setTitle(symbol);
            tvPriceTag.setText(getString(R.string.text_price) + "(" + symbol.substring(0, symbol.indexOf("/")) + ")");
            tvCountTag.setText(getString(R.string.text_number) + "(" + CommonUtils.getUnitBySymbol(symbol) + ")");
            isFace = addFace(symbol);
            ivCollect.setImageDrawable(isFace ? getResources().getDrawable(R.mipmap.icon_collect_hover) : getResources().getDrawable(R.mipmap.icon_collect_normal));
            String strClose = String.valueOf(currency.getClose());
            etBuyPrice.setText(strClose);
            etSellPrice.setText(strClose);
            tvPrice.setText(strClose);
            tvPrice.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) :
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
            tvLatest.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) :
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
            String strSymbol = symbol.substring(0, currency.getSymbol().indexOf("/"));
            tvBuySymbol.setText(strSymbol);
            tvSellSymbol.setText(strSymbol);
            btnBuy.setText(String.valueOf(getString(R.string.text_buy_in) + strSymbol));
            btnSale.setText(String.valueOf(getString(R.string.text_sale_out) + strSymbol));
            if (GlobalConstant.CNY.equals(CommonUtils.getUnitBySymbol(symbol))) {
                tvMoney.setText(String.valueOf("≈" + MathUtils.getRundNumber(currency.getClose() * 1 * currency.getBaseUsdRate(),
                        2, null) + GlobalConstant.CNY));
            } else {
                tvMoney.setText(String.valueOf("≈" + MathUtils.getRundNumber(currency.getClose() * MainActivity.rate * currency.getBaseUsdRate(),
                        2, null) + GlobalConstant.CNY));
            }
        }
    }

    /**
     * 初始化委托信息
     */
    private void initTrustView() {
        entrustList = new ArrayList<>();
        recyclerCurrentEntrust.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        recyclerSell.setLayoutManager(new LinearLayoutManager(getActivity()));
        sellAdapter = new SellAdapter(sellExchangeList, 0);
        recyclerSell.setAdapter(sellAdapter);

        recyclerBuy.setLayoutManager(new LinearLayoutManager(getActivity()));
        buyAdapter = new SellAdapter(buyExchangeList, 1);
        recyclerBuy.setAdapter(buyAdapter);
    }

    @OnClick({R.id.ivBack, R.id.tvPrice, R.id.ivCollect, R.id.ivMarket, R.id.tvLogin, R.id.tvBuyAdd, R.id.tvBuyReduce, R.id.tvSellAdd, R.id.tvSellReduce, R.id.tvAll, R.id.btnBuy, R.id.btnSell})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
//            case R.id.ivOpen:
//                ((MainActivity) getActivity()).getDlRoot().openDrawer(Gravity.LEFT);
//                break;
            case R.id.ivBack:
                ((OperateCallback) getActivity()).tohome();
                break;
            case R.id.tvPrice:
                if (rbBuy.isChecked()) {
                    etBuyPrice.setText(tvPrice.getText());
                } else {
                    etSellPrice.setText(tvPrice.getText());
                }
                break;
            case R.id.ivCollect:
                MainActivity.isAgain = true;
                HashMap<String, String> map = new HashMap<>();
                map.put("symbol", currency.getSymbol());
                if (isFace) { // 已经收藏 则删除
                    mPresenter.deleteCollect(map);
                } else {
//                    mPresenter.deleteCollect(map);
                    mPresenter.addCollect(map);
                }
                break;
            case R.id.ivMarket:
                Bundle bundle = new Bundle();
                bundle.putString("symbol", currency.getSymbol());
                showActivity(KlineActivity.class, bundle, 1);
                break;
            case R.id.tvLogin: // 点击登录
                showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
                break;
            case R.id.tvBuyAdd:
                double num4 = Double.valueOf(etBuyPrice.getText().toString());
                etBuyPrice.setText(String.valueOf(num4 + 0.01));
                break;
            case R.id.tvBuyReduce:
                try {
                    double num = Double.valueOf(etBuyPrice.getText().toString());
                    if ((num - 0.01) > 0) {
                        etBuyPrice.setText(String.valueOf(num - 0.01));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvSellAdd:
                double num3 = Double.valueOf(etSellPrice.getText().toString());
                etSellPrice.setText((num3 + 0.01) + "");
                break;
            case R.id.tvSellReduce:
                try {
                    double num1 = Double.valueOf(etSellPrice.getText().toString());
                    if ((num1 - 0.01) > 0) {
                        etSellPrice.setText("" + (num1 - 0.01));
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
                buyOrSell(0);
                break;
            case R.id.btnSell: // 卖出
                buyOrSell(1);
                break;
        }
    }

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
                    ((MainActivity) getActivity()).skipTradeView(bundle);
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

    @Override
    protected String getmTag() {
        return TAG;
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
                            + currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length())));
                    //新家的
                    if (sellCountBalance == 0) sellCountBalance = 1;
                    if (doubleSellPrice == 0) doubleSellPrice = 1;

                    double progress = doubleSellCount / sellCountBalance;

//                    double progress = doubleSellPrice * doubleSellCount / sellCountBalance;
                    sellSeekBar.setProgress(progress * 1000 >= 1000 ? 1000 : (float) progress * 1000);
                } else {
                    tvSellTradeCount.setText(getString(R.string.text_entrust) + " " + String.valueOf("--"));
                    // 数量  etSellCount   价格   etSellPrice
//                    新家的
                    if (sellCountBalance == 0) sellCountBalance = 1;
//                    double progress = doubleSellPrice * doubleSellCount;//
//                    sellSeekBar.setProgress(progress * 1000 >= 1000 ? 1000 : (float) progress * 1000);//
                    double progress = doubleSellCount / sellCountBalance;
//                    double progress = doubleSellCount / usdeBalance;
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

    @Override
    protected void setListener() {
        super.setListener();
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
                        double d = buyCountBalance / price;
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
                        // 数量  etSellCount   价格   etSellPrice
                        double price = Double.valueOf(etSellPrice.getText().toString());
                        if (price == 0) price = 1;
//                        double d = sellCountBalance / price;
                        double d = sellCountBalance;
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
            String symbol = (intType == 0 ? currency.getSymbol().substring(0, currency.getSymbol().indexOf("/")) : currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length()));
            tvBuySymbol.setText(symbol);
            tvSellSymbol.setText(symbol);
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
        String strFirst = symbol.substring(0, currency.getSymbol().indexOf("/"));
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
        ((MainActivity) getmActivity()).find();
    }

    @Override
    public void deleteCollectSuccess(String message) {
        ToastUtils.showToast(getString(R.string.text_cancel_success));
        isFace = false;
        ivCollect.setImageDrawable(getResources().getDrawable(R.mipmap.icon_collect_normal));
        ((MainActivity) getmActivity()).find();
    }

    @Override
    public void safeSettingSuccess(SafeSetting obj) {

    }

    /**
     * 获取精度
     *
     * @param mCoinScale
     * @param mBaseCoinScale
     */
    @Override
    public void getSymbolInfoResult(final int mCoinScale, final int mBaseCoinScale) {
        coinScale = mCoinScale; // 数量
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
        if (entrusts == null) return;
        this.entrustList.clear();
        this.entrustList.addAll(entrusts);
        tvEmpty.setVisibility(entrusts.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        trustAdapter.notifyDataSetChanged();
    }


    /**
     * 获取钱包成功
     */
    @Override
    public void getWalletSuccess(Money obj, int type) {
        isGetWallet = false;
        String symbol = currency.getSymbol();
        switch (type) {
            case 1: // 可卖
                if (obj.getCode() == 0 && obj.getData() != null) {
                    sellCountBalance = obj.getData().getBalance();
                    tvCanSell.setText(getString(R.string.text_can_sell) + String.valueOf(MathUtils.getRundNumber(sellCountBalance, 2, null) + symbol.substring(0, symbol.indexOf("/"))));
                } else {
                    sellCountBalance = 0.0;
                    tvCanSell.setText(getString(R.string.text_can_sell) + String.valueOf(sellCountBalance +
                            symbol.substring(0, currency.getSymbol().indexOf("/"))));
                }
                break;
            case 2: // 可用
                if (obj.getCode() == 0 && obj.getData() != null) {
                    if ("USDT".equals(CommonUtils.getUnitBySymbol(symbol))) {
                        usdeBalance = obj.getData().getBalance();
                    }
                    buyCountBalance = obj.getData().getBalance();
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
                        symbol.substring(0, symbol.indexOf("/"))));
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

    private JSONObject buildGetBodyJson(String symbol, int id) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("symbol", symbol);
            if (id != 0) // 不需要id
                obj.put("uid", id);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 这里我开始订阅某个币盘口的信息
     */
    private void startTCP(String symbol, int id) {
//        EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.SUBSCRIBE_EXCHANGE_TRADE,
//                buildGetBodyJson(symbol, id).toString())); // 需要id
//        oldSymbol = symbol;
    }

    /**
     * 这里我取消某个币盘口的信息
     */
    private void stop(String symbol, int id) {
//        EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.UNSUBSCRIBE_EXCHANGE_TRADE,
//                buildGetBodyJson(symbol, id).toString()));

    }

    //10.19更改
    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (currency != null) {
                try {
                    String symbol = (String) msg.obj;
                    int pageNo = msg.arg1;
                    tvTitle.setText(symbol);
                    if (GlobalConstant.LIMIT_PRICE.equals(type))
                        tvBuySymbol.setText(symbol.substring(0, currency.getSymbol().indexOf("/")));
                    tvSellSymbol.setText(symbol.substring(0, currency.getSymbol().indexOf("/")));
                    btnBuy.setText(String.valueOf(getString(R.string.text_buy_in) + symbol.substring(0, currency.getSymbol().indexOf("/"))));
                    btnSale.setText(String.valueOf(getString(R.string.text_sale_out) + symbol.substring(0, currency.getSymbol().indexOf("/"))));
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
    };
    public void showPageFragment(final String symbol, final int pageNo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    Message message = new Message();
                    message.arg1 = pageNo;
                    message.obj = symbol;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        if (currencies == null) return;
        for (Currency currency : currencies) {
            if (symbol.equals(currency.getSymbol())) {
                this.currency = currency;
                break;
            }
        }

//        ((OperateCallback) getActivity()).GoneLine();
    }

    //10.19更改
//    public void showPageFragment(String symbol, int pageNo) {
//        if (currencies == null) return;
//        for (Currency currency : currencies) {
//            if (symbol.equals(currency.getSymbol())) {
//                this.currency = currency;
//                break;
//            }
//        }
//        if (currency != null) {
//            try {
//                tvTitle.setText(symbol);
//                if (GlobalConstant.LIMIT_PRICE.equals(type))
//                    tvBuySymbol.setText(symbol.substring(0, currency.getSymbol().indexOf("/")));
//                tvSellSymbol.setText(symbol.substring(0, currency.getSymbol().indexOf("/")));
//                btnBuy.setText(String.valueOf(getString(R.string.text_buy_in) + symbol.substring(0, currency.getSymbol().indexOf("/"))));
//                btnSale.setText(String.valueOf(getString(R.string.text_sale_out) + symbol.substring(0, currency.getSymbol().indexOf("/"))));
//                getExchangeAndSymbolInfo();
//                setTCPBySymbol();
//                if (pageNo == 0) { // 买入
//                    mRadioGroup.check(R.id.rbBuy);
//                } else { //卖出
//                    mRadioGroup.check(R.id.rbSell);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        ((OperateCallback) getActivity()).GoneLine();
//    }

    /**
     * 第一次进入界面
     */
    public void setViewContent(List<Currency> currencies) {
        this.currencies.clear();
        this.currencies.addAll(currencies);
        this.currency = currencies.get(0);
        if (this.currency != null) {
            getExchangeAndSymbolInfo();
            setTCPBySymbol();
            setViewText();
        }
    }


    /**
     * 代表选择了哪个币种，需要重新订阅 应该先取消原来的再订阅现在的
     */
    private void setTCPBySymbol() {
        if (StringUtils.isNotEmpty(oldSymbol))
            stop(oldSymbol, getmActivity().getId());
        startTCP(currency.getSymbol(), getmActivity().getId());
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
        String lastUnit = currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length());
        String fistUnit = currency.getSymbol().substring(0, currency.getSymbol().indexOf("/"));
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
        if (response.getCmd() == null) return;
        switch (response.getCmd()) {
//            case PUSH_EXCHANGE_PLATE:
//                try {
//                    LogUtils.i("盘口返回数据===" + response.getResponse());
//                    setResponse(response.getResponse());
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

    private void setResponse(String response) {

        if (StringUtils.isEmpty(response)) return; // 如果返回为空不处理
        TextItems items = new Gson().fromJson(response, TextItems.class);
        if (items == null) return;
        if (!items.getSymbol().equals(this.currency.getSymbol())) { // 这里加了层判断，如果推送来的币不是当前选择的币则不处理
            return;
        }
        if (GlobalConstant.SELL.equals(items.getDirection())) { // 卖
            this.sellExchangeList.clear();
            for (int i = 0; i < 5; i++) {
                sellExchangeList.add(new Exchange(5 - i, "--", "--"));
            }
            List<Exchange> ask = items.getItems();
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
        } else { // 买
            this.buyExchangeList.clear();
            for (int i = 0; i < 5; i++) {
                buyExchangeList.add(new Exchange(5 - i, "--", "--"));
            }
            List<Exchange> bid = items.getItems();
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

    }

}
