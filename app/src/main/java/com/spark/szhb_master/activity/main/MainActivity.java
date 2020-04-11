package com.spark.szhb_master.activity.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.main.presenter.HeyuePresenterImpl;
import com.spark.szhb_master.activity.main.presenter.MyPresenterImpl;
import com.spark.szhb_master.activity.main.presenter.C2CPresenterImpl;
import com.spark.szhb_master.activity.main.presenter.MainPresenter;
import com.spark.szhb_master.activity.main.presenter.HomePresenterImpl;
import com.spark.szhb_master.activity.main.presenter.MarketPresenterImpl;
import com.spark.szhb_master.activity.main.presenter.USDTMarketFragment;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.VersionDialogFragment;
import com.spark.szhb_master.entity.CommonSymbol;
import com.spark.szhb_master.entity.NewCurrency;
import com.spark.szhb_master.factory.socket.NEWCMD;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.base.BaseTransFragmentActivity;
import com.spark.szhb_master.entity.Currency;
import com.spark.szhb_master.entity.Favorite;
import com.spark.szhb_master.entity.Vision;
import com.spark.szhb_master.serivce.MyTextService;
import com.spark.szhb_master.serivce.SocketMessage;
import com.spark.szhb_master.serivce.SocketResponse;
import com.spark.szhb_master.utils.CommonUtils;
import com.spark.szhb_master.utils.FileUtils;
import com.spark.szhb_master.utils.LogUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.PermissionUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.VersionCompareUtil;
import com.spark.szhb_master.utils.okhttp.FileCallback;
import com.spark.szhb_master.utils.okhttp.OkhttpUtils;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;
import okhttp3.Request;

public class MainActivity extends BaseTransFragmentActivity implements MainContract.View, MarketBaseFragment.MarketOperateCallback,
        HomeFragment.OperateCallback, PreheatingFragment.OnHeadlineSelectedListener, DiggingFragment.OnHeadlineSelectedListener,
        TradeFragment.OperateCallback {
    @BindView(R.id.flContainer)
    FrameLayout flContainer;
    @BindView(R.id.llHome)
    LinearLayout llHome;
    @BindView(R.id.llMarket)
    LinearLayout llMarket;
    @BindView(R.id.llTrade)
    LinearLayout llTrade;

    @BindView(R.id.llC2C)
    LinearLayout llC2C;
    @BindView(R.id.llMy)
    LinearLayout llMy;
    @BindView(R.id.llTab)
    LinearLayout llTab;
    @BindView(R.id.ivClose)
    ImageButton ivClose;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vpMenu)
    ViewPager vpMenu;
    @BindView(R.id.dlRoot)
    DrawerLayout dlRoot;
    @BindArray(R.array.home_two_top_tab)
    String[] titles;
    private int currentPage;
    private List<Currency> currencies = new ArrayList<>();
    private List<Currency> currenciesTwo = new ArrayList<>();

    private List<NewCurrency> mCurrencies = new ArrayList<>();
    private List<NewCurrency> mCurrenciesTwo = new ArrayList<>();
    private List<NewCurrency> mCurrencyListAll = new ArrayList<>();

    private List<Currency> currencyListAll = new ArrayList<>();
    private List<Currency> baseUsdt = new ArrayList<>();
    private List<Currency> baseBtc = new ArrayList<>();
    private List<Currency> baseEth = new ArrayList<>();
    private List<BaseFragment> menusFragments = new ArrayList<>();

    private HomeFragment homeFragment;
    //private MarketFragment marketFragment;
    private HeyueFragment heyueFragment;
    private TradeFragment tradeFragment;
    private C2CFragment c2cFragment;
    private MyFragment myFragment;
    private TreasureFragment treasureFragment;

    private USDTMarketFragment usdtMarketFragment;
    private BTCMarketFragment btcMarketFragment;
    private ETHMarketFragment ethMarketFragment;
    private SelfSelectionragment favoriteFragment;

    private MainContract.Presenter presenter;
    private long lastPressTime = 0;
    private int type; // 1 去买币  2 去卖币
    private LinearLayout[] lls;
    private Gson gson = new Gson();
    private boolean hasNew = false;
    private ProgressDialog progressDialog;
    public static double rate = 1.0;
    public static boolean isAgain = false;
    public static List<Favorite> mFavorte = new ArrayList<>();
    private Vision vision;
    private Bundle savedInstanceState;

    public MainContract.Presenter getPresenter() {
        return presenter;
    }

    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }
    private final String mPageName = "MainActivity";

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        //EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.SUBSCRIBE_SYMBOL_THUMB, null));

        //首页货币信息推送
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_HOME_TRADE,
                buildGetBodyJson("market.overview3", 1).toString())); // 需要id


        //合约推送
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SIDE_TRADE,
                buildGetBodyJson("market.overview", 1).toString())); // 需要id

        if (isAgain) {
            isAgain = false;
            if (StringUtils.isNotEmpty(getToken())) {
                presenter.find();
            }
        }
        if (MyApplication.app.isLogin()) {
//            Intent intent = new Intent(MainActivity.this, MyService.class);
//            startService(intent);
            //SocketMessage message = new SocketMessage(1, ISocket.CMD.SUBSCRIBE_GROUP_CHAT, buildGetBodyJson().toString());
            //EventBus.getDefault().post(message);
        }
        hasNew = SharedPreferenceInstance.getInstance().getHasNew();
//        homeFragment.setChatTip(hasNew);
        SharedPreferenceInstance.getInstance().saveHasNew(false);
        if (!MyApplication.getApp().isLogin() && currencyListAll != null && currencyListAll.size() != 0) { // 退出登录后 刷新展示未登录状态的数据
            notLoginCurrencies();
        }
        if (MyApplication.getApp().isLoginStatusChange()) { // 登录回来，刷新展示登录状态的数据
            presenter.allCurrency();
            MyApplication.getApp().setLoginStatusChange(false);
        }

    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }

    /**
     * 以下是点击了K线页面的买入 卖出 后的页面跳转逻辑
     */
    public void skipTradeView(Bundle bundle) {
        if (bundle != null) {
            type = bundle.getInt("type");
            String symbol = bundle.getString("symbol");
            if (type == 0) return; // 默认值 或是 不需要跳转 就返回
            selecte(llTrade, 2);
            if (tradeFragment != null)
                tradeFragment.showPageFragment(symbol, type - 1); // 当type=1 交易fragment就显示买入，对应的page就是0 （即type -1），当type=2，同理
            bundle.putInt("type", 0);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        int page = savedInstanceState.getInt("page");
        selecte(lls[page], page);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("page", currentPage);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferenceInstance.getInstance().saveIsOpen(true);
        EventBus.getDefault().register(this);
        MobclickAgent.openActivityDurationTrack(false);//还有fragment的activity页面需要先设置false
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fragment!=null){
            fragment.dismiss();
        }
        stopService(new Intent(MainActivity.this, MyTextService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.UNSUBSCRIBE_SYMBOL_THUMB, null));
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (lastPressTime == 0 || now - lastPressTime > 2 * 1000) {
            ToastUtils.showToast(getString(R.string.exit_again));
            lastPressTime = now;
        } else if (now - lastPressTime < 2 * 1000) super.onBackPressed();

    }


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        startService(new Intent(activity, MyTextService.class));
        //startService(new Intent(activity, GroupService.class));
        initProgressDialog();
        new MainPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        startService(new Intent(activity, MyTextService.class)); // 开启服务
        //startService(new Intent(activity, GroupService.class));
        lls = new LinearLayout[]{llHome, llMarket, llTrade, llC2C, llMy};
        reCoveryView();
        initPresenter();
        fragment = new VersionDialogFragment(this);
    }

    @Override
    protected void initData() {
        super.initData();
        initPager();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initJump();
    }

    private int bibi = 0;

    private void initJump() {
        Intent intent = getIntent();
        bibi = intent.getIntExtra("bibi", 1);
        if (bibi == 2) {
            selecte(llMarket, 1);
            intent.removeExtra("bibi");
        }
    }

    /**
     * 初始化tab栏
     */
    private void initPager() {
//        vpMenu.setOffscreenPageLimit(3);
//        List<String> titles = Arrays.asList(this.titles);
//        vpMenu.setAdapter(new PagerAdapter(getSupportFragmentManager(), menusFragments, titles));
//        tab.setupWithViewPager(vpMenu);
    }


    /**
     * 避免出现activity被销毁，fragment重新创建，造成界面重叠
     */
    private void reCoveryView() {
        if (fragments.size() == 0) {
            recoverFragment();
        }
        if (savedInstanceState == null) {
            hideFragment(homeFragment);
            selecte(llTrade, 2);
            selecte(llHome, 0);
            addFragments();
        } else {
            recoverMenuFragment();
        }
    }

    private void addFragments() {
//        int type = MarketBaseFragment.MarketOperateCallback.TYPE_SWITCH_SYMBOL;
//        if (usdtMarketFragment == null)
//            menusFragments.add(usdtMarketFragment = USDTMarketFragment.getInstance(type));
//        if (btcMarketFragment == null)
//            menusFragments.add(btcMarketFragment = BTCMarketFragment.getInstance(type));
//        if (ethMarketFragment == null)
//            menusFragments.add(ethMarketFragment = ETHMarketFragment.getInstance(type));
//        if (favoriteFragment == null)
//            menusFragments.add(favoriteFragment = SelfSelectionragment.getInstance(type));

    }

    private void recoverMenuFragment() {
//        usdtMarketFragment = (USDTMarketFragment) getSupportFragmentManager().findFragmentByTag(BaseFragment.makeFragmentName(vpMenu.getId(), 0));
//        btcMarketFragment = (BTCMarketFragment) getSupportFragmentManager().findFragmentByTag(BaseFragment.makeFragmentName(vpMenu.getId(), 1));
//        ethMarketFragment = (ETHMarketFragment) getSupportFragmentManager().findFragmentByTag(BaseFragment.makeFragmentName(vpMenu.getId(), 2));
//        favoriteFragment = (SelfSelectionragment) getSupportFragmentManager().findFragmentByTag(BaseFragment.makeFragmentName(vpMenu.getId(), 3));
//        menusFragments.add(usdtMarketFragment);
//        menusFragments.add(btcMarketFragment);
//        menusFragments.add(ethMarketFragment);
//        menusFragments.add(favoriteFragment);

    }

    /**
     * 初始化presenter
     */
    private void initPresenter() {
        new HomePresenterImpl(Injection.provideTasksRepository(getApplicationContext()), homeFragment);
        new HeyuePresenterImpl(Injection.provideTasksRepository(getApplicationContext()), heyueFragment);
        //new MarketPresenterImpl(Injection.provideTasksRepository(getApplicationContext()), marketFragment);
        new C2CPresenterImpl(Injection.provideTasksRepository(getApplicationContext()), c2cFragment);
        new MyPresenterImpl(Injection.provideTasksRepository(getApplicationContext()), myFragment);
    }

    @OnClick({R.id.llHome, R.id.llMarket, R.id.llTrade, R.id.llC2C, R.id.llMy, R.id.ivClose})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llHome:
                selecte(v, 0);
                break;
            case R.id.llMarket:
                selecte(v, 1);
                break;
            case R.id.llTrade:
                selecte(v, 2);
                break;

            case R.id.llC2C:
                selecte(v, 3);
                break;
            case R.id.llMy:
                selecte(v, 4);
                break;
            case R.id.ivClose:
                dlRoot.closeDrawers();
                break;
        }

    }

    @Override
    protected void loadData() {
        presenter.getNewVersion();
        presenter.getRate();
        presenter.homeCurrency();
        presenter.allCurrency();
    }

    private void tcpNotify() {
        homeFragment.tcpNotify();
        //marketFragment.tcpNotify();
        //heyueFragment.tcpNotify();
//        treasureFragment.tcpNotify();
        tradeFragment.tcpNotify();
        //usdtMarketFragment.tcpNotify();
        //btcMarketFragment.tcpNotify();
        //ethMarketFragment.tcpNotify();
        //favoriteFragment.tcpNotify();
    }

    /**
     * socket 推送过来的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketMessage(SocketResponse response) {
        NEWCMD cmd = response.getCmd();
        if (cmd == NEWCMD.SUBSCRIBE_HOME_TRADE) {
            // 如果是盘口返回的信息
            try {
                List<NewCurrency> newDatas = gson.fromJson(response.getResponse(), new TypeToken<List<NewCurrency>>() {}.getType());
                if (newDatas == null) return;
                if (mCurrencies.size() == 0){
                    mCurrencies.addAll(newDatas);
                    homeFragment.dataLoaded(mCurrencies,mCurrencies);
                }else{
                    for (NewCurrency localdata : mCurrencies) {
                        for (NewCurrency newdata : newDatas){
                            if (localdata.equals(newdata)) {
                                NewCurrency.shallowClone(localdata, newdata);
                                break;
                            }
                        }
                    }
                }

//                for (Currency currency : currenciesTwo) {
//                    if (temp.getSymbol().equals(currency.getSymbol())) {
//                        Currency.shallowClone(currency, temp);
//                        break;
//                    }
//                }
//                for (Currency currency : currencyListAll) {
//                    if (temp.getSymbol().equals(currency.getSymbol())) {
//                        Currency.shallowClone(currency, temp);
//                        break;
//                    }
//                }
                //tcpNotify();
                homeFragment.tcpNotify();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        else if (cmd == NEWCMD.SUBSCRIBE_SIDE_TRADE){
            try {
                List<NewCurrency> newDatas = gson.fromJson(response.getResponse(), new TypeToken<List<NewCurrency>>() {}.getType());
                if (newDatas == null) return;
                if (mCurrencyListAll.size() == 0){
                    mCurrencyListAll.addAll(newDatas);
                    heyueFragment.dataLoaded(mCurrencyListAll);
                }else{
                    for (NewCurrency localdata : mCurrencyListAll) {
                        for (NewCurrency newdata : newDatas){
                            if (localdata.equals(newdata)) {
                                NewCurrency.shallowClone(localdata, newdata);
                                break;
                            }
                        }
                    }
                }
                heyueFragment.tcpNotify();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public void find() {
        presenter.find();
    }

    @Override
    public void findSuccess(List<Favorite> obj) {
        if (obj == null) return;
        mFavorte.clear();
        mFavorte.addAll(obj);
        for (Currency currency : currencyListAll) {
            currency.setCollect(false);
        }
        for (Currency currency : currencyListAll) {
            for (Favorite favorite : mFavorte) {
                if (favorite.getSymbol().equals(currency.getSymbol())) {
                    currency.setCollect(true);
                }
            }
        }
//        if (marketFragment != null)
//            marketFragment.dataLoaded(baseUsdt, baseBtc, baseEth, currencyListAll);
        if (favoriteFragment != null) favoriteFragment.dataLoaded(currencyListAll);
    }

    @Override
    public void findFail(Integer code, String toastMessage) {
    }

    @Override
    public void getNewVersionSuccess(Vision obj) {
        vision = obj;
        if (!PermissionUtils.isCanUseStorage(activity)) {
            checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
        } else {
            showUpDialog(obj);
        }
    }

    @Override
    public void getNewVersionFail(Integer code, String toastMessage) {

    }

    @Override
    public void getRateSuccess(double obj) {
        rate = obj;
    }


    @Override
    public void getRateFail(Integer code, String toastMessage) {
        rate = 0;
    }


    @Override
    public void allCurrencySuccess(Object obj) {
        setCurrency((List<Currency>) obj);
    }

    @Override
    public void allCurrencyFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
        homeFragment.showEmptyView();
    }

    @Override
    public void homeCurrencySuccess(String obj) {
        try {
            JsonObject object = new JsonParser().parse((String) obj).getAsJsonObject();
            JsonArray array = object.getAsJsonArray("changeRank").getAsJsonArray();
            List<Currency> objs = gson.fromJson(array, new TypeToken<List<Currency>>() {
            }.getType());

            JsonArray array1 = object.getAsJsonArray("recommend").getAsJsonArray();
            List<Currency> currency1 = gson.fromJson(array1, new TypeToken<List<Currency>>() {
            }.getType());
            this.currenciesTwo.clear();
            this.currenciesTwo.addAll(currency1);
            this.currencies.clear();
            this.currencies.addAll(objs);
            //if (homeFragment != null) homeFragment.dataLoaded(currencies, currenciesTwo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void homeCurrencyFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    private void selecte(View v, int page) {
        currentPage = page;
        llHome.setSelected(false);
        llMarket.setSelected(false);
        llC2C.setSelected(false);
        llMy.setSelected(false);
        llTrade.setSelected(false);
        v.setSelected(true);
        showFragment(fragments.get(page));
        if (currentFragment == fragments.get(2)) {
            dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void initProgressDialog() {
        //创建进度条对话框
        progressDialog = new ProgressDialog(this);
        //设置标题
        progressDialog.setTitle(getString(R.string.downloading_tag));
        //设置信息
        progressDialog.setMessage(getString(R.string.downloading_crazy_tag));
        progressDialog.setProgress(0);
        //设置显示的格式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }


    @Override
    protected void initFragments() {
        if (homeFragment == null) fragments.add(homeFragment = new HomeFragment());
        //if (marketFragment == null) fragments.add(marketFragment = new MarketFragment());
        if (heyueFragment == null) fragments.add(heyueFragment = new HeyueFragment());
        if (tradeFragment == null) fragments.add(tradeFragment = new TradeFragment());
        if (c2cFragment == null) fragments.add(c2cFragment = new C2CFragment());
        if (myFragment == null) fragments.add(myFragment = new MyFragment());
        if (treasureFragment == null) fragments.add(treasureFragment = new TreasureFragment());
    }

    @Override
    protected void recoverFragment() {
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        heyueFragment = (HeyueFragment) getSupportFragmentManager().findFragmentByTag(HeyueFragment.TAG);
        //marketFragment = (MarketFragment) getSupportFragmentManager().findFragmentByTag(MarketFragment.TAG);
        tradeFragment = (TradeFragment) getSupportFragmentManager().findFragmentByTag(TradeFragment.TAG);
        c2cFragment = (C2CFragment) getSupportFragmentManager().findFragmentByTag(C2CFragment.TAG);
        myFragment = (MyFragment) getSupportFragmentManager().findFragmentByTag(MyFragment.TAG);

        treasureFragment = (TreasureFragment) getSupportFragmentManager().findFragmentByTag(TreasureFragment.TAG);
        if (homeFragment == null) fragments.add(homeFragment = new HomeFragment());
        else fragments.add(homeFragment);
        if (heyueFragment == null) fragments.add(heyueFragment = new HeyueFragment());
        else fragments.add(heyueFragment);
        if (tradeFragment == null) fragments.add(tradeFragment = new TradeFragment());
        else fragments.add(tradeFragment);
        if (c2cFragment == null) fragments.add(c2cFragment = new C2CFragment());
        else fragments.add(c2cFragment);
        if (myFragment == null) fragments.add(myFragment = new MyFragment());
        else fragments.add(myFragment);
        if (treasureFragment == null) fragments.add(treasureFragment = new TreasureFragment());
        else fragments.add(treasureFragment);
    }

    @Override
    public int getContainerId() {
        return R.id.flContainer;
    }

    public DrawerLayout getDlRoot() {
        return dlRoot;
    }

    private void notLoginCurrencies() {
        for (Currency currency : currencyListAll) {
            currency.setCollect(false);
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void setCurrency(List<Currency> obj) {
        if (obj == null || obj.size() == 0) return;
        this.currencyListAll.clear();
        this.currencyListAll.addAll(obj);
        baseUsdt = Currency.baseCurrencies(currencyListAll, "USDT");
        baseBtc = Currency.baseCurrencies(currencyListAll, "BTC");
        baseEth = Currency.baseCurrencies(currencyListAll, "ETH");
        if (MyApplication.getApp().isLogin()) {
            presenter.find();
        }
        setData(); // 请求成功为前三个模块设置数据，这里所有数据源都是这里的currencies，几个fragment共用，这段内存数据 一改全改
        if (tradeFragment != null)
            tradeFragment.setViewContent(currencyListAll); // 当请求成功 为交易fragment设置初始交易对 即symbol值
    }


    private void setData() {
        //homeFragment.dataLoaded(currencies, currenciesTwo);
        //marketFragment.dataLoaded(baseUsdt, baseBtc, baseEth, currencyListAll);
        //usdtMarketFragment.dataLoaded(baseUsdt);
        //btcMarketFragment.dataLoaded(baseBtc);
        //ethMarketFragment.dataLoaded(baseEth);
        //favoriteFragment.dataLoaded(currencyListAll);
    }

    @Override
    public void itemClick(Currency currency, int type) {
        if (type == MarketBaseFragment.MarketOperateCallback.TYPE_SWITCH_SYMBOL) {
            dlRoot.closeDrawers();
            tradeFragment.resetSymbol(currency);
        } else if (type == MarketBaseFragment.MarketOperateCallback.TYPE_TO_KLINE) {
            Bundle bundle = new Bundle();
            bundle.putString("symbol", currency.getSymbol());
            bundle.putInt("type", 0);
//            showActivity(KlineActivity.class, bundle, 0);

            selecte(llTrade, 2);
            if (tradeFragment != null)
                tradeFragment.showPageFragment(currency.getSymbol(), type - 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            skipTradeView(bundle);
        }
    }


    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(MainActivity.this).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    showUpDialog(vision);
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    ToastUtils.showToast(getString(R.string.storage_permission));
                    break;
            }
        }
    };
    private VersionDialogFragment fragment;

    private void showUpDialog(final Vision obj) {
        if (obj.getData() == null) return;

        String getVersion = CommonUtils.getVersionName(activity).replace(".","");//本地的
        String vers = obj.getData().getVersion().replace(".","");//服务器的

        int i = VersionCompareUtil.compareVersion(obj.getData().getVersion(),CommonUtils.getVersionName(activity));
        if (i == 1){
//        if (Integer.valueOf(getVersion) > Integer.valueOf(vers)){
//        if (!CommonUtils.getVersionName(activity).equals(obj.getData().getVersion())) {

//            new AlertDialog.Builder(MainActivity.this)
//                    .setIcon(null)
//                    .setTitle("ATC")
//                    .setMessage(getString(R.string.confirm_update))
//                    .setPositiveButton(getString(R.string.text_yes), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (obj.getData().getDownloadUrl() == null || "".equals(obj.getData().getDownloadUrl())) {
//                                ToastUtils.showToast(getString(R.string.update_address_error_tag));
//                            } else {
//                                download(obj.getData().getDownloadUrl());
//                            }
//                        }
//                    })
//                    .setNegativeButton(getString(R.string.text_no), null)
//                    .show();
            if (fragment != null) {
                fragment.show();
                fragment.setEntrust(obj.getData().getVersion(), obj.getData().getRemark(), obj.getData().getPublishTime());

//            fragment.setCancelable(false);

                fragment.tvConfirm_version().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 升级
//                    download(obj.getData().getDownloadUrl());
                        if (obj.getData().getDownloadUrl() == null || "".equals(obj.getData().getDownloadUrl())) {
                            ToastUtils.showToast(getString(R.string.update_address_error_tag));
                        } else download(obj.getData().getDownloadUrl());
                        fragment.dismiss();
                    }
                });
            }

//            fragment.setCallback(new VersionDialogFragment.OperateVersionCallback() {
//                @Override
//                public void confirm() {
//                    // 升级
////                    download(obj.getData().getDownloadUrl());
//                    if (obj.getData().getDownloadUrl() == null || "".equals(obj.getData().getDownloadUrl())) {
//                        ToastUtils.showToast(getString(R.string.update_address_error_tag));
//                    } else download(obj.getData().getDownloadUrl());
//                }
//            });
        }
    }

    private void download(String url) {
        OkhttpUtils.get().url(url).build().execute(new FileCallback(FileUtils.getCacheSaveFile(this, "application.apk").getAbsolutePath()) {
            @Override
            public void inProgress(float progress) {
                progressDialog.show();
                progressDialog.setProgress((int) (progress * 100));
            }

            @Override
            public void onError(Request request, Exception e) {
                progressDialog.dismiss();
                NetCodeUtils.checkedErrorCode(MainActivity.this, GlobalConstant.OKHTTP_ERROR, "null");
            }

            @Override
            public boolean onResponse(File response) {
                progressDialog.dismiss();
                CommonUtils.installAPk(response);
                return false;
            }
        });
    }

    private JSONObject buildGetBodyJson(String value, int id) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("value", value);
            if (id != 0) // 不需要id
                obj.put("type", id);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }

    private JSONObject buildGetBodyJson() {
        JSONObject obj = new JSONObject();
        try {
            //obj.put("orderId", orderDetial.getOrderSn());
            //obj.put("uid", orderDetial.getMyId());
            if (MyApplication.getApp().getCurrentUser() == null) {
                obj.put("uid", null);
            } else obj.put("uid", MyApplication.getApp().getCurrentUser().getId());
            LogUtils.i(String.valueOf(obj.get("uid")));
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }


    @Override
    public void tofabi() {
        currentPage = 3;
        llHome.setSelected(false);
        llMarket.setSelected(false);
        llC2C.setSelected(true);
        llMy.setSelected(false);
        llTrade.setSelected(false);
        showFragment(fragments.get(3));
        if (currentFragment == fragments.get(2))
            dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        else dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onArticleSelected(int position) {
        treasureFragment.referfragment(position);
    }

    @Override
    public void GoneLine() {
        llTab.setVisibility(View.GONE);
    }

    @Override
    public void tohome() {
        currentPage = 0;
        llHome.setSelected(false);
        llMarket.setSelected(true);
        llC2C.setSelected(false);
        llMy.setSelected(false);
        llTrade.setSelected(false);
        showFragment(fragments.get(1));
        llTab.setVisibility(View.VISIBLE);
        if (currentFragment == fragments.get(2))
            dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        else dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    //10.19更改
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (currentPage == 1){
//            TradeFragment.onKeyDown(keyCode, event);
//        }
//        return super.onKeyDown(keyCode, event);
//    }


}
