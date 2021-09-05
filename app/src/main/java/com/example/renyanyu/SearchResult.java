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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchResult extends AppCompatActivity {
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter ;
    List<News> mNewsList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    public SearchView search;
    private Spinner mySpinner,mySpinner2;
    private ArrayAdapter<String> adapter,adapter2;
    private List<String> list1 = new ArrayList<String>();
    private List<String> list2 = new ArrayList<String>();
    private Intent get_intnt;
    private String result;
    private ServerHttpResponse serverHttpResponse = ServerHttpResponse.getServerHttpResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_result);
        get_intnt=getIntent();
        result=get_intnt.getStringExtra("result");
        mRecyclerView = findViewById(R.id.recyclerview);
        search=(SearchView) findViewById(R.id.search2);
        list1.add("所有结果");
        list1.add("语文");
        list1.add("数学");
        list1.add("英语");
        list1.add("物理");
        list1.add("化学");
        list1.add("生物");
        list1.add("政治");
        list1.add("历史");
        list1.add("地理");
        list2.add("正序");
        list2.add("倒序");

        mySpinner = (Spinner) findViewById(R.id.spinner1);
        mySpinner2 = (Spinner) findViewById(R.id.spinner2);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list1);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list2);
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于sipnner这个控件的建立。用到myspinner
        mySpinner.setAdapter(adapter);
        mySpinner2.setAdapter(adapter2);
        //4.为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener
        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String choose=adapter.getItem(arg2);
                Toast.makeText(SearchResult.this, "" + adapter.getItem(arg2), Toast.LENGTH_SHORT).show();
                if(choose.equals("全部结果")){}
                if(choose.equals("语文")){}
                if(choose.equals("数学")){}
                if(choose.equals("英语")){}
                if(choose.equals("物理")){}
                if(choose.equals("化学")){}
                if(choose.equals("生物")){}
                if(choose.equals("政治")){}
                if(choose.equals("历史")){}
                if(choose.equals("地理")){}
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        mySpinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String choose=adapter2.getItem(arg2);
                if(choose.equals("正序")){}
                if(choose.equals("逆序")){}
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // 构造一些数据
        initlist();

        DividerItemDecoration mDivider = new
                DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        layoutManager = new LinearLayoutManager(SearchResult.this);
        mRecyclerView.setLayoutManager(layoutManager);
        RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        search.setIconifiedByDefault(true);
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

    public void initlist(){
        mNewsList.clear();
        try{
            JSONObject answer_json = new JSONObject(result);
            for(int i=0;i<((JSONArray) answer_json.get("data")).length();i++){
                JSONObject data = ((JSONArray) answer_json.get("data")).getJSONObject(i);
                String label=data.get("label").toString();
                String category=data.get("category").toString();
                News n=new News(label,category);
                mNewsList.add(n);
            }
        }catch(Exception e){
            Toast.makeText(SearchResult.this,"对不起，未能找到相应的实体！",Toast.LENGTH_LONG).show();
        }

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
                        try{
                            String url = SearchResult.this.getString(R.string.backend_ip) + "/request/card";
                            String msg="?course=chinese&name="+news.title;
                            url=url+msg;
                            String res= serverHttpResponse.getResponse(url);
                            Toast.makeText(SearchResult.this, "结果为"+res, Toast.LENGTH_SHORT).show();
                            holder.mTitleContent.setText(res);
                            System.out.println("结果为："+res);
                            //JSONObject answer_json = new JSONObject(res);
                            //JSONObject data = ((JSONArray) answer_json.get("data")).getJSONObject(0);
                            //String answer=data.get("uri").toString();
                            //System.out.println("结果为："+answer);

                            Intent intent1=new Intent(SearchResult.this, EntityDetails.class);
                            intent1.putExtra("content",res);
                            startActivity(intent1);
                        }catch (Exception e){

                        }
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
