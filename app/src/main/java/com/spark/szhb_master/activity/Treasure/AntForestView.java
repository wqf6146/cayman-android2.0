
package com.spark.szhb_master.activity.Treasure;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spark.szhb_master.R;
import com.spark.szhb_master.utils.DateUtils;
import com.spark.szhb_master.utils.ToastUtils;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by 54966 on 2018/5/12.
 */

public class AntForestView extends FrameLayout {

    private List<WaterBallBean> beanList = new ArrayList<>();

    private List<WaterBallBean> surplusBeanList = new ArrayList<>();                                                                                                        // 如果服务器返回的不止10个，其他的就都放到这个集合中

    private List<View> viewList = new ArrayList<>();

    public List<WaterBallBean> disappearWaterBallList = new ArrayList<>();                                                                                                        // 需要上传到服务器的数据

    private LayoutInflater mInflater;                                                                                                                                            // 加载

    private int width;// 宽度

    private int height;// 高度

    private Random mRandom = new Random();// 随机数

    private List<Float> xCanChooseList = Arrays.asList(
            0.03f, 0.25f, 0.50f, 0.78f);

    private List<Float> yCanChooseList = Arrays.asList(
            0.45f, 0.53f, 0.42f, 0.33f);

    private List<Float> xCurrentList = new ArrayList<>(xCanChooseList);

    private List<Float> yCurrentList = new ArrayList<>(yCanChooseList);

    private final int RANGE_OF_MOTION = 20;// 初始运动范围

    private final int MSG_WHART = 0;// 发送消息的标志

    private int childViewWidth, childViewHeight;

    private final int DURATION_APPEARANCE = 800;// 显示

    private final int DURATION_DISAPPEARANCE = 800;// 显示

    private final int MAX_COUNT = 10;// 一个页面最多能显示多少个

    private int totalWaterBeanSize;// 全部小球集合长度

    private boolean needShowDefaultDynamic = true;// 是否需要显示默认弹力球

    private boolean isNewUserDefault;// 新注册用户，默认弹力球

    private int viewDisappearX, viewDisAppearY;// 小球动画消失的x,y轴

    private List<Float> randomSpeed = Arrays.asList(0.15f, 0.3f, 0.55f, 0.7f, 0.65f);

    private int everyTimeMaxListSize = 10;// 每次最大显示多少

    private float lastViewLocationX, lastViewLocationY;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            setAllViewTranslation();
            handler.sendEmptyMessageDelayed(MSG_WHART, 12);
        }
    };

    /***
     * 以Y轴为例，设置view的初始Y坐标为20,方向是y轴，当前运动到了30,辣么 view.setTranslationY();
     */
    private void setAllViewTranslation() {
        int size = viewList.size();
        for (int i = 0; i < size; i++) {
            View view = viewList.get(i);
            // 拿到上次view保存的速度
            float spd = (float) view.getTag(R.string.string_origin_spe);
            // 水滴初始的位置
            float original = (float) view.getTag(R.string.string_origin_location);
            float step = spd;
            boolean isUp = (boolean) view.getTag(R.string.string_origin_direction);
            float translationY;
            // 根据水滴tag中的上下移动标识移动view
            if (isUp) {
                translationY = view.getY() - step;
            } else {
                translationY = view.getY() + step;
            }
            // 对水滴位移范围的控制
            if (translationY - original > RANGE_OF_MOTION) {
                translationY = original + RANGE_OF_MOTION;
                view.setTag(R.string.string_origin_direction, true);
            } else if (translationY - original < -RANGE_OF_MOTION) {
                translationY = original - RANGE_OF_MOTION;
                // FIXME:每次当水滴回到初始点时再一次设置水滴的速度，从而达到时而快时而慢
                // view.setTag(R.string.string_origin_spe, spd);
                view.setTag(R.string.string_origin_spe, randomSpeed.get(mRandom.nextInt(randomSpeed.size())));
                view.setTag(R.string.string_origin_direction, false);
            }
            view.setY(translationY);
        }
    }

    public AntForestView(@NonNull Context context) {
        this(context, null);
    }

    public AntForestView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AntForestView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<WaterBallBean> list) {
        if (list == null) {
            return;
        }
        beanList.clear();
        viewList.clear();
        totalWaterBeanSize = list.size();
        cuttingList(list);

        postRunnable();
    }

    private void postRunnable() {
        post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < beanList.size(); i++) {
                    WaterBallBean bean = beanList.get(i);
                    addViewList(bean, i);
                }
                handler.sendEmptyMessageDelayed(MSG_WHART, DURATION_APPEARANCE);
            }
        });
    }

    private void cuttingList(List<WaterBallBean> list) {
        if (list.size() > everyTimeMaxListSize) {
            beanList = list.subList(0, everyTimeMaxListSize);
            for (int i = everyTimeMaxListSize; i < list.size(); i++) {
                surplusBeanList.add(list.get(i));
            }
        } else {
            beanList = list;
        }
    }

    View view1,view2,view3;
    WaterBallBean bean1,bean2,bean3;

    public void addViewList(WaterBallBean bean, int i) {
        View view = mInflater.inflate(R.layout.layout_water_ball, this, false);
        if ( i == 1){
            view1 = view;
            bean1 = bean;
        }else  if ( i == 2){
            view2 = view;
            bean2 = bean;
        }else  if ( i == 3){
            view3 = view;
            bean3 = bean;
        }
        findView(bean, view, i);

        if (bean.isDefault) {
            setDefaultViewLocation(view);
        } else {
            setOnClickBallView(view, bean, i);
            if (i == -1) {
                setViewLocation(view);
            } else {
                setViewLocation(view, i);
            }
        }
        addView(view);
        setViewAnimation(view);
        viewList.add(view);
    }

    private TextView id_tv_waterball;
    private ImageView image_head;

    private void findView(WaterBallBean bean, View view, int pos) {

        id_tv_waterball = view.findViewById(R.id.id_tv_waterball);
        image_head = view.findViewById(R.id.image_head);
        ImageView id_iv_waterball = view.findViewById(R.id.id_iv_waterball);

        //将时间转换为时间戳
        long creatTime = DateUtils.getSimpleDate(bean.getCreateTime());
        long thisTime = DateUtils.getSimpleDateFormat();
        if (creatTime + 3600000 > thisTime) {
            id_tv_waterball.setText(getResources().getString(R.string.growing_up));
            id_tv_waterball.setTextColor(getResources().getColor(R.color.jin_trea));
            id_tv_waterball.setBackground(getResources().getDrawable(R.mipmap.trea_yellow_back));
            image_head.setVisibility(GONE);
        } else {
            id_tv_waterball.setBackground(getResources().getDrawable(R.mipmap.trea_blue_back));
            id_tv_waterball.setText(bean.getAmount() + getResources().getString(R.string.power));
            if (pos == 0 && ballOne == 0) {
                image_head.setVisibility(VISIBLE);
            }else if (pos == 1 && ballTwo == 0 && ballOne == 1) {
                image_head.setVisibility(VISIBLE);
            }else if (pos == 2 && ballThree == 0 & ballTwo == 1 && ballOne == 1) {
                image_head.setVisibility(VISIBLE);
            }else if (pos == 3 && ballFour == 0 & ballThree == 1 & ballTwo == 1 && ballOne == 1) {
                image_head.setVisibility(VISIBLE);
            }
        }
        id_iv_waterball.setImageResource(R.mipmap.power);
        if (bean.isDefault) id_tv_waterball.setText(bean.getAmount() + "");
    }

    int ballOne = 0;
    int ballTwo = 0;
    int ballThree = 0;
    int ballFour = 0;

    private void setOnClickBallView(final View view, final WaterBallBean bean, final int position) {

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                long creatTime = DateUtils.getSimpleDate(bean.getCreateTime());
                long thisTime = DateUtils.getSimpleDateFormat();
                if (creatTime + 3600000 > thisTime) {
                    long afterTime = (creatTime + 3600000) - thisTime;
                    String after = DateUtils.getSFMtime(afterTime);
                    ToastUtils.show(after + "后产生算力", Toast.LENGTH_SHORT);
                    image_head.setVisibility(GONE);
                    return;
                } else {
                    view.setEnabled(true);
                }
                if (position == 0){
                    ballOne = 1;
                    if (ballTwo == 0){
                        findView(bean1, view1, 1);
                    }else if (ballThree == 0){
                        findView(bean2, view2, 2);
                    }else if (ballFour == 0){
                        findView(bean3, view3, 3);
                    }
                }else  if (position == 1){
                    ballTwo = 1;
                    if (ballThree == 0){
                        findView(bean2, view2, 2);
                    }else if (ballFour == 0){
                        findView(bean3, view3, 3);
                    }
                }else  if (position == 2){
                    ballThree = 1;
                    if (ballFour == 0){
                        findView(bean3, view3, 3);
                    }
                }

                onStopAnimateListener.onBallClick();
                if (!disappearWaterBallList.contains(bean)) {
                    disappearWaterBallList.add(bean);
                }
                viewList.remove(view);
                beanList.remove(bean);
                startClickBallDisappearAnim(view, bean, position);
            }
        });
    }

    private int count;

    private void startClickBallDisappearAnim(final View view, final WaterBallBean bean, final int position) {
        lastViewLocationX = view.getX();
        lastViewLocationY = view.getY();

        view.animate().translationY(viewDisAppearY).translationX(viewDisappearX).alpha(0).setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 弹力值全部收取完成的时候，才出现默认的小球，不可重复出现默认小球
                onBallDisappearAnimEnd(view, bean, position);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        }).setDuration(DURATION_DISAPPEARANCE).start();
    }

    private void onBallDisappearAnimEnd(View view, WaterBallBean bean, int position) {
        removeView(view);
        if (onStopAnimateListener != null) {
            onStopAnimateListener.onBallDisappearAnimListener(bean.getAmount() + "", bean.getId(), beanList.size());
        }

        if (!surplusBeanList.isEmpty()) {
            addViewList(surplusBeanList.get(0), -1);
            surplusBeanList.remove(0);
        } else {
            if (needShowDefaultDynamic && totalWaterBeanSize == disappearWaterBallList.size()) {
                needShowDefaultDynamic = false;
                addViewList(getDefaultWaterBallBean(), -1);
            }
        }
    }

    private void setViewAnimation(final View view) {
        view.setAlpha(0);
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate().alpha(1).scaleX(1).scaleY(1).setDuration(DURATION_APPEARANCE).start();
    }

    private void setViewLocation(View view, int i) {
        int randomInt = 0;
        if (i >= 4) {
            randomInt = mRandom.nextInt(xCurrentList.size());
        }

        float x = xCurrentList.get(randomInt) * width;
        view.setX(x);

        float y = yCurrentList.get(randomInt) * height;
        view.setY(y);

        Log.e("TAG", "randomInt=" + randomInt + "设置的x位置是" + xCurrentList.get(randomInt) + "y轴位置是" + yCurrentList.get(randomInt));

        view.setTag(R.string.string_origin_location, (float) y);
        view.setTag(R.string.string_origin_direction, mRandom.nextBoolean());
        view.setTag(R.string.string_origin_spe, randomSpeed.get(mRandom.nextInt(randomSpeed.size())));

        xCurrentList.remove(randomInt);
        yCurrentList.remove(randomInt);
    }

    private void setViewLocation(View view) {
        view.setX(lastViewLocationX);
        view.setY(lastViewLocationY);

        view.setTag(R.string.string_origin_location, lastViewLocationY);
        view.setTag(R.string.string_origin_direction, mRandom.nextBoolean());
        view.setTag(R.string.string_origin_spe, randomSpeed.get(mRandom.nextInt(randomSpeed.size())));
    }

    /***
     * 设置默认的小球位置
     *
     * @param view
     */
    private void setDefaultViewLocation(View view) {
        id_tv_waterball.setText(getResources().getString(R.string.growing_up));
        id_tv_waterball.setTextColor(getResources().getColor(R.color.jin_trea));
        id_tv_waterball.setBackground(getResources().getDrawable(R.mipmap.trea_yellow_back));

        view.setX((width - childViewWidth) / 2);
        view.setY(height / 2);

        view.setTag(R.string.string_origin_location, (float) height / 2);
        view.setTag(R.string.string_origin_direction, mRandom.nextBoolean());
        view.setTag(R.string.string_origin_spe, 0.1f);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimate();
    }

    /**
     * 获取默认的弹力值
     *
     * @return
     */
    public WaterBallBean getDefaultWaterBallBean() {
        return new WaterBallBean(1, 2, "", true);
    }

    public void stopAnimate() {
        if (viewList == null || viewList.isEmpty()) {
            destroy();
            return;
        }

        for (int i = 0; i < viewList.size(); i++) {
            startDisappearAnim(viewList.get(i), i);
        }

    }

    private void startDisappearAnim(View childView, final int i) {
        ViewPropertyAnimator viewPropertyAnimator = childView.animate().alpha(0).scaleX(0).scaleY(0).setDuration(DURATION_APPEARANCE);
        if (i + 1 == viewList.size()) {
            viewPropertyAnimator.setListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    destroy();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        viewPropertyAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w - getPaddingLeft() - getPaddingRight();
        height = h - getPaddingTop() - getPaddingBottom();

        childViewWidth = DensityUtil.dip2px(40);
        childViewHeight = DensityUtil.dip2px(40);
    }

    public void setViewDisappearLocation(int[] location) {
        viewDisappearX = location[0];
        viewDisAppearY = location[1];
    }

    public void dest() {
        handler.removeMessages(MSG_WHART);
        if (onStopAnimateListener != null) {
            onStopAnimateListener.onExitAnimateListener();
        }
    }

    public void destroy() {
        handler.removeMessages(MSG_WHART);
        if (onStopAnimateListener != null) {
            onStopAnimateListener.onExitAnimateListener();
        }
        removeAllViews();
    }

    private OnStopAnimateListener onStopAnimateListener;

    public void setOnStopAnimateListener(OnStopAnimateListener listener) {
        this.onStopAnimateListener = listener;
    }

    public interface OnStopAnimateListener {

        void onBallDisappearAnimListener(String number, int id, int position);

        void onExitAnimateListener();

        void onBallClick();
    }
}
