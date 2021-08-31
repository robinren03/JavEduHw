package com.example.renyanyu;

import android.os.Bundle;
import android.graphics.drawable.Drawable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.view.View;

import android.widget.SearchView;

import java.util.*;
import android.text.TextUtils;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity /*implements SearchView.OnQueryTextListener*/{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }



    public void initView(){


        //获取数据 在values/arrays.xml中进行定义然后调用
        //Toast.makeText(MainActivity.this,"mainactivity",Toast.LENGTH_LONG).show();
        String[] tabTitle = getResources().getStringArray(R.array.tab_titles);
        //将fragment装进列表中
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new BlankFragment1());
        fragmentList.add(new BlankFragment2());
        fragmentList.add(new BlankFragment3());
        //声明viewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        //viewpager加载adapter
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, tabTitle));
        //viewPager事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Toast.makeText(MainActivity.this,"点击了1：",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(MainActivity.this,"点击了2：",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Toast.makeText(MainActivity.this,"点击了3：",Toast.LENGTH_LONG).show();
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
        try {
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                switch (i) {
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
        }catch(NullPointerException e) {

        }
    }


}
