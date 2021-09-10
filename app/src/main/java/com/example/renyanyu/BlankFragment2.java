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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import com.example.javeduhw.databinding.FragmentFirstBinding;

public class BlankFragment2 extends Fragment {
    Button bt1,bt2,bt3,bt4,bt5,bt6,bt7,bt8,bt9;
    private ImageView channel_change;
    private SearchView search;
    RecyclerView mRecyclerView;
    MyAdapter1 mMyAdapter ;
    ImageView img;
    TextView hot;
    JSONArray array;

    ArrayList<News> mNewsList = new ArrayList<News>();
    LinearLayoutManager layoutManager;
    LinearLayout mylinear;
    String user_name;
    String[] subs=new String[]{"语文","数学","英语","物理","化学","生物","政治","历史","地理"};
    boolean[] select=new boolean[]{false,false,false,false,false,false,false,false,false};
    int[] order=new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1};
    String[] showorder=new String[]{"","","","","","","","",""};
    Map<String,String> sub_towrite=new HashMap<String,String>();
    String[] message=new String[]{};
    List<String> mess=new ArrayList<>();
    int msg_num=0;
    int begin_num;
    private Thread thread;
    public int[] sub;
    String subject;
    boolean beg;
    int test=0;
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
        bt1=(Button)view.findViewById(R.id.Chinese1);
        bt2=(Button)view.findViewById(R.id.math1);
        bt3=(Button)view.findViewById(R.id.English1);
        bt4=(Button)view.findViewById(R.id.physics1);
        bt5=(Button)view.findViewById(R.id.Chemistry1);
        bt6=(Button)view.findViewById(R.id.biology1);
        bt7=(Button)view.findViewById(R.id.politics1);
        bt8=(Button)view.findViewById(R.id.history1);
        bt9=(Button)view.findViewById(R.id.geography1);
        channel_change=view.findViewById(R.id.channel1);
        search=(SearchView) view.findViewById(R.id.search1);
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
                initdata();
                initlist(begin_num,0);
            }
        });
        channel_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                if(user_name.length()==0)Toast.makeText(getActivity(), "对不起，您还没有登录，无法进行频道选择！", Toast.LENGTH_SHORT).show();
                else{
                    Intent intent=new Intent();
                    intent.setClass(getActivity(),ChannelActivity.class);
                    try{intent.putExtra("json",message);}catch(Exception e){}
                    startActivityForResult(intent,101);
                }
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                int num=0;
                for(int i=0;i<9;i++)
                {
                    if(bt1.getText().equals(subs[i])){
                        num=i;
                        break;
                    }
                }
                initlist(num,0);
                channel_view();
                sethot();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                int num=0;
                for(int i=0;i<9;i++)
                {
                    if(bt2.getText().equals(subs[i])){
                        num=i;
                        System.out.println("点击了"+subs[i]);
                        break;
                    }
                }
                initlist(num,0);
                channel_view();
                sethot();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                int num=0;
                for(int i=0;i<9;i++)
                {
                    if(bt3.getText().equals(subs[i])){
                        num=i;
                        break;
                    }
                }
                initlist(num,0);
                channel_view();
                sethot();
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                int num=0;
                for(int i=0;i<9;i++)
                {
                    if(bt4.getText().equals(subs[i])){
                        num=i;
                        break;
                    }
                }
                initlist(num,0);
                channel_view();
                sethot();
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                int num=0;
                for(int i=0;i<9;i++)
                {
                    if(bt5.getText().equals(subs[i])){
                        num=i;
                        break;
                    }
                }
                initlist(num,0);
                channel_view();
                sethot();
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                int num=0;
                for(int i=0;i<9;i++)
                {
                    if(bt6.getText().equals(subs[i])){
                        num=i;
                        break;
                    }
                }
                initlist(num,0);
                channel_view();
                sethot();
            }
        });
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                int num=0;
                for(int i=0;i<9;i++)
                {
                    if(bt7.getText().equals(subs[i])){
                        num=i;
                        break;
                    }
                }
                initlist(num,0);
                channel_view();
                sethot();
            }
        });
        bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                int num=0;
                for(int i=0;i<9;i++)
                {
                    if(bt8.getText().equals(subs[i])){
                        num=i;
                        break;
                    }
                }
                initlist(num,0);
                channel_view();
                sethot();
            }
        });
        bt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                beg=true;
                int num=0;
                for(int i=0;i<9;i++)
                {
                    if(bt9.getText().equals(subs[i])){
                        num=i;
                        break;
                    }
                }
                initlist(num,0);
                channel_view();
                sethot();
            }
        });
        thread.run();
        if(showorder[0].length()!=0)
        {
            begin_num=-1;
            initdata();
            initlist(begin_num,0);
        }

        initview();
        mRecyclerView = view.findViewById(R.id.recyclerview);
        // 构造一些数据

        //thread.start();
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
//                for(int i=0;i<10;i++){
//                    mNewsList.add(new News("标题："+i+" test:"+test,"内容 "+i+" test:"+test));
//                }
//                test++;
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
        return view;
    }
    public void initdata(){
        if(!fileIsExists("/data/data/com.example.javeduhw/shared_prefs/"+user_name+"subinfo.xml")){begin_num=0;return;}
        //if(!fileIsExists("/data/data/com.example.javeduhw/shared_prefs/subinfo.xml"))return;
        try{
            for(int i=0;i<9;i++){
                String tem=getSettingNote(this.getActivity(),user_name+"subinfo",subs[i]);
                int num=Integer.parseInt(tem);
                if(num>=0){
                    if(num==0)
                    {
                        begin_num=i;
                    }
                    showorder[num]=subs[i];
                }
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

            JSONObject one=ddd.optJSONObject(0);
            hot_news=new News(ddd.optJSONObject(0).opt("entity_name").toString(),ddd.optJSONObject(0).opt("entity_type").toString(),ddd.optJSONObject(0).opt("entity_url").toString(),course);
            for(int i=1;i<ddd.length();i++){
                JSONObject data2=ddd.optJSONObject(i);

                String uri=data2.opt("entity_url").toString();
                String type=data2.opt("entity_type").toString();
                String name=data2.opt("entity_name").toString();
                //System.out.println("结果"+i+"= "+uri+" "+type+" "+name+" "+course);
                News n=new News(name,type,uri,course);
                mNewsList.add(n);
            }
        }catch(Exception e){}

    }

    public void channel_view(){

        DividerItemDecoration mDivider = new
                DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        mMyAdapter = new MyAdapter1(getActivity(),mNewsList);
        mRecyclerView.setAdapter(mMyAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1&&requestCode==0){
            //Toast.makeText(getContext(), "!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getActivity(),MainActivity.class);
            startActivity(i);
        }

        if(requestCode==101&&requestCode==101){
            Bundle bundle =data.getExtras();
            String result =bundle.getString("json");

            try{
                System.out.println("!!!!!!!!!"+result);
                JSONArray answer_json = new JSONArray(result);
                for(int i=0;i<answer_json.length();i++){
                    String name=answer_json.optJSONObject(i).opt("name").toString();
                    String selected=answer_json.optJSONObject(i).opt("isSelect").toString();
                    for(int j=0;j<9;j++){
                        if(name.equals(subs[j])){
                            if(selected.equals("true"))
                                order[j]=i;
                            else
                                order[j]=-1;
                            break;
                        }
                    }
                }

                saveorder();
            }catch(Exception e){}

        }
    }

    void sethot(){
        try{
            hot.setText("  名称："+hot_news.title+"\n\n  类型："+hot_news.content);
            hot.setTextColor(Color.rgb(255, 0, 0));
            hot.setVisibility(View.VISIBLE);
            img.setVisibility(View.VISIBLE);
        }catch (Exception e){}

    }

    public void saveorder(){
        for(int i=0;i<9;i++)showorder[i]="";
        for(int i=0;i<9;i++){
            sub_towrite.put(subs[i],order[i]+"");
            saveSettingNote(getActivity(),user_name+"subinfo",sub_towrite);
            if(order[i]!=-1){
                showorder[order[i]]=subs[i];
            }
        }
        initview();
        for(int i=0;i<9;i++){
            if(showorder[0]==subs[i]){
                begin_num=i;
                System.out.println("重建："+showorder[0]);
                initlist(i,0);
                channel_view();
                sethot();
                break;
            }

        }
    }
    public void initview(){
        int i=0;
        //bt1.setVisibility
        if(user_name.length()!=0&&fileIsExists("/data/data/com.example.javeduhw/shared_prefs/"+user_name+"subinfo.xml")){
            bt1.setVisibility(View.GONE);
            bt2.setVisibility(View.GONE);
            bt3.setVisibility(View.GONE);
            bt4.setVisibility(View.GONE);
            bt5.setVisibility(View.GONE);
            bt6.setVisibility(View.GONE);
            bt7.setVisibility(View.GONE);
            bt8.setVisibility(View.GONE);
            bt9.setVisibility(View.GONE);
        }
        mess.clear();
        while(showorder[i].length()!=0){
            String msg="";
            if(i==0){bt1.setText(showorder[i]);bt1.setVisibility(View.VISIBLE);msg=showorder[i]+"1";for(int j=0;j<9;j++)if(subs[j].equals(showorder[i])){select[j]=true;break;}}
            if(i==1){bt2.setText(showorder[i]);bt2.setVisibility(View.VISIBLE);msg=showorder[i]+"1";for(int j=0;j<9;j++)if(subs[j].equals(showorder[i])){select[j]=true;break;}}
            if(i==2){bt3.setText(showorder[i]);bt3.setVisibility(View.VISIBLE);msg=showorder[i]+"1";for(int j=0;j<9;j++)if(subs[j].equals(showorder[i])){select[j]=true;break;}}
            if(i==3){bt4.setText(showorder[i]);bt4.setVisibility(View.VISIBLE);msg=showorder[i]+"1";for(int j=0;j<9;j++)if(subs[j].equals(showorder[i])){select[j]=true;break;}}
            if(i==4){bt5.setText(showorder[i]);bt5.setVisibility(View.VISIBLE);msg=showorder[i]+"1";for(int j=0;j<9;j++)if(subs[j].equals(showorder[i])){select[j]=true;break;}}
            if(i==5){bt6.setText(showorder[i]);bt6.setVisibility(View.VISIBLE);msg=showorder[i]+"1";for(int j=0;j<9;j++)if(subs[j].equals(showorder[i])){select[j]=true;break;}}
            if(i==6){bt7.setText(showorder[i]);bt7.setVisibility(View.VISIBLE);msg=showorder[i]+"1";for(int j=0;j<9;j++)if(subs[j].equals(showorder[i])){select[j]=true;break;}}
            if(i==7){bt8.setText(showorder[i]);bt8.setVisibility(View.VISIBLE);msg=showorder[i]+"1";for(int j=0;j<9;j++)if(subs[j].equals(showorder[i])){select[j]=true;break;}}
            if(i==8){bt9.setText(showorder[i]);bt9.setVisibility(View.VISIBLE);msg=showorder[i]+"1";for(int j=0;j<9;j++)if(subs[j].equals(showorder[i])){select[j]=true;break;}}
            i++;
            mess.add(msg);
            //message[msg_num]=msg;
            //msg_num++;
        }

        for(int j=0;j<9;j++){
            if(!select[j]){
                String ss=subs[j]+"0";
                mess.add(ss);
            }
        }
        message=mess.toArray(new String[mess.size()]);
        System.out.println("最终信息为："+mess.toString());
    }
}
