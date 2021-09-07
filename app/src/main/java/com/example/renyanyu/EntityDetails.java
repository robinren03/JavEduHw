package com.example.renyanyu;

//package com.example.renyanyu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
    public boolean collected;
    public News detail;
    public String ss; // 标题
    public String content1,content2,content3; //内容
    public String[] related;
    public TextView text1,text2;
    List<News> mNewsList = new ArrayList<>();
    List<Exercise> mex = new ArrayList<>();
    private ServerHttpResponse serverHttpResponse = ServerHttpResponse.getServerHttpResponse();
    RecyclerView mRecyclerView;
    MyAdapter1 mMyAdapter ;
    LinearLayoutManager layoutManager;
    Intent t1;
    public String result,card,course,mcontent;
    public String user_name,entity_name,kuri;
    KEntityRepository kdb;
    private Thread thread,thread1;
    private ViewPager mViewPager;
    Map<String,String> his_ent=new HashMap<String,String>();
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_details);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mCardAdapter = new CardPagerAdapter(EntityDetails.this);

        //db=new OrderDBHelper(EntityDetails.this).getWritableDatabase();
        mRecyclerView = findViewById(R.id.recyclerview1);
        t1=getIntent();
        //result=t1.getStringExtra("result");
        //card=t1.getStringExtra("card");
        course=t1.getStringExtra("course");
        mcontent=t1.getStringExtra("content");
        entity_name=t1.getStringExtra("entity_name");
        kuri=t1.getStringExtra("uri");
        getinfo();
        //System.out.println("uri="+kuri);
        text1=findViewById(R.id.txt);
        text2=findViewById(R.id.txt1);
        SharedPreferences userInfo= EntityDetails.this.getSharedPreferences("user", 0);
        user_name = userInfo.getString("username","");

        kdb=new KEntityRepository(AppDB.getAppDB(EntityDetails.this,user_name));




        // region 收藏和历史记录
        Button shareButton=findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }) ;
        Button addToCollectionButton=findViewById(R.id.addToCollectionButton);
        Button hadAddedToCollectionButton=findViewById(R.id.hadAddedToCollectionButton);
        hadAddedToCollectionButton.setVisibility(View.GONE);
        String severIP=EntityDetails.this.getString(R.string.backend_ip);
        String userToken = userInfo.getString("token","");
        String type=t1.getStringExtra("type");
        String haveStarredUrl =  severIP+ "/request/haveStarred";
        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
        String message="token="+userToken+"&name="+entity_name+"&type="+type+"&uri="+kuri;
        System.out.println(message);
        String responseString = serverHttpResponse.postResponse(haveStarredUrl,message);
        System.out.println(responseString);
        if(responseString.equals("true"))
        {
            addToCollectionButton.setVisibility(View.GONE);
            hadAddedToCollectionButton.setVisibility(View.VISIBLE);
        }
        else if(responseString.equals("false"))
        {
            addToCollectionButton.setVisibility(View.VISIBLE);
            hadAddedToCollectionButton.setVisibility(View.GONE);
        }
        else
        {
            Toast.makeText(EntityDetails.this,"www,好像断网了，请检查您的网络设置",Toast.LENGTH_LONG).show();
        }

        String addToHistoryUrl =  severIP+ "/request/addToHistory";
        message="token="+userToken+"&name="+entity_name+"&type="+type+"&uri="+kuri;
        System.out.println(message);
        responseString = serverHttpResponse.postResponse(addToHistoryUrl,message);
        System.out.println(responseString);

        // endregion




        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                initstr();
            }
        });
        thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                setqa();
            }
        });
        if(kuri!=null){
            thread.run();
            setview(ss);
            thread1.run();
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
                    thread.run();
                    setview(ss);
                }
            }catch (Exception e){}
        }
        for(int i=0;i<mex.size();i++)
        {
            //System.out.println("i="+i+" "+mex.get(i).stem);
            mCardAdapter.addCardItem(mex.get(i));
        }
        if(mex.size()!=0){
            mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
            mCardShadowTransformer.enableScaling(true);

            mViewPager.setAdapter(mCardAdapter);
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
            mViewPager.setOffscreenPageLimit(3);
        }else{
            mViewPager.setVisibility(View.GONE);
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


        addToCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EntityDetails.this,"已收藏",Toast.LENGTH_LONG).show();
                addToCollectionButton.setVisibility(View.GONE);
                hadAddedToCollectionButton.setVisibility(View.VISIBLE);

                String url = severIP + "/request/star";
                ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                String message="token="+userToken+"&name="+entity_name+"&type="+type+"&uri="+kuri;
                System.out.println(message);
                String responseString = serverHttpResponse.postResponse(url,message);
                System.out.println(responseString);
            }
        }) ;

        hadAddedToCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EntityDetails.this,"已取消收藏",Toast.LENGTH_LONG).show();
                addToCollectionButton.setVisibility(View.VISIBLE);
                hadAddedToCollectionButton.setVisibility(View.GONE);

                String url = severIP + "/request/star";
                ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                String message="token="+userToken+"&name="+entity_name+"&type="+type+"&uri="+kuri;
                System.out.println(message);
                String responseString = serverHttpResponse.postResponse(url,message);
                System.out.println(responseString);
            }
        }) ;






    }


    public void initstr(){
        //System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
        ss="";
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
    }

    void setview(String ss){
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

       // List<News> list;

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
//                            String ur=EntityDetails.this.getString(R.string.backend_ip) + "/request/card";
//                            String ms="course="+ news.course+"&uri="+news.uri;
//                            String re= serverHttpResponse.postResponse(ur,ms);
//
//                            String url = EntityDetails.this.getString(R.string.backend_ip) + "/request/instance";
//                            String msg="?course="+course+"&name="+news.content;
//                            String res= serverHttpResponse.getResponse(url+msg);

                            Intent intent1=new Intent(EntityDetails.this, EntityDetails.class);
                            //intent1.putExtra("result",res);
                            //intent1.putExtra("content",res);
                            intent1.putExtra("entity_name",news.title);
                            intent1.putExtra("uri",news.uri);
                            intent1.putExtra("course",news.course);
                            intent1.putExtra("type",news.content);
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

    void setqa(){
        String url = EntityDetails.this.getString(R.string.backend_ip) + "/request/exercise";
        String msg="?uriName="+entity_name;
        //System.out.println("msg:"+msg);
        String res= serverHttpResponse.getResponse(url+msg);
        try{
            JSONObject answer_json = new JSONObject(res);
            //System.out.println(((JSONArray) answer_json.opt("data")).length());
            for(int i=0;i<((JSONArray) answer_json.opt("data")).length();i++){
                JSONObject data1 = ((JSONArray) answer_json.opt("data")).optJSONObject(i);
                String stemall=data1.opt("qBody").toString();
                String answer=data1.opt("qAnswer").toString();
                if(answer.contains("A")&&answer.length()>0)continue;
                String id=data1.opt("id").toString();
                int index_a=stemall.indexOf("A.");
                int index_b=stemall.indexOf("B.");
                int index_c=stemall.indexOf("C.");
                int index_d=stemall.indexOf("D.");
                if(index_a==-1&&index_b==-1&&index_c==-1&&index_d==-1){
                    Exercise e=new Exercise(stemall,answer,id,entity_name);
                    mex.add(e);
                }
                else{
                    String stem=stemall.substring(0,index_a);
                    String text_a=stemall.substring(index_a+2,index_b);
                    String text_b=stemall.substring(index_b+2,index_c);
                    String text_c=stemall.substring(index_c+2,index_d);
                    String text_d=stemall.substring(index_d+2);
                    Exercise e=new Exercise(stem,text_a,text_b,text_c,text_d,answer,id,entity_name);
                    mex.add(e);
                }
            }
        }catch(Exception e){}
    }

    public void getinfo(){
        String ur=EntityDetails.this.getString(R.string.backend_ip) + "/request/card";
        String ms="course="+ course+"&uri="+kuri;
        card= serverHttpResponse.postResponse(ur,ms);


        String url = EntityDetails.this.getString(R.string.backend_ip) + "/request/instance";
        String msg="?course="+course+"&name="+entity_name;

        result= serverHttpResponse.getResponse(url+msg);
    }
}