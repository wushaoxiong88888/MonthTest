package com.example.mycart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by pc on 2017/10/25.
 */

public class RlvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ItemBean.DataBean.HotcourseBean> list;

    public RlvAdapter(Context context, List<ItemBean.DataBean.HotcourseBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rlv_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final MyViewHolder myViewHolder = (MyViewHolder) holder;

        ImageLoader.getInstance().displayImage(list.get(position).getImg(), myViewHolder.iv);
        myViewHolder.tv_name.setText(list.get(position).getTitle());
        myViewHolder.tv_price.setText("人民币:120.00元");
        myViewHolder.add.setOnItemClick(new MyAddDelView.OnItemClick() {
            @Override
            public void onItemAddClick(int count) {
                if (list.get(position).isChecked()) {
                    float money = list.get(position).getMoney();
                    MsgMoneyCountEvent msgMC = new MsgMoneyCountEvent();
                    msgMC.setNum(1);
                    msgMC.setMoney(money);
                    EventBus.getDefault().post(msgMC);
                } else {
                    Toast.makeText(context, "请先勾选", Toast.LENGTH_SHORT).show();
                    myViewHolder.add.setCount();
                }
            }

            @Override
            public void onItemDelClick(int count) {
                if (list.get(position).isChecked()) {
                    float money = list.get(position).getMoney();
                    MsgMoneyCountEvent msgMC = new MsgMoneyCountEvent();
                    msgMC.setNum(-1);
                    msgMC.setMoney(-money);
                    EventBus.getDefault().post(msgMC);
                } else {
                    Toast.makeText(context, "请先勾选", Toast.LENGTH_SHORT).show();
                    myViewHolder.add.setCount();
                }
            }
        });

        //条目复选框的逻辑
        myViewHolder.cb.setChecked(list.get(position).isChecked());
        myViewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //线判断是否选中,如选中则判断其他checkBox是否选中如选中,则让全选checkBox选中
                if (myViewHolder.cb.isChecked()) {
                    list.get(position).setChecked(true);

                    //把选中条目的商品价钱和数量传过去
                    MsgMoneyCountEvent msgEvent = new MsgMoneyCountEvent();
                    msgEvent.setNum(1);
                    msgEvent.setMoney(120);
                    EventBus.getDefault().post(msgEvent);

                    if (isAllChecked()) {
                        //让全选checkBox选中
                        MessageEvent msg = new MessageEvent();
                        msg.setChecked(true);
                        //发送给接收消息的
                        EventBus.getDefault().post(msg);
                    }
                } else {//点击取消选中情况

                    //把选中条目的商品价钱和数量传过去
                    MsgMoneyCountEvent msgEvent = new MsgMoneyCountEvent();
                    msgEvent.setNum(-1);
                    msgEvent.setMoney(-120);
                    EventBus.getDefault().post(msgEvent);

                    list.get(position).setChecked(false);
                    MessageEvent msg = new MessageEvent();
                    msg.setChecked(false);
                    EventBus.getDefault().post(msg);
                }
            }
        });


    }

    //让复选框跟随全选框变化  全选
    public void cbChecked(boolean flag) {
        float sum=0;
        int count= list.size();
        for (ItemBean.DataBean.HotcourseBean hotcourseBean : list) {

            MsgMoneyCountEvent msgMcEvent = new MsgMoneyCountEvent();
            msgMcEvent.setFlag(true);
            EventBus.getDefault().post(msgMcEvent);

            if(flag){
                //遍历叠加所有的钱
                sum+=hotcourseBean.getMoney();

                MsgMoneyCountEvent msgEvent = new MsgMoneyCountEvent();
                msgEvent.setNum(count);
                msgEvent.setMoney(sum);
                EventBus.getDefault().post(msgEvent);
            }

            hotcourseBean.setChecked(flag);
            notifyDataSetChanged();
        }
    }


    //判断其他复选框是否被选中
    private boolean isAllChecked() {
        for (ItemBean.DataBean.HotcourseBean hotcourseBean : list) {
            if (!hotcourseBean.isChecked()) {
                //有未选中的复选框
                return false;
            }
        }
        return true;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox cb;
        private final ImageView iv;
        private final TextView tv_name;
        private final TextView tv_price;
        private final MyAddDelView add;
        private final Button bt_del;

        public MyViewHolder(View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.rlv_cb);
            iv = itemView.findViewById(R.id.rlv_iv);
            tv_name = itemView.findViewById(R.id.rlv_tv_name);
            tv_price = itemView.findViewById(R.id.rlv_price);
            add = itemView.findViewById(R.id.rlv_add);
            bt_del = itemView.findViewById(R.id.rlv_bt_del);
        }
    }


}
