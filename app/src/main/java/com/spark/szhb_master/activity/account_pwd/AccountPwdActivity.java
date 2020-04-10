package com.spark.szhb_master.activity.account_pwd;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 设置/修改资金密码
 */
public class AccountPwdActivity extends BaseActivity implements AccountPwdContract.View {
    @BindView(R.id.etOldPwd)
    TextView etOldPwd;
    @BindView(R.id.etAccountPwd)
    EditText etAccountPwd;
    @BindView(R.id.etRepeatPwd)
    EditText etRepeatPwd;
    @BindView(R.id.tvSet)
    TextView tvSet;
    @BindView(R.id.llOldPwd)
    LinearLayout llOldPwd;   @BindView(R.id.llForgot)
    LinearLayout llForgot;@BindView(R.id.llPrompt)
    LinearLayout llPrompt;
    @BindView(R.id.tvPwdTag)
    TextView tvPwdTag;
    @BindView(R.id.tvRePwdTag)
    TextView tvRePwdTag;
    @BindView(R.id.tvTag)
    TextView tvTag;

    private AccountPwdContract.Presenter presenter;
    private boolean isSet;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_account_pwd;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.set_money_pwd));
        tvGoto.setVisibility(View.INVISIBLE);
        new AccountPwdPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isSet = bundle.getBoolean("isSet");
            if (!isSet) {
                setTitle(getString(R.string.change_money_pwd));
                llOldPwd.setVisibility(View.VISIBLE);
                tvTag.setVisibility(View.VISIBLE);
                tvTag.setText(getString(R.string.set_money_pwd_tag));
                tvPwdTag.setText(getString(R.string.new_money_pwd));
                etAccountPwd.setHint(getString(R.string.new_money_pwd));
                tvRePwdTag.setText(getString(R.string.repeat_password));
                etRepeatPwd.setHint(getString(R.string.repeat_password));
                tvSet.setText(getString(R.string.text_change));
                llForgot.setVisibility(View.VISIBLE);
                llPrompt.setVisibility(View.VISIBLE);
            }else {
                tvTag.setVisibility(View.GONE);
            }
        }
    }
    @OnClick({R.id.tvSet,R.id.tvForgot})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()){
            case R.id.tvForgot:
                showActivity(ResetAccountPwdActivity.class,null,0);
                finish();
                break;
            case R.id.tvSet:
                accountPwdw();
                break;
        }
    }

    private void accountPwdw() {
        String jyPassword = etAccountPwd.getText().toString().trim();
        String repeatPWd = etRepeatPwd.getText().toString().trim();
        String oldPwd = etOldPwd.getText().toString().trim();
        if ((StringUtils.isEmpty(jyPassword, repeatPWd) && isSet)) {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        } else if (StringUtils.isEmpty(jyPassword, repeatPWd, oldPwd) && !isSet) {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        } else if (!jyPassword.equals(repeatPWd)) {
            ToastUtils.showToast(getString(R.string.pwd_diff));
        } else {
            HashMap<String, String> map = new HashMap<>();
            if (isSet) {
                map.put("jyPassword", jyPassword);
                presenter.accountPwd(map);
            } else {
                map.put("newPassword", jyPassword);
                map.put("oldPassword", oldPwd);
                presenter.editAccountPwd(map);
            }
        }
    }

    @Override

    protected void loadData() {

    }


    @Override
    public void setPresenter(AccountPwdContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void accountPwdSuccess(String obj) {
        ToastUtils.showToast(obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void editAccountPwdSuccess(String obj) {
        ToastUtils.showToast(obj);
        finish();
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }
}
