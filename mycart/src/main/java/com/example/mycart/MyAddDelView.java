package com.example.mycart;

/**
 * Created by pc on 2017/10/25.
 */

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by yz on 2017/07/20.
 */


public class MyAddDelView extends LinearLayout {


    private TextView num;
    private int count = 1;


    private OnItemClick onItemClick;


    public interface OnItemClick {
        public void onItemAddClick(int count);


        public void onItemDelClick(int count);
    }


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    public MyAddDelView(Context context) {
        this(context, null);
    }


    public MyAddDelView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.add_jian_item, this);
        final TextView add = findViewById(R.id.add);
        TextView del = findViewById(R.id.del);
        num = findViewById(R.id.num);

        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                num.setText(++count + "");
                onItemClick.onItemAddClick(1);
            }
        });
        del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count != 1) {
                    count = --count;
                    onItemClick.onItemDelClick(-1);
                }


                num.setText(count >= 1 ? count + "" : 1 + "");


            }
        });


    }


    /**
     * 获取商品数量
     *
     * @return
     */
    public int getCount() {
        return count;
    }


    public void setCount() {
        count = 1;
        num.setText(count + "");
    }


}