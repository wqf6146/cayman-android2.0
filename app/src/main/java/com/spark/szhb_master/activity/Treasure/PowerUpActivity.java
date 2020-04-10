package com.spark.szhb_master.activity.Treasure;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.SignInDialog;
import com.spark.szhb_master.entity.CallHistory;
import com.spark.szhb_master.entity.TreasureInfo;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * Created by Administrator on 2018/9/20 0020.
 */

public class PowerUpActivity extends BaseActivity implements CallHistoryContract.View, AntForestView.OnStopAnimateListener {

    @BindView(R.id.rule)
    ImageView rule;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ll_bb)
    LinearLayout ll_bb;
    @BindView(R.id.ll_yaoqing)
    LinearLayout ll_yaoqing;
    @BindView(R.id.ll_toup)
    LinearLayout ll_toup;
    @BindView(R.id.anminal_text)
    TextView anminal_text;
    @BindView(R.id.history)
    TextView history;
    @BindView(R.id.sign_in)
    TextView sign_in;
    @BindView(R.id.power_img)
    ImageView power_img;
    @BindView(R.id.id_ll_add_assets_anim)
    FrameLayout idLlAddAssetsAnim;    // 资产layout

    private CallHistoryContract.Presenter presenter;
    private SignInDialog signInDialog;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.powerup_layout;
    }

    @Override
    protected void initData() {
        super.initData();
        new CallHistoryPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        presenter.getTreaInfo();
        signInDialog = new SignInDialog(this);
    }

    @OnClick({R.id.ivBack, R.id.rule, R.id.ll_bb, R.id.ll_yaoqing, R.id.ll_toup, R.id.history, R.id.sign_in})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.rule:
                startActivity(new Intent(this, RuleAcitivity.class).putExtra("num", 3));
//                showActivity(RuleAcitivity.class,null);
                break;
            case R.id.history:
                Bundle bundle = new Bundle();
                bundle.putInt("gcx_num", gcx_nu);
                bundle.putDouble("num", num_pw);
                showActivity(PowerActivity.class, bundle);
                break;
            case R.id.ll_yaoqing:
                showActivity(SuanActivity.class, null);
//                showActivity(PreheatFrdActivity.class,null);
                break;
            case R.id.ll_toup:
                SharedPreferenceInstance.getInstance().saveMSG(1);
                finish();
                break;
            case R.id.ll_bb:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("bibi", 2);
                startActivity(intent);
                finish();
                break;
            case R.id.sign_in:
                break;
        }
    }

    private int msg = 1;



    @Override
    public void getVoteHistorySuccess(List<CallHistory> list) {

    }

    private double num_pw;
    private int gcx_nu;

    @Override
    public void getTreaInfoSuccess(TreasureInfo treasureInfo) {
        num_pw = treasureInfo.getAvailablePower();
        anminal_text.setText(num_pw + " ");
        gcx_nu = (int) Math.floor(treasureInfo.getGcxAmount());
        ivBack.setEnabled(true);
    }

    @Override
    public void getSigninSuccess() {
        signInDialog.setEntrust(1);
        signInDialog.show();
    }

    @Override
    public void getSigninFiled(Integer code, String toastMessage) {
        if (code == 500) {
            signInDialog.setEntrust(2);
            signInDialog.show();
        } else {
            NetCodeUtils.checkedErrorCode(this, code, toastMessage);
        }
    }

    @Override
    public void doFiled(Integer code, String toastMessage) {
        ToastUtils.show(toastMessage, Toast.LENGTH_SHORT);
    }

    @Override
    public void setPresenter(CallHistoryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @BindView(R.id.id_waterball)
    AntForestView idWaterball;

    @Override
    protected void loadData() {
        super.loadData();

        presenter.getPowerRecord();
    }

    @Override
    public void getPowerRecordSuccess(List<WaterBallBean> list) {

        final List<WaterBallBean> waterBallBeanList = new ArrayList<>();
        WaterBallBean waterBallBean = null;
        waterBallBeanList.clear();
        for (int i = 0; i < list.size(); i++) {
            waterBallBean = new WaterBallBean(list.get(i).getId(), list.get(i).getAmount(), list.get(i).getCreateTime(), false);
            waterBallBeanList.add(waterBallBean);
        }
        idWaterball.setData(waterBallBeanList);
        idWaterball.setOnStopAnimateListener(PowerUpActivity.this);
    }

    @Override
    public void powerCollectSuccess() {
        presenter.getTreaInfo();
    }

    @Override
    public void onBallDisappearAnimListener(String number, int id, int position) {
        startAssetTextAnimStep1(number);
        HashMap<String, String> map = new HashMap<>();
        map.put("powerId", id + "");
        presenter.powerCollect(map);
    }

    @Override
    public void onExitAnimateListener() {
        finish();
    }

    @Override
    public void onBallClick() {
        ivBack.setEnabled(false);
    }


    private void startAssetTextAnimStep1(String number) {
        final TextView textView = new TextView(this);
        textView.setText("+" + number);
        textView.setTextColor(getResources().getColor(R.color.font_yellow));
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);

//        idLlAddAssetsAnim.addView(textView);
        startAssetTextAnimStep2(textView);
    }

    private void startAssetTextAnimStep2(final TextView textView) {
        textView.animate().translationY(100).alpha(0).setDuration(700).setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        idLlAddAssetsAnim.removeView(textView);
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        }).setInterpolator(new DecelerateInterpolator()).start();
    }


}
