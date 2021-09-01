package com.example.renyanyu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

import com.google.gson.Gson;

public class SearchPage extends AppCompatActivity {
    Button Chinese,math,English,physics,Chemistry,biology,politics,history,geography;
    boolean Chi,mat,Eng,phy,Che,bio,pol,his,geo;
    private SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);
        Chinese=(Button)findViewById(R.id.Chi);
        math=(Button)findViewById(R.id.mat);
        English=(Button)findViewById(R.id.Eng);
        physics=(Button)findViewById(R.id.phy);
        Chemistry=(Button)findViewById(R.id.Che);
        biology=(Button)findViewById(R.id.bio);
        politics=(Button)findViewById(R.id.pol);
        history=(Button)findViewById(R.id.pol);
        geography=(Button)findViewById(R.id.geo);
        search = (SearchView)findViewById(R.id.search);
        search.setIconifiedByDefault(false);
        //显示搜索按钮
        search.setSubmitButtonEnabled(true);
        Chi=false;mat=false;Eng=false;phy=false;Che=false;bio=false;pol=false;his=false;geo=false;

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //单击搜索按钮的监听
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, "您输入的文本为" + query, Toast.LENGTH_SHORT).show();

                Intent intent1=new Intent(SearchPage.this, SearchResult.class);
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
        Chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SearchPage.this, "123" , Toast.LENGTH_SHORT).show();
                Chi=true;
            }
        });
        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SearchPage.this, "456" , Toast.LENGTH_SHORT).show();
                mat=true;
            }
        });
        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eng=true;
            }
        });
        physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phy=true;
            }
        });
        Chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Che=true;
            }
        });
        biology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bio=true;
            }
        });
        politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pol=true;
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                his=true;
            }
        });
        geography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geo=true;
            }
        });
    }

}