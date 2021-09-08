package com.example.renyanyu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import com.example.javeduhw.databinding.FragmentFirstBinding;

public class BlankFragment2 extends Fragment {
    Button Chinese,math,English,physics,Chemistry,biology,politics,history,geography,sort;
    private Button channel_change;
    private SearchView search;
    RecyclerView mRecyclerView;
    MyAdapter1 mMyAdapter ;
    ImageView img;
    TextView hot;
    ArrayList<News> mNewsList = new ArrayList<News>();
    LinearLayoutManager layoutManager;
    LinearLayout mylinear;
    String user_name;
    int begin_num;
    private Thread thread;
    public int[] sub;
    String subject;
    boolean beg;
    News hot_news;
    private ServerHttpResponse serverHttpResponse = ServerHttpResponse.getServerHttpResponse();
    public BlankFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank_fragment2, container, false);
        mylinear = (LinearLayout) view.findViewById(R.id.Mylinear1);
        Chinese=(Button)view.findViewById(R.id.Chinese1);
        math=(Button)view.findViewById(R.id.math1);
        English=(Button)view.findViewById(R.id.English1);
        physics=(Button)view.findViewById(R.id.physics1);
        Chemistry=(Button)view.findViewById(R.id.Chemistry1);
        biology=(Button)view.findViewById(R.id.biology1);
        politics=(Button)view.findViewById(R.id.politics1);
        history=(Button)view.findViewById(R.id.history1);
        geography=(Button)view.findViewById(R.id.geography1);
        channel_change=(Button)view.findViewById(R.id.channel1);
        search=(SearchView) view.findViewById(R.id.search1);
        sort=(Button)view.findViewById(R.id.channel2);
        SharedPreferences userInfo= getActivity().getSharedPreferences("user", 0);
        user_name = userInfo.getString("username","");
        img=view.findViewById(R.id.hot_img);
        hot=view.findViewById(R.id.hot);
        subject="";
        beg=true;
        sub=new int[9];
        for(int i=0;i<9;i++)sub[i]=0;
        search.setIconifiedByDefault(true);
        //显示搜索按钮
        search.setSubmitButtonEnabled(true);
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                initlist(begin_num,0);
            }
        });
        channel_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),Channel.class);
                startActivityForResult(intent,0);
            }
        });
        Chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                initlist(0,0);
                channel_view();
                sethot();
            }
        });
        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                initlist(1,0);
                channel_view();
                sethot();
            }
        });
        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                initlist(2,0);
                channel_view();
                sethot();
            }
        });
        physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                initlist(3,0);
                channel_view();
                sethot();
            }
        });
        Chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                initlist(4,0);
                channel_view();
                sethot();
            }
        });
        biology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                initlist(5,0);
                channel_view();
                sethot();
            }
        });
        politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                initlist(6,0);
                channel_view();
                sethot();
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                initlist(7,0);
                channel_view();
                sethot();
            }
        });
        geography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                initlist(8,0);
                channel_view();
                sethot();
            }
        });

        begin_num=-1;
        initview();
        mRecyclerView = view.findViewById(R.id.recyclerview);
        // 构造一些数据
        System.out.println("begin:"+begin_num);
        //thread.start();
        thread.run();
        channel_view();
        sethot();

        RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000);//传入false表示刷新失败
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(1000);//传入false表示加载失败
                beg=false;
                int page=-1,subje=-1;
                for(int i=0;i<9;i++){
                    if(sub[i]!=0){
                        subje=i;
                        page=sub[i];
                        break;
                    }
                }
                initlist(subje,page);
                channel_view();
                /*
                for(int i=0;i<9;i++) {
                    if(sub[i]>0){
                        initlist(i,sub[i]);
                    }
                }*/
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //单击搜索按钮的监听
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, "您输入的文本为" + query, Toast.LENGTH_SHORT).show();
                try{
                    String url = getActivity().getString(R.string.backend_ip) + "/request/search";
                    String msg="?course=chinese&searchKey="+query;
                    url=url+msg;
                    String res= serverHttpResponse.getResponse(url);
                    //Toast.makeText(getActivity(), "结果为"+res, Toast.LENGTH_SHORT).show();

                    JSONObject answer_json = new JSONObject(res);
                    JSONObject data = ((JSONArray) answer_json.opt("data")).optJSONObject(0);

                    Intent intent1=new Intent(getActivity(), SearchResult.class);
                    intent1.putExtra("result",res);
                    intent1.putExtra("query",query);
                    startActivity(intent1);
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

        hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(getActivity(), EntityDetails.class);
                //intent1.putExtra("result",res);
                //intent1.putExtra("card",re);
                intent1.putExtra("course",hot_news.course);
                intent1.putExtra("uri",hot_news.uri);
                intent1.putExtra("entity_name",hot_news.title);
                intent1.putExtra("type",hot_news.content);
                startActivity(intent1);
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(getActivity(), EntityDetails.class);
                //intent1.putExtra("result",res);
                //intent1.putExtra("card",re);
                intent1.putExtra("course",hot_news.course);
                intent1.putExtra("uri",hot_news.uri);
                intent1.putExtra("entity_name",hot_news.title);
                intent1.putExtra("type",hot_news.content);
                startActivity(intent1);
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t=new Intent(getActivity(),SearchPage.class);
                t.putExtra("subject",subject);
                startActivity(t);
            }
        });
        return view;
    }
    public void initview(){
        if(!fileIsExists("/data/data/com.example.javeduhw/shared_prefs/"+user_name+"subinfo.xml")){begin_num=0;return;}
        //if(!fileIsExists("/data/data/com.example.javeduhw/shared_prefs/subinfo.xml"))return;
        try{
            String zero="0";
            for(int i=0;i<9;i++){
                Button bt=(Button)mylinear.getChildAt(i);
                String tx=bt.getText().toString();
                //if()
                if(!getSettingNote(this.getActivity(),user_name+"subinfo",tx).equals(zero)){
                    System.out.println(tx+" "+begin_num);
                    if(tx.equals("语文")){subject=subject+"0";if(begin_num==-1)begin_num=0;}
                    if(tx.equals("数学")){subject=subject+"1";if(begin_num==-1)begin_num=0;}
                    if(tx.equals("英语")){subject=subject+"2";if(begin_num==-1)begin_num=0;}
                    if(tx.equals("物理")){subject=subject+"3";if(begin_num==-1)begin_num=0;}
                    if(tx.equals("化学")){subject=subject+"4";if(begin_num==-1)begin_num=0;}
                    if(tx.equals("生物")){subject=subject+"5";if(begin_num==-1)begin_num=0;}
                    if(tx.equals("政治")){subject=subject+"6";if(begin_num==-1)begin_num=0;}
                    if(tx.equals("历史")){subject=subject+"7";if(begin_num==-1)begin_num=0;}
                    if(tx.equals("地理")){subject=subject+"8";if(begin_num==-1)begin_num=0;}
                    bt.setVisibility(View.VISIBLE);
                }
                else
                    bt.setVisibility(View.GONE);
            }
        }catch(NullPointerException e){}
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
    //判断文件是否存在
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1&&requestCode==0){
            //Toast.makeText(getContext(), "!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getActivity(),MainActivity.class);
            startActivity(i);
        }
    }

    class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.MyViewHoder> {

        List<News> list;
        private Context context;
        public MyAdapter1(Context c, ArrayList<News> l){
            context=c;
            list=l;
        }
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(context, R.layout.item_list, null);
            return new MyViewHoder(view);
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
//                            String ur=getActivity().getString(R.string.backend_ip) + "/request/card";
//                            String ms="course="+ news.course+"&uri="+news.uri;
//                            String re= serverHttpResponse.postResponse(ur,ms);
//
//
//                            String url = getActivity().getString(R.string.backend_ip) + "/request/instance";
//                            String msg="?course="+news.course+"&name="+news.title;
//
//                            String res= serverHttpResponse.getResponse(url+msg);
//
                            Intent intent1=new Intent(getActivity(), EntityDetails.class);
                            //intent1.putExtra("result",res);
                            //intent1.putExtra("card",re);
                            intent1.putExtra("course",news.course);
                            intent1.putExtra("uri",news.uri);
                            intent1.putExtra("entity_name",news.title);
                            intent1.putExtra("type",news.content);
                            startActivity(intent1);
                        }catch (Exception e){

                        }
                    }
                });
            }catch(NullPointerException e){
                //.makeText(getActivity(),"111111111111111",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
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

    public void initlist(int a,int b){
        for(int i=0;i<9;i++)sub[i]=0;
        sub[a]=b;
        String course="";
        sub[a]++;
        if(a==0){course="chinese";}
        if(a==1){course="math";}
        if(a==2){course="english";}
        if(a==3){course="physics";}
        if(a==4){course="chemistry";}
        if(a==5){course="biology";}
        if(a==6){course="politics";}
        if(a==7){course="history";}
        if(a==8){course="geo";}
        if(b==0)
            mNewsList.clear();
        try{
            String url = getActivity().getString(R.string.backend_ip) + "/request/list";
            String msg="?course="+course+"&page="+b;
            String res= serverHttpResponse.getResponse(url+msg);

            JSONArray ddd=new JSONArray(res);
            //System.out.println(answer_json);
            //System.out.println(("!!!!!!!!!!:"+(JSONObject) answer_json.opt("data")));
            //System.out.println(ddd.length());
            JSONObject one=ddd.optJSONObject(0);
            hot_news=new News(ddd.optJSONObject(0).opt("entity_name").toString(),ddd.optJSONObject(0).opt("entity_type").toString(),ddd.optJSONObject(0).opt("entity_url").toString(),course);
            for(int i=1;i<ddd.length();i++){
                JSONObject data2=ddd.optJSONObject(i);
                //System.out.println("i="+i+" "+data2);
                String uri=data2.opt("entity_url").toString();
                String type=data2.opt("entity_type").toString();
                String name=data2.opt("entity_name").toString();
                //System.out.println("结果"+i+"= "+uri+" "+type+" "+name+" "+course);
                News n=new News(name,type,uri,course);
                mNewsList.add(n);
            }
        }catch(Exception e){}
        //System.out.println("@@@@@@@@@@@@@@@@");
    }

    public void channel_view(){
        //System.out.println("enter!!!!!!!!!!!!!!");
        DividerItemDecoration mDivider = new
                DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        mMyAdapter = new MyAdapter1(getActivity(),mNewsList);
        mRecyclerView.setAdapter(mMyAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    void sethot(){
        try{
            hot.setText("  名称："+hot_news.title+"\n\n  类型："+hot_news.content);
            hot.setTextColor(Color.rgb(255, 0, 0));
            hot.setVisibility(View.VISIBLE);
            img.setVisibility(View.VISIBLE);
        }catch (Exception e){}

    }
}
