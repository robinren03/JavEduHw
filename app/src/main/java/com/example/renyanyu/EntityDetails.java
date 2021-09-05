package com.example.renyanyu;

//package com.example.renyanyu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;


public class EntityDetails extends AppCompatActivity {
    private Gson gson=new Gson();
    public String name;
    public boolean collected;
    public News detail;
    public String title1,title2,title3; // 标题
    public String content1,content2,content3; //内容
    public String[] related;
    public TextView text1,text2;
    List<News> mNewsList = new ArrayList<>();
    private ServerHttpResponse serverHttpResponse = ServerHttpResponse.getServerHttpResponse();
    RecyclerView mRecyclerView;
    MyAdapter1 mMyAdapter ;
    LinearLayoutManager layoutManager;
    Intent t1;
    public String result,card,course,mcontent;
    public String user_name,entity_name,kuri;
    KEntityRepository kdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_details);
        //db=new OrderDBHelper(EntityDetails.this).getWritableDatabase();
        mRecyclerView = findViewById(R.id.recyclerview1);
        t1=getIntent();
        result=t1.getStringExtra("result");
        card=t1.getStringExtra("card");
        course=t1.getStringExtra("course");
        mcontent=t1.getStringExtra("content");
        entity_name=t1.getStringExtra("entity_name");
        kuri=t1.getStringExtra("uri");
        System.out.println("uri="+kuri);
        text1=findViewById(R.id.txt);
        text2=findViewById(R.id.txt1);
        SharedPreferences userInfo= EntityDetails.this.getSharedPreferences("user", 0);
        kdb=new KEntityRepository(AppDB.getAppDB(EntityDetails.this,"test"));
        user_name = userInfo.getString("username","");

        name="name_type";
        if(result!=null){
            initstr();
            KEntity tem=new KEntity(kuri,entity_name,card,result,new Date());
            kdb.insertkEntity(tem);
        }
        else{
            try{
                KEntity tem=kdb.getKEntityByKEntityUri(kuri);
                if(tem==null){
                    Toast.makeText(EntityDetails.this, "没有可用的网络！！！", Toast.LENGTH_SHORT).show();
                    text1.setText("404");
                    text2.setVisibility(View.GONE);
                }
                else{
                    course=tem.getCourse();
                    card=tem.getProperty();
                    result=tem.getContent();
                    initstr();
                }
            }catch (Exception e){System.out.println("！！！！！！！！！！");}
        }
        /*
        if(fileIsExists(name))
        {
            getPage();
            str=new String[]{detail.title+"\n"+detail.content,detail.title1+"\n"+detail.content1,detail.title2+"\n"+detail.content2};
        }
        else{
            str=new String[]{title1+"\n"+content1,title2+"\n"+content2,title3+"\n"+content3};
            setDetail();
            savePackageData();
        }*/

        //配置ArrayAdapter适配器



        //设置选中选项监听
        Button shareButton=findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }) ;
        Button addToCollectionButton=findViewById(R.id.addToCollectionButton);
        Button hadAddedToCollectionButton=findViewById(R.id.hadAddedToCollectionButton);
        hadAddedToCollectionButton.setVisibility(View.GONE);

        addToCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //collected=true;
                //detail.collected=collected;
                //savePackageData();
                Toast.makeText(EntityDetails.this,"已收藏",Toast.LENGTH_LONG).show();
                addToCollectionButton.setVisibility(View.GONE);
                hadAddedToCollectionButton.setVisibility(View.VISIBLE);

            }
        }) ;

        hadAddedToCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //collected=false;
                //detail.collected=collected;
                //savePackageData();
                Toast.makeText(EntityDetails.this,"已取消收藏",Toast.LENGTH_LONG).show();
//                addToCollectionButton.setPointerIcon(R.id.shareButton);
                //  shareButton.setIm
                addToCollectionButton.setVisibility(View.VISIBLE);
                hadAddedToCollectionButton.setVisibility(View.GONE);
            }
        }) ;

    }


    public void initstr(){
        if(result==null)
        {
            //调用本地
            //if 本地没有
            //text1.setText(没有可用的网络！);
        }
        String ss="";
        try{
            JSONObject an1 = new JSONObject(card);
            JSONObject da1 = ((JSONObject) an1.opt("data"));
            //text1.setText(card);
            int i=0;
            while(true){
                if(((JSONArray)da1.opt("entity_features")).optJSONObject(i)==null) { break; }
                JSONObject da2=((JSONArray)da1.opt("entity_features")).optJSONObject(i);
                i++;
                String feature_key=da2.opt("feature_key").toString();
                String feature_value=da2.opt("feature_value").toString();
                String s=feature_key+": "+feature_value;
                //str.
                ss=ss+"\n"+s;
            }
            //Toast.makeText(EntityDetails.this,((JSONArray)da2.opt("entity_features")).optJSONObject(0).toString(),Toast.LENGTH_LONG).show();
        }catch (Exception e){}
        try{

                JSONObject answer_json = new JSONObject(result);

                JSONObject data1 = ((JSONObject) answer_json.opt("data"));
                //lab=data1.opt("label").toString();
                text1.setText(data1.opt("label").toString());
                int j=0;
                while(true){
                    JSONObject data2=((JSONArray)data1.opt("content")).optJSONObject(j);
                    //System.out.println("j= "+j+" "+((JSONArray)data1.opt("content")).optJSONObject(j).toString());
                    //System.out.println(j);
                    j++;
                    //System.out.println(j);
                    if(data2==null)break;
                    //System.out.println("?");
                    String predicate_label="",subject_label="",object_label="",subject="",object="";
                    predicate_label=data2.opt("predicate_label").toString();
                    if(data2.opt("subject_label")!=null)
                        subject_label=data2.opt("subject_label").toString();
                    if(data2.opt("object_label")!=null)
                        object_label=data2.opt("object_label").toString();
                    if(data2.opt("subject")!=null)
                        subject=data2.opt("subject").toString();
                    if(data2.opt("object")!=null)
                        object=data2.opt("object").toString();
                    if(subject_label.length()!=0){
                        News n=new News(predicate_label,subject_label,subject,course);
                        //System.out.println("j="+j+" "+predicate_label+" "+subject_label+" "+subject);
                        mNewsList.add(n);
                    }
                    if(object_label.length()!=0){
                        News n=new News(predicate_label,object_label,object,course);
                        //System.out.println("j="+j+" "+predicate_label+" "+object_label+" "+object);
                        mNewsList.add(n);
                    }
                }
        }catch(Exception e){
            Toast.makeText(EntityDetails.this,"对不起，未能找到相应的实体！",Toast.LENGTH_LONG).show();
        }
        if(ss.length()==0)
        try{
            JSONObject answer_json11 = new JSONObject(mcontent);

            JSONObject data11 = ((JSONObject) answer_json11.opt("data"));
            int i=0;
            while(true){
                String predicateLabel="",object="";
                if(((JSONArray)data11.opt("property")).optJSONObject(i)==null) {
                    //System.out.println("break");
                    break;
                }
                JSONObject data22=((JSONArray)data11.opt("property")).optJSONObject(i);

                i++;
                if(data22.opt("predicateLabel")!=null)
                    predicateLabel=data22.opt("predicateLabel").toString();
                if(data22.opt("object")!=null)
                    object=data22.opt("object").toString();
                //if(predicateLabel.length()!=0){
                String s=predicateLabel+": "+object;
                //str.
                ss=ss+"\n"+s;
                //}
            }
        }catch (Exception e){}


        DividerItemDecoration mDivider = new
                DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        mMyAdapter = new MyAdapter1();
        mRecyclerView.setAdapter(mMyAdapter);
        layoutManager = new LinearLayoutManager(EntityDetails.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setNestedScrollingEnabled(false);
        //System.out.println(mNewsList.toString());
        text2.setText(ss);
    }

    class MyAdapter1 extends RecyclerView.Adapter<MyViewHoder1> {

        List<News> list;

        @Override
        public MyViewHoder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(EntityDetails.this, R.layout.item_list, null);
            MyViewHoder1 myViewHoder1 = new MyViewHoder1(view);
            return myViewHoder1;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder1 holder, final int position) {

            News news = mNewsList.get(position);
            holder.mTitleTv.setText(news.title);
            holder.mTitleContent.setText(news.content);
            try{
                holder.mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            String ur=EntityDetails.this.getString(R.string.backend_ip) + "/request/card";
                            String ms="course="+ news.course+"&uri="+news.uri;
                            String re= serverHttpResponse.postResponse(ur,ms);

                            String url = EntityDetails.this.getString(R.string.backend_ip) + "/request/instance";
                            String msg="?course="+course+"&name="+news.content;
                            String res= serverHttpResponse.getResponse(url+msg);

                            Intent intent1=new Intent(EntityDetails.this, EntityDetails.class);
                            intent1.putExtra("result",res);
                            intent1.putExtra("content",res);
                            intent1.putExtra("course",news.course);
                            startActivity(intent1);
                        }catch (Exception e){

                        }
                    }
                });
            }catch(NullPointerException e){
                Toast.makeText(EntityDetails.this,"111111111111111",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }

    class MyViewHoder1 extends RecyclerView.ViewHolder {
        TextView mTitleTv;
        TextView mTitleContent;
        ConstraintLayout mRootView;

        public MyViewHoder1(@NonNull View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.search_textView);
            mTitleContent = itemView.findViewById(R.id.search_textView2);
            mRootView = itemView.findViewById(R.id.rootview);
        }
    }
}