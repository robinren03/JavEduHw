package com.example.javeduhw;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import android.widget.*;
//import com.example.javeduhw.databinding.FragmentFirstBinding;

public class BlankFragment1 extends Fragment {

    public BlankFragment1() {
        // Required empty public constructor
    }

    LinearLayout mylinear;
    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    Button Chinese,math,English,physics,Chemistry,biology,politics,history,geography,qa;
    private SearchView search;
    private Button channel_change;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Toast.makeText(getContext(),"点击了：",Toast.LENGTH_LONG).show();
        View view = inflater.inflate(R.layout.fragment_blank_fragment1, container, false);
        mylinear = (LinearLayout) view.findViewById(R.id.Mylinear);

        Chinese=(Button)view.findViewById(R.id.Chinese);
        math=(Button)view.findViewById(R.id.math);
        English=(Button)view.findViewById(R.id.English);
        physics=(Button)view.findViewById(R.id.physics);
        Chemistry=(Button)view.findViewById(R.id.Chemistry);
        biology=(Button)view.findViewById(R.id.biology);
        politics=(Button)view.findViewById(R.id.politics);
        history=(Button)view.findViewById(R.id.politics);
        geography=(Button)view.findViewById(R.id.geography);
        qa=(Button)view.findViewById(R.id.IVButton_Id);
        qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),QA.class);
                //传输消息 todo
                startActivity(intent);
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
}
