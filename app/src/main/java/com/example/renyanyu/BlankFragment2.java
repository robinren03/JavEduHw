package com.example.renyanyu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import com.example.javeduhw.databinding.FragmentFirstBinding;

public class BlankFragment2 extends Fragment {
    Button Chinese,math,English,physics,Chemistry,biology,politics,history,geography;
    private Button channel_change;
    private SearchView search;
    RecyclerView mRecyclerView;
    MyAdapter1 mMyAdapter ;
    ArrayList<News> mNewsList = new ArrayList<News>();
    LinearLayoutManager layoutManager;
    LinearLayout mylinear;
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
        history=(Button)view.findViewById(R.id.politics1);
        geography=(Button)view.findViewById(R.id.geography1);
        channel_change=(Button)view.findViewById(R.id.channel1);
        search=(SearchView) view.findViewById(R.id.search1);
        search.setIconifiedByDefault(false);
        //显示搜索按钮
        search.setSubmitButtonEnabled(true);
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
                Intent intent=new Intent();
                intent.setClass(getActivity(),SearchResult.class);
                //传输消息 todo
                startActivity(intent);
            }
        });
        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),SearchResult.class);
                //传输消息 todo
                startActivity(intent);
            }
        });
        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),SearchResult.class);
                //传输消息 todo
                startActivity(intent);
            }
        });
        physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),SearchResult.class);
                //传输消息 todo
                startActivity(intent);
            }
        });
        Chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),SearchResult.class);
                //传输消息 todo
                startActivity(intent);
            }
        });
        biology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),SearchResult.class);
                //传输消息 todo
                startActivity(intent);
            }
        });
        politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),SearchResult.class);
                //传输消息 todo
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),SearchResult.class);
                //传输消息 todo
                startActivity(intent);
            }
        });
        geography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),SearchResult.class);
                //传输消息 todo
                startActivity(intent);
            }
        });
        initview();
        mRecyclerView = view.findViewById(R.id.recyclerview);
        // 构造一些数据
        for (int i = 0; i < 10; i++) {
            News news = new News("标题xxx" + i,"内容xxx" + i);
            mNewsList.add(news);
        }
        DividerItemDecoration mDivider = new
                DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        mMyAdapter = new MyAdapter1(getActivity(),mNewsList);
        mRecyclerView.setAdapter(mMyAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);


        RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
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
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //单击搜索按钮的监听
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, "您输入的文本为" + query, Toast.LENGTH_SHORT).show();

                Intent intent1=new Intent(getActivity(), SearchResult.class);
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
        return view;
    }
    public void initview(){

        if(!fileIsExists("/data/data/com.example.javeduhw/shared_prefs/subinfo.xml"))return;
        try{
            String zero="0";
            for(int i=0;i<9;i++){
                Button bt=(Button)mylinear.getChildAt(i);
                String tx=bt.getText().toString();
                //if()
                if(!getSettingNote(this.getActivity(),"subinfo",tx).equals(zero)){
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

                        Intent intent=new Intent(getActivity(),Detail.class);
                        startActivity(intent);
                    }
                });
            }catch(NullPointerException e){
                Toast.makeText(getActivity(),"111111111111111",Toast.LENGTH_LONG).show();
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


}
