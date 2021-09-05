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
    public String query1;
    private boolean[] sub;
    public String[] subj;
    private boolean zheng;
    private ServerHttpResponse serverHttpResponse = ServerHttpResponse.getServerHttpResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_result);
        get_intnt=getIntent();
        result=get_intnt.getStringExtra("result");
        query1=get_intnt.getStringExtra("query");
        sub=new boolean[9];
        zheng=true;
        subj=new String[]{"chinese","math","english","physics","chemistry","biology","politics","history","geo"};
        for(int i=0;i<9;i++){sub[i]=false;}
        mRecyclerView = findViewById(R.id.recyclerview);
        search=(SearchView) findViewById(R.id.search2);
        list1.add("筛选");
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
               // Toast.makeText(SearchResult.this, "" + adapter.getItem(arg2), Toast.LENGTH_SHORT).show();
                classify(choose);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        mySpinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String choose=adapter2.getItem(arg2);
                if(choose.equals("正序")){
                    if(!zheng){
                        zheng=true;
                        List<News> tem = new ArrayList<>();
                        for(int i=0;i<mNewsList.size();i++){
                            tem.add(mNewsList.get(i));
                        }
                        mNewsList.clear();
                        mNewsList=tem;
                    }
                }
                if(choose.equals("逆序")){
                    if(zheng){
                        zheng=false;
                        List<News> tem = new ArrayList<>();
                        for(int i=mNewsList.size()-1;i>0;i--){
                            tem.add(mNewsList.get(i));
                        }
                        mNewsList.clear();
                        mNewsList=tem;
                    }

                }
                DividerItemDecoration mDivider = new
                        DividerItemDecoration(SearchResult.this,DividerItemDecoration.VERTICAL);
                mRecyclerView.addItemDecoration(mDivider);
                mMyAdapter = new MyAdapter();
                mRecyclerView.setAdapter(mMyAdapter);
                layoutManager = new LinearLayoutManager(SearchResult.this);
                mRecyclerView.setLayoutManager(layoutManager);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        // 构造一些数据
        //initlist();


        RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        search.setIconifiedByDefault(false);
        //显示搜索按钮
        search.setSubmitButtonEnabled(true);
        search.setQueryHint(query1);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //单击搜索按钮的监听
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, "您输入的文本为" + query, Toast.LENGTH_SHORT).show();

                try{
                    query1=query;
                    String url = SearchResult.this.getString(R.string.backend_ip) + "/request/search";

                    //Toast.makeText(SearchResult.this, "结果为"+res, Toast.LENGTH_SHORT).show();
                    //JSONObject answer_json = new JSONObject(res);
                    //JSONObject data = ((JSONArray) answer_json.get("data")).getJSONObject(0);
                    //String answer=data.get("uri").toString();
                    //System.out.println("结果为："+answer);
                    for(int i=0;i<9;i++){
                        if(sub[i])
                        {
                            String msg="?course="+subj[i]+"&searchKey="+query;
                            url=url+msg;
                            String res= serverHttpResponse.getResponse(url);
                            result=res;
                            initlist(subj[i]);
                            break;
                        }

                    }
                    //Intent intent1=new Intent(SearchResult.this, SearchResult.class);
                    //intent1.putExtra("result",res);
                    //startActivity(intent1);
                }catch (Exception e){

                }

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

    public void initlist(String course){
        mNewsList.clear();
        try{
            JSONObject answer_json = new JSONObject(result);
            System.out.println(((JSONArray) answer_json.opt("data")));
            if(((JSONArray) answer_json.opt("data")).optJSONObject(0)==null)
                Toast.makeText(SearchResult.this,"对不起，未能找到相应的实体！",Toast.LENGTH_LONG).show();
            else
            for(int i=0;i<((JSONArray) answer_json.opt("data")).length();i++){
                JSONObject data = ((JSONArray) answer_json.opt("data")).optJSONObject(i);
                if(data==null)break;
                System.out.println("i="+i+" "+data);
                String label=data.opt("label").toString();
                String category=data.opt("category").toString();
                String uri=data.opt("uri").toString();
                News n=new News(label,category,uri,course);
                mNewsList.add(n);
            }
        }catch(Exception e){
            //Toast.makeText(SearchResult.this,"对不起，未能找到相应的实体！",Toast.LENGTH_LONG).show();
        }
        DividerItemDecoration mDivider = new
                DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        layoutManager = new LinearLayoutManager(SearchResult.this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {

       // List<News> list;

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
                            String ur=SearchResult.this.getString(R.string.backend_ip) + "/request/card";
                            String ms="course="+ news.course+"&uri="+news.uri;
                            String re= serverHttpResponse.postResponse(ur,ms);


                            String url = SearchResult.this.getString(R.string.backend_ip) + "/request/instance";
                            String msg="?course="+news.course+"&name="+news.title;

                            String res= serverHttpResponse.getResponse(url+msg);
                            //Toast.makeText(SearchResult.this,res,Toast.LENGTH_LONG).show();
                            //System.out.println("结果为1111："+res);
                            //JSONObject answer_json = new JSONObject(res);
                            //JSONObject data1 = ((JSONObject) answer_json.opt("data"));
                            //JSONObject data2=((JSONArray)data1.get("content"));
                            //holder.mTitleContent.setText(((JSONArray)data1.opt("content")).toString());

                            Intent intent1=new Intent(SearchResult.this, EntityDetails.class);
                            intent1.putExtra("result",res);
                            intent1.putExtra("card",re);
                            intent1.putExtra("course",news.course);
                            intent1.putExtra("uri",news.uri);
                            intent1.putExtra("entity_name",news.title);
                            startActivity(intent1);
                        }catch (Exception e){

                        }
                    }
                });
            }catch(NullPointerException e){
                //Toast.makeText(SearchResult.this,"111111111111111",Toast.LENGTH_LONG).show();
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

    public void classify(String choose){
        String course="";
        if(choose.equals("语文")){course="chinese";}
        if(choose.equals("数学")){course="math";}
        if(choose.equals("英语")){course="english";}
        if(choose.equals("物理")){course="physics";}
        if(choose.equals("化学")){course="chemistry";}
        if(choose.equals("生物")){course="biology";}
        if(choose.equals("政治")){course="politics";}
        if(choose.equals("历史")){course="history";}
        if(choose.equals("地理")){course="geo";}
        for(int i=0;i<9;i++){ sub[i]=false;}
        if(course.equals("chinese")){sub[0]=true;}
        if(course.equals("math")){sub[1]=true;}
        if(course.equals("english")){sub[2]=true;}
        if(course.equals("physics")){sub[3]=true;}
        if(course.equals("chemistry")){sub[4]=true;}
        if(course.equals("biology")){sub[5]=true;}
        if(course.equals("politics")){sub[6]=true;}
        if(course.equals("history")){sub[7]=true;}
        if(course.equals("geo")){sub[8]=true;}
        if(!course.equals(""))
        try{
            String url = SearchResult.this.getString(R.string.backend_ip) + "/request/search";
            String msg="?course="+course+"&searchKey="+query1;
            url=url+msg;
            String res= serverHttpResponse.getResponse(url);
            //Toast.makeText(SearchResult.this, "结果为"+res, Toast.LENGTH_SHORT).show();
            System.out.println("结果为："+res);
            //JSONObject answer_json = new JSONObject(res);
            //JSONObject data = ((JSONArray) answer_json.get("data")).getJSONObject(0);
            //String answer=data.get("uri").toString();
            //System.out.println("结果为："+answer);
            result=res;
            initlist(course);
            //Intent intent1=new Intent(SearchResult.this, SearchResult.class);
            //intent1.putExtra("result",res);
            //startActivity(intent1);
        }catch (Exception e){
        }
    }

    public void sort(boolean dao){
        if(dao){

        }
    }
}
