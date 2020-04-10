package com.spark.szhb_master.activity.bind_email;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;

import butterknife.BindView;

/**
 * 邮箱显示
 */
public class EmailActivity extends BaseActivity {
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    private String email;

    public static void actionStart(Context context, String email) {
        Intent intent = new Intent(context, EmailActivity.class);
        intent.putExtra("email", email);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_email;
    }


    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.binding_email));
        tvGoto.setVisibility(View.INVISIBLE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            email = bundle.getString("email");
            tvEmail.setText(email);
        }
    }

}
