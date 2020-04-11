package com.spark.szhb_master.activity.releaseAd;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.country.CountryActivity;
import com.spark.szhb_master.activity.ads.AdsActivity;
import com.spark.szhb_master.adapter.SelectPayWayAdapter;
import com.spark.szhb_master.entity.AccountSetting;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.utils.CommonUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Ads;
import com.spark.szhb_master.entity.CoinInfo;
import com.spark.szhb_master.entity.Country;
import com.spark.szhb_master.entity.PayWay;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.IMyTextChange;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 发布广告
 */
public class PubAdsActivity extends BaseActivity implements ReleaseAdContract.View {
    public static final int REQUEST_COUNTRY = 0;
    @BindView(R.id.ivHelp)
    ImageButton ibHelp;
    @BindView(R.id.tvCoin)
    TextView tvCoin;
    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.tvCoinKind)
    TextView tvCoinKind;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.etOverflow)
    EditText etOverflow;
    @BindView(R.id.etMin)
    EditText etMin;
    @BindView(R.id.etMax)
    EditText etMax;
    @BindView(R.id.tvCountText)
    TextView tvCountText;
    @BindView(R.id.etCount)
    EditText etCount;
    @BindView(R.id.tvPayWayText)
    TextView tvPayWayText;
    @BindView(R.id.tvPayWay)
    TextView tvPayWay;
    @BindView(R.id.tvPayTime)
    TextView tvPayTime;
    @BindView(R.id.etPayTime)
    EditText etPayTime;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.tvRelease)
    TextView tvRelease;
    @BindView(R.id.llTime)
    LinearLayout llTime;
    @BindView(R.id.rbFixed)
    RadioButton rbFixed;
    @BindView(R.id.rbChange)
    RadioButton rbChange;
    @BindView(R.id.rgPriceType)
    RadioGroup rgPriceType;
    @BindView(R.id.rbYes)
    RadioButton rbYes;
    @BindView(R.id.rbNo)
    RadioButton rbNo;
    @BindView(R.id.rgReply)
    RadioGroup rgReply;
    @BindView(R.id.etReplyContent)
    EditText etReplyContent;
    @BindView(R.id.etjyPrice)
    EditText etjyPrice;
    @BindView(R.id.llOverflow)
    LinearLayout llOverflow;
    @BindView(R.id.tvLocalCurrency)
    TextView tvLocalCurrency;
    @BindView(R.id.tvjyPriceCurrency)
    TextView tvjyPriceCurrency;
    @BindView(R.id.tvMinCurrency)
    TextView tvMinCurrency;
    @BindView(R.id.tvMaxCurrency)
    TextView tvMaxCurrency;
    @BindView(R.id.llReply)
    LinearLayout llReply;
    @BindView(R.id.llMsg)
    LinearLayout llMsg;
    @BindView(R.id.llBuyAndSell)
    LinearLayout llBuyAndSell;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    private String advertiseType = "";
    private CoinInfo coinInfo;
    private List<CoinInfo> coinInfos = new ArrayList<>();
    private View payWayView;
    private List<PayWay> payWays;
    private ReleaseAdContract.Presenter presenter;
    private RecyclerView rvpayWay;
    private SelectPayWayAdapter payWayAdapter;
    private AlertDialog payWayDialog;
    private Country country;
    private Ads ads;
    private String[] timeArray;
    private String currency;
    private String[] coinNames;
    private AccountSetting accountSetting;


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_pub_ads;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        payWays = new ArrayList<>();
        timeArray = getResources().getStringArray(R.array.timeLimit);
        new ReleasePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        tvGoto.setVisibility(View.INVISIBLE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tvGoto.setVisibility(View.INVISIBLE);
            advertiseType = bundle.getString("type");
            ads = (Ads) bundle.getSerializable("ads");
            if (advertiseType.equals(GlobalConstant.BUY)) {
//                tvTitle.setText(getString(R.string.text_title));
                tvCountText.setText(getString(R.string.text_buy_num));
                if (ads == null) {
                    llBuyAndSell.setVisibility(View.VISIBLE);
                    tvBuy.setSelected(true);
                    tvSell.setSelected(false);
                    tvTitle.setVisibility(View.GONE);
                } else {
                    tvTitle.setText(getString(R.string.text_title));
                }
            } else if (advertiseType.equals(GlobalConstant.SELL)) {
//                tvTitle.setText(getString(R.string.text_titles));
                tvCountText.setText(getString(R.string.text_sell_num));
                if (ads == null) {
                    llBuyAndSell.setVisibility(View.VISIBLE);
                    tvBuy.setSelected(false);
                    tvTitle.setVisibility(View.GONE);
                    tvSell.setSelected(true);
                } else {
                    tvTitle.setText(getString(R.string.text_title));
                }
            }
            if (ads == null) {
                tvRelease.setText(getString(R.string.create));
            } else {
                tvTitle.setText(getString(R.string.text_ad_change));
                tvRelease.setText(getString(R.string.text_change));
            }
        }
    }

    @Override
    protected void loadData() {
        presenter.all();
        if (ads != null) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", ads.getId() + "");
            presenter.adDetail(map);
        }
    }

    /**
     * 卖出
     */
    private void clickTabSell() {
        tvBuy.setSelected(false);
        tvSell.setSelected(true);
        tvCountText.setText(getString(R.string.text_sell_num));
        advertiseType = GlobalConstant.BUY;
    }

    /**
     * 买入
     */
    private void clickTabBuy() {
        tvBuy.setSelected(true);
        tvSell.setSelected(false);
        tvCountText.setText(getString(R.string.text_buy_num));
        advertiseType = GlobalConstant.SELL;
    }


    @OnClick({R.id.tvCoin, R.id.tvCountry, R.id.tvPayWay, R.id.tvRelease, R.id.tvPayTime,R.id.tvBuy, R.id.tvSell})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvCountry:
                showActivity(CountryActivity.class, null, REQUEST_COUNTRY);
                break;
            case R.id.tvCoin:
                showListDialog(0);
                break;
            case R.id.tvPayTime:
                showListDialog(1);
                break;
            case R.id.tvPayWay:
                if (accountSetting == null) {
                    presenter.getAccountSetting();
                } else if (payWays != null && payWays.size() > 0) {
                    showPayWayDialog();
                } else {
                    ToastUtils.showToast(getString(R.string.bind_account));
                }
                break;
            case R.id.tvRelease:
                releaseOrEditAd();
                break;
            case R.id.tvBuy:
                clickTabBuy();
                break;
            case R.id.tvSell:
                clickTabSell();
                break;
        }

    }

    @Override
    protected void setListener() {
        super.setListener();
        rgPriceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setViewByFixPrice(checkedId);
            }
        });

        rgReply.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rbYes) {
                    llReply.setVisibility(View.VISIBLE);
                } else {
                    llReply.setVisibility(View.GONE);
                }
            }
        });

        etOverflow.addTextChangedListener(new IMyTextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                String priceStr = tvPrice.getText().toString();
                String overStr = etOverflow.getText().toString();
                if (!StringUtils.isVaildText(etOverflow))
                    return;
                if (!StringUtils.isEmpty(priceStr, overStr)) {
                    double price = Double.parseDouble(priceStr);
                    double over = Double.parseDouble(overStr);
                    double finalPrice = price * (1 + 0.01 * over);
                    etjyPrice.setText(MathUtils.getRundNumber(finalPrice, 2, null));
                }
            }
        });
//        etMin.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable s) {
//                minChange();
//            }
//        });
//        etMax.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable s) {
//                maxChange();
//            }
//        });

        etjyPrice.addTextChangedListener(new IMyTextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                setEditText(etjyPrice, 2);
            }
        });

        etCount.addTextChangedListener(new IMyTextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                setEditText(etCount, 6);
            }
        });
    }


    /**
     * 列表类选择框
     *
     * @param type 0-币种,1-付款期限
     */
    private void showListDialog(final int type) {
        NormalListDialog normalDialog = null;
        if (type == 0) {
            normalDialog = new NormalListDialog(activity, coinNames);
            normalDialog.title(getString(R.string.text_coin_type));
        } else if (type == 1) {
            normalDialog = new NormalListDialog(activity, timeArray);
            normalDialog.title(getString(R.string.text_pay_time));
        }
        normalDialog.titleBgColor(getResources().getColor(R.color.main_head_bg));
        final NormalListDialog finalNormalDialog = normalDialog;
        normalDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == 0) {
                    doClickCoinClick(position);
                } else if (type == 1) {
                    tvPayTime.setText(timeArray[position]);
                }
                finalNormalDialog.dismiss();
            }
        });
        normalDialog.show();
    }

    /**
     * 选择币种的点击事件
     *
     * @param position
     */
    private void doClickCoinClick(int position) {
        coinInfo = coinInfos.get(position);
        tvCoin.setText(coinInfo.getUnit());
        String priceStr = coinInfo.getMarketPrice();
        tvPrice.setText(priceStr);
        String overStr = etOverflow.getText().toString();
        if (StringUtils.isNotEmpty(overStr) && StringUtils.isNotEmpty(priceStr)) {
            double price = Double.parseDouble(priceStr);
            double over = Double.parseDouble(overStr);
            double finalPrice = price * (1 + 0.01 * over);
            etjyPrice.setText(MathUtils.getRundNumber(finalPrice, 2, null));
        }
    }

    /**
     * 确认是否直接去上架
     */
    private void showCofirmDialog() {
        final MaterialDialog dialog = new MaterialDialog(activity);
        dialog.title(getString(R.string.warm_prompt)).titleTextColor(getResources().getColor(R.color.colorPrimary)).content(getString(R.string.pub_ads_confirm)).setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        finish();
                        setResult(RESULT_OK);
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        User user = MyApplication.getApp().getCurrentUser();
                        String tvNickName = user.getNick_name();
                        String avatar = user.getAvatar();
                        Bundle bundle = new Bundle();
                        bundle.putString("avatar", avatar == null ? "" : avatar);
                        bundle.putString("username", tvNickName);

                        showActivity(AdsActivity.class, bundle);
                        activity.finish();
                        dialog.superDismiss();
                    }
                });
        dialog.show();
    }


    /**
     * 根据价格显示
     *
     * @param type
     */

    private void setViewByFixPrice(int type) {
        switch (type) {
            case R.id.rbFixed: // 固定价格
                llOverflow.setVisibility(View.GONE);
                etjyPrice.setText("");
                etjyPrice.setHint(getString(R.string.text_enter_trade_price));
                etjyPrice.setEnabled(true);
                break;
            case R.id.rbChange: // 实时价格
                llOverflow.setVisibility(View.VISIBLE);
                etjyPrice.setText("");
                etjyPrice.setHint(getString(R.string.text_auto_calculate_price));
                etjyPrice.setEnabled(false);
                break;
        }
    }

    /**
     * 最大量
     */
    private void maxChange() {
        String maxLimit = etMax.getText().toString();
        String price = etjyPrice.getText().toString();
        String number = etCount.getText().toString();
        if (!StringUtils.isEmpty(maxLimit, price, number)) {
            Double max = Double.valueOf(price) * Double.valueOf(number);
            if (Double.valueOf(maxLimit) > max) {
                ToastUtils.showToast(getString(R.string.max_limit_tag) + max);
            }
        }
    }

    /**
     * 最小量
     */
    private void minChange() {
        String minLimit = etMin.getText().toString();
        if (!StringUtils.isEmpty(minLimit, currency)) {
            if (currency.equals(GlobalConstant.CNY) && Double.valueOf(minLimit) < 100) {
                ToastUtils.showToast(getString(R.string.min_limit_tag) + 100 + currency);
            }
            if (currency.contains("HK") && Double.valueOf(minLimit) < 100) {
                ToastUtils.showToast(getString(R.string.min_limit_tag) + 100 + currency);
            }
            if (currency.contains("USD") && Double.valueOf(minLimit) < 10) {
                ToastUtils.showToast(getString(R.string.min_limit_tag) + 10 + currency);
            }

            if (currency.equals("JPY") && Double.valueOf(minLimit) < 2000) {
                ToastUtils.showToast(getString(R.string.min_limit_tag) + 2000 + currency);
            }
        }

    }

    /**
     * 发布或修改广告
     */
    private void releaseOrEditAd() {
        String coinId = "";
        if (coinInfo != null) {
            coinId = coinInfo.getId();
        } else if (ads != null) {
            coinId = ads.getCoinId() + "";
        }
        String price = etjyPrice.getText().toString().trim();
        String minLimit = etMin.getText().toString().trim();
        String maxLimit = etMax.getText().toString().trim();
        String timeLimit = tvPayTime.getText().toString().split(" ")[0];
//        String timeLimit = etPayTime.getText().toString().trim();
        String countryZhName = "";
        if (country != null){
            countryZhName = country.getZhName();
        }else {
            countryZhName = "中国";
        }

        String priceType = rbFixed.isChecked() ? "0" : "1";
        String strOverFlow = "0";
        if (priceType.equals("1")) strOverFlow = etOverflow.getText().toString().trim();
        String remark = etMessage.getText().toString();
        String number = etCount.getText().toString().trim();
        String payway = tvPayWay.getText().toString();
        String pay = getPayByCode(payway);
        String jyPassword = etPassword.getText().toString().trim();
        String auto = rbYes.isChecked() ? "1" : "0";
        String autoword = etReplyContent.getText().toString();
        if (!StringUtils.isEmpty(minLimit)) {
            if (Double.valueOf(minLimit) < 100) {
                ToastUtils.show("交易限额不得小于100",Toast.LENGTH_SHORT);
                return;
            }
        }
        if (!StringUtils.isEmpty(maxLimit)) {
            if (Double.valueOf(minLimit) > Double.valueOf(maxLimit)) {
                ToastUtils.show("最大交易限额必须大于最小交易限额",Toast.LENGTH_SHORT);
                return;
            }
        }
        if (!StringUtils.isEmpty(price) && !StringUtils.isEmpty(number) && !StringUtils.isEmpty(maxLimit)) {
            if (Double.valueOf(price) * Double.valueOf(number) < Double.valueOf(maxLimit)) {
                ToastUtils.show("最大交易额不能超过您的卖出总金额",Toast.LENGTH_SHORT);
                return;
            }
        }
        if (StringUtils.isEmpty(coinId)) {
            ToastUtils.showToast(getString(R.string.str_prompt_coin_kind));
        } else if (countryZhName == null) {
            ToastUtils.showToast(getString(R.string.str_prompt_country));
        } else if (!rbFixed.isChecked() && StringUtils.isEmpty(strOverFlow)) {
            ToastUtils.showToast(getString(R.string.text_over_price));
            etOverflow.requestFocus();
        } else if (StringUtils.isEmpty(price)) {
            ToastUtils.showToast(getString(R.string.text_enter_trade_price));
            etjyPrice.requestFocus();
        } else if (StringUtils.isEmpty(number)) {
            ToastUtils.showToast(getString(R.string.str_prompt_trade_count));
            etCount.requestFocus();
        } else if (StringUtils.isEmpty(minLimit)) {
            ToastUtils.showToast(getString(R.string.str_prompt_min_count));
            etMin.requestFocus();
        } else if (StringUtils.isEmpty(maxLimit)) {
            ToastUtils.showToast(getString(R.string.str_prompt_max_count));
            etMax.requestFocus();
        } else if (StringUtils.isEmpty(payway)) {
            ToastUtils.showToast(getString(R.string.str_prompt_recieve_kind));
        } else if (StringUtils.isEmpty(timeLimit)) {
            ToastUtils.showToast(getString(R.string.str_prompt_pay_time));
//            etPayTime.requestFocus();
            tvPayTime.requestFocus();
        } else if (StringUtils.isEmpty(jyPassword)) {
            ToastUtils.showToast(getString(R.string.text_enter_money_pwd));
            etPassword.requestFocus();
        } else {
            if (ads == null) {
                doPost(-1, price, coinId, minLimit, maxLimit, Integer.valueOf(timeLimit), countryZhName
                        , priceType, strOverFlow, remark, number, pay, jyPassword, auto, autoword);
            } else {
                doPost(ads.getId(), price, coinId, minLimit, maxLimit, Integer.valueOf(timeLimit), countryZhName
                        , priceType, strOverFlow, remark, number, pay, jyPassword, auto, autoword);
            }
        }
    }


    /**
     * 数据请求
     *
     * @param id
     * @param price
     * @param coinId
     * @param minLimit
     * @param maxLimit
     * @param timeLimit
     * @param countryZhName
     * @param priceType
     * @param premiseRate
     * @param remark
     * @param number
     * @param pay
     * @param jyPassword
     * @param auto
     * @param autoword
     */
    private void doPost(int id, String price, String coinId, String minLimit, String maxLimit, Integer timeLimit, String countryZhName,
                        String priceType, String premiseRate, String remark, String number, String pay, String jyPassword, String auto, String autoword) {
        HashMap<String, String> map = new HashMap<>();

        map.put("price", price);
        map.put("advertiseType", advertiseType);
        map.put("coin.id", coinId);
        map.put("minLimit", minLimit);
        map.put("maxLimit", maxLimit);
        map.put("timeLimit", timeLimit + "");
        map.put("country.ZhName", countryZhName);
        map.put("priceType", priceType);
        map.put("premiseRate", premiseRate);
        map.put("remark", remark);
        map.put("number", number);
        map.put("pay[]", pay);
        map.put("jyPassword", jyPassword);
        map.put("auto", auto);
        if (rbYes.isChecked())
            map.put("autoword", autoword);
        else
            map.put("autoword", "");
        if (id != -1) {
            map.put("id", id + "");
            presenter.updateAd(map);
        } else {
            presenter.create(map);
        }
    }

    /**
     * 选择支付方式
     */
    private void showPayWayDialog() {
        if (payWayView == null) {
            payWayView = getLayoutInflater().inflate(R.layout.dialog_coins_view, null);
            rvpayWay = payWayView.findViewById(R.id.rvCoinList);
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvpayWay.setLayoutManager(manager);
            payWayAdapter = new SelectPayWayAdapter(R.layout.item_pub_ads_dialog_coin_info, payWays);
            payWayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    payWays.get(position).setSelect(!payWays.get(position).isSelect());
                    payWayAdapter.notifyDataSetChanged();
                }
            });
            rvpayWay.setAdapter(payWayAdapter);
            payWayDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.str_prompt_recieve_kind)).setView(payWayView)
                    .setPositiveButton(getString(R.string.dialog_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doClickPayWayItem();
                        }
                    }).setNegativeButton(getString(R.string.dialog_one_cancel), null).create();
        }
        payWayDialog.show();
    }

    /**
     * 点击选择支付方式
     */
    private void doClickPayWayItem() {
        String content = "";
        for (PayWay payWay : payWays) {
            if (payWay.isSelect()) content = content + "," + payWay.getName();
        }
        if (StringUtils.isEmpty(content)) tvPayWay.setText("");
        else
            tvPayWay.setText(content.length() > 1 ? content.substring(content.indexOf(",") + 1) : content);
    }

    private void getPrice(String coin, String type) {
        if (!StringUtils.isEmpty(coin) && StringUtils.isEmpty(type)) {
            tvPrice.setText(MathUtils.getRundNumber(Double.valueOf(coinInfo.getUsdMarketPrice()), 2, null));
        } else if (!StringUtils.isEmpty(coin) && !StringUtils.isEmpty(type)) {
            CoinInfo mCoinInfo = coinInfo;
            if (mCoinInfo == null) {
                for (CoinInfo obj : coinInfos) {
                    if (obj.getUnit().equals(tvCoin.getText().toString())) {
                        mCoinInfo = obj;
                    }
                }
            }
            if (type.equals("USD") && mCoinInfo.getUsdMarketPrice() != null) {
                tvPrice.setText(MathUtils.getRundNumber(Double.valueOf(mCoinInfo.getUsdMarketPrice()), 2, null));
            } else if (type.equals("JPY")) {
                tvPrice.setText(mCoinInfo.getJpyMarketPrice());
            }
            if (type.equals("HKD")) {
                tvPrice.setText(mCoinInfo.getHkMarketPrice());
            } else if (type.equals("CNY")) {
                tvPrice.setText(mCoinInfo.getMarketPrice());
            }
        }
    }

    /**
     * 根据返回数据显示
     *
     * @param ads
     */
    private void initAdsData(Ads ads) {
        tvCoin.setText(ads.getCoinUnit());
        country = ads.getCountry();
        tvCountry.setText(CommonUtils.getNameByCode(country));
        tvCoinKind.setText(country.getLocalCurrency());
        tvLocalCurrency.setText(country.getLocalCurrency());
        tvjyPriceCurrency.setText(country.getLocalCurrency());
        tvMinCurrency.setText(country.getLocalCurrency());
        tvMaxCurrency.setText(country.getLocalCurrency());
        tvPrice.setText(ads.getMarketPrice() + "");
        rbFixed.setChecked(ads.getPriceType() == 0);
        rbChange.setChecked(ads.getPriceType() != 0);
        etOverflow.setText(ads.getPremiseRate() + "");
        etjyPrice.setText(ads.getPrice() + "");
        etMin.setText(ads.getMinLimit() + "");
        etMax.setText(ads.getMaxLimit() + "");
        etCount.setText(ads.getNumber() + "");
        String payway = getSetPayByCode(ads.getPayMode());
        for (int i = 0; i < payWays.size(); i++) {
            if (StringUtils.isNotEmpty(payway)) {
                if (payway.contains(payWays.get(i).getName())) {
                    payWays.get(i).setSelect(true);
                }
            }
        }
        tvPayWay.setText(payway);
        tvPayTime.setText(ads.getTimeLimit() + " " + getString(R.string.text_minute));
//        etPayTime.setText(ads.getTimeLimit() + "");
        rbYes.setChecked(ads.getAuto() == 1);
        rbNo.setChecked(ads.getAuto() == 0);
        etReplyContent.setText(StringUtils.isEmpty(ads.getAutoword()) ? "" : ads.getAutoword());
        etMessage.setText(StringUtils.isEmpty(ads.getRemark()) ? "" : ads.getRemark());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_COUNTRY) {
            if (resultCode == RESULT_OK) {
                Country country = (Country) data.getSerializableExtra("country");
                this.country = country;
                currency = country.getLocalCurrency();
                tvCountry.setText(CommonUtils.getNameByCode(country));
                tvCoinKind.setText(country.getLocalCurrency());
                tvLocalCurrency.setText(currency);
                tvjyPriceCurrency.setText(currency);
                tvMinCurrency.setText(currency);
                tvMaxCurrency.setText(currency);
                getPrice(tvCoin.getText().toString(), tvLocalCurrency.getText().toString());
            }
        }
    }

    @Override
    public void setPresenter(ReleaseAdContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void allSuccess(List<CoinInfo> obj) {
        if (obj == null) return;
        coinInfos.clear();
        coinInfos.addAll(obj);
        coinNames = new String[coinInfos.size()];
        for (int i = 0; i < coinInfos.size(); i++) {
            coinNames[i] = coinInfos.get(i).getUnit();
        }
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void createSuccess(String obj) {
        ToastUtils.showToast(obj);
//        showCofirmDialog();

        User user = MyApplication.getApp().getCurrentUser();
        String tvNickName = user.getNick_name();
        String avatar = user.getAvatar();
        Bundle bundle = new Bundle();
        bundle.putString("avatar", avatar == null ? "" : avatar);
        bundle.putString("username", tvNickName);
        bundle.putInt("comefrom", 1);
        showActivity(AdsActivity.class, bundle);
        finish();
    }


    @Override
    public void adDetailSuccess(Ads obj) {
        if (obj == null) return;
        ads = obj;
        initAdsData(ads);
    }

    @Override
    public void updateSuccess(String obj) {
        ToastUtils.showToast(obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void getAccountSettingSuccess(AccountSetting obj) {
        accountSetting = obj;
        if (accountSetting.getAliVerified() == 1 && accountSetting.getAlipay().getAliStatus() != 0) {
            payWays.add(new PayWay(getString(R.string.str_payway_ali)));
        }
        if (accountSetting.getWechatVerified() == 1 && accountSetting.getWechatPay().getWechatStatus() != 0) {
            payWays.add(new PayWay(getString(R.string.str_payway_wechat)));
        }
        if (accountSetting.getBankVerified() == 1 && accountSetting.getBankInfo().getBankStatus() != 0) {
            payWays.add(new PayWay(getString(R.string.str_payway_union)));
        }
        if (payWays.size() != 0) {
            showPayWayDialog();
        } else {
            ToastUtils.showToast(getString(R.string.bind_account));
        }
    }

    /**
     * 将返回的中文根据系统语言
     *
     * @param payway
     * @return
     */
    private String getSetPayByCode(String payway) {
        if (StringUtils.isNotEmpty(payway) && SharedPreferenceInstance.getInstance().getLanguageCode() != 1) { // 非中文语言状态下，取系统值
            StringBuffer stringBuffer = new StringBuffer();
            if (payway.contains("支付宝")) {
                stringBuffer = stringBuffer.append(getString(R.string.str_payway_ali)).append(",");
            }
            if (payway.contains("微信")) {
                stringBuffer = stringBuffer.append(getString(R.string.str_payway_wechat)).append(",");
            }
            if (payway.contains("银联")) {
                stringBuffer = stringBuffer.append(getString(R.string.str_payway_union)).append(",");
            }
            return StringUtils.getRealString(stringBuffer.toString());
        }
        return payway;
    }

    /**
     * 将选择的支付方式转换成中文
     *
     * @param payway
     * @return
     */
    private String getPayByCode(String payway) {
        if (StringUtils.isNotEmpty(payway) && SharedPreferenceInstance.getInstance().getLanguageCode() != 1) { // 非中文状态下，转换成中文
            StringBuffer stringBuffer = new StringBuffer();
            if (payway.contains(getString(R.string.str_payway_ali))) {
                stringBuffer = stringBuffer.append("支付宝").append(",");
            }
            if (payway.contains(getString(R.string.str_payway_wechat))) {
                stringBuffer = stringBuffer.append("微信").append(",");
            }
            if (payway.contains(getString(R.string.str_payway_union))) {
                stringBuffer = stringBuffer.append("银联").append(",");
            }
            return StringUtils.getRealString(stringBuffer.toString());
        }
        return payway;
    }


    /**
     * 控制小数位数
     *
     * @param editText
     * @param decimalNum
     */
    private void setEditText(EditText editText, int decimalNum) {
        String charSequence = editText.getText().toString();
        if (StringUtils.isVaildText(editText)) {
            if (charSequence.toString().contains(".")) { // 删除“.”后面超过限制位数后的数据
                if (charSequence.length() - 1 - charSequence.toString().indexOf(".") > decimalNum) {
                    charSequence = charSequence.toString().subSequence(0, charSequence.toString().indexOf(".") + decimalNum + 1).toString();
                    editText.setText(charSequence);
                    editText.setSelection(charSequence.length());
                    ToastUtils.showToast(getString(R.string.edit_decimals_tag));
                }
            }
        }
    }
}
