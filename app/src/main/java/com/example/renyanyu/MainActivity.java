package com.example.renyanyu;

import android.os.Bundle;
import android.graphics.drawable.Drawable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.*;
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


public class MainActivity extends AppCompatActivity {

    private FragmentManager fmanager;
    private FragmentTransaction ftransaction;
    //private Fragment f1,f2,f3;
    public MyFragmentPagerAdapter mf;
    private static GlobalParms globalParms;
    TabLayout tabLayout;
    public int ss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalParms.setFragmentSelected(new ChangeFragment() {
            @Override
            public void changge(int position) {
                //调用BottomNavigationBar的setlecTab方法来改变Tab
                tabLayout.selectTab(tabLayout.getTabAt(1));
            }
        });
        //GlobalParms.f1=new BlankFragment1();
        //GlobalParms.f2=new BlankFragment2();
        //GlobalParms.f3=new BlankFragment3();
        initView();

    }




    public void initView(){


        //获取数据 在values/arrays.xml中进行定义然后调用
        //Toast.makeText(MainActivity.this,"mainactivity",Toast.LENGTH_LONG).show();
        String[] tabTitle = getResources().getStringArray(R.array.tab_titles);
        //将fragment装进列表中
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(GlobalParms.getHomeFragment());
        fragmentList.add(GlobalParms.getChartsFragment());
        fragmentList.add(GlobalParms.getZiXunFragment());
        //声明viewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        //viewpager加载adapter
        mf=new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, tabTitle);
        viewPager.setAdapter(mf);

        viewPager.setOffscreenPageLimit(3);

        //viewPager事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Toast.makeText(MainActivity.this,"点击了1：",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(MainActivity.this,"点击了："+position,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Toast.makeText(MainActivity.this,"点击了3：",Toast.LENGTH_LONG).show();
            }
        });

        //定义TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tab);
        //TabLayout的事件
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //if(tab==)
                //Toast.makeText(MainActivity.this,tab.getText(),Toast.LENGTH_SHORT).show();
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
