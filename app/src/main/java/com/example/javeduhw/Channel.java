package com.example.javeduhw;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends AppCompatActivity{
    private DragGridlayout mSelectedChannel;
    private DragGridlayout mUnSelectedChannel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_change);
        initView();
        initData();
        initEvent();
    }
    private void initView(){
        //setContentView(R.layout.channel_change);
        mSelectedChannel = (DragGridlayout) findViewById(R.id.selectedChannel);
        mUnSelectedChannel = (DragGridlayout) findViewById(R.id.unSelectedChannel);
        mSelectedChannel.setAllowDrag(true);
        mUnSelectedChannel.setAllowDrag(true);
    }
    private void initData() {
        List<String> selectedChannel = new ArrayList<>();
        selectedChannel.add("数学");
        selectedChannel.add("语文");
        selectedChannel.add("英语");
        selectedChannel.add("体育");
        mSelectedChannel.setItems(selectedChannel);

        List<String> unSelectedChannel = new ArrayList<>();
        unSelectedChannel.add("物理");
        unSelectedChannel.add("化学");
        unSelectedChannel.add("生物");
        unSelectedChannel.add("地理");
        unSelectedChannel.add("历史");
        unSelectedChannel.add("政治");
        unSelectedChannel.add("美术");
        unSelectedChannel.add("音乐");
        mUnSelectedChannel.setItems(unSelectedChannel);
    }

    public void initEvent(){
        //设置条目点击监听
        mSelectedChannel.setOnDragItemClickListener(new DragGridlayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                //移除点击的条目，把条目添加到下面的Gridlayout
                mSelectedChannel.removeView(tv);//移除是需要时间,不能直接添加
                mUnSelectedChannel.addItem(tv.getText().toString(),0);
            }
        });

        mUnSelectedChannel.setOnDragItemClickListener(new DragGridlayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                //移除点击的条目，把条目添加到上面的Gridlayout
                mUnSelectedChannel.removeView(tv);//移除是需要时间,不能直接添加
                mSelectedChannel.addItem(tv.getText().toString());
            }
        });
    }

    private int index = 0;

    public void addItem(View view) {
        mSelectedChannel.addItem("频道" + index++,0);
    }
}
