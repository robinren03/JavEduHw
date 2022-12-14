package com.example.renyanyu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SearchResult extends AppCompatActivity {
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter ;
    List<News> mNewsList = new ArrayList<>();
    List<News> tem = new ArrayList<>();
    LinearLayoutManager layoutManager;
    public SearchView search,high_search;
    private Spinner mySpinner;
    private ArrayAdapter<String> adapter,adapter2;
    private List<String> list1 = new ArrayList<String>();
    private List<String> list2 = new ArrayList<String>();
    private Intent get_intnt;
    private String result,user_name;
    public String query1;
    Map<String,String> his_ent=new HashMap<String,String>();
    private boolean[] sub;
    public String[] subj;
    private boolean zheng;
    private ServerHttpResponse serverHttpResponse = ServerHttpResponse.getServerHttpResponse();
    private Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_result);
        get_intnt=getIntent();
        //result=get_intnt.getStringExtra("result");
        query1=get_intnt.getStringExtra("query");
        high_search=findViewById(R.id.searchView1);

        bt=findViewById(R.id.fanhui);
        int magId = getResources().getIdentifier("android:id/search_mag_icon",null, null);
        ImageView magImage = (ImageView) high_search.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        sub=new boolean[9];
        zheng=true;
        subj=new String[]{"chinese","math","english","physics","chemistry","biology","politics","history","geo"};
        for(int i=0;i<9;i++){sub[i]=false;}
        mRecyclerView = findViewById(R.id.recyclerview);
        SharedPreferences userInfo= SearchResult.this.getSharedPreferences("user", 0);
        user_name = userInfo.getString("username","");
        search=(SearchView) findViewById(R.id.search2);
        list1.add("??????");
        list1.add("??????");
        list1.add("??????");
        list1.add("??????");
        list1.add("??????");
        list1.add("??????");
        list1.add("??????");
        list1.add("??????");
        list1.add("??????");
        list1.add("??????");
        list2.add("??????");
        list2.add("??????");

        mySpinner = (Spinner) findViewById(R.id.spinner1);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list1);//??????????????????????????????android.R.layout.simple_spinner_item??????????????????????????????list?????????
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list2);
        //2.???????????????????????????????????????adapter.setDropDownViewResource
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.???????????????????????????????????????,?????????sipnner??????????????????????????????myspinner
        mySpinner.setAdapter(adapter);
        //4.?????????????????????????????????????????????????????????????????????item??????????????????setOnItemSelectedListener
        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//??????item???????????????????????????
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String choose=adapter.getItem(arg2);
               // Toast.makeText(SearchResult.this, "" + adapter.getItem(arg2), Toast.LENGTH_SHORT).show();
                classify(choose);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        // ??????????????????
        //initlist();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewsList=new ArrayList<News>(tem);
                tem.clear();
                DividerItemDecoration mDivider = new
                        DividerItemDecoration(SearchResult.this,DividerItemDecoration.VERTICAL);
                mRecyclerView.addItemDecoration(mDivider);
                mMyAdapter = new MyAdapter();
                mRecyclerView.setAdapter(mMyAdapter);
                layoutManager = new LinearLayoutManager(SearchResult.this);
                mRecyclerView.setLayoutManager(layoutManager);
            }
        });

        RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        search.setIconifiedByDefault(false);
        //??????????????????
        search.setSubmitButtonEnabled(true);
        search.setQueryHint(query1);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //???????????????????????????
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, "?????????????????????" + query, Toast.LENGTH_SHORT).show();

                try{
                    query1=query;
                    String url = SearchResult.this.getString(R.string.backend_ip) + "/request/search";

                    //Toast.makeText(SearchResult.this, "?????????"+res, Toast.LENGTH_SHORT).show();
                    //JSONObject answer_json = new JSONObject(res);
                    //JSONObject data = ((JSONArray) answer_json.get("data")).getJSONObject(0);
                    //String answer=data.get("uri").toString();
                    //System.out.println("????????????"+answer);
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
                }catch (Exception e){}
                return true;
            }
            //?????????????????????
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                }
                else {
                }
                return true;
            }
        });
        high_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //???????????????????????????
            @Override
            public boolean onQueryTextSubmit(String query) {
                try{
                    if(mNewsList.size()==0){
                        Toast.makeText(SearchResult.this, "?????????????????????????????????\n???????????????????????????" + query, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(tem.size()==0)
                            tem= new ArrayList<News>(mNewsList);
                        mNewsList.clear();
                        for(int i=0;i<tem.size();i++){
                            System.out.println("i "+i+" "+tem.get(i).title);
                            if(tem.get(i).title.contains(query)){
                                mNewsList.add(tem.get(i));
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
                }catch (Exception e){

                }

                return true;
            }
            //?????????????????????
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
                refreshlayout.finishRefresh(1000);//??????false??????????????????

                mMyAdapter.notifyDataSetChanged();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(1000);//??????false??????????????????

                mMyAdapter.notifyDataSetChanged();
            }
        });

    }

    public void initlist(String course){
        mNewsList.clear();
        try{
            JSONObject answer_json = new JSONObject(result);
            //System.out.println(((JSONArray) answer_json.opt("data")));
            if(((JSONArray) answer_json.opt("data")).optJSONObject(0)==null)
                Toast.makeText(SearchResult.this,"??????????????????????????????????????????",Toast.LENGTH_LONG).show();
            else
            for(int i=0;i<((JSONArray) answer_json.opt("data")).length();i++){
                JSONObject data = ((JSONArray) answer_json.opt("data")).optJSONObject(i);
                if(data==null)break;
                //System.out.println("i="+i+" "+data);
                String label=data.opt("label").toString();
                String category=data.opt("category").toString();
                String uri=data.opt("uri").toString();
                //System.out.println("uri?"+uri);
                News n=new News(label,category,uri,course);
                mNewsList.add(n);
            }
        }catch(Exception e){
            //Toast.makeText(SearchResult.this,"??????????????????????????????????????????",Toast.LENGTH_LONG).show();
        }
        DividerItemDecoration mDivider = new
                DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        layoutManager = new LinearLayoutManager(SearchResult.this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume(){
        super.onResume();
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
            StringBuffer tem=new StringBuffer(news.title);
            /*
            for(int i=0;i<news.title.length()/17;i++){
                tem.insert(17*(i+1),"\n");
            }
             */
            news.title=new String(tem);
            //System.out.println("??????????????????"+news.title);
            holder.mTitleTv.setText(news.title);
            holder.mTitleContent.setText(news.content);
            if(fileIsExists("/data/data/com.example.javeduhw/shared_prefs/"+user_name+"his_ent.xml"))
            {
                try{
                    if(getSettingNote(getApplicationContext(),user_name+"his_ent",news.uri).equals("1")){
                        holder.mTitleContent.setTextColor(Color.GRAY);
                        holder.mTitleTv.setTextColor(Color.GRAY);
                    }
                }catch (Exception e){}

            }
            try{
                holder.mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{

//
//
//                            String ur=SearchResult.this.getString(R.string.backend_ip) + "/request/card";
//                            String ms="course="+ news.course+"&uri="+news.uri;
//                            String re= serverHttpResponse.postResponse(ur,ms);
//
//
//                            String url = SearchResult.this.getString(R.string.backend_ip) + "/request/instance";
//                            String msg="?course="+news.course+"&name="+news.title;
//
//                            String res= serverHttpResponse.getResponse(url+msg);
//
                            TextView textView = (TextView) v.findViewById(R.id.search_textView);
                            textView.setTextColor(Color.GRAY);
                            Intent intent1=new Intent(SearchResult.this, Blank.class);
                            intent1.putExtra("type",news.content);
                            //intent1.putExtra("result",res);
                            //intent1.putExtra("card",re);
                            intent1.putExtra("course",news.course);
                            intent1.putExtra("uri",news.uri);
                            intent1.putExtra("entity_name",news.title);
                            startActivity(intent1);


                            //saveSettingNote(SearchResult.this, user_name+"his_ent", his_ent);


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
        if(choose.equals("??????")){course="chinese";}
        if(choose.equals("??????")){course="math";}
        if(choose.equals("??????")){course="english";}
        if(choose.equals("??????")){course="physics";}
        if(choose.equals("??????")){course="chemistry";}
        if(choose.equals("??????")){course="biology";}
        if(choose.equals("??????")){course="politics";}
        if(choose.equals("??????")){course="history";}
        if(choose.equals("??????")){course="geo";}
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
            //Toast.makeText(SearchResult.this, "?????????"+res, Toast.LENGTH_SHORT).show();
            //System.out.println("????????????"+res);
            //JSONObject answer_json = new JSONObject(res);
            //JSONObject data = ((JSONArray) answer_json.get("data")).getJSONObject(0);
            //String answer=data.get("uri").toString();
            //System.out.println("????????????"+answer);
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



    public static void saveSettingNote(Context context, String filename , Map<String, String> map) {
        SharedPreferences.Editor note = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            note.putString(entry.getKey(), entry.getValue());
            note.commit();
        }

    }
    public static String getSettingNote(Context context,String filename ,String dataname) {
        SharedPreferences read = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return read.getString(dataname,null);
    }
    //????????????????????????
    public boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }
}
