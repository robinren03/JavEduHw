package com.example.renyanyu;

//package com.example.renyanyu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.*;


public class EntityDetails extends AppCompatActivity {
    private Gson gson=new Gson();
    public String name;
    public boolean collected;
    public News detail;
    public String title1,title2,title3; // 标题
    public String content1,content2,content3; //内容
    public String[] str=new String[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_details);
        ListView listView=(ListView)findViewById(R.id.ListView1);
        str =new String[]{"属性名\n属性值","属性名\n属性值","属性名\n属性值"};
        name="name_type";
        if(fileIsExists(name))
        {
            getPage();
            str=new String[]{detail.title+"\n"+detail.content,detail.title1+"\n"+detail.content1,detail.title2+"\n"+detail.content2};
        }
        else{
            str=new String[]{title1+"\n"+content1,title2+"\n"+content2,title3+"\n"+content3};
            setDetail();
            savePackageData();
        }
        //配置ArrayAdapter适配器
        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (EntityDetails.this,android.R.layout.simple_expandable_list_item_1,str);
        listView.setAdapter(adapter);
        //设置选中选项监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                Intent goToHistoryPage = new Intent(History.this,History.class);
//                startActivity(goToHistoryPage);
            }
        });
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
                collected=true;
                detail.collected=collected;
                savePackageData();
                Toast.makeText(EntityDetails.this,"已收藏",Toast.LENGTH_LONG).show();
                addToCollectionButton.setVisibility(View.GONE);
                hadAddedToCollectionButton.setVisibility(View.VISIBLE);

            }
        }) ;

        hadAddedToCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collected=false;
                detail.collected=collected;
                savePackageData();
                Toast.makeText(EntityDetails.this,"已取消收藏",Toast.LENGTH_LONG).show();
//                addToCollectionButton.setPointerIcon(R.id.shareButton);
                //  shareButton.setIm
                addToCollectionButton.setVisibility(View.VISIBLE);
                hadAddedToCollectionButton.setVisibility(View.GONE);
            }
        }) ;

    }

    //保存应用信息到本地文件
    private void savePackageData() {
        try {
            FileOutputStream fout = openFileOutput(name+".json", MODE_PRIVATE);
            BufferedOutputStream buffout = new BufferedOutputStream(fout);
            String jsonArray = gson.toJson(new TypeToken<News>() {
            }.getType());
            buffout.write(jsonArray.getBytes());
            buffout.close();
        } catch (Exception e) {
        }
    }

    public  void getPage() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(name));
            Gson gson = new GsonBuilder().create();
            detail = gson.fromJson(reader, new TypeToken<News>(){}.getType());

        } catch (FileNotFoundException ex) { }
    }

    public void setDetail(){
        detail.title=title1;
        detail.title1=title2;
        detail.title2=title3;
        detail.content=content1;
        detail.content1=content2;
        detail.content2=content3;
        detail.collected=collected;
    }

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