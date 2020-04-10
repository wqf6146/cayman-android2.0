package com.spark.szhb_master.activity.Treasure;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/18 0018.
 */

public class RuleAcitivity extends BaseActivity {


    @BindView(R.id.text_one)
    TextView text_one;
    @BindView(R.id.text_two)
    TextView text_two;
    @BindView(R.id.text_three)
    TextView text_three;
    @BindView(R.id.text_four)
    TextView text_four;
    @BindView(R.id.text_five)
    TextView text_five;
    @BindView(R.id.text_six)
    TextView text_six;
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.text_top_rule)
    TextView text_top_rule;

    @BindView(R.id.text_one_suan)
    TextView text_one_suan;
    @BindView(R.id.text_two_suan)
    TextView text_two_suan;
    @BindView(R.id.text_three_suan)
    TextView text_three_suan;
    @BindView(R.id.text_four_suan)
    TextView text_four_suan;
    @BindView(R.id.text_five_suan)
    TextView text_five_suan;
    @BindView(R.id.text_title_suan)
    TextView text_title_suan;
    @BindView(R.id.text_top_rule_suan)
    TextView text_top_rule_suan;

    @BindView(R.id.text_one_call)
    TextView text_one_call;
    @BindView(R.id.text_two_call)
    TextView text_two_call;
    @BindView(R.id.text_three_call)
    TextView text_three_call;
    @BindView(R.id.text_four_call)
    TextView text_four_call;
    @BindView(R.id.text_title_call)
    TextView text_title_call;
    @BindView(R.id.text_top_rule_call)
    TextView text_top_rule_call;


    @BindView(R.id.text_top)
    TextView text_top;


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(RuleAcitivity.this, llTitle);
            isSetTitle = true;
        }
    }

    @OnClick({R.id.ivBack})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }

    private int num;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.rule_layout;
    }

    @Override
    protected void initData() {
        super.initData();
        ivBack.setVisibility(View.VISIBLE);
        tvGoto.setVisibility(View.INVISIBLE);
        ivBack.setBackground(null);

        llTitle.setBackgroundDrawable(getResources().getDrawable(R.mipmap.trea_back_head));
    }

    @Override
    protected void initView() {
        super.initView();


        tvTitle.setText("攻略");
//            text_top.setText("挖宝规则说明");
            text_title.setText("挖宝规则");
        text_top_rule.setText("参与产品的挖宝即有机会获得此产品，用户可以使用算力进行挖宝，使用1算力挖宝需要消耗2个GCX作为燃料，用户可以通过使用更多算力，增加挖宝的概率；");
        text_one.setText("1、用户可以使用算力进行挖宝，使用1算力挖宝需要消耗2个GCX作为燃料，用户可以通过使用更多算力，增加挖宝的概率；");
        text_two.setText("2、每个产品的挖宝难度值不同，需要挖宝算力的上限也不同，挖宝时参与者每使用1算力，产品的挖宝完成度增加1；");
        text_three.setText("3、当挖宝产品的完成度达到100%，挖宝结束，根据用户使用算力以及随机算法在区块链上产生成功挖宝的用户；");
        text_four.setText("4、产品挖宝成功后，参与此产品挖宝的用户使用的算力及GCX不退回；");
        text_five.setText("5、成功挖宝的用户通过表单提交收货地址等相关资料，GCX社区供应商会在三个工作日内发放产品。");
        text_six.setText("6、若挖宝时间结束，商品挖宝完成度未达到100% ，则不产生中奖用户，参与此商品挖宝的GCX退回用户，消耗的算力不予退回。");


        text_title_call.setText("打Call规则");
        text_top_rule_call.setText("打Call即为喜欢的产品拉升人气，提高热度值，热度值排名靠前的产品才有机会进入挖宝。");
        text_one_call.setText("1、用户可使用GCX为在预热中的产品打Call，每使用1个GCX为产品打Call，该产品增加10热度值，为产品打Call的GCX将被冻结，每期打Call时间结束后24H内给予解冻；");
        text_two_call.setText("2、每个账号可为每个产品使用1-500GCX打Call；");
        text_three_call.setText("3、打Call可获得算力奖励，每为一个产品打Call可获得1算力；每期最多可获得3算力；");
        text_four_call.setText("4、每期打Call结束后，热度前2名的产品进入到挖宝，其他参与打Call的产品热度清零。");


        text_title_suan.setText("算力规则");
        text_top_rule_suan.setText("算力是用户在参与挖宝时的必备要素，算力需GCX作为燃料激活才能挖宝，1算力需要2个GCX作为燃料激活。");
        text_one_suan.setText("1、基础算力：5（即每个用户都有5个基础算力）；");
        text_two_suan.setText("2、邀请算力：5算力/个，即每邀请一个新用户注册并完成实名认证，增加5算力，被邀请用户也将获得5个算力。");
        text_three_suan.setText("3、打Call算力：每为一个产品打Call，可获得1算力；");
        text_four_suan.setText("4、交易算力：根据用户最近24小时内币币交易累计交易额折合成USDT量计算浮动算力 ,每小时更新一次，每折合2USDT交易额计1浮动算力，浮动算力不累积，随用户最近24小时内币币交易累计交易额动态变化。");
        text_five_suan.setText("5、收集算力：系统每小时会生成一次随机算力，点击即可收集，若连续4小时未领取，则停止生成新的算力，收取之后，继续生成新的算力。");


    }
}
