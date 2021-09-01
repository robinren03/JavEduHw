package com.example.renyanyu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


public class SearchResult extends AppCompatActivity {
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter ;
    List<News> mNewsList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    public SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_result);
        mRecyclerView = findViewById(R.id.recyclerview);
        search=(SearchView) findViewById(R.id.search2);
        // 构造一些数据
        for (int i = 0; i < 10; i++) {
            News news = new News("标题xxx" + i,"内容xxx" + i);
            mNewsList.add(news);
        }
        DividerItemDecoration mDivider = new
                DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        layoutManager = new LinearLayoutManager(SearchResult.this);
        mRecyclerView.setLayoutManager(layoutManager);
        RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        search.setIconifiedByDefault(false);
        //显示搜索按钮
        search.setSubmitButtonEnabled(true);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //单击搜索按钮的监听
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, "您输入的文本为" + query, Toast.LENGTH_SHORT).show();

                Intent intent1=new Intent(SearchResult.this, SearchResult.class);
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
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000);//传入false表示刷新失败
                mNewsList.clear();
                for (int i = 0; i < 10; i++) {
                    News news = new News("标题 新内容" + i,"内容" + i);
                    mNewsList.add(news);
                }
                mMyAdapter.notifyDataSetChanged();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(1000);//传入false表示加载失败
                for (int i = 0; i < 10; i++) {
                    News news = new News("标题 新内容" + i,"内容" + i);
                    mNewsList.add(news);
                }
                mMyAdapter.notifyDataSetChanged();
            }
        });

    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {

        List<News> list;

        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(SearchResult.this, R.layout.item_list, null);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, final int position) {

            News news = mNewsList.get(position);
            holder.mTitleTv.setText(news.title);
            holder.mTitleContent.setText(news.content);
            try{
                holder.mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            Intent intent=new Intent(SearchResult.this,Detail.class);
                            startActivity(intent);
                    }
                });
            }catch(NullPointerException e){
                Toast.makeText(SearchResult.this,"111111111111111",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }

    class MyViewHoder extends RecyclerView.ViewHolder {
        TextView mTitleTv;
        TextView mTitleContent;
        ConstraintLayout mRootView;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.search_textView);
            mTitleContent = itemView.findViewById(R.id.search_textView2);
            mRootView = itemView.findViewById(R.id.rootview);
        }
    }
}
