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

import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity /*implements SearchView.OnQueryTextListener*/{

    private SearchView search;
    private Button channel_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        channel_change=(Button)findViewById(R.id.channel);
        search = (SearchView) findViewById(R.id.search);
        search.setIconifiedByDefault(false);
        //显示搜索按钮
        search.setSubmitButtonEnabled(true);
        //初始化各控件
        initView();
        channel_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,Channel.class);
                startActivityForResult(intent,0);
            }
        });


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //单击搜索按钮的监听
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, "您输入的文本为" + query, Toast.LENGTH_SHORT).show();

                Intent intent1=new Intent(MainActivity.this, SearchResult.class);
                startActivity(intent1);

                return true;
            }
            //输入字符的监听
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){

                }
                else {

                }

                return true;
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1&&requestCode==0){
            initView();
        }
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