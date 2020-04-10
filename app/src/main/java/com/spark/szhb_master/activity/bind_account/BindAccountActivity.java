package com.spark.szhb_master.activity.bind_account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.AccountSetting;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 绑定账号
 * Created by Administrator on 2018/5/2 0002.
 */

public class BindAccountActivity extends BaseActivity implements BindAccountContact.View {
    @BindView(R.id.tvAli)
    TextView tvAli;
    @BindView(R.id.tvWeiChat)
    TextView tvWeiChat;
    @BindView(R.id.tvUnionPay)
    TextView tvUnionPay;
    @BindView(R.id.zfb_img)
    ImageView zfb_img;
    @BindView(R.id.wx_img)
    ImageView wx_img;
    @BindView(R.id.bank_img)
    ImageView bank_img;
    @BindView(R.id.llAliAccount)
    LinearLayout llAliAccount;
    @BindView(R.id.llWeiChatAccount)
    LinearLayout llWeiChatAccount;
    @BindView(R.id.llUnionPayAccount)
    LinearLayout llUnionPayAccount;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.line_bank)
    LinearLayout line_bank;
    @BindView(R.id.line_zfb)
    LinearLayout line_zfb;
    @BindView(R.id.line_wx)
    LinearLayout line_wx;

    @BindView(R.id.wx_name)
    TextView wx_name;
    @BindView(R.id.num_wx)
    TextView num_wx;
    @BindView(R.id.tv_zfb)
    TextView tv_zfb;
    @BindView(R.id.num_zfb)
    TextView num_zfb;
    @BindView(R.id.bank_name)
    TextView bank_name;
    @BindView(R.id.tv_name)
    TextView tv_name;

    private BindAccountContact.Presenter presenter;
    private AccountSetting accountSetting;

    private int aListauts = 0;
    private int wxstauts = 0;
    private int backstauts = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            loadData();
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_account;
    }


    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.binding_account));
        tvGoto.setVisibility(View.INVISIBLE);
        new BindAccountPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @OnClick({R.id.llAliAccount, R.id.llWeiChatAccount, R.id.llUnionPayAccount,R.id.bank_img,R.id.zfb_img,R.id.wx_img})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (accountSetting == null) return;
        Bundle bundle = new Bundle();
        bundle.putSerializable("accountSetting", accountSetting);
        HashMap<String, String> map = new HashMap<>();
        switch (v.getId()) {
            case R.id.llUnionPayAccount:
                showActivity(BindBankActivity.class, bundle, 0);
                break;
            case R.id.llWeiChatAccount:
            case R.id.llAliAccount:
                if (v.getId() == R.id.llWeiChatAccount)
                    bundle.putBoolean("isAli", false);
                else
                    bundle.putBoolean("isAli", true);
                showActivity(BindAliActivity.class, bundle, 0);
                break;
            case R.id.bank_img:
                map.put("status", + backstauts +"");
                presenter.setBankStatus(map);
                break;
            case R.id.wx_img:
                map.put("status", wxstauts+"");
                presenter.setWxStatus(map);
                break;
            case R.id.zfb_img:
                map.put("status", aListauts+"");
                presenter.setAliStatus(map);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void loadData() {
        presenter.getAccountSetting();
    }

    @Override
    public void setPresenter(BindAccountContact.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getAccountSettingSuccess(AccountSetting obj) {
        this.accountSetting = obj;
        if (accountSetting.getAliVerified() == 0){
            tvAli.setText(getString(R.string.unbound));
        }else {
            tvAli.setVisibility(View.INVISIBLE);
            zfb_img.setVisibility(View.VISIBLE);
            if (accountSetting.getAlipay().getAliStatus() == 0){
                line_zfb.setVisibility(View.GONE);
                aListauts = 1;
                zfb_img.setImageResource(R.mipmap.close_image);
            }else {
                line_zfb.setVisibility(View.VISIBLE);
                aListauts = 0;
                zfb_img.setImageResource(R.mipmap.open_img);
            }
            num_zfb.setText(accountSetting.getAlipay().getAliNo());
            tv_zfb.setText(accountSetting.getRealName());
//            zfb_img.setImageResource(R.mipmap.close_image);
        }
//        tvAli.setText(accountSetting.getAliVerified() == 0 ? getString(R.string.unbound) : getString(R.string.to_edit));
        tvAli.setEnabled(accountSetting.getAliVerified() == 0);

        if (accountSetting.getWechatVerified() == 0){
            tvWeiChat.setText(getString(R.string.unbound));
        }else {
            tvWeiChat.setVisibility(View.INVISIBLE);
            wx_img.setVisibility(View.VISIBLE);
            if (accountSetting.getWechatPay().getWechatStatus() == 0){
                line_wx.setVisibility(View.GONE);
                wxstauts = 1;
                wx_img.setImageResource(R.mipmap.close_image);
            }else {
                line_wx.setVisibility(View.VISIBLE);
                wxstauts = 0;
                wx_img.setImageResource(R.mipmap.open_img);
            }
            wx_name.setText(accountSetting.getRealName());
            num_wx.setText(accountSetting.getWechatPay().getweChat());
        }
//        tvWeiChat.setText(accountSetting.getWechatVerified() == 0 ? getString(R.string.unbound) : getString(R.string.to_edit));
        tvWeiChat.setEnabled(accountSetting.getWechatVerified() == 0);

        if (accountSetting.getBankVerified() == 0){
            tvUnionPay.setText(getString(R.string.unbound));
        }else {
            tvUnionPay.setVisibility(View.INVISIBLE);
            bank_img.setVisibility(View.VISIBLE);
            if (accountSetting.getBankInfo().getBankStatus() == 0){
                line_bank.setVisibility(View.GONE);
                backstauts = 1;
                bank_img.setImageResource(R.mipmap.close_image);
            }else {
                line_bank.setVisibility(View.VISIBLE);
                backstauts = 0;
                bank_img.setImageResource(R.mipmap.open_img);
            }
            bank_name.setText(accountSetting.getBankInfo().getCardNo());
            tv_name.setText(accountSetting.getRealName());
        }
//        tvUnionPay.setText(accountSetting.getBankVerified() == 0 ? getString(R.string.unbound) : getString(R.string.to_edit));
        tvUnionPay.setEnabled(accountSetting.getBankVerified() == 0);

    }

    @Override
    public void getAccountSettingFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void setAliStatusSuccess(String obj) {
        ToastUtils.showToast(obj);
        presenter.getAccountSetting();
    }

    @Override
    public void setAliStatusFail(String obj) {
        ToastUtils.showToast(obj);
    }

    @Override
    public void setBankStatusSuccess(String obj) {
        ToastUtils.showToast(obj);
        presenter.getAccountSetting();
    }

    @Override
    public void setBankStatusFail(String obj) {
        ToastUtils.showToast(obj);
    }

    @Override
    public void setWxStatusSuccess(String obj) {
        ToastUtils.showToast(obj);
        presenter.getAccountSetting();
    }

    @Override
    public void setWxStatusFail(String obj) {
        ToastUtils.showToast(obj);
    }
}
