package com.spark.szhb_master.activity.message;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.base.ContractGet;
import com.spark.szhb_master.base.GlobalGetPresenter;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;


import butterknife.BindView;
import config.Injection;

/**
 * 平台消息详情/首页图片点击进入
 */
public class WebViewActivity extends BaseActivity implements ContractGet.BaseView {
    @BindView(R.id.webview)
    WebView webview;
    private GlobalGetPresenter presenter;
    private String id;
    private boolean isImage;
    private ProgressBar progressBar1;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_message_detail;
    }


    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        progressBar1 = findViewById(R.id.progressBar1);
        initwebview();
    }

    private void initwebview() {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webview.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void initData() {
        super.initData();
        new GlobalGetPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isImage = bundle.getBoolean("isImage");
            tvGoto.setVisibility(View.INVISIBLE);
            if (isImage) { // 首页图片跳转链接
                String title = bundle.getString("title");
                String url = bundle.getString("url");
                setTitle(title);
                setWebData(url);
            } else {
                String title = bundle.getString("title");
                if (StringUtils.isEmpty(title)){
                    setTitle(getString(R.string.message_details));
                }else {
                    setTitle(title);
                }
//                if (!StringUtils.isEmpty(title) && title.equals("攻略")) {
//                    setTitle(title);
//                } else if (!StringUtils.isEmpty(title) && title.equals("活动规则")) {
//                    setTitle(title);
//                } else if (!StringUtils.isEmpty(title) && title.equals("用户协议")) {
//                    setTitle(title);
//                } else {
//                    setTitle(getString(R.string.message_details));
//                }
//                id = bundle.getString("id");
                String content = bundle.getString("url");
                setWebData(content);
            }
        }

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根

                if (newProgress != 100) {
                    progressBar1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar1.setProgress(newProgress);//设置进度值
                } else {
                    progressBar1.setVisibility(View.GONE);//加载完网页进度条消失
                }

            }
        });
    }

    /**
     * 设置webview链接
     *
     * @param url
     */
    private void setWebData(String url) {
        if (StringUtils.isNotEmpty(url)) {
            if (!url.contains("http")) {
                url = UrlFactory.getHost() + "/" + url;
            }
            webview.loadUrl(url);
        }
    }

    @Override
    protected void loadData() {
//        if (!isImage)
//            presenter.doStringGet(id);
    }

    @Override
    public void setPresenter(Object presenter) {
        this.presenter = (GlobalGetPresenter) presenter;
    }

    @Override
    public void doGetSuccess(String content) {
        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }

    @Override
    public void doGetFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }
}
