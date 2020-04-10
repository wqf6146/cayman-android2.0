package com.spark.szhb_master.activity.bind_account;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.AccountSetting;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 绑定银行卡
 * Created by Administrator on 2018/5/2 0002.
 */

public class BindBankActivity extends BaseActivity implements BindAccountContact.BankView {
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.llBank)
    LinearLayout llBank;
    @BindView(R.id.etBranch)
    EditText etBranch;
    @BindView(R.id.llBranch)
    LinearLayout llBranch;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etConfirmAccount)
    EditText etConfirmAccount;
    @BindView(R.id.llConfirmAccount)
    LinearLayout llConfirmAccount;
    @BindView(R.id.etNewPwd)
    EditText etNewPwd;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.tvBank)
    TextView tvBank;
    private BindAccountContact.BankPresenter presenter;
    private AccountSetting setting;
    private NormalListDialog dialog;
    private String[] bankNames;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_bank;
    }


    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.bank_card_binding));
        tvGoto.setVisibility(View.INVISIBLE);
        new BindBankPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            setting = (AccountSetting) bundle.getSerializable("accountSetting");
            etName.setText(setting.getRealName());
            if (setting.getBankVerified() == 1) {
                tvBank.setText(setting.getBankInfo().getBank());
                etBranch.setText(setting.getBankInfo().getBranch());
                etAccount.setText(setting.getBankInfo().getCardNo());
                tvTitle.setText(getString(R.string.text_change) + getString(R.string.unionpay_account));
            } else {
                tvTitle.setText(getString(R.string.binding) + getString(R.string.unionpay_account));
            }
            bankNames = getResources().getStringArray(R.array.bank_name);
            tvBank.setText(bankNames[0]);
        }
    }

    @OnClick({R.id.llBank, R.id.tvConfirm})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llBank:
                showDialog();
                break;
            case R.id.tvConfirm:
                confirm();
                break;
        }
    }

    /**
     * 提交数据
     */
    private void confirm() {
        String name = etName.getText().toString();
        String bank = tvBank.getText().toString();
        String branch = etBranch.getText().toString();
        String account = etAccount.getText().toString();
        String reaccount = etConfirmAccount.getText().toString();
        String pwd = etNewPwd.getText().toString();
        if (!StringUtils.isEmpty(name, bank, branch, account, reaccount, pwd)) {
            if (account.equals(reaccount)) {
                bindOrUpdateBnak(bank, branch, pwd, name, account);
            } else {
                ToastUtils.showToast(getString(R.string.diff_cardnumber));
            }
        } else {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        }

    }

    private void bindOrUpdateBnak(String bank, String branch, String pwd, String name, String account) {
        HashMap<String, String> map = new HashMap<>();
        map.put("bank", bank);
        map.put("branch", branch);
        map.put("jyPassword", pwd);
        map.put("realName", name);
        map.put("cardNo", account);
        if (setting.getBankVerified() == 1) {
            presenter.doUpdateBank(map);
        } else {
            presenter.doBindBank(map);
        }
    }

    /**
     * 选择开户银行
     */
    private void showDialog() {
        dialog = new NormalListDialog(activity, bankNames);
        dialog.title("请选择银行");
        dialog.titleBgColor(getResources().getColor(R.color.sec_font_black));
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bankName = bankNames[position];
                tvBank.setText(bankName);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void setPresenter(BindAccountContact.BankPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doBindBankSuccess(String obj) {
        ToastUtils.showToast(getString(R.string.upload_success));
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doUpdateBankSuccess(String obj) {
        ToastUtils.showToast(getString(R.string.upload_success));
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doPostFailFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }
}
