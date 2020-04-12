package com.spark.szhb_master.activity.main;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.aboutus.AboutUsActivity;
import com.spark.szhb_master.activity.entrust.NowTrustActivity;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.activity.login.LoginStepOneActivity;
import com.spark.szhb_master.activity.message.MessageActivity;
import com.spark.szhb_master.activity.ads.AdsActivity;
import com.spark.szhb_master.activity.my_match.MatchActivity;
import com.spark.szhb_master.activity.order.MyOrderActivity;
import com.spark.szhb_master.activity.my_promotion.PromotionActivity;
import com.spark.szhb_master.activity.myinfo.MyInfoActivity;
import com.spark.szhb_master.activity.safe.SafeActivity;
import com.spark.szhb_master.activity.setting.SettingActivity;
import com.spark.szhb_master.activity.wallet.WalletActivity;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.entity.AssetsInfo;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.ui.AvatarImageView;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseTransFragment;
import com.spark.szhb_master.entity.Coin;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.entity.Vision;
import com.spark.szhb_master.utils.CommonUtils;
import com.spark.szhb_master.utils.FileUtils;
import com.spark.szhb_master.utils.PermissionUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.ui.intercept.MyScrollView;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.okhttp.FileCallback;
import com.spark.szhb_master.utils.okhttp.OkhttpUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/1/29.
 */

public class MyFragment extends BaseTransFragment implements MainContract.MyView {

    public static final String TAG = MyFragment.class.getSimpleName();

    @BindView(R.id.llVersion)
    LinearLayout llVersion;

    @BindView(R.id.llSettings)
    LinearLayout llSettings;

    @BindView(R.id.scrollView)
    MyScrollView scrollView;

    @BindView(R.id.tvVersionNum)
    TextView tvVersionNum;

    @BindView(R.id.fm_tv_username)
    TextView tvUsername;

    @BindView(R.id.fm_aiv_headerimg)
    ImageView ivHead;

    @BindView(R.id.fm_tv_vipsystem)
    TextView tvVipSystem;

    @BindView(R.id.fm_tv_editinfo)
    TextView tvEditInfo;

    @BindView(R.id.fm_tv_totalassets)
    TextView tvTotalAssets;

    @BindView(R.id.fm_tv_zjzhbtc)
    TextView tvZjzhBtc;

    @BindView(R.id.fm_tv_zjzhusdt)
    TextView tvZjzhUsdt;

    @BindView(R.id.fm_tv_hyqczhbtc)
    TextView tvHyqczhBtc;

    @BindView(R.id.fm_tv_hyqczhusdt)
    TextView tvHyqczhUsdt;

    @BindView(R.id.llZhuanr)
    LinearLayout llZhuanr;

    @BindView(R.id.llHuaz)
    LinearLayout llHuaz;

    @BindView(R.id.llWdzj)
    LinearLayout llWdzj;

    @BindView(R.id.llFxjl)
    LinearLayout llFxjl;

    @BindView(R.id.llSfrz)
    LinearLayout llSfrz;

    @BindView(R.id.llAqzx)
    LinearLayout llAqzx;

    @BindView(R.id.ll_Bzzx)
    LinearLayout ll_Bzzx;

    @BindView(R.id.fm_rl_hyqczh)
    RelativeLayout rlHyqczh;

    @BindView(R.id.fm_rl_zjzh)
    RelativeLayout rlZjzh;

    @BindView(R.id.fm_rl_userinfo)
    RelativeLayout rlUserinfo;

    @BindView(R.id.fm_tv_totalassetsconverts)
    TextView tvTotalAssetsConverts;

    private MainContract.MyPresenter presenter;
    private List<Coin> coins = new ArrayList<>();
    private User mUser;
    private ProgressDialog progressDialog;
    private boolean isfirst =false;


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            loadData(); // 获取用户信息
//        }
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        super.initView();

    }

    @Override
    protected void initData() {
        tvVersionNum.setText("V" + CommonUtils.getAppVersionNum());
    }


    @Override
    protected void loadData() {

    }

    @OnClick({R.id.fm_rl_userinfo,
            R.id.fm_tv_vipsystem,
            R.id.fm_rl_zjzh,R.id.fm_rl_hyqczh,
            R.id.llZhuanr, R.id.llZhuanc,R.id.llHuaz,
            R.id.llWdzj,R.id.llFxjl,R.id.llSfrz,R.id.llAqzx,R.id.ll_Bzzx,R.id.llSettings,R.id.llVersion})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);

//        if (v.getId() != R.id.ivSee && v.getId() != R.id.ll_aboutUs && !MyApplication.getApp().isLogin()) {
//            showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
//            return;
//        }

        if (!MyApplication.getApp().isLogin()){
            showActivity(LoginStepOneActivity.class, null, LoginActivity.RETURN_LOGIN);
            return;
        }
        Bundle bundle = new Bundle();
        switch (v.getId()) {
//            case R.id.llTitle:
//                showActivity(MyInfoActivity.class, null, 1);
//                break;
//            case R.id.llOrder:
//                bundle.putInt("type", 0);
//                showActivity(MyOrderActivity.class, bundle);
//                break;
//            case R.id.llAds:
//                bundle.putString("username", user.getUsername());
//                bundle.putString("avatar", user.getAvatar());
//                showActivity(AdsActivity.class, bundle);
//                break;
//            case R.id.llMessage:
//                showActivity(MessageActivity.class, null);
//                break;
//            case R.id.ll_aboutUs:
//                showActivity(AboutUsActivity.class, null);
//                break;
//            case R.id.llSafe:
//                showActivity(SafeActivity.class, null);
//                break;
            case R.id.llSettings:
                //showActivity(SafeActivity.class, null, 2);
                showActivity(SettingActivity.class, null, 2);
                break;
//            case R.id.llEntrust:
//                bundle.putBoolean("isMy", true);
////                showActivity(AllTrustActivity.class, bundle);
//                showActivity(NowTrustActivity.class, bundle);
//                break;
//            case R.id.llPromotion:
//                showActivity(PromotionActivity.class, null);
//                break;
//            case R.id.llKefu:
////                isNeedChecke = false;
//                SharedPreferenceInstance.getInstance().saveIsOpen(false);
//                String url = "http://kefu.caymanex.pro:80/im/text/15FwEk.html";
//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//                break;
//            case R.id.llMatch:
//                showActivity(MatchActivity.class, null);
////                presenter.safeSetting();
//                break;
            case R.id.llVersion:
                if (!PermissionUtils.isCanUseStorage(getActivity()))
                    checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
                else {
                    presenter.getNewVision();
                }
                break;
//            case R.id.tvLogOut:
//                showCofirmDialog();
//                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(getActivity()).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    presenter.getNewVision();
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    ToastUtils.showToast(getString(R.string.storage_permission));
                    break;
            }
        }
    };

    /**
     * 资产可见状态
     */
    private void switchSee() {
//        if (!"*****".equals(tvCnyAmount.getText())) {
//            tvCnyAmount.setText("*****");
//
//            SharedPreferenceInstance.getInstance().saveMoneyShowtype(2);
//            ivSee.setImageResource(R.mipmap.icon_eye_close);
//        } else {
//            tvCnyAmount.setText(" ≈ " + MathUtils.getRundNumber(sumCny, 2, null) +" "+ GlobalConstant.CNY);
//            SharedPreferenceInstance.getInstance().saveMoneyShowtype(1);
//            ivSee.setImageResource(R.mipmap.icon_eye_open);
//        }
    }

    /**
     * 未登录显示
     */
    private void notLoginViewText() {
       // ivSee.setVisibility(View.GONE);
//        sumCny = 0.00;
//        sumUsd = 0.000000;

        Glide.with(getActivity().getApplicationContext()).load(R.mipmap.icon_default_header).into(ivHead);

        tvUsername.setText(getString(R.string.hydl));
        tvVipSystem.setVisibility(View.GONE);
        tvEditInfo.setVisibility(View.GONE);

        tvTotalAssetsConverts.setVisibility(View.GONE);

        tvTotalAssets.setText("--");
        tvZjzhBtc.setText("━ ━");
        tvZjzhUsdt.setVisibility(View.GONE);

        tvHyqczhBtc.setText("━ ━");
        tvHyqczhUsdt.setVisibility(View.GONE);
    }

    /**
     * 登录显示
     */
    private void loginingViewText() {
//        user = MyApplication.getApp().getCurrentUser();
//        if (user != null) {
//            if (!StringUtils.isEmpty(user.getUsername())) {
//                String str = user.getUsername().substring(0, 1).toUpperCase();
//                tvLogOut.setVisibility(View.VISIBLE);
//            }
//        }
        presenter.getUserInfo();
        presenter.myWallet();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }

    public void loadUserInfo(){
        mUser = MyApplication.getApp().getCurrentUser();
        if (MyApplication.getApp().isLogin()) {
            loginingViewText();
        } else {
            notLoginViewText();
        }
    }

    @Override
    public void getUserInfoSuccess(User user) {
        if (user != null){
            user.setToken(mUser.getToken());
            this.mUser = user;
            MyApplication.getApp().setCurrentUser(user);
            getivSee();
        }

    }

    private void getivSee() {
        if (!TextUtils.isEmpty(mUser.getAvatar()))
            Glide.with(getActivity().getApplicationContext()).load(mUser.getAvatar()).error(R.mipmap.icon_default_header).into(ivHead);

        tvUsername.setText(mUser.getNick_name());
        tvVipSystem.setVisibility(View.VISIBLE);
        tvEditInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(MainContract.MyPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void myWalletSuccess(AssetsInfo assetsInfo) {
        if (assetsInfo == null) return;


        tvTotalAssets.setText(String.format(getString(R.string.mytotalassets),assetsInfo.getSum()));
        tvTotalAssetsConverts.setVisibility(View.VISIBLE);
        tvTotalAssetsConverts.setText(String.format(getString(R.string.mytotalassetsconverts),assetsInfo.getSum_convert()));
        tvZjzhBtc.setText(String.format(getString(R.string.mytotalassets),assetsInfo.getUsable()));
        tvZjzhUsdt.setVisibility(View.VISIBLE);
        tvZjzhUsdt.setText(String.format(getString(R.string.mytotalassetsconverts),assetsInfo.getUsable_convert()));
        tvHyqczhBtc.setText(String.format(getString(R.string.mytotalassets),assetsInfo.getFrost()));
        tvHyqczhUsdt.setVisibility(View.VISIBLE);
        tvHyqczhUsdt.setText(String.format(getString(R.string.mytotalassetsconverts),assetsInfo.getFrost_convert()));

    }
//    7073

    private void calcuTotal(List<Coin> coins) {
//        sumUsd = 0;
//        sumCny = 0;
//        for (Coin coin : coins) {
//            sumUsd += ((coin.getBalance() * coin.getCoin().getUsdRate()) + (coin.getFrozenBalance() * coin.getCoin().getUsdRate()));
//            sumCny += ((coin.getBalance() * coin.getCoin().getCnyRate()) + (coin.getFrozenBalance() * coin.getCoin().getCnyRate()));
//        }
//        if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 1) {
//            tvAmount.setText(MathUtils.getRundNumber(sumUsd, 6, null));
//            tvCnyAmount.setText(" ≈ " + MathUtils.getRundNumber(sumCny, 2, null) + " CNY");
//        } else if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 2) {
//            tvAmount.setText("********");
//            tvCnyAmount.setText("*****");
//        }
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        //10.19更改
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
//        ToastUtils.show(toastMessage, Toast.LENGTH_SHORT);
        if (code == GlobalConstant.TOKEN_DISABLE1 || code == GlobalConstant.TOKEN_DISABLE2) {
            if (MyApplication.getApp().isLogin()) { // 当出现用户登录失效时，请求数据失败，需要重新刷新页面显示信息
                loginingViewText();
            } else {
                notLoginViewText();
                MyApplication.getApp().deleteCurrentUser();
            }
        }
    }

    @Override
    public void getNewVisionSuccess(Vision obj) {
        if (obj.getData() == null) {
            ToastUtils.showToast(getString(R.string.not_need_update_tag));
            return;
        }
        if (!CommonUtils.compareVersions(obj.getData().getVersion(), CommonUtils.getAppVersionNum())) {
            ToastUtils.showToast(getString(R.string.not_need_update_tag));
        } else {
            showDialog(obj);
        }
    }

    private SafeSetting safeSetting;
    @Override
    public void safeSettingSuccess(SafeSetting obj) {
        if (obj == null)
            return;
        safeSetting = obj;
        if (safeSetting.getRealVerified() == 0) {
            ShiMingDialog shiMingDialog = new ShiMingDialog(getContext());
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
        } else {

        }
    }

    private void showDialog(final Vision obj) {
        final MaterialDialog dialog = new MaterialDialog(activity);
        dialog.title(getString(R.string.app_name)).titleTextColor(getResources().getColor(R.color.colorPrimary)).content(getString(R.string.confirm_update)).setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        if (StringUtils.isNotEmpty(obj.getData().getDownloadUrl())) {
                            download(obj.getData().getDownloadUrl());
                        } else {
                            ToastUtils.showToast(getString(R.string.update_address_error_tag));
                        }
                        dialog.superDismiss();
                    }
                });
        dialog.show();
    }

    private void download(String url) {
        OkhttpUtils.get().url(url).build().execute(new FileCallback(FileUtils.getCacheSaveFile(getActivity(), "application.apk").getAbsolutePath()) {
            @Override
            public void inProgress(float progress) {
                showProgressDialog(progress);
            }

            @Override
            public void onError(Request request, Exception e) {
                progressDialog.dismiss();
                NetCodeUtils.checkedErrorCode(getmActivity(), GlobalConstant.OKHTTP_ERROR, "null");
            }

            @Override
            public boolean onResponse(File response) {
                progressDialog.dismiss();
                CommonUtils.installAPk(response);
                return false;
            }
        });
    }

    private void showProgressDialog(float progress) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("正在下载");
            progressDialog.setMessage("玩命下载中...");
            progressDialog.setProgress(0);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        }
        progressDialog.show();
        progressDialog.setProgress((int) (progress * 100));
    }

    @Override
    protected String getmTag() {
        return TAG;
    }

    /**
     * 确认是否退出登录
     */
    private void showCofirmDialog() {
        final MaterialDialog dialog = new MaterialDialog(activity);
        dialog.title("提示").titleTextColor(getResources().getColor(R.color.colorPrimary)).content(getString(R.string.logout_current_account)).btnText(getString(R.string.cancle), getString(R.string.confirm)).setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        MyApplication.getApp().deleteCurrentUser();
                        MyApplication.getApp().loginAgain(getmActivity());
                        SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
                        SharedPreferenceInstance.getInstance().saveLockPwd("");
                        SharedPreferenceInstance.getInstance().saveToken("");
                        SharedPreferenceInstance.getInstance().saveGoogle("close");
                        dialog.superDismiss();
                    }
//                        MyApplication.getApp().isLogin()
                });
        dialog.show();
    }
}
