package com.example.javeduhw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class Collection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ListView listView=(ListView)findViewById(R.id.ListView1);
        final String[] str ={"实体类型\n实体名称","实体类型\n实体名称","实体类型\n实体名称"};
        //配置ArrayAdapter适配器
        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (Collection.this,android.R.layout.simple_expandable_list_item_1,str);
        listView.setAdapter(adapter);
        //设置选中选项监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent goToEntityDetailsPage = new Intent(Collection.this,EntityDetails.class);
                startActivity(goToEntityDetailsPage);
            }
        });
    }
}
