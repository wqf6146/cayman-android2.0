package com.spark.szhb_master.activity.account_pwd;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import config.Injection;

public class ResetAccountPwdActivity extends BaseActivity implements AccountPwdContract.ResetView {
    public static final int RETURN_FROM_RESETACCOUNT_PWD = 0;

    @BindView(R.id.etLoginPwd)
    EditText etLoginPwd;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.etRepeatePwd)
    EditText etRepeatePwd;
    @BindView(R.id.tvEdit)
    TextView tvEdit;
    private CountDownTimer timer;
    private AccountPwdContract.ResetPresenter presenter;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_reset_account_pwd;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        setTitle(getString(R.string.change_money_pwd));
        tvGoto.setVisibility(View.INVISIBLE);
        new ResetAccountPwdPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
    }


    private void sendCode() {
        presenter.resetAccountPwdCode();
    }

    private void reset() {
        String newPassword = etPwd.getText().toString();
        String re = etRepeatePwd.getText().toString();
        String code = etCode.getText().toString();
        if (StringUtils.isEmpty(newPassword, re, code)) {
            ToastUtils.showToast(getString(R.string.incomplete_information));
            return;
        }
        if (!re.equals(newPassword)) ToastUtils.showToast(getString(R.string.pwd_diff));
        else {
            HashMap<String, String> map = new HashMap<>();
            map.put("newPassword", newPassword);
            map.put("code", code);
            presenter.resetAccountPwd(map);
        }
    }

    @Override
    protected void loadData() {

    }


    @Override
    public void setPresenter(AccountPwdContract.ResetPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void resetAccountPwdSuccess(String obj) {
        ToastUtils.showToast(obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        ToastUtils.showToast(toastMessage);
    }


    @Override
    public void resetAccountPwdCodeSuccess(String obj) {
        ToastUtils.showToast(obj);
        fillCodeView(90 * 1000);
    }

    private void fillCodeView(long time) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvSend.setText(getResources().getString(R.string.re_send) + "（" + millisUntilFinished / 1000 + "）");
            }

            @Override
            public void onFinish() {
                tvSend.setText(R.string.send_code);
                tvSend.setEnabled(true);
                timer.cancel();
                timer = null;
            }
        };
        timer.start();
    }


}
