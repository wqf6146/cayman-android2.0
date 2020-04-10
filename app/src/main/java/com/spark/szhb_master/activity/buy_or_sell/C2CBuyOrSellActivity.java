package com.spark.szhb_master.activity.buy_or_sell;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.activity.my_match.MatchActivity;
import com.spark.szhb_master.activity.order.OrderDetailActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.PayWayDialog;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.entity.AccountSetting;
import com.spark.szhb_master.entity.C2C;
import com.spark.szhb_master.entity.C2CExchangeInfo;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.LogUtils;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 买入或卖出
 */
public class C2CBuyOrSellActivity extends BaseActivity implements C2CBuyOrSellContract.View {
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvLocalCoinText)
    TextView tvLocalCoinText;
    @BindView(R.id.etLocalCoin)
    EditText etLocalCoin;
    @BindView(R.id.tvOtherCoinText)
    TextView tvOtherCoinText;
    @BindView(R.id.all_buy)
    TextView all_buy;
    @BindView(R.id.etOtherCoin)
    EditText etOtherCoin;
    @BindView(R.id.tvInfo)
    TextView tvInfo;
    @BindView(R.id.ivZhifubao)
    ImageView ivZhifubao;
    @BindView(R.id.ivWeChat)
    ImageView ivWeChat;
    @BindView(R.id.ivUnionPay)
    ImageView ivUnionPay;
    @BindView(R.id.txZhifubao)
    TextView txZhifubao;
    @BindView(R.id.txWeChat)
    TextView txWeChat;
    @BindView(R.id.txUnionPay)
    TextView txUnionPay;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.text_red)
    TextView text_red;
    private C2C.C2CBean c2cBean;
    private C2CExchangeInfo c2CExchangeInfo;
    private C2CBuyOrSellContract.Presenter presenter;
    private String mode = "0";
    private MyTextWathcer baseWhater;
    private MyTextWathcer recWhater;
    private OrderConfirmDialog dialog;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_c2c_buy_sell;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        dialog = new OrderConfirmDialog(activity);
        baseWhater = new MyTextWathcer(0, etLocalCoin);
        recWhater = new MyTextWathcer(1, etOtherCoin);
        new C2CBuyOrSellPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        tvGoto.setVisibility(View.INVISIBLE);
        if (bundle != null) {
            c2cBean = (C2C.C2CBean) bundle.getSerializable("c2cBean");
            if (c2cBean != null) {
                if ("0".equals(c2cBean.getAdvertiseType())) {
                    tvTitle.setText(getString(R.string.text_sell) + c2cBean.getUnit());
//                    tvInfo.setText(getString(R.string.text_much_sale));
                    all_buy.setText(getString(R.string.all_sell));
                } else {
                    tvTitle.setText(getString(R.string.text_buy) + c2cBean.getUnit());
                    all_buy.setText(getString(R.string.all_buy));
//                    tvInfo.setText(getString(R.string.text_much_buy));
                }
                setButtonText();
            }
        }
    }


    @Override
    protected void setListener() {
        super.setListener();
        etLocalCoin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    etLocalCoin.addTextChangedListener(baseWhater);
                    etOtherCoin.removeTextChangedListener(recWhater);
                }
            }
        });

        etOtherCoin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    etLocalCoin.removeTextChangedListener(baseWhater);
                    etOtherCoin.addTextChangedListener(recWhater);
                }
            }
        });
        dialog.getTvConfirm().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyOrSell();
                dialog.dismiss();
            }
        });
        all_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etLocalCoin.setFocusable(true);
                etLocalCoin.setFocusableInTouchMode(true);
                etLocalCoin.requestFocus();
                etLocalCoin.setText(c2CExchangeInfo.getMaxLimit() + "");
            }
        });
    }

    @OnClick(R.id.tvConfirm)
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (MyApplication.getApp().isLogin()) {
            presenter.safeSetting();
        } else {
            showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
        }
    }

    private void isShiMing() {
        String countStr = etOtherCoin.getText().toString();
        String totalStr = etLocalCoin.getText().toString();
        if (StringUtils.isEmpty(countStr, totalStr)) {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        } else if (num_true == 0) {
            ToastUtils.showToast("输入金额有误");
        } else {
            if (!MyApplication.getApp().isLogin()) {
                startActivityForResult(new Intent(this, LoginActivity.class), LoginActivity.RETURN_LOGIN);
            } else {
                showConfirmDialog();
            }
        }
    }

    /**
     * 下单
     */
    private void showConfirmDialog() {
        double count = Double.parseDouble(etOtherCoin.getText().toString().trim());
        String strCount = MathUtils.getRundNumber(count, 4, null) + " " + c2CExchangeInfo.getUnit();
        String strPrice = MathUtils.getRundNumber(c2CExchangeInfo.getPrice(), 2, null) + " " + GlobalConstant.CNY;
        double total = Double.parseDouble(etLocalCoin.getText().toString().trim());
        String strTotal = MathUtils.getRundNumber(total, 4, null) + " " + tvLocalCoinText
                .getText().toString();
        dialog.setData(c2cBean.getAdvertiseType(), strPrice, strCount, strTotal);
        dialog.show();
    }

    /**
     * 买或者卖
     */
    public void buyOrSell() {
        if (c2cBean == null || c2CExchangeInfo == null) return;
        String id = c2cBean.getAdvertiseId() + "";
        String coinId = c2CExchangeInfo.getOtcCoinId() + "";
        String price = c2CExchangeInfo.getPrice() + "";
        String money = etLocalCoin.getText().toString();
        String amount = etOtherCoin.getText().toString();
        String remark = "";
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("coinId", coinId);
        map.put("price", price);
        map.put("money", money);
        map.put("amount", amount);
        map.put("doFeedBack", remark);
        map.put("mode", mode);
        if ("0".equals(c2cBean.getAdvertiseType())) {
            presenter.c2cSell(map);
        } else {
            presenter.c2cBuy(map);
        }
    }

    /**
     * 根据是否登录显示按钮
     */
    private void setButtonText() {
        if ("0".equals(c2cBean.getAdvertiseType())) {
            if (MyApplication.getApp().isLogin()) {
                tvConfirm.setText(getString(R.string.text_sell));
            } else {
                tvConfirm.setText(getString(R.string.text_to_login));
            }
        } else {
            if (MyApplication.getApp().isLogin()) {
                tvConfirm.setText(getString(R.string.text_buy));
            } else {
                tvConfirm.setText(getString(R.string.text_to_login));
            }
        }
    }

    @Override
    protected void loadData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", c2cBean.getAdvertiseId() + "");
        presenter.c2cInfo(map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LoginActivity.RETURN_LOGIN:
                setButtonText();
                break;
        }
    }

    @Override
    public void setPresenter(C2CBuyOrSellContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void c2cInfoSuccess(C2CExchangeInfo obj) {
        if (obj == null) return;
        c2CExchangeInfo = obj;
        fillViews();
    }

    private List<String> pays;

    private void fillViews() {
        tvLimit.setText(c2CExchangeInfo.getMinLimit() + "~" + c2CExchangeInfo.getMaxLimit() + " " + GlobalConstant.CNY);
        tvOtherCoinText.setText(c2CExchangeInfo.getUnit());
//        if (!StringUtils.isEmpty(c2cBean.getAvatar()))
//            Glide.with(this).load(c2cBean.getAvatar()).into(ivHeader);
//        else Glide.with(this).load(R.mipmap.icon_default_header_grey).into(ivHeader);
//        tvName.setText(c2CExchangeInfo.getUsername());
        pays = Arrays.asList(c2CExchangeInfo.getPayMode().split(","));
//        if (pays.contains("支付宝")) ivZhifubao.setVisibility(View.VISIBLE);
//        else ivZhifubao.setVisibility(View.GONE);
//        if (pays.contains("微信")) ivWeChat.setVisibility(View.VISIBLE);
//        else ivWeChat.setVisibility(View.GONE);
//        if (pays.contains("银联") || (pays.contains("银行卡"))) ivUnionPay.setVisibility(View.VISIBLE);
//        else ivUnionPay.setVisibility(View.GONE);
        if (pays.contains("支付宝")) {
            ivZhifubao.setVisibility(View.VISIBLE);
            txZhifubao.setVisibility(View.VISIBLE);
        } else {
            ivZhifubao.setVisibility(View.GONE);
            txZhifubao.setVisibility(View.GONE);
        }
        if (pays.contains("微信")) {
            ivWeChat.setVisibility(View.VISIBLE);
            txWeChat.setVisibility(View.VISIBLE);
        } else {
            ivWeChat.setVisibility(View.GONE);
            txWeChat.setVisibility(View.GONE);
        }
        if (pays.contains("银联") || (pays.contains("银行卡"))) {
            ivUnionPay.setVisibility(View.VISIBLE);
            txUnionPay.setVisibility(View.VISIBLE);
        } else {
            ivUnionPay.setVisibility(View.GONE);
            txUnionPay.setVisibility(View.GONE);
        }

        tvPrice.setText(c2CExchangeInfo.getPrice() + " " + GlobalConstant.CNY);
        String html = "<font color=#A0B0BC>" + getString(R.string.text_ad_message) + "：" + "<br/>" + "</font>" + c2CExchangeInfo.getRemark();
        tvMessage.setText(Html.fromHtml(html));
//        tvExchangeCount.setText(getString(R.string.text_deal_num) + c2CExchangeInfo.getTransactions());
//        tvRemainAmount.setText(getString(R.string.text_surplus_num) + MathUtils.getRundNumber(Double.valueOf(BigDecimal.valueOf(c2CExchangeInfo.getMaxTradableAmount()).toString()), 8, null));
        if (c2cBean != null) {
            if ("0".equals(c2cBean.getAdvertiseType())) {
                if ((c2cBean.getRemainAmount() * c2cBean.getPrice()) >= c2cBean.getMaxLimit()) {//剩余数量大于最大卖出数量
                    etLocalCoin.setHint("最大可卖" + c2CExchangeInfo.getMaxLimit());
                    etOtherCoin.setHint("最大可卖" + c2CExchangeInfo.getMaxLimit() / c2cBean.getPrice());
                } else {
                    etLocalCoin.setHint("最大可卖" + c2cBean.getRemainAmount() * c2cBean.getPrice());
                    etOtherCoin.setHint("最大可卖" + c2CExchangeInfo.getNumber());
                }
            } else {
                if ((c2cBean.getRemainAmount() * c2cBean.getPrice()) >= c2cBean.getMaxLimit()) {//剩余数量大于最大卖出数量
                    etLocalCoin.setHint("最大可买" + c2CExchangeInfo.getMaxLimit());
                    etOtherCoin.setHint("最大可买" + c2CExchangeInfo.getMaxLimit() / c2cBean.getPrice());
                } else {
                    etLocalCoin.setHint("最大可买" + c2cBean.getRemainAmount() * c2cBean.getPrice());
                    etOtherCoin.setHint("最大可买" + c2CExchangeInfo.getNumber());
                }
            }
        }
    }


    @Override
    public void c2cBuyOrSellSuccess(String obj) {
        ToastUtils.showToast("创建订单成功");
        Bundle bundle = new Bundle();
        bundle.putString("orderSn", obj);
//        Intent intent = new Intent(activity, OrderDetailActivity.class);
//        intent.putExtras(bundle);
//        startActivityForResult(intent, 0);
        showActivity(OrderDetailActivity.class, bundle);
//        Bundle bundle = new Bundle();
//        bundle.putInt("type", 0);
//        showActivity(MyOrderActivity.class, bundle);
        finish();
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
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
            if ("0".equals(c2cBean.getAdvertiseType())) {
                presenter.getAccountSetting();
            }else {
                isShiMing();

            }

        }
    }

    AccountSetting accountSetting;

    @Override
    public void getAccountSettingSuccess(AccountSetting obj) {
        this.accountSetting = obj;

        if (accountSetting.getAliVerified() == 1 && pays.contains("支付宝")){
            isShiMing();
        }else if(accountSetting.getWechatVerified() == 1 && pays.contains("微信")){
            isShiMing();
        }else if (accountSetting.getAliVerified() == 1 && pays.contains("银联")){
            isShiMing();
        }else {
            PayWayDialog paywayDialog = new PayWayDialog(this);
            paywayDialog.show();
        }
    }

    private class MyTextWathcer implements android.text.TextWatcher {
        private int type; // 0-兑换币，1-接收币
        private EditText editText;

        public MyTextWathcer(int type, EditText editText) {
            this.type = type;
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            CharSequence charSequence = editable.toString();
            LogUtils.i("charSequence===" + charSequence);
            // 如果"."在起始位置,则起始位置自动补0
            if (charSequence.equals(".")) {
                charSequence = "0" + charSequence;
                editText.setText(charSequence);
                editText.setSelection(2);
                return;
            }

            // 如果起始位置为0,且第二位跟的不是".",则无法后续输入
            if (charSequence.toString().startsWith("0")
                    && charSequence.toString().trim().length() > 1) {
                if (!charSequence.toString().substring(1, 2).equals(".")) {
                    editText.setText(charSequence.subSequence(0, 1));
                    editText.setSelection(1);
                    return;
                }
            }
            String str = charSequence.toString();
            if (type == 0) {
                mode = "0";
                if (StringUtils.isEmpty(str) || c2cBean.getPrice() == 0) {
                    etOtherCoin.setText("");
                    num_true = 0;
                    text_red.setText(getResources().getString(R.string.min_min));
                } else if (c2CExchangeInfo != null) {
                    etOtherCoin.setText(MathUtils.getRundNumber(Double.parseDouble(str) / c2CExchangeInfo.getPrice(), 8, null));

                    if ((c2cBean.getRemainAmount() * c2cBean.getPrice()) >= c2cBean.getMaxLimit()) {//剩余数量大于最大卖出数量
                        if (Double.parseDouble(str) > c2CExchangeInfo.getMaxLimit()) {
                            text_red.setText(getResources().getString(R.string.max_max));
                            num_true = 0;
                        } else {
                            text_red.setText("");
                            num_true = 1;
                        }
                    } else {
                        if (Double.parseDouble(str) > c2cBean.getRemainAmount() * c2cBean.getPrice()) {
                            text_red.setText(getResources().getString(R.string.max_max));
                            num_true = 0;
                        } else {
                            text_red.setText("");
                            num_true = 1;
                        }
                    }
                    if (Double.parseDouble(str) < c2CExchangeInfo.getMinLimit()) {
                        text_red.setText(getResources().getString(R.string.min_min));
                        num_true = 0;
                    }
                }

            } else {
                mode = "1";
                if (StringUtils.isEmpty(str) || c2cBean.getPrice() == 0) {
                    etLocalCoin.setText("");
                    num_true = 0;
                    text_red.setText(getResources().getString(R.string.min_min));
                } else if (c2CExchangeInfo != null) {
                    etLocalCoin.setText(MathUtils.getRundNumber(Double.parseDouble(str) * c2CExchangeInfo.getPrice(), 2, null));
                    if ((c2cBean.getRemainAmount() * c2cBean.getPrice()) >= c2cBean.getMaxLimit()) {//剩余数量大于最大卖出数量
                        if (Double.parseDouble(etLocalCoin.getText().toString()) > c2CExchangeInfo.getMaxLimit()) {
                            text_red.setText(getResources().getString(R.string.max_max));
                            num_true = 0;
                        } else {
                            text_red.setText("");
                            num_true = 1;
                        }
                    } else {
                        if (Double.parseDouble(etLocalCoin.getText().toString()) > c2cBean.getRemainAmount() * c2cBean.getPrice()) {
                            text_red.setText(getResources().getString(R.string.max_max));
                            num_true = 0;
                        } else {
                            text_red.setText("");
                            num_true = 1;
                        }
                    }
                    if (Double.parseDouble(etLocalCoin.getText().toString()) < c2CExchangeInfo.getMinLimit()) {
                        text_red.setText(getResources().getString(R.string.min_min));
                        num_true = 0;
                    }

                }
            }
        }
    }

    private int num_true = 0;

}