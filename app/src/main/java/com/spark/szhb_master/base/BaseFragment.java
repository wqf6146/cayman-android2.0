package com.spark.szhb_master.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.spark.szhb_master.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/9/26.
 */

public abstract class BaseFragment extends Fragment {
    protected View rootView;
    Unbinder unbinder;
    protected ImmersionBar immersionBar;
    protected boolean isInit = false;
    protected boolean isNeedLoad = true;
    protected boolean isSetTitle = false;
    protected ImageView ivBack;
    protected TextView tvTitle;
    protected TextView tvGoto;
    protected LinearLayout llTitle;
    protected Activity activity;
    protected List<BaseFragment> fragments = new ArrayList<>();

    private static final String TAG = BaseFragment.class.getSimpleName();
    private boolean isReuseView;
    private boolean isFirstVisible;
    private boolean isFragmentVisible;

    //setUserVisibleHint()在Fragment创建时会先被调用一次，传入isVisibleToUser = false
    //如果当前Fragment可见，那么setUserVisibleHint()会再次被调用一次，传入isVisibleToUser = true
    //如果Fragment从可见-&gt;不可见，那么setUserVisibleHint()也会被调用，传入isVisibleToUser = false
    //总结：setUserVisibleHint()除了Fragment的可见状态发生变化时会被回调外，在new Fragment()时也会被回调
    //如果我们需要在 Fragment 可见与不可见时干点事，用这个的话就会有多余的回调了，那么就需要重新封装一个
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //setUserVisibleHint()有可能在fragment的生命周期外被调用
        if (rootView == null) {
            return;
        }
        if (isFirstVisible && isVisibleToUser) {
            onFragmentFirstVisible();
            isFirstVisible = false;
        }
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            isFragmentVisible = false;
            onFragmentVisibleChange(false);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        if (getUserVisibleHint()) {
            if (isFirstVisible) {
                onFragmentFirstVisible();
                isFirstVisible = false;
            }else{
                onFragmentVisibleChange(true);
                isFragmentVisible = true;
            }
        }

        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        tvTitle = rootView.findViewById(R.id.tvTitle);
        ivBack = rootView.findViewById(R.id.ivBack);
        llTitle = rootView.findViewById(R.id.llTitle);
        tvGoto = rootView.findViewById(R.id.tvGoto);
        activity = getmActivity();
        isInit = true;
        if (isImmersionBarEnabled())
            initImmersionBar();
        initView();
        initData();
        setListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        initVariable();
    }

    private void initVariable() {
        isFirstVisible = true;
        isFragmentVisible = false;
        rootView = null;
        isReuseView = true;
    }

    /**
     * 设置是否使用 view 的复用，默认开启
     * view 的复用是指，ViewPager 在销毁和重建 Fragment 时会不断调用 onCreateView() -&gt; onDestroyView()
     * 之间的生命函数，这样可能会出现重复创建 view 的情况，导致界面上显示多个相同的 Fragment
     * view 的复用其实就是指保存第一次创建的 view，后面再 onCreateView() 时直接返回第一次创建的 view
     *
     * @param isReuse
     */
    protected void reuseView(boolean isReuse) {
        isReuseView = isReuse;
    }

    /**
     * 去除setUserVisibleHint()多余的回调场景，保证只有当fragment可见状态发生变化时才回调
     * 回调时机在view创建完后，所以支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
     *
     * 可在该回调方法里进行一些ui显示与隐藏，比如加载框的显示和隐藏
     *
     * @param isVisible true  不可见 -&gt; 可见
     *                  false 可见  -&gt; 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    /**
     * 在fragment首次可见时回调，可在这里进行加载数据，保证只在第一次打开Fragment时才会加载数据，
     * 这样就可以防止每次进入都重复加载数据
     * 该方法会在 onFragmentVisibleChange() 之前调用，所以第一次打开时，可以用一个全局变量表示数据下载状态，
     * 然后在该方法内将状态设置为下载状态，接着去执行下载的任务
     * 最后在 onFragmentVisibleChange() 里根据数据下载状态来控制下载进度ui控件的显示与隐藏
     */
    protected void onFragmentFirstVisible() {

    }

    protected boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    protected void setTitle(String title) {
        if (tvTitle != null)
            tvTitle.setText(title);
    }

    public void setShowBackBtn(boolean showBackBtn) {
        if (ivBack != null && showBackBtn) {
            if (showBackBtn) {
                ivBack.setVisibility(View.VISIBLE);
                ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                });
            } else {
                ivBack.setVisibility(View.GONE);
            }
        }
    }

    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected void initView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 各控件的点击事件
     *
     * @param v
     */
    protected void setOnClickListener(View v) {

    }

    /**
     * 各控件的点击
     */
    protected void setListener() {

    }

    protected void fillWidget() {
    }

    ;

    protected void loadData() {
    }

    ;

    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void initImmersionBar() {
        immersionBar = ImmersionBar.with(this);
        immersionBar.keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).init();
    }

    public static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void displayLoadingPopup() {
        if (getActivity() != null) ((BaseActivity) getActivity()).displayLoadingPopup();
    }

    public void hideLoadingPopup() {
        if (getActivity() != null) ((BaseActivity) getActivity()).hideLoadingPopup();
    }

    @Override
    public View getView() {
        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (immersionBar != null) immersionBar.destroy();
        unbinder.unbind();
        super.onDestroyView();
    }

    protected void finish() {
        getActivity().finish();
    }

    public BaseActivity getmActivity() {
        return (BaseActivity) super.getActivity();
    }

    /**
     * 跳转activity,不关闭当前界面
     *
     * @param cls
     * @param bundle
     */
    protected void showActivity(Class<?> cls, Bundle bundle) {
        showActivity(cls, bundle, -1);
    }

    /**
     * 跳转activity,不关闭当前界面，含跳转回来的的回调
     *
     * @param cls
     * @param bundle
     */
    protected void showActivity(Class<?> cls, Bundle bundle, int requesCode) {
        Intent intent = new Intent(activity, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        if (requesCode >= 0)
            startActivityForResult(intent, requesCode);
        else
            startActivity(intent);
    }
}
