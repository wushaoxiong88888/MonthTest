package com.example.mycart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRlv;
    /**
     * 全选
     */
    private CheckBox mCbAll;
    /**
     * 总计金额
     */
    private TextView mTvSumPrice;
    /**
     * 合计:
     */
    private TextView mTvSum;
    private RlvAdapter adapter;
    private int count;
    private float sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initView();

        //添加数据
        OkhttpUtils.getInstance().doGet("http://www.meirixue.com/api.php?c=index&a=index", ItemBean.class, new OnNetListener() {
            @Override
            public void onSuccess(ItemBean itemBean) {
                List<ItemBean.DataBean.HotcourseBean> list = itemBean.getData().getHotcourse();
                //循环设置钱
                for (ItemBean.DataBean.HotcourseBean hotcourseBean: list ) {
                    hotcourseBean.setMoney(120f);
                }
                //设置rlv
                mRlv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                mRlv.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
                adapter = new RlvAdapter(MainActivity.this, list);
                mRlv.setAdapter(adapter);

            }
        });

    }

    private void initView() {
        mRlv = (RecyclerView) findViewById(R.id.rlv);
        mCbAll = (CheckBox) findViewById(R.id.cbAll);
        mTvSumPrice = (TextView) findViewById(R.id.tv_sumPrice);
        mTvSum = (TextView) findViewById(R.id.tv_sum);
        //给mCbAll全选设置点击监听事件
        mCbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.cbChecked(mCbAll.isChecked());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //接收MessageEvent的消息
    @Subscribe
    public void onMessageEvent(MessageEvent msg) {
        mCbAll.setChecked(msg.isChecked());
    }

    //接收MsgMoneyCountEvent的消息
    @Subscribe
    public void onMsgMoneyCountEvent(MsgMoneyCountEvent msg) {

        if (msg.isFlag()) {
            sum = 0;
            count = 0;
        }

        float money = msg.getMoney();
        int num = msg.getNum();
        sum += money;
        count += num;

        if (sum < 0 || count < 0) {
            sum = 0;
            count = 0;
        }

        //把加起来的给设置上去
        mTvSumPrice.setText("合计" + sum + "钱");
        mTvSum.setText("总计" + count + "个");
    }

}
