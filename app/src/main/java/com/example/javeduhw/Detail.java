package com.example.javeduhw;

import android.os.Bundle;
import android.graphics.drawable.Drawable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.util.*;
import android.text.TextUtils;
import android.content.Intent;
import androidx.core.content.ContextCompat;

public class Detail extends AppCompatActivity {

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent=getIntent();
        setContentView(R.layout.detail_page);
        //初始化各控件
        initView();
    }

    private void initView(){

        //获取数据 在values/arrays.xml中进行定义然后调用
        String[] tabTitle = getResources().getStringArray(R.array.detail_titles);
        //将fragment装进列表中
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new IntroductionFragment());
        fragmentList.add(new DetailFragment());
        fragmentList.add(new AnswerFragment());
        //声明viewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        //viewpager加载adapter
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, tabTitle));
        //viewPager事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //定义TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        //TabLayout的事件
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选中了tab的逻辑
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //未选中tab的逻辑
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //再次选中tab的逻辑
            }
        });
        //TabLayout加载viewpager
        //一行代码和ViewPager联动起来，简单粗暴。
        tabLayout.setupWithViewPager(viewPager);
        Drawable d = null;
        for (int i = 0; i < tabLayout.getTabCount(); i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            switch (i){
                case 0:
                    d = ContextCompat.getDrawable(this, R.drawable.tab1);
                    break;
                case 1:
                    d = ContextCompat.getDrawable(this, R.drawable.tab2);
                    break;
                case 2:
                    d = ContextCompat.getDrawable(this, R.drawable.tab3);
                    break;
            }
            tab.setIcon(d);
        }
    }
}

