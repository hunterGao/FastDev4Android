package com.huntergao.fastdev4android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huntergao.fastdev4android.widget.DynamicGridLayout;
import com.huntergao.fastdev4android.widget.MyGridLayout;
import com.huntergao.fastdev4android.widget.SecondClock;


public class MainActivity extends Activity {

    MyGridLayout grid;
    int[] srcs = { R.drawable.actions_booktag, R.drawable.actions_comment,
            R.drawable.actions_order, R.drawable.actions_account,
            R.drawable.actions_cent, R.drawable.actions_weibo,
            R.drawable.actions_feedback, R.drawable.actions_about };
    String titles[] = { "书签", "推荐", "订阅", "账户", "积分", "微博", "反馈", "关于我们" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grid = (MyGridLayout) findViewById(R.id.list);
        grid.setGridAdapter(new MyGridLayout.GridAdatper() {

            @Override
            public View getView(int index) {
                View view = getLayoutInflater().inflate(R.layout.actions_item,
                        null);
                ImageView iv = (ImageView) view.findViewById(R.id.iv);
                TextView tv = (TextView) view.findViewById(R.id.tv);
                iv.setImageResource(srcs[index]);
                tv.setText(titles[index]);
                return view;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return titles.length;
            }
        });
        grid.setOnItemClickListener(new MyGridLayout.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int index) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "item="+index, 0).show();
            }
        });
    }

}

