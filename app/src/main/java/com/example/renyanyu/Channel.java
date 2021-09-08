package com.example.renyanyu;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import java.util.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Channel extends AppCompatActivity{
    private DragGridlayout mSelectedChannel;
    private DragGridlayout mUnSelectedChannel;

    List<String> selectedChannel = new ArrayList<>();
    List<String> unSelectedChannel = new ArrayList<>();
    Map<String,String> sub=new HashMap<String,String>();
    String user_name;
    String[] subs={"语文","数学","英语","生物","地理","化学","物理","政治","历史"};
    boolean[] have_sub=new boolean[]{false,false,false,false,false,false,false,false,false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_change);
        SharedPreferences userInfo= Channel.this.getSharedPreferences("user", 0);
        user_name = userInfo.getString("username","");
        initView();
        initData();
        initEvent();
        setResult(1);
    }
    private void initView(){
        //setContentView(R.layout.channel_change);
        mSelectedChannel = (DragGridlayout) findViewById(R.id.selectedChannel);
        mUnSelectedChannel = (DragGridlayout) findViewById(R.id.unSelectedChannel);
        mSelectedChannel.setAllowDrag(true);
        mUnSelectedChannel.setAllowDrag(true);
    }
    private void initData() {
        if(!fileIsExists("/data/data/com.example.javeduhw/shared_prefs/"+user_name+"subinfo.xml")){
        //if(!fileIsExists("/data/data/com.example.javeduhw/shared_prefs/subinfo.xml")){
            System.out.println("sssssssssssssssss");
            for(int i=0;i<9;i++){
                if(i<3){
                    selectedChannel.add(subs[i].toString());
                    sub.put(subs[i],"1");
                }
                else{
                    unSelectedChannel.add(subs[i].toString());
                    sub.put(subs[i],"0");
                }
            }
            //saveSettingNote(Channel.this, "subinfo", sub);
            saveSettingNote(Channel.this, user_name+"subinfo", sub);
        }
        else{
            try{
                String zero="0";
                for(int i=0;i<9;i++){
                    if(getSettingNote(Channel.this,user_name+"subinfo",subs[i].toString()).equals(zero)){
                    //if(getSettingNote(Channel.this,"subinfo",subs[i].toString()).equals(zero)){
                        unSelectedChannel.add(subs[i].toString());
                        sub.put(subs[i],"0");
                    }
                    else{
                        selectedChannel.add(subs[i].toString());
                        sub.put(subs[i],"1");
                        have_sub[i]=true;
                    }

                }
            }catch(NullPointerException e){}
        }
        mSelectedChannel.setItems(selectedChannel);
        mUnSelectedChannel.setItems(unSelectedChannel);
    }

    public void initEvent(){
        //设置条目点击监听
        mSelectedChannel.setOnDragItemClickListener(new DragGridlayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                //移除点击的条目，把条目添加到下面的Gridlayout
                mSelectedChannel.removeView(tv);//移除是需要时间,不能直接添加
                mUnSelectedChannel.addItem(tv.getText().toString(),0);
                sub.put(tv.getText().toString(),"0");

                saveSettingNote(Channel.this, user_name+"subinfo", sub);
            }
        });


        mUnSelectedChannel.setOnDragItemClickListener(new DragGridlayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                //移除点击的条目，把条目添加到上面的Gridlayout
                mUnSelectedChannel.removeView(tv);//移除是需要时间,不能直接添加
                mSelectedChannel.addItem(tv.getText().toString());
                sub.put(tv.getText().toString(),"1");
                //saveSettingNote(Channel.this, "subinfo", sub);
                saveSettingNote(Channel.this, user_name+"subinfo", sub);
            }
        });
    }

    private int index = 0;

    public void addItem(View view) {

    }






    public static void saveSettingNote(Context context,String filename ,Map<String, String> map) {
        SharedPreferences.Editor note = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            note.putString(entry.getKey(), entry.getValue());
            note.commit();
        }

    }


    /**
     * 从本地取出要保存的数据
     * @param context 上下文
     * @param filename 文件名
     * @param dataname 生成XML中每条数据名
     * @return 对应的数据(找不到为NUll)
     */
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
}
