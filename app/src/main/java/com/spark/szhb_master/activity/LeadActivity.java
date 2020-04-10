package com.spark.szhb_master.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.R;
import com.spark.szhb_master.utils.SharedPreferenceInstance;

import java.util.ArrayList;


/**
 * 引导页
 */
public class LeadActivity extends Activity implements ViewPager.OnPageChangeListener {
    private ViewPager vPager;
    private VpAdapter vpAdapter;
    private TextView ivLead;
    private TextView btnStartExperience;
    private static int[] imgs = {R.mipmap.bg_lead_first, R.mipmap.bg_lead_sec, R.mipmap.bg_lead_third,R.mipmap.bg_lead_four}; // 要显示的图片资源
    private ArrayList<ImageView> imageViews; // 用于包含引导页要显示的图片
    private ImageView[] dotViews; // 用于包含底部小圆点的图片，只要设置每个imageview的图片资源为刚刚写的dot_selector，选择和没选中就会有不同的效果，实现导航的功能。
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        initView();
        initData();
        setLisenter();
    }


    private void initView() {
        vPager = (ViewPager) findViewById(R.id.my_viewpager);
        btnStartExperience = (TextView) findViewById(R.id.ivLead);
        ivLead = findViewById(R.id.ivLead);
    }

    private void initData() {
        activity = this;
        initImages();
        initDots();
        vpAdapter = new VpAdapter(imageViews);
        vPager.setAdapter(vpAdapter);
        vPager.setOnPageChangeListener(this);
    }

    private void setLisenter() {
        ivLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferenceInstance.getInstance().saveIsFirstUse(false);
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 把引导页要显示的图片添加到集合中，以传递给适配器，用来显示图片
     */
    private void initImages() {
        LayoutParams mParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); // 设置每一张图片都填充窗口
        imageViews = new ArrayList<ImageView>();
        for (int i = 0; i < imgs.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(imgs[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews.add(iv);
            if (i == imgs.length - 1) {
                iv.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    /**
     * 根据引导页的数量，动态生成相应数量的导航小圆点，并添加到LinearLayout中显示。
     */
    private void initDots() {
        LinearLayout layout = findViewById(R.id.llDot);
        LayoutParams mParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mParams.setMargins(10, 0, 10, 0); // 设置小圆点左右之间的间隔
        dotViews = new ImageView[imgs.length];
        for (int i = 0; i < imageViews.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(mParams);
            imageView.setImageResource(R.drawable.dot_selector);
            if (i == 0) {
                imageView.setSelected(true); // 默认启动时，选中第一个小圆点
            } else {
                imageView.setSelected(false);
            }
            dotViews[i] = imageView; // 得到每个小圆点的引用，用于滑动页面时，（onPageSelected方法中）更改它们的状态。
            layout.addView(imageView); // 添加到布局里面显示
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    /**
     * arg0：当前滑动显示页面的索引值，可以根据这个值，来设置相应小圆点的状态。
     */
    @Override
    public void onPageSelected(int arg0) {
        for (int i = 0; i < dotViews.length; i++) {
            if (arg0 == i) {
                dotViews[i].setSelected(true);
            } else {
                dotViews[i].setSelected(false);
            }
        }
        if (arg0 == dotViews.length - 1) {
            btnStartExperience.setVisibility(View.VISIBLE);// 显示
        } else {
            btnStartExperience.setVisibility(View.GONE);// 隐藏
        }
    }

    class VpAdapter extends PagerAdapter {
        private ArrayList<ImageView> imageViews;

        public VpAdapter(ArrayList<ImageView> imageViews) {
            this.imageViews = imageViews;
        }

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }

    }
}
