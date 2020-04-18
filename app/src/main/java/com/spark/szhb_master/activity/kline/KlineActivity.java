package com.spark.szhb_master.activity.kline;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.chartingtwo.charts.LineChart;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.MinuteChartView;
import com.github.tifezh.kchartlib.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.Trade.TradeActivity;
import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.activity.mychart.DataParse;
import com.spark.szhb_master.activity.mychart.KLineBean;
import com.spark.szhb_master.activity.mychart.MinutesBean;
import com.spark.szhb_master.adapter.MyPagerAdapter;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.entity.ChartBean;
import com.spark.szhb_master.entity.Exchange;
import com.spark.szhb_master.entity.NewCurrency;
import com.spark.szhb_master.entity.SymbolBean;
import com.spark.szhb_master.entity.SymbolStep;
import com.spark.szhb_master.factory.socket.NEWCMD;
import com.spark.szhb_master.ui.CustomViewPager;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Currency;
import com.spark.szhb_master.entity.Favorite;
import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.serivce.SocketMessage;
import com.spark.szhb_master.serivce.SocketResponse;
import com.spark.szhb_master.ui.MyViewPager;
import com.spark.szhb_master.ui.kchart.DataHelper;
import com.spark.szhb_master.ui.kchart.KChartAdapter;
import com.spark.szhb_master.ui.kchart.KLineEntity;
import com.spark.szhb_master.ui.kchart.MinuteLineEntity;
import com.spark.szhb_master.utils.DateUtils;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

public class KlineActivity extends BaseActivity implements KlineContract.View, View.OnClickListener {
    @BindView(R.id.tvCurrencyName)
    TextView tvCurrencyName;
    @BindView(R.id.llLandText)
    LinearLayout llLandText;
    @BindView(R.id.kDataText)
    TextView mDataText;
    @BindView(R.id.kDataOne)
    TextView mDataOne;
    @BindView(R.id.kCount)
    TextView kCount;
    @BindView(R.id.kUp)
    TextView kUp;
    @BindView(R.id.kLow)
    TextView kLow;
    @BindView(R.id.kLandDataText)
    TextView kLandDataText;
    @BindView(R.id.kLandDataOne)
    TextView kLandDataOne;
    @BindView(R.id.kLandCount)
    TextView kLandCount;
    @BindView(R.id.kLandUp)
    TextView kLandUp;
    @BindView(R.id.kLandLow)
    TextView kLandLow;
    @BindView(R.id.tab)
    LinearLayout tab;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.kLandRange)
    TextView kLandRange;
    @BindView(R.id.kRange)
    TextView kRange;
    @BindView(R.id.viewPager)
    MyViewPager viewPager;
    @BindView(R.id.tv_collect)
    TextView mTvCollect; // 收藏的意思
    @BindView(R.id.ivBack)
    ImageButton ivBack;
    @BindView(R.id.tvMore)
    TextView tvMore;
    @BindView(R.id.tvIndex)
    TextView tvIndex;
    @BindView(R.id.llAllTab)
    LinearLayout llAllTab;
    @BindView(R.id.llVertical)
    LinearLayout llVertical;
    @BindView(R.id.llState)
    LinearLayout llState;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.vpDepth)
    CustomViewPager depthPager;
    @BindView(R.id.llDepthTab)
    TabLayout depthTab;

    @BindView(R.id.lineChart)
    LineChart mLineChart;
    @BindView(R.id.lineChartbuy)
    LineChart lineChartbuy;
    private KChartView kChartView;
//    private MinuteChartView minuteChartView;
    private ArrayList<TextView> textViews;
    private ArrayList<View> views;
    private TextView selectedTextView;
    private KChartAdapter kChartAdapter;
    private int type;
    private String symbol = "";
    private String resolution;
    private KlineContract.Presenter presenter;
    private ArrayList<KLineBean> kLineDatas;     // K线图数据
    private SymbolBean mCurrency;
    private List<Currency> currencies = new ArrayList<>();
    private boolean isStart = false;
    private Date startDate;
    private Date endDate;
    private ProgressBar mProgressBar;
    private boolean isFace = false;
    private boolean isPopClick;
    private TextView maView;
    private TextView bollView;
    private TextView macdView;
    private TextView kdjView;
    private TextView rsiView;
    private TextView hideChildView;
    private TextView hideMainView;
    private int childType = -1;
    private boolean isVertical;
    private boolean isFirstLoad = true;
    private List<BaseFragment> fragments = new ArrayList<>();
    private PagerAdapter adapter;
    private List<String> tabs;
    private int currentCount;
//    private String[] titleArray;
    private boolean isInit = true;
    private NewCurrency currency;
    private String symbolType;
    private DataParse kData = new DataParse();

    private String typeLists[] = {"1min", "5min", "15min", "30min", "60min","4hour","1day", "1mon"};
    private int listType = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isVertical) {
                finish();
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 切换横竖屏
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
        tab.removeAllViews();
        moreTabLayout.removeAllViews();
        textViews = new ArrayList<>();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
            isVertical = false;
            llState.setVisibility(View.GONE);
            llLandText.setVisibility(View.VISIBLE);
            llVertical.setVisibility(View.GONE);
            ivBack.setVisibility(View.GONE);
            depthTab.setVisibility(View.GONE);
            depthPager.setVisibility(View.GONE);
            params.height = LinearLayout.LayoutParams.MATCH_PARENT;
            viewPager.setLayoutParams(params);
            currentCount = 6;
            initTextView(currentCount);
            intMoreTab(currentCount);
            if (type == GlobalConstant.TAG_30_MINUTE) {
                isPopClick = false;
            }
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            isVertical = true;
            llState.setVisibility(View.VISIBLE);
            llLandText.setVisibility(View.INVISIBLE);
            llVertical.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            depthTab.setVisibility(View.VISIBLE);
            depthPager.setVisibility(View.VISIBLE);
            params.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 340, getResources().getDisplayMetrics()));
            viewPager.setLayoutParams(params);
            currentCount = 5;
            initTextView(currentCount);
            intMoreTab(currentCount);
            if (type == GlobalConstant.TAG_30_MINUTE) {
                isPopClick = true;
            }
        }
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            KChartView kChartView = view.findViewById(R.id.kchart_view);
//            MinuteChartView minuteChartView = view.findViewById(R.id.minuteChartView);
            if (i != 0) {
                if (isVertical) {
                    kChartView.setGridRows(4);
                    kChartView.setGridColumns(4);
                } else {
                    kChartView.setGridRows(3);
                    kChartView.setGridColumns(8);
                }
            }
        }
        setPagerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        String st = "market." + symbol + "_" + symbolType + ".klist." + typeLists[listType];
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_KLIST,
                buildGetBodyJson(st, "0").toString())); //

        String sthead = "market." + symbol + "_" + symbolType + ".detail";
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_DETAIL,
                buildGetBodyJson(sthead, "0").toString()));
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_kline;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
//        titleArray = activity.getResources().getStringArray(R.array.k_line_tab);
        isVertical = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        new KlinePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        textViews = new ArrayList<>();
        views = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
            currency = (NewCurrency) bundle.getSerializable("currency");
            symbolType = currency.getType();
            tvCurrencyName.setText(symbol);
            isFace = addFace(symbol);
            if (symbol != null) {
                String[] s = symbol.split("/");
                tvBuy.setText(String.valueOf(getString(R.string.text_buy_in) + s[0]));
                tvSell.setText(String.valueOf(getString(R.string.text_sale_out) + s[0]));
            }
        }
        if (isFace) { // 已经收藏
            mTvCollect.setText(getString(R.string.text_collected));
            mTvCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_collect_hover), null, null);
        } else {
            mTvCollect.setText(getString(R.string.text_add_favorite));
            mTvCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_collect_normal), null, null);
        }
        startTCP();
    }

//    private LineChartManager lineChartManager2;
//    private LineChartManager lineChartManager;
//
//    private void initchart() {
//        lineChartManager2 = new LineChartManager(mLineChart);
//        lineChartManager = new LineChartManager(lineChartbuy);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInit) {
            isInit = false;
            List<String> titles = Arrays.asList(typeLists);
            if (titles != null) {
                initViewpager(titles);
                currentCount = 5;
                initTextView(currentCount);
                initPopWindow(currentCount);
                initDepthData();
            }
            selectedTextView = textViews.get(1);
            type = (int) selectedTextView.getTag();
            viewPager.setCurrentItem(1);
//            initDepthData();
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
//        presenter.allCurrency();
    }


    @OnClick({R.id.ivBack, R.id.ivFullScreen, R.id.tvSell, R.id.tvBuy, R.id.tv_collect, R.id.tvMore, R.id.tvIndex})
    @Override
    protected void setOnClickListener(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                return;
            case R.id.ivFullScreen:
                if (isVertical) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }
                return;
            case R.id.tvSell:
            case R.id.tvBuy:
                Bundle bundle = new Bundle();
                bundle.putString("symbol", symbol);
                bundle.putString("comefrom", "comefrom");
                bundle.putSerializable("currency", currency);
                if (view.getId() == R.id.tvBuy) {
                    bundle.putInt("type", 1);
                } else {
                    bundle.putInt("type", 2);
                }
                Intent intent = new Intent(this, TradeActivity.class);
                intent.putExtras(bundle);
//                setResult(RESULT_OK, intent);
                showActivity(TradeActivity.class,bundle,0);
                finish();
                return;
            case R.id.tv_collect:
                MainActivity.isAgain = true;
                deleteOrCollect();
                return;
            case R.id.tvMore:
                tvMore.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_hover));
                tvIndex.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
                moreTabLayout.setVisibility(View.VISIBLE);
                indexLayout.setVisibility(View.GONE);
                break;
            case R.id.tvIndex:
                tvMore.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
                tvIndex.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_hover));
                moreTabLayout.setVisibility(View.GONE);
                indexLayout.setVisibility(View.VISIBLE);
                break;
        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAsDropDown(llAllTab);
        }
    }

    /**
     * 添加收藏或取消收藏
     */
    private void deleteOrCollect() {
        if (!MyApplication.getApp().isLogin()) {
            ToastUtils.showToast(getString(R.string.text_login_first));
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        if (isFace) { // 删除
            presenter.doDelete(map);
        } else {
            presenter.doCollect(map);
        }

    }

    private PopupWindow popupWindow;
    private LinearLayout moreTabLayout;
    private LinearLayout indexLayout;

    /**
     * 初始化popwindow
     *
     * @param count
     */
    private void initPopWindow(int count) {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.layout_kline_popwindow, null);
        initPopChidView(contentView);
        intMoreTab(count);
        popupWindow = new PopupWindow(activity);
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
    }

    /**
     * 设置more显示内容
     *
     * @param count
     */
    private void intMoreTab(int count) {
        List<String> titles = Arrays.asList(typeLists);
        for (int i = count; i < titles.size(); i++) {
            View popTextView = LayoutInflater.from(activity).inflate(R.layout.textview_pop, null);
            TextView textView = popTextView.findViewById(R.id.tvPop);
            LinearLayout tvLayout = popTextView.findViewById(R.id.tvLayout);
            tvLayout.removeAllViews();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(layoutParams);
            textView.setPadding(ViewUtil.Dp2Px(activity, 20), 0, 0, 0);
            textView.setText(titles.get(i));
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isPopClick = true;
                    selectedTextView = (TextView) view;
                    int selectedTag = (int) selectedTextView.getTag();
                    type = selectedTag;
                    viewPager.setCurrentItem(selectedTag);
                    popupWindow.dismiss();
                }
            });
            moreTabLayout.addView(textView);
            textViews.add(textView);
        }
    }

    /**
     * 设置tab栏显示内容
     *
     * @param count
     */
    private void initTextView(int count) {
        List<String> titles = Arrays.asList(typeLists);
        for (int i = 0; i < titles.size(); i++) {
            if (i < count) {
                View popTextView = LayoutInflater.from(activity).inflate(R.layout.textview_pop, null);
                TextView textView = popTextView.findViewById(R.id.tvPop);
                LinearLayout tvLayout = popTextView.findViewById(R.id.tvLayout);
                tvLayout.removeAllViews();
                textView.setText(titles.get(i));
                textView.setTag(i);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isPopClick = false;
                        selectedTextView = (TextView) view;
                        int selectedTag = (int) selectedTextView.getTag();
                        type = selectedTag;
                        viewPager.setCurrentItem(selectedTag);
                    }
                });
                textViews.add(textView);
                tab.addView(textView);
            }
        }
    }

    /**
     * 初始化popwindow里的控件
     *
     * @param contentView
     */
    private void initPopChidView(View contentView) {
        moreTabLayout = contentView.findViewById(R.id.tabPop);
        indexLayout = contentView.findViewById(R.id.llIndex);
        maView = contentView.findViewById(R.id.tvMA);
        maView.setSelected(true);
        maView.setOnClickListener(this);
        bollView = contentView.findViewById(R.id.tvBOLL);
        bollView.setOnClickListener(this);
        macdView = contentView.findViewById(R.id.tvMACD);
        kdjView = contentView.findViewById(R.id.tvKDJ);
        rsiView = contentView.findViewById(R.id.tvRSI);
        hideMainView = contentView.findViewById(R.id.tvMainHide);
        hideMainView.setOnClickListener(this);
        macdView = contentView.findViewById(R.id.tvMACD);
        macdView.setOnClickListener(this);
        kdjView = contentView.findViewById(R.id.tvKDJ);
        kdjView.setOnClickListener(this);
        rsiView = contentView.findViewById(R.id.tvRSI);
        rsiView.setOnClickListener(this);
        hideChildView = contentView.findViewById(R.id.tvChildHide);
        hideChildView.setSelected(true);
        hideChildView.setOnClickListener(this);
    }

    /**
     * socket 推送过来的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketMessage(SocketResponse response) {
        if (response.getCmd() == NEWCMD.SUBSCRIBE_SYMBOL_KLIST) {    // 如果是盘口返回的信息
            try {
                List<ChartBean> currencyList = new Gson().fromJson(response.getResponse(), new TypeToken<List<ChartBean>>() {}.getType());

                //setCurrentcy(currencyList);

                try {
                    kData.parseKLine(currencyList, type);
                    kLineDatas = kData.getKLineDatas();
                    if (kLineDatas != null && kLineDatas.size() > 0) {
                        ArrayList<KLineEntity> kLineEntities = new ArrayList<>();
                        for (int i = 0; i < kLineDatas.size(); i++) {
                            KLineEntity lineEntity = new KLineEntity();
                            KLineBean kLineBean = kLineDatas.get(i);
                            lineEntity.setDate(kLineBean.getDate());
                            lineEntity.setOpen(kLineBean.getOpen());
                            lineEntity.setClose(kLineBean.getClose());
                            lineEntity.setHigh(kLineBean.getHigh());
                            lineEntity.setLow(kLineBean.getLow());
                            lineEntity.setVolume(kLineBean.getVol());
                            kLineEntities.add(lineEntity);
                        }
                        kChartAdapter.addFooterData(DataHelper.getALL(activity, kLineEntities));
                        kChartView.startAnimation();
                        kChartView.refreshEnd();
                    } else {
                        kChartView.refreshEnd();
                    }
                } catch (Exception e) {
                    ToastUtils.showToast(getString(R.string.parse_error));
                }

            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showToast("解析出错");
            }
        }else if (response.getCmd() == NEWCMD.SUBSCRIBE_SYMBOL_DETAIL){
            SymbolBean symbolBean = new Gson().fromJson(response.getResponse(), SymbolBean.class);
            setCurrentcy(symbolBean);
        }
    }

    /**
     * 加载k线数据
     */
    private void loadKLineData() {
//        if (type != GlobalConstant.TAG_DIVIDE_TIME)
//            kChartView.showLoading();
//        else
//        kChartView.showLoading();
//        mProgressBar.setVisibility(View.VISIBLE);
//        Long to = System.currentTimeMillis();
//        endDate = DateUtils.getDate("HH:mm", to);
//        Long from = to;
//        switch (type) {
////            case GlobalConstant.TAG_DIVIDE_TIME:
////                Calendar c = Calendar.getInstance();
////                int hour = c.get(Calendar.HOUR_OF_DAY) - 6;
////                c.set(Calendar.HOUR_OF_DAY, hour);
////                String strDate = DateUtils.getFormatTime("HH:mm", c.getTime());
////                startDate = DateUtils.getDateTransformString(strDate, "HH:mm");
////                resolution = 1 + "";
////                String str = DateUtils.getFormatTime(null, c.getTime());
////                from = DateUtils.getTimeMillis(null, str);
////                break;
//            case GlobalConstant.TAG_ONE_MINUTE:
//                from = to - 24L * 60 * 60 * 1000;//前一天数据
//                resolution = 1 + "";
//                break;
//            case GlobalConstant.TAG_FIVE_MINUTE:
//                from = to - 2 * 24L * 60 * 60 * 1000;//前两天数据
//                resolution = 5 + "";
//                break;
//            case GlobalConstant.TAG_THIRTY_MINUTE:
//                from = to - 12 * 24L * 60 * 60 * 1000; //前12天数据
//                resolution = 30 + "";
//                break;
//            case GlobalConstant.TAG_AN_HOUR:
//                from = to - 24 * 24L * 60 * 60 * 1000;//前 24天数据
//                resolution = 1 + "H";
//                break;
//            case GlobalConstant.TAG_DAY:
//                from = to - 60 * 24L * 60 * 60 * 1000; //前60天数据
//                resolution = 1 + "D";
//                break;
//            case GlobalConstant.TAG_MONTH:
//                from = to - 1095 * 24L * 60 * 60 * 1000; //前三年数据
//                resolution = 1 + "M";
//                break;
//        }
//        getKLineData(symbol, from, to, resolution);
    }

    /**
     * 获取网络数据
     *
     * @param symbol
     * @param from
     * @param to
     * @param resolution
     */
    private void getKLineData(String symbol, Long from, Long to, String resolution) {
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("from", from + "");
        map.put("to", to + "");
        map.put("resolution", resolution);
        presenter.KData(map);
    }


    /**
     * 头部显示内容
     *
     * @param
     */
    private void setCurrentcy(SymbolBean symbolBean) {
        try {
            mCurrency = symbolBean;
//            for (NewCurrency currency : objs) {
//                if (symbol.equals(currency.getSymbol())) {
//                    mCurrency = currency;
//                    break;
//                }
//            }
            if (mCurrency != null) {
                String strUp = String.valueOf(mCurrency.getHigh());
                String strLow = String.valueOf(mCurrency.getLow());
                String strCount = String.valueOf(mCurrency.getVol());
//                Double douChg = mCurrency.getS();
//                String strRang = MathUtils.getRundNumber(mCurrency.getChg() * 100, 2, "########0.") + "%";
//                String strDataText = "≈" + MathUtils.getRundNumber(mCurrency.getClose() * MainActivity.rate * mCurrency.getBaseUsdRate(),
//                        2, null) + GlobalConstant.CNY;
                String strDataOne = String.valueOf(mCurrency.getClose());
                kUp.setText(strUp);
                kLow.setText(strLow);
                kCount.setText(strCount);
//                kRange.setText(getString(R.string.gains) + mCurrency.getScale() + "%");
                mDataOne.setText(strDataOne);
                mDataText.setText("≈" + mCurrency.getConvert() + "USDT");
                mDataOne.setTextColor(mCurrency.getScale() < 0 ? getResources().getColor(R.color.main_font_red) : getResources().getColor(R.color.main_font_green));
//                kRange.setTextColor(mCurrency.getScale()  < 0 ? getResources().getColor(R.color.main_font_red) : getResources().getColor(R.color.main_font_green));
//                kRange.setTextColor(mCurrency.getScale()  < 0 ? getResources().getColor(R.color.main_font_red) : getResources().getColor(R.color.main_font_green));
                kRange.setBackground(mCurrency.getScale() < 0 ? getResources().getDrawable(R.drawable.bg_kl_corner_red) : getResources().getDrawable(R.drawable.bg_kl_corner_green));
                kLandDataOne.setTextColor(mCurrency.getScale()  < 0 ? getResources().getColor(R.color.main_font_red) : getResources().getColor(R.color.main_font_green));
                kLandUp.setText(strUp);
                kLandLow.setText(strLow);
                kLandCount.setText(strCount);
                kRange.setText((mCurrency.getScale()  < 0 ? "-" : "+") + mCurrency.getScale() +"%");
                kLandDataOne.setText(strDataOne);
                kLandDataText.setText("≈" + mCurrency.getConvert() + "USDT");
            }
            if (!isStart) {
                isStart = true;
                //startTCP();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean addFace(String symbol) {
        for (Favorite favorite : MainActivity.mFavorte) {
            if (symbol.equals(favorite.getSymbol())) return true;
        }
        return false;
    }

    private void startTCP() {
        String klist = "market." + symbol + "_" + symbolType + ".klist." + typeLists[listType];
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_KLIST,
                buildGetBodyJson(klist, "1").toString())); //


        String detail = "market." + symbol + "_" + symbolType + ".detail";
        EventBus.getDefault().post(new SocketMessage(0, NEWCMD.SUBSCRIBE_SYMBOL_DETAIL,
                buildGetBodyJson(detail, "1").toString()));

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
     * 初始化viewpager
     *
     * @param titles
     */
    private void initViewpager(List<String> titles) {
        for (int i = 0; i < titles.size(); i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_kchartview, null);
//            if (i == 0) {
//                minuteChartView = view.findViewById(R.id.minuteChartView);
//                minuteChartView.setVisibility(View.VISIBLE);
//                RelativeLayout mLayout = view.findViewById(R.id.mLayout);
//                mProgressBar = new ProgressBar(activity);
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewUtil.Dp2Px(activity, 50), ViewUtil.Dp2Px(activity, 50));
//                lp.addRule(CENTER_IN_PARENT);
//                mLayout.addView(mProgressBar, lp);
//            } else {
                KChartView kChartView = view.findViewById(R.id.kchart_view);
                initKchartView(kChartView);
                kChartView.setVisibility(View.VISIBLE);
                kChartView.setAdapter(new KChartAdapter());
//            }
            views.add(view);
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(views);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPagerView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置kchartview
     *
     * @param kChartView
     */
    private void initKchartView(KChartView kChartView) {
        kChartView.setCandleSolid(true);
        kChartView.setGridRows(4);
        kChartView.setGridColumns(4);
        kChartView.setOnSelectedChangedListener(new BaseKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(BaseKChartView view, Object point, int index) {
                KLineEntity data = (KLineEntity) point;
            }
        });
    }

    /**
     * viewpager和textview的点击事件
     */
    private void setPagerView() {
        for (int j = 0; j < textViews.size(); j++) {
            textViews.get(j).setSelected(false);
            int tag = (int) textViews.get(j).getTag();
            if (tag == type) {
                if (isPopClick) {
                    tvMore.setText(selectedTextView.getText());
                    tvMore.setSelected(true);
                    tvMore.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
                } else {
                    tvMore.setText(getString(R.string.more));
                    tvMore.setSelected(false);
                    tvMore.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
                    tvIndex.setBackground(getDrawable(R.drawable.shape_bg_kline_tab_normal));
                    textViews.get(j).setSelected(true);
                }
                View view = views.get(j);
//                if (type != GlobalConstant.TAG_DIVIDE_TIME) {
                    kChartView = view.findViewById(R.id.kchart_view);
                    kChartView.setMAandBOLL(maView.isSelected(), bollView.isSelected());
                    kChartView.setChidType(childType);
                    kChartAdapter = (KChartAdapter) kChartView.getAdapter();
                    if (kChartAdapter.getDatas() == null || kChartAdapter.getDatas().size() == 0) {
                        loadKLineData();
                    }
//                } else {
//                    minuteChartView.setMAandBOLL(maView.isSelected(), bollView.isSelected());
//                    if (isFirstLoad)
//                        loadKLineData();
//                }
            } else if (!isPopClick) {
                tvMore.setSelected(false);
            }
        }
    }


    @Override
    public void setPresenter(KlineContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void KDataSuccess(JSONArray obj) {
        DataParse kData = new DataParse();
        switch (type) {
//            case GlobalConstant.TAG_DIVIDE_TIME: // 分时图
//                com.github.tifezh.kchartlib.utils.WonderfulLogUtils.logi("KDataSuccess");
//                mProgressBar.setVisibility(View.GONE);
//                try {
//                    kData.parseMinutes(obj, (float) mCurrency.getLastDayClose());
//                    ArrayList<MinutesBean> objList = kData.getDatas();
//                    if (objList != null && objList.size() > 0) {
//                        ArrayList<MinuteLineEntity> minuteLineEntities = new ArrayList<>();
//                        for (int i = 0; i < objList.size(); i++) {
//                            MinuteLineEntity minuteLineEntity = new MinuteLineEntity();
//                            MinutesBean minutesBean = objList.get(i);
//                            minuteLineEntity.setAvg(minutesBean.getAvprice()); // 成交价
//                            minuteLineEntity.setPrice(minutesBean.getCjprice());
//                            minuteLineEntity.setTime(DateUtils.getDateTransformString(minutesBean.getTime(), "HH:mm"));
//                            minuteLineEntity.setVolume(minutesBean.getCjnum());
//                            minuteLineEntity.setClose(minutesBean.getClose());
//                            minuteLineEntities.add(minuteLineEntity);
//                        }
//                        if (isFirstLoad) {
//                            DataHelper.calculateMA30andBOLL(minuteLineEntities);
//                            minuteChartView.initData(minuteLineEntities,
//                                    startDate,
//                                    endDate,
//                                    null,
//                                    null,
//                                    (float) mCurrency.getLow(), maView.isSelected());
//                            isFirstLoad = false;
//                        }
//                    }
//                } catch (Exception e) {
//                    ToastUtils.showToast(getString(R.string.parse_error));
//                }
//                break;
//            default:
//                try {
//                    kData.parseKLine(obj, type);
//                    kLineDatas = kData.getKLineDatas();
//                    if (kLineDatas != null && kLineDatas.size() > 0) {
//                        ArrayList<KLineEntity> kLineEntities = new ArrayList<>();
//                        for (int i = 0; i < kLineDatas.size(); i++) {
//                            KLineEntity lineEntity = new KLineEntity();
//                            KLineBean kLineBean = kLineDatas.get(i);
//                            lineEntity.setDate(kLineBean.getDate());
//                            lineEntity.setOpen(kLineBean.getOpen());
//                            lineEntity.setClose(kLineBean.getClose());
//                            lineEntity.setHigh(kLineBean.getHigh());
//                            lineEntity.setLow(kLineBean.getLow());
//                            lineEntity.setVolume(kLineBean.getVol());
//                            kLineEntities.add(lineEntity);
//                        }
//                        kChartAdapter.addFooterData(DataHelper.getALL(activity, kLineEntities));
//                        kChartView.startAnimation();
//                        kChartView.refreshEnd();
//                    } else {
//                        kChartView.refreshEnd();
//                    }
//                } catch (Exception e) {
//                    ToastUtils.showToast(getString(R.string.parse_error));
//                }
//
//                break;
        }
    }

    @Override
    public void allCurrencySuccess(List<Currency> obj) {
//        if (obj != null) {
//            currencies.clear();
//            currencies.addAll(obj);
//            setCurrentcy(currencies);
//        }
    }

    @Override
    public void doDeleteOrCollectSuccess(String msg) {
        if (isFace) {
            ToastUtils.showToast(getString(R.string.text_cancel_success));
            mTvCollect.setText(getString(R.string.text_add_favorite));
            isFace = false;
            mTvCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_collect_normal), null, null);
        } else {
            ToastUtils.showToast(getString(R.string.text_add_success));
            isFace = true;
            mTvCollect.setText(getString(R.string.text_collected));
            mTvCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_collect_hover), null, null);
        }

    }

    @Override
    public void dpPostFail(Integer code, String toastMessage) {

    }

    /**
     * 副图的点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMA:
            case R.id.tvBOLL:
            case R.id.tvMainHide:
                if (view.getId() == R.id.tvMA) {
                    maView.setSelected(true);
                    bollView.setSelected(false);
                    hideMainView.setSelected(false);
                } else if (view.getId() == R.id.tvBOLL) {
                    maView.setSelected(false);
                    bollView.setSelected(true);
                    hideMainView.setSelected(false);
                } else {
                    maView.setSelected(false);
                    bollView.setSelected(false);
                    hideMainView.setSelected(true);
                }
//                if (type == GlobalConstant.TAG_DIVIDE_TIME) {
//                    minuteChartView.setMAandBOLL(maView.isSelected(), bollView.isSelected());
//                } else {
                    kChartView.setMAandBOLL(maView.isSelected(), bollView.isSelected());
//                }
                popupWindow.dismiss();
                break;
            case R.id.tvMACD:
            case R.id.tvRSI:
            case R.id.tvKDJ:
            case R.id.tvChildHide:
                if (view.getId() == R.id.tvMACD) {
                    childType = 0;
                    macdView.setSelected(true);
                    rsiView.setSelected(false);
                    kdjView.setSelected(false);
                    hideChildView.setSelected(false);
                } else if (view.getId() == R.id.tvKDJ) {
                    childType = 1;
                    macdView.setSelected(false);
                    rsiView.setSelected(false);
                    kdjView.setSelected(true);
                    hideChildView.setSelected(false);
                } else if (view.getId() == R.id.tvRSI) {
                    childType = 2;
                    macdView.setSelected(false);
                    rsiView.setSelected(true);
                    kdjView.setSelected(false);
                    hideChildView.setSelected(false);
                } else {
                    childType = -1;
                    macdView.setSelected(false);
                    rsiView.setSelected(false);
                    kdjView.setSelected(false);
                    hideChildView.setSelected(true);
                }

                kChartView.setChidType(childType);

                popupWindow.dismiss();
                break;
        }
    }

    /**
     * 初始化深度图数据
     */
    private void initDepthData() {
        fragments.add(DepthFragment.getInstance(symbol,currency.getType()));
        fragments.add(VolumeFragment.getInstance(symbol,currency.getType()));
        String[] tabArray = getResources().getStringArray(R.array.k_line_depth);
        tabs = new ArrayList<>();
        for (int i = 0; i < tabArray.length; i++) {
            tabs.add(tabArray[i]);
        }
        depthPager.setAdapter(adapter = new PagerAdapter(getSupportFragmentManager(), fragments, tabs));
        depthTab.setTabMode(TabLayout.MODE_FIXED);
        depthTab.setupWithViewPager(depthPager);
        depthPager.setOffscreenPageLimit(fragments.size() - 1);
        depthPager.setCurrentItem(0);
    }


    /**
     * 获取深度图数据
     */
//    private void getDepth() {
//        post().url(UrlFactory.getDepth()).addParams("symbol", symbol).build().execute(new StringCallback() {
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//
//            @Override
//            public boolean onResponse(String response) {
//                doLogicData(response);
//                return false;
//            }
//        });

//    }

//    /**
//     * 解析数据
//     *
//     * @param response
//    */
//
//    private List<Exchange> sellExchangeList = new ArrayList<>();
//    private List<Exchange> buyExchangeList = new ArrayList<>();
//    private void doLogicData(String response) {
//        try {
//            gson = new Gson();
//            DepthChart result = gson.fromJson(response, new TypeToken<DepthChart>() {
//            }.getType());
//            if (result != null) {
//                DepthChart.AskBean ask = result.getAsk();
//                if (ask.getMinAmount() == 0 && ask.getMaxAmount()==0)return;
//
////                lineChartBeansell = LocalJsonAnalyzeUtil.JsonToObject(this,"line_lib.json",Linelibbean.class);
//                itemsBeensell = ask.getItems();
//                if (itemsBeensell.size() == 0){
//                    mLineChart.setVisibility(View.GONE);
//                }
//                lineChartManager2.showLineChartwo(itemsBeensell, "累计", getResources().getColor(R.color.kgreen));
//                lineChartManager2.setMarkerView(this);
//
//                DepthChart.BidBean bid = result.getBid();
//                if (bid.getMinAmount() == 0 && bid.getMaxAmount()==0)return;
//
//                itemsBeenBuy = bid.getItems();
//                if(itemsBeenBuy.size() == 0){
//                    lineChartbuy.setVisibility(View.GONE);
//                }
//                lineChartManager.showLineChar(itemsBeenBuy, "累计", getResources().getColor(R.color.kgreen));
//                lineChartManager.setMarkerView(this);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//    private List<DepthChart.AskBean.ItemsBean>itemsBeensell;
//    private List<DepthChart.BidBean.ItemsBeanX>itemsBeenBuy;
//    private Gson gson;
}
