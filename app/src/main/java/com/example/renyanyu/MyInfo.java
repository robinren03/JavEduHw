package com.example.renyanyu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyInfo extends AppCompatActivity implements View.OnClickListener{

    private ListView listview;
    private List<String> mContent = new ArrayList<>();
    private List<Integer> selectId = new ArrayList<>();
    private boolean isDeleteMode = false; //是否多选
    private Adapter adapter;
    private RelativeLayout layout;
    private ImageView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        initView();
        initDatas();
        initEvent();
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.list_view);
        layout = (RelativeLayout) findViewById(R.id.rll_view);
        delete = (ImageView) findViewById(R.id.btn_delete);
        adapter = new Adapter();
    }

    private void initDatas() {
        for (int i = 0; i < 20; i++) {
            mContent.add("这里用来测试长按删除的功能" + i);
        }
    }

    private void initEvent() {
        listview.setAdapter((ListAdapter) adapter);
        delete.setOnClickListener(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MyInfo.this, "被点击了", Toast.LENGTH_SHORT).show();
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isDeleteMode = true;
                selectId.clear();
                layout.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }


    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_delete:
                isDeleteMode = false;
                Integer[] select = selectId.toArray(new Integer[selectId.size()]);
                Arrays.sort(select);
                for (int i = 0; i < select.length; i++) {
                    Log.d("tag",select[i]+"****************");
                }

                for (int i = select.length-1; i>=0 ; i--) {
                    mContent.remove((int)select[i]);
                    Log.d("tag","移除了"+select[i]);

                }
                selectId.clear();
                adapter.notifyDataSetChanged();
                layout.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }
    class Adapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater = null;

        public int getCount() {
            return mContent.size();
        }

        public Object getItem(int position) {
            return mContent.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MyInfo.this, R.layout.history_list_item, null);
                holder = new ViewHolder();
                holder.content = (TextView) convertView.findViewById(R.id.txtName);
                holder.check = (CheckBox) convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final String content = mContent.get(position);
            holder.content.setText(content);
            holder.check.setChecked(false);
            if (selectId.contains(position)){
                holder.check.setChecked(true);
            }
            if (isDeleteMode) {
                holder.check.setVisibility(View.VISIBLE);
            } else {
                holder.check.setVisibility(View.INVISIBLE);
            }
            holder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.check.isChecked()) {

                        selectId.add(position);
                        Log.d("tag","添加"+position);
                    }else{
                        //需要强制转化使用移除对象的方法
                        selectId.remove((Integer)position);
                    }
                    Log.d("tag",position+"被选中");
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView content;
            CheckBox check;
        }
    }
}



