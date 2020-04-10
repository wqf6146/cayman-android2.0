package com.spark.szhb_master.activity.aboutus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.feed.FeedbackActivity;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.VersionDialogFragment;
import com.spark.szhb_master.entity.AppInfo;
import com.spark.szhb_master.entity.Vision;
import com.spark.szhb_master.utils.FileUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.CommonUtils;
import com.spark.szhb_master.utils.PermissionUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.VersionCompareUtil;
import com.spark.szhb_master.utils.okhttp.FileCallback;
import com.spark.szhb_master.utils.okhttp.OkhttpUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;
import okhttp3.Request;

public class AboutUsActivity extends BaseActivity implements AboutUsContract.View {
    @BindView(R.id.ivLogo)
    ImageView ivLogo;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvWeb)
    TextView tvWeb;
    @BindView(R.id.llFeed)
    LinearLayout llFeed;
    @BindView(R.id.llVersion)
    LinearLayout llVersion;
    @BindView(R.id.tvVersionNum)
    TextView tvVersionNum;
    private AboutUsContract.Presenter presenter;
    private ProgressDialog progressDialog;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AboutUsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_about_us;
    }


    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        initProgressDialog();
        fragment = new VersionDialogFragment(AboutUsActivity.this);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.about_us));
        tvGoto.setVisibility(View.INVISIBLE);
        new AboutUsPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Glide.with(this).load(R.mipmap.ic_launcher).into(ivLogo);
        tvName.setText(getString(R.string.app_name) + "  " + "V" + CommonUtils.getVersionName(this));
        tvPhone.setText(getString(R.string.service_tel) + "  " + getString(R.string.phone));
        tvWeb.setText( getString(R.string.web));
        tvVersionNum.setText("V" + CommonUtils.getAppVersionNum());
    }

    @OnClick({R.id.llFeed,R.id.llVersion})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llFeed:
                if (!MyApplication.getApp().isLogin()){
                    showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
                    return;
                }
                showActivity(FeedbackActivity.class, null);
                break;
            case R.id.llVersion:
                if (!PermissionUtils.isCanUseStorage(this))
                    checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
                else {
                    presenter.getNewVision();
                }
                break;
        }
    }

    @Override
    protected void loadData() {
//       presenter.appInfo();
    }

    @Override
    public void setPresenter(AboutUsContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void appInfoFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void appInfoSuccess(AppInfo obj) {
        if (obj == null) return;
        if (StringUtils.isEmpty(obj.getLogo()))
            Glide.with(this).load(R.mipmap.ic_launcher).into(ivLogo);
        else Glide.with(this).load(obj.getLogo()).into(ivLogo);
        tvName.setText(obj.getName() + "V" + CommonUtils.getVersionName(this));
        tvDesc.setText(Html.fromHtml(obj.getDescription()));
        tvPhone.setText(getString(R.string.service_tel) + obj.getContact());
//        tvWeb.setText(getString(R.string.webSite) + obj.getUrl());
        tvWeb.setText( obj.getUrl());
    }

    @Override
    public void getNewVersionSuccess(Vision obj) {
        if (obj.getData() == null) {
            ToastUtils.showToast(getString(R.string.not_need_update_tag));
            return;
        }

        if (CommonUtils.getAppVersionNum().equals(obj.getData().getVersion())) {
            ToastUtils.showToast(getString(R.string.not_need_update_tag));
        }else {
            showUpDialog(obj);
        }
//        if (!CommonUtils.compareVersions(obj.getData().getVersion(), CommonUtils.getAppVersionNum())) {}

    }

    @Override
    public void getNewVersionFail(Integer code, String toastMessage) {

    }


    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(this).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
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

    private  VersionDialogFragment fragment;
    private void showUpDialog(final Vision obj) {
        if (obj.getData() == null) return;

        String getVersion = CommonUtils.getVersionName(activity).replace(".","");//本地的2.0.0
        String vers = obj.getData().getVersion().replace(".","");//服务器的1.1.5
        int i = VersionCompareUtil.compareVersion(obj.getData().getVersion(),CommonUtils.getVersionName(activity));
        if (i == 1){
//        if (Integer.valueOf(getVersion) > Integer.valueOf(vers)){
//        if (!CommonUtils.getVersionName(activity).equals(obj.getData().getVersion())) {

            fragment.show();
            fragment.setEntrust(obj.getData().getVersion(), obj.getData().getRemark(), obj.getData().getPublishTime());

//            fragment.setCancelable(false);

            fragment.tvConfirm_version().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 升级
//                    download(obj.getData().getDownloadUrl());
                    if (obj.getData().getDownloadUrl() == null || "".equals(obj.getData().getDownloadUrl())) {
                        ToastUtils.showToast(getString(R.string.update_address_error_tag));
                    } else download(obj.getData().getDownloadUrl());
                    fragment.dismiss();
                }
            });
        }else {
            ToastUtils.showToast(getString(R.string.not_need_update_tag));
        }
    }
    private void download(String url) {
        OkhttpUtils.get().url(url).build().execute(new FileCallback(FileUtils.getCacheSaveFile(this, "application.apk").getAbsolutePath()) {
            @Override
            public void inProgress(float progress) {
                progressDialog.show();
                progressDialog.setProgress((int) (progress * 100));
            }

            @Override
            public void onError(Request request, Exception e) {
                progressDialog.dismiss();
                NetCodeUtils.checkedErrorCode(AboutUsActivity.this, GlobalConstant.OKHTTP_ERROR, "null");
            }

            @Override
            public boolean onResponse(File response) {
                progressDialog.dismiss();
                CommonUtils.installAPk(response);
                return false;
            }
        });
    }

    private void initProgressDialog() {
        //创建进度条对话框
        progressDialog = new ProgressDialog(this);
        //设置标题
        progressDialog.setTitle(getString(R.string.downloading_tag));
        //设置信息
        progressDialog.setMessage(getString(R.string.downloading_crazy_tag));
        progressDialog.setProgress(0);
        //设置显示的格式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }


}
