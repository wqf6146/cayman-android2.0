package com.spark.szhb_master.activity.wallet;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.account_pwd.AccountPwdActivity;
import com.spark.szhb_master.activity.my_match.MatchActivity;
import com.spark.szhb_master.activity.wallet_coin.ExtractActivity;
import com.spark.szhb_master.activity.wallet_coin.RechargeActivity;
import com.spark.szhb_master.dialog.DialogTure;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.widget.TextWatcher;
import com.spark.szhb_master.adapter.WalletAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Coin;
import com.spark.szhb_master.entity.GccMatch;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class WalletActivity extends BaseActivity implements WalletContract.View {
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvCnyAmount)
    TextView tvCnyAmount;
    @BindView(R.id.llAccount)
    LinearLayout llAccount;
    @BindView(R.id.rvWallet)
    RecyclerView rvWallet;
    @BindView(R.id.ivSee)
    ImageView ivSee;
    @BindView(R.id.llContainer)
    LinearLayout llContainer;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.evSearch)
    EditText evSearch;
    @BindView(R.id.cbHide)
    CheckBox cbHide;
    private List<Coin> coins = new ArrayList<>();
    private List<Coin> removeCoins = new ArrayList<>();
    private List<Coin> keepCoins = new ArrayList<>();
    private List<Coin> zeroCoins = new ArrayList<>();
    private WalletAdapter adapter;
    double sumUsd = 0;
    double sumCny = 0;
    private WalletContract.Presenter presenter;
    private PopupWindow popWnd;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.wallet));
        new WalletPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        ivBack.setVisibility(View.VISIBLE);
        tvGoto.setVisibility(View.VISIBLE);
        tvGoto.setText(getString(R.string.detail));
        initRvWallet();
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @OnClick({R.id.tvGoto, R.id.ivSee,R.id.ivBack})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvGoto:
                showActivity(WalletDetailActivity.class, null);
                break;
            case R.id.ivSee:
                switchSee();
                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        cbHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    zeroCoins.clear();
                    for (int i = 0; i < coins.size(); i++) {
                        if (coins.get(i).getBalance() == 0) {
                            zeroCoins.add(coins.get(i));
                        }
                    }
                    coins.removeAll(zeroCoins);
                    adapter.notifyDataSetChanged();
                } else {
                    coins.addAll(zeroCoins);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (coins.get(position).getCoin().getCanRecharge() == 0 && coins.get(position).getCoin().getCanWithdraw() == 0) {
                    ToastUtils.showToast(getString(R.string.not_raise_money_tag));
                } else {
                    if (coins.get(position).getCoin().getUnit().equals("GCC") || coins.get(position).getCoin().getUnit().equals("GCA")|| coins.get(position).getCoin().getUnit().equals("GCB")) {
                        String[] stringItems = {getString(R.string.charge_money), getString(R.string.mention_money), getString(R.string.match_money)};
                        actionSheetDialogNoTitle(stringItems, coins.get(position));
                    } else {
                        String[] stringItems = {getString(R.string.charge_money), getString(R.string.mention_money)};
                        actionSheetDialogNoTitle(stringItems, coins.get(position));
                    }
                }
            }
        });

    }

    /**
     * 弹框
     */
    private void actionSheetDialogNoTitle(String[] stringItems, final Coin coin) {
        final ActionSheetDialog dialog = new ActionSheetDialog(activity, stringItems, null);
        dialog.isTitleShow(false).show();
        presenter.safeSetting();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("coin", coin);
                switch (position) {
                    case 0: // 充币
                        if (StringUtils.isEmpty(coin.getAddress())) {
                            ToastUtils.showToast(getString(R.string.no_address_tag));
                        } else {
                            showActivity(RechargeActivity.class, bundle);
                        }
                        break;
                    case 1: // 提币
                        if (safeSetting.getRealVerified() == 0) {
                            initsafe();
                        }else if (safeSetting.getFundsVerified() == 0){
                            showzijmm();
                        }else {
                            showActivity(ExtractActivity.class, bundle);
                        }
                        break;
                    case 2: // 配对
//                        presenter.getCheckMatch();
                        if (safeSetting.getRealVerified() == 0) {
                            initsafe();
                        }else {
                            MatchActivity.actionStart(WalletActivity.this);
                            finish();
                        }
                        break;
                }
                dialog.superDismiss();
            }
        });
    }

    private void showzijmm() {
        final DialogTure dialogTure = new DialogTure(this);
        dialogTure.show();
        dialogTure.cometwo().setVisibility(View.GONE);

        dialogTure.go_up().setText(getResources().getString(R.string.go_to_certification));
        dialogTure.cancle().setText(getResources().getString(R.string.cancle));
        dialogTure.name_title().setText(getResources().getString(R.string.not_funding));
        dialogTure.name_title().setTextSize(18);

        dialogTure.cancle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTure.dismiss();
            }
        });

        dialogTure.img_close().setVisibility(View.INVISIBLE);
        dialogTure.go_up().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSet", true);
                showActivity(AccountPwdActivity.class, bundle, 0);
                dialogTure.dismiss();
            }
        });
    }

    private void initsafe() {
        if (safeSetting.getRealVerified() == 0) {
            ShiMingDialog shiMingDialog = new ShiMingDialog(this);
            String name = safeSetting.getRealNameRejectReason();
            if (safeSetting.getRealVerified() == 0) {
                if (safeSetting.getRealAuditing() == 1) {
                    shiMingDialog.setEntrust(7, name,1);
                } else {
                    if (safeSetting.getRealNameRejectReason() != null)
                        shiMingDialog.setEntrust(8, name,1);
                    else
                        shiMingDialog.setEntrust(9, name,1);
                }
            } else {
                shiMingDialog.setEntrust(6, name,1);
            }
            shiMingDialog.show();
        }
    }

    /**
     * 控制资产数额可见
     */
    private void switchSee() {
        if (!"*****".equals(tvCnyAmount.getText())) {
            tvCnyAmount.setText("*****");
            tvAmount.setText("********");
            SharedPreferenceInstance.getInstance().saveMoneyShowtype(2);
            ivSee.setImageResource(R.mipmap.icon_eye_close);
        } else {
            tvAmount.setText(MathUtils.getRundNumber(sumUsd, 6, null));
            tvCnyAmount.setText(" ≈ "+MathUtils.getRundNumber(sumCny, 2, null) +" "+ GlobalConstant.CNY);
            SharedPreferenceInstance.getInstance().saveMoneyShowtype(1);
            ivSee.setImageResource(R.mipmap.icon_eye_open);
        }
    }

    @Override
    protected ViewGroup getEmptyView() {
        return llContainer;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(WalletActivity.this, llTitle);
            isSetTitle = true;
        }
    }
    /**
     * 列表初始化
     */
    private void initRvWallet() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvWallet.setLayoutManager(manager);
        adapter = new WalletAdapter(R.layout.item_lv_wallet, coins);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(true);
        rvWallet.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        presenter.myWallet();
    }

    @Override
    public void setPresenter(WalletContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void myWalletSuccess(List<Coin> obj) {
        if (obj == null) return;
        this.coins.clear();
        this.coins.addAll(obj);
        keepCoins.addAll(obj);
        adapter.notifyDataSetChanged();
        calcuTotal(coins);
        evSearch.addTextChangedListener(localChangeWatcher);
    }

    private void calcuTotal(List<Coin> coins) {
        sumUsd = 0;
        sumCny = 0;
        for (Coin coin : coins) {
//            sumUsd += (coin.getBalance() * coin.getCoin().getUsdRate());
//            sumCny += (coin.getBalance() * coin.getCoin().getCnyRate());
            sumUsd += ((coin.getBalance() * coin.getCoin().getUsdRate()) + (coin.getFrozenBalance() * coin.getCoin().getUsdRate()));
            sumCny += ((coin.getBalance() * coin.getCoin().getCnyRate()) + (coin.getFrozenBalance() * coin.getCoin().getCnyRate()));
        }
        if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 1) {
            tvAmount.setText(MathUtils.getRundNumber(sumUsd, 6, null));
            tvCnyAmount.setText(" ≈ " + MathUtils.getRundNumber(sumCny, 2, null) + " CNY");
        } else if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 2) {
            tvAmount.setText("********");
            tvCnyAmount.setText("*****");
            ivSee.setImageResource(R.mipmap.icon_eye_close);
        }
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
//        if (popWnd.isShowing())
//            popWnd.dismiss();
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    private SafeSetting safeSetting;
    @Override
    public void safeSettingSuccess(SafeSetting obj) {
        if (obj == null)
            return;
        safeSetting = obj;
    }

    @Override
    public void getCheckMatchSuccess(GccMatch obj) {
        showPopWindow(obj.getData());
    }

    private void showPopWindow(final double data) {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.view_pop_gcc_match, null);
        popWnd = new PopupWindow(activity);
        popWnd.setContentView(contentView);
        popWnd.setWidth((int) (MyApplication.getApp().getmWidth() * 0.8));
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWnd.setOutsideTouchable(true);
        popWnd.setTouchable(true);
        popWnd.setFocusable(true);
        popWnd.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        darkenBackground(0.4f);
        TextView confirm = contentView.findViewById(R.id.tvGccConfirm);
        TextView maxAmount = contentView.findViewById(R.id.tvMaxMatcchAmount);
        final EditText matchAmount = contentView.findViewById(R.id.evMatcchAmount);

        maxAmount.setText(String.valueOf(data));
        confirm.requestFocus();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = matchAmount.getText().toString();
                if (!StringUtils.isEmpty(amount)) {
                    if (Double.valueOf(amount) > 0 && Double.valueOf(amount) <= data) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("amount", amount);
                        presenter.getStartMatch(map);
                    }
                }
            }
        });
        View rootview = LayoutInflater.from(WalletActivity.this).inflate(R.layout.activity_wallet, null);
        popWnd.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
    }

    /**
     * 改变背景颜色
     */
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

    }

    @Override
    public void getStartMatchSuccess(String obj) {
        popWnd.dismiss();
        ToastUtils.showToast(obj);
    }

    private TextWatcher localChangeWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            removeCoins.clear();
            localCoinChange();
        }
    };

    private void localCoinChange() {
        String str = evSearch.getText().toString().toUpperCase();
        if (str.isEmpty()) {
            coins.clear();
            coins.addAll(keepCoins);
            adapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < coins.size(); i++) {
                if (!coins.get(i).getCoin().getUnit().contains(str)) {
                    removeCoins.add(coins.get(i));
                }
            }
            coins.removeAll(removeCoins);
            adapter.notifyDataSetChanged();
        }
    }

}
