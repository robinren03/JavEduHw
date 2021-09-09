package com.example.renyanyu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.io.File;
import java.time.chrono.MinguoChronology;
import java.util.Iterator;
import java.util.*;
import java.util.Map;
import android.widget.*;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

//import com.example.javeduhw.databinding.FragmentFirstBinding;

public class BlankFragment1 extends Fragment {

    public BlankFragment1() {
        // Required empty public constructor
    }

    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    Button Chinese,math,English,physics,Chemistry,biology,politics,history,geography;
    SearchView search;
    RecyclerView mRecyclerView;
    MyAdapter1 mMyAdapter ;
    ArrayList<News> mNewsList = new ArrayList<News>();
    ArrayList<News> tem = new ArrayList<News>();
    LinearLayoutManager layoutManager;
    CardView card1,card2,card3;
    Thread thread;

    private ServerHttpResponse serverHttpResponse = ServerHttpResponse.getServerHttpResponse();
    //private ListView mLvMsgList;
    private List<Message> mDatas = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Toast.makeText(getContext(),"点击了：",Toast.LENGTH_LONG).show();

        View view = inflater.inflate(R.layout.fragment_blank_fragment1, container, false);
        Chinese=(Button)view.findViewById(R.id.Chinese2);
        math=(Button)view.findViewById(R.id.math2);
        English=(Button)view.findViewById(R.id.English2);
        physics=(Button)view.findViewById(R.id.physics2);
        Chemistry=(Button)view.findViewById(R.id.Chemistry2);
        biology=(Button)view.findViewById(R.id.biology2);
        politics=(Button)view.findViewById(R.id.politics2);
        history=(Button)view.findViewById(R.id.history2);
        geography=(Button)view.findViewById(R.id.geography2);
        search=view.findViewById(R.id.searc);
        card1=view.findViewById(R.id.card1);
        card2=view.findViewById(R.id.card2);
        card3=view.findViewById(R.id.card3);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t=new Intent(getActivity(),QA.class);
                startActivity(t);
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t=new Intent(getActivity(),EntityLink.class);
                startActivity(t);
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalParms.sChangeFragment.changge(1);
            }
        });
        mRecyclerView = view.findViewById(R.id.recyclerview2);

        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                initstr("chinese");
            }
        });
        //thread.start();
        initstr("chinese");
        channel_view();
        search.setIconifiedByDefault(true);
        //显示搜索按钮
        search.setSubmitButtonEnabled(true);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //单击搜索按钮的监听
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, "您输入的文本为" + query, Toast.LENGTH_SHORT).show();

                try{

                    Intent intent1=new Intent(getActivity(), SearchResult.class);

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
        RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout2);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000);//传入false表示刷新失败
                if(mNewsList.size()==0){
                    initstr("chinese");
                    channel_view();
                }
                Toast.makeText(getContext(),"刷新成功！",Toast.LENGTH_LONG).show();

            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(1000);
                Toast.makeText(getContext(),"这已经是今日的所有推荐了",Toast.LENGTH_LONG).show();
            }
        });

        Chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                initstr("chinese");
                channel_view();
            }
        });
        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                initstr("math");
                channel_view();
            }
        });
        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                initstr("english");
                channel_view();
            }
        });
        physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                initstr("physics");
                channel_view();
            }
        });
        Chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                initstr("chemistry");
                channel_view();
            }
        });
        biology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                initstr("biology");
                channel_view();
            }
        });
        politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                initstr("politics");
                channel_view();
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                initstr("history");
                channel_view();
            }
        });
        geography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                initstr("geo");
                channel_view();
            }
        });
        //mLvMsgList = view.findViewById(R.id.listview_MsgList);

        /**
         * 多调用两次，数据会更多
         */
        if(mDatas.size()==0)
            mDatas.addAll(MessageLab.generateMockList());


        //mLvMsgList.setAdapter(mAdapter);
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("code:"+requestCode+" "+resultCode);
        if(resultCode==1&&requestCode==0){
            Intent i=new Intent(getActivity(),MainActivity.class);
            startActivity(i);
        }
        if(requestCode==101&&requestCode==101){
            Bundle bundle =data.getExtras();
            String result =bundle.getString("json");
            System.out.println("学科排序的结果为："+result);
        }
    }



    public void channel_view(){
        DividerItemDecoration mDivider = new
                DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        mMyAdapter = new BlankFragment1.MyAdapter1(getActivity(),mNewsList);
        mRecyclerView.setAdapter(mMyAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
    }


    class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.MyViewHoder> {

        List<News> list;
        private Context context;
        public MyAdapter1(Context c, ArrayList<News> l){
            context=c;
            list=l;
        }
        @Override
        public MyAdapter1.MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(context, R.layout.item_list, null);
            return new MyAdapter1.MyViewHoder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter1.MyViewHoder holder, final int position) {

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
//
//                            String re= serverHttpResponse.postResponse(ur,ms);
//
//
//                            String url = getActivity().getString(R.string.backend_ip) + "/request/instance";
//                            String msg="?course="+news.course+"&name="+news.title;
//
//                            String res= serverHttpResponse.getResponse(url+msg);

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

    public void initstr(String course){
        try{
            mNewsList.clear();
            String url = getActivity().getString(R.string.backend_ip) + "/request/recommend";
            String msg="?course="+course;
            String res= serverHttpResponse.getResponse(url+msg);

            JSONArray ddd=new JSONArray(res);
            //System.out.println(answer_json);
            //System.out.println(("!!!!!!!!!!:"+(JSONObject) answer_json.opt("data")));
            //System.out.println(ddd.length());
            for(int i=0;i<ddd.length();i++) {
                JSONObject data2 = ddd.optJSONObject(i);
                //System.out.println("i="+i+" "+data2);
                String uri = data2.opt("entity_url").toString();
                String type = data2.opt("entity_type").toString();
                String name = data2.opt("entity_name").toString();
                //System.out.println("结果"+i+"= "+uri+" "+type+" "+name+" "+course);
                News n = new News(name, type, uri, course);
                mNewsList.add(n);
            }
        }catch(Exception e){}
    }
}
