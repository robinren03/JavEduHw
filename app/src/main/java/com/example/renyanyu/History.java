package com.example.renyanyu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class History extends AppCompatActivity {

    boolean isDeleteMode = false;
    boolean allChecked=false;
    ArrayList<Integer> selectedIdList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_history);

        // region 复制的部分
        TextView guideInfoTextView=(TextView) findViewById(R.id.history_page_guide_info);
        guideInfoTextView.setVisibility(View.GONE);
        ListView listView=(ListView)findViewById(R.id.history_page_entity_list);
        RelativeLayout buttonGroup = (RelativeLayout) findViewById(R.id.history_list_image_button_group);
        RelativeLayout buttonTextGroup = (RelativeLayout) findViewById(R.id.history_list_image_button_text_group);
        buttonGroup.setVisibility(View.GONE);
        buttonTextGroup.setVisibility(View.GONE);

        // region 获取responseString
        String url = History.this.getString(R.string.backend_ip) + "/user/history";
        SharedPreferences userInfo= History.this.getSharedPreferences("user", 0);
        String userToken = userInfo.getString("token","");
        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
        String message="token="+userToken;
        url=url+"?"+message;
        String responseString = serverHttpResponse.getResponse(url);
        System.out.println(responseString);
        // endregion

        if(responseString==null)
        {
            Toast.makeText(History.this, "好像断网了，请检查您的网络设置", Toast.LENGTH_SHORT).show();
//            guideInfoTextView.setText("好像断网了，请检查您的网络设置");
//            guideInfoTextView.setVisibility(View.VISIBLE);
//            listView.setVisibility(View.GONE);
            String userName = userInfo.getString("username","");
            KEntityRepository kEntityRepository =new KEntityRepository(AppDB.getAppDB(History.this, userName));
            List<KEntity> kEntityList = kEntityRepository.getAllKEntities();

            class Entity
            {
                Entity(String _name,String _type, String _uri,String _time,Long _id, String _course)
                {
                    name=_name;
                    type=_type;
                    uri=_uri;
                    time=_time;
                    id=_id;
                    course = _course;
                }
                String name;
                String type;
                String uri;
                String time;
                String course;
                Long id;
            }

            ArrayList<Entity> entityList=new ArrayList<>();
            for(int i=0;i<kEntityList.size();i++)
            {
                KEntity kEntity=kEntityList.get(i);
                String name =kEntity.getLabel();
                String type =kEntity.getType();
                String uri = kEntity.getKEntityUri();
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(kEntity.getCreatedate());
                //TODO:日期的格式
                String course = kEntity.getCourse();
                Long id = 0L;//TODO:已经弄好了吗？
                entityList.add(new Entity(name,type,uri,time,id, course));
            }

            entityList.sort( new Comparator<Entity>(){
                @Override
                public int compare(Entity x , Entity y )
                {
                    if ( x.time== null || y.time== null ) return 0;
                    return y.time.compareTo(x.time); //这是顺序
                }
            });

            class HistoryListAdapter extends BaseAdapter
            {
                class ViewHolder {
                    TextView itemTextView;
                    CheckBox itemCheckBox;
                }

                private Context context;
                private LayoutInflater inflater = null;

                public int getCount() {
                    return entityList.size();
                }

                public Object getItem(int position) {
                    return entityList.get(position);
                }

                public long getItemId(int position) {
                    return position;
                }

                public View getView(final int position, View convertView, ViewGroup parent)
                {
                    final ViewHolder holder;
                    if (convertView == null)
                    {
                        convertView = View.inflate(History.this, R.layout.history_list_item, null);
                        holder = new ViewHolder();
                        holder.itemTextView = (TextView) convertView.findViewById(R.id.history_list_item_text);
                        holder.itemCheckBox = (CheckBox) convertView.findViewById(R.id.history_list_item_checkbox);
                        convertView.setTag(holder);
                    }
                    else
                    {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    Entity entity=entityList.get(position);
                    final String content = "实体类型："+entity.type + "\n实体名称："+entity.name
                            + "\n浏览时间："+entity.time.substring(0,entity.time.length()-3);
                    holder.itemTextView.setText(content);
                    holder.itemCheckBox.setChecked(false);
                    if (selectedIdList.contains(position))    holder.itemCheckBox.setChecked(true);
                    if (isDeleteMode)     holder.itemCheckBox.setVisibility(View.VISIBLE);
                    else    holder.itemCheckBox.setVisibility(View.INVISIBLE);

                    holder.itemCheckBox.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (holder.itemCheckBox.isChecked())
                            {
                                selectedIdList.add(position);
                                Log.d("tag","添加"+position);
                            }
                            else
                            {
                                selectedIdList.remove((Integer)position);
                            }
                            Log.d("tag",position+"被点击");
                        }
                    });
                    return convertView;
                }
            }

            HistoryListAdapter adapter = new HistoryListAdapter();
            listView.setAdapter((ListAdapter) adapter);
            // region 每个list item的点击事件
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent goToEntityDetailsPage = new Intent(History.this,EntityDetails.class);
                    System.out.println("Very important" + entityList.get(position).course);
                    goToEntityDetailsPage.putExtra("entity_name",entityList.get(position).name);
                    goToEntityDetailsPage.putExtra("type",entityList.get(position).type);
                    goToEntityDetailsPage.putExtra("uri",entityList.get(position).uri);
                    goToEntityDetailsPage.putExtra("course",entityList.get(position).course);
                    System.out.println("真正历史记录："+entityList.get(position).name+" "+entityList.get(position).uri);
                    startActivity(goToEntityDetailsPage);
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
            {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                {
                    isDeleteMode = true;
                    selectedIdList.clear();
                    adapter.notifyDataSetChanged();
                    buttonGroup.setVisibility(View.VISIBLE);
                    buttonTextGroup.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            // endregion

            // region 删除按钮
            ImageView deleteButton=(ImageView)findViewById(R.id.history_page_delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(selectedIdList.size()==0)
                    {
                        Toast.makeText(History.this,"您还没有选择历史记录哦",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        AlertDialog.Builder dialog=new AlertDialog.Builder(History.this);
                        dialog.setTitle("删除历史记录");//设置标题
                        dialog.setMessage("确定要删除选中的"+selectedIdList.size()+"项历史记录吗？");//设置信息具体内容
                        dialog.setCancelable(false);//设置是否可取消
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override//确认退出登录
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                for (int j = 0; j < selectedIdList.size(); j++)
                                {
                                    String url = History.this.getString(R.string.backend_ip) + "/request/deleteFromHistory";
                                    SharedPreferences userInfo= History.this.getSharedPreferences("user", 0);
                                    String userToken = userInfo.getString("token","");
                                    ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                                    String message="token="+userToken+"&id="+entityList.get(selectedIdList.get(j)).id;
                                    String responseString = serverHttpResponse.postResponse(url,message);
                                    System.out.println(responseString);
                                }
                                isDeleteMode = false;
                                selectedIdList.clear();
                                adapter.notifyDataSetChanged();
                                buttonGroup.setVisibility(View.GONE);
                                buttonTextGroup.setVisibility(View.GONE);
                                onStart();
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //取消删除，就什么也没有发生
                            }
                        });
                        dialog.show();
                    }

                }
            });

            TextView deleteButtonText=(TextView)findViewById(R.id.history_page_delete_button_text);
            deleteButtonText.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(selectedIdList.size()==0)
                    {
                        Toast.makeText(History.this,"您还没有选择历史记录哦",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        AlertDialog.Builder dialog=new AlertDialog.Builder(History.this);
                        dialog.setTitle("删除历史记录");//设置标题
                        dialog.setMessage("确定要删除选中的"+selectedIdList.size()+"项历史记录吗？");//设置信息具体内容
                        dialog.setCancelable(false);//设置是否可取消
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override//确认退出登录
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                for (int j = 0; j < selectedIdList.size(); j++)
                                {
                                    String url = History.this.getString(R.string.backend_ip) + "/request/deleteFromHistory";
                                    SharedPreferences userInfo= History.this.getSharedPreferences("user", 0);
                                    String userToken = userInfo.getString("token","");
                                    ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                                    String message="token="+userToken+"&id="+entityList.get(selectedIdList.get(j)).id;
                                    String responseString = serverHttpResponse.postResponse(url,message);
                                    System.out.println(responseString);
                                }
                                isDeleteMode = false;
                                selectedIdList.clear();
                                adapter.notifyDataSetChanged();
                                buttonGroup.setVisibility(View.GONE);
                                buttonTextGroup.setVisibility(View.GONE);
                                onStart();
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //取消删除，就什么也没有发生
                            }
                        });
                        dialog.show();
                    }
                }
            });
            // endregion

            // region 取消删除按钮
            ImageView cancelButton=(ImageView)findViewById(R.id.history_page_cancel_button);
            cancelButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    isDeleteMode = false;
                    selectedIdList.clear();
                    adapter.notifyDataSetChanged();
                    buttonGroup.setVisibility(View.GONE);
                    buttonTextGroup.setVisibility(View.GONE);
                }
            });

            TextView cancelButtonText=(TextView)findViewById(R.id.history_page_cancel_button_text);
            cancelButtonText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isDeleteMode = false;
                    selectedIdList.clear();
                    adapter.notifyDataSetChanged();
                    buttonGroup.setVisibility(View.GONE);
                    buttonTextGroup.setVisibility(View.GONE);
                }
            });
            // endregion

            // region 全选按钮
            ImageView checkAllButton=(ImageView)findViewById(R.id.history_page_check_all_button);
            checkAllButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(!allChecked)
                    {
                        allChecked=true;
                        selectedIdList.clear();
                        for(int i=0;i<entityList.size();i++)
                        {
                            selectedIdList.add(i);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        allChecked=false;
                        selectedIdList.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            TextView checkAllButtonText=(TextView)findViewById(R.id.history_page_check_all_button_text);
            checkAllButtonText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!allChecked)
                    {
                        allChecked=true;
                        selectedIdList.clear();
                        for(int i=0;i<entityList.size();i++)
                        {
                            selectedIdList.add(i);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        allChecked=false;
                        selectedIdList.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
            });
            // endregion


        }
        else if(responseString.equals("[]"))
        {
            guideInfoTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else
        {
            try
            {
                JSONArray collectionArray = new JSONArray(responseString);
                System.out.println(collectionArray.get(0));

                class Entity
                {
                    Entity(String _name,String _type, String _uri,String _time,Long _id, String _course)
                    {
                        name=_name;
                        type=_type;
                        uri=_uri;
                        time=_time;
                        id=_id;
                        course = _course;
                    }
                    String name;
                    String type;
                    String uri;
                    String time;
                    String course;
                    Long id;
                }

                ArrayList<Entity> entityList=new ArrayList<>();
                for(int i=0;i<collectionArray.length();i++)
                {
                    JSONObject entity_json=(JSONObject)collectionArray.get(i);
                    String name =entity_json.get("name").toString();
                    String type =entity_json.get("type").toString();
                    String uri = entity_json.get("uri").toString();
                    String time = entity_json.get("time").toString();
                    String course = entity_json.get("course").toString();
                    Long id = Long.parseLong(entity_json.get("id").toString());
                    entityList.add(new Entity(name,type,uri,time,id, course));
                }
                //TODO:给entityList按时间顺序排序
                entityList.sort( new Comparator<Entity>(){
                    @Override
                    public int compare(Entity x , Entity y )
                    {
                        if ( x.time== null || y.time== null ) return 0;
                        return y.time.compareTo(x.time); //这是顺序
                    }
                });

                class HistoryListAdapter extends BaseAdapter
                {
                    class ViewHolder {
                        TextView itemTextView;
                        CheckBox itemCheckBox;
                    }

                    private Context context;
                    private LayoutInflater inflater = null;

                    public int getCount() {
                        return entityList.size();
                    }

                    public Object getItem(int position) {
                        return entityList.get(position);
                    }

                    public long getItemId(int position) {
                        return position;
                    }

                    public View getView(final int position, View convertView, ViewGroup parent)
                    {
                        final ViewHolder holder;
                        if (convertView == null)
                        {
                            convertView = View.inflate(History.this, R.layout.history_list_item, null);
                            holder = new ViewHolder();
                            holder.itemTextView = (TextView) convertView.findViewById(R.id.history_list_item_text);
                            holder.itemCheckBox = (CheckBox) convertView.findViewById(R.id.history_list_item_checkbox);
                            convertView.setTag(holder);
                        }
                        else
                        {
                            holder = (ViewHolder) convertView.getTag();
                        }
                        Entity entity=entityList.get(position);
                        final String content = "实体类型："+entity.type + "\n实体名称："+entity.name
                                + "\n浏览时间："+entity.time.substring(0,entity.time.length()-3);
                        holder.itemTextView.setText(content);
                        holder.itemCheckBox.setChecked(false);
                        if (selectedIdList.contains(position))    holder.itemCheckBox.setChecked(true);
                        if (isDeleteMode)     holder.itemCheckBox.setVisibility(View.VISIBLE);
                        else    holder.itemCheckBox.setVisibility(View.INVISIBLE);

                        holder.itemCheckBox.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (holder.itemCheckBox.isChecked())
                                {
                                    selectedIdList.add(position);
                                    Log.d("tag","添加"+position);
                                }
                                else
                                {
                                    selectedIdList.remove((Integer)position);
                                }
                                Log.d("tag",position+"被点击");
                            }
                        });
                        return convertView;
                    }
                }


                HistoryListAdapter adapter = new HistoryListAdapter();
                listView.setAdapter((ListAdapter) adapter);
                // region 每个list item的点击事件
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Intent goToEntityDetailsPage = new Intent(History.this,EntityDetails.class);
                        System.out.println("Very important" + entityList.get(position).course);
                        goToEntityDetailsPage.putExtra("entity_name",entityList.get(position).name);
                        goToEntityDetailsPage.putExtra("type",entityList.get(position).type);
                        goToEntityDetailsPage.putExtra("uri",entityList.get(position).uri);
                        goToEntityDetailsPage.putExtra("course",entityList.get(position).course);
                        System.out.println("真正历史记录："+entityList.get(position).name+" "+entityList.get(position).uri);
                        startActivity(goToEntityDetailsPage);
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
                {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        isDeleteMode = true;
                        selectedIdList.clear();
                        adapter.notifyDataSetChanged();
                        buttonGroup.setVisibility(View.VISIBLE);
                        buttonTextGroup.setVisibility(View.VISIBLE);
                        return true;
                    }
                });
                // endregion

                // region 删除按钮
                ImageView deleteButton=(ImageView)findViewById(R.id.history_page_delete_button);
                deleteButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(selectedIdList.size()==0)
                        {
                            Toast.makeText(History.this,"您还没有选择历史记录哦",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            AlertDialog.Builder dialog=new AlertDialog.Builder(History.this);
                            dialog.setTitle("删除历史记录");//设置标题
                            dialog.setMessage("确定要删除选中的"+selectedIdList.size()+"项历史记录吗？");//设置信息具体内容
                            dialog.setCancelable(false);//设置是否可取消
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override//确认退出登录
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    for (int j = 0; j < selectedIdList.size(); j++)
                                    {
                                        String url = History.this.getString(R.string.backend_ip) + "/request/deleteFromHistory";
                                        SharedPreferences userInfo= History.this.getSharedPreferences("user", 0);
                                        String userToken = userInfo.getString("token","");
                                        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                                        String message="token="+userToken+"&id="+entityList.get(selectedIdList.get(j)).id;
                                        String responseString = serverHttpResponse.postResponse(url,message);
                                        System.out.println(responseString);
                                    }
                                    isDeleteMode = false;
                                    selectedIdList.clear();
                                    adapter.notifyDataSetChanged();
                                    buttonGroup.setVisibility(View.GONE);
                                    buttonTextGroup.setVisibility(View.GONE);
                                    onStart();
                                }
                            });
                            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //取消删除，就什么也没有发生
                                }
                            });
                            dialog.show();
                        }

                    }
                });

                TextView deleteButtonText=(TextView)findViewById(R.id.history_page_delete_button_text);
                deleteButtonText.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(selectedIdList.size()==0)
                        {
                            Toast.makeText(History.this,"您还没有选择历史记录哦",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            AlertDialog.Builder dialog=new AlertDialog.Builder(History.this);
                            dialog.setTitle("删除历史记录");//设置标题
                            dialog.setMessage("确定要删除选中的"+selectedIdList.size()+"项历史记录吗？");//设置信息具体内容
                            dialog.setCancelable(false);//设置是否可取消
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override//确认退出登录
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    for (int j = 0; j < selectedIdList.size(); j++)
                                    {
                                        String url = History.this.getString(R.string.backend_ip) + "/request/deleteFromHistory";
                                        SharedPreferences userInfo= History.this.getSharedPreferences("user", 0);
                                        String userToken = userInfo.getString("token","");
                                        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                                        String message="token="+userToken+"&id="+entityList.get(selectedIdList.get(j)).id;
                                        String responseString = serverHttpResponse.postResponse(url,message);
                                        System.out.println(responseString);
                                    }
                                    isDeleteMode = false;
                                    selectedIdList.clear();
                                    adapter.notifyDataSetChanged();
                                    buttonGroup.setVisibility(View.GONE);
                                    buttonTextGroup.setVisibility(View.GONE);
                                    onStart();
                                }
                            });
                            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //取消删除，就什么也没有发生
                                }
                            });
                            dialog.show();
                        }
                    }
                });
                // endregion

                // region 取消删除按钮
                ImageView cancelButton=(ImageView)findViewById(R.id.history_page_cancel_button);
                cancelButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        isDeleteMode = false;
                        selectedIdList.clear();
                        adapter.notifyDataSetChanged();
                        buttonGroup.setVisibility(View.GONE);
                        buttonTextGroup.setVisibility(View.GONE);
                    }
                });

                TextView cancelButtonText=(TextView)findViewById(R.id.history_page_cancel_button_text);
                cancelButtonText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isDeleteMode = false;
                        selectedIdList.clear();
                        adapter.notifyDataSetChanged();
                        buttonGroup.setVisibility(View.GONE);
                        buttonTextGroup.setVisibility(View.GONE);
                    }
                });
                // endregion

                // region 全选按钮
                ImageView checkAllButton=(ImageView)findViewById(R.id.history_page_check_all_button);
                checkAllButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(!allChecked)
                        {
                            allChecked=true;
                            selectedIdList.clear();
                            for(int i=0;i<entityList.size();i++)
                            {
                                selectedIdList.add(i);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            allChecked=false;
                            selectedIdList.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

                TextView checkAllButtonText=(TextView)findViewById(R.id.history_page_check_all_button_text);
                checkAllButtonText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!allChecked)
                        {
                            allChecked=true;
                            selectedIdList.clear();
                            for(int i=0;i<entityList.size();i++)
                            {
                                selectedIdList.add(i);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            allChecked=false;
                            selectedIdList.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                // endregion

            }
            catch (JSONException e)
            {
                guideInfoTextView.setText("抱歉，服务器好像出了点问题，请稍后重试");
                guideInfoTextView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }

        // endregion

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        // region 复制的部分
        TextView guideInfoTextView=(TextView) findViewById(R.id.history_page_guide_info);
        guideInfoTextView.setVisibility(View.GONE);
        ListView listView=(ListView)findViewById(R.id.history_page_entity_list);
        RelativeLayout buttonGroup = (RelativeLayout) findViewById(R.id.history_list_image_button_group);
        RelativeLayout buttonTextGroup = (RelativeLayout) findViewById(R.id.history_list_image_button_text_group);
        buttonGroup.setVisibility(View.GONE);
        buttonTextGroup.setVisibility(View.GONE);

        // region 获取responseString
        String url = History.this.getString(R.string.backend_ip) + "/user/history";
        SharedPreferences userInfo= History.this.getSharedPreferences("user", 0);
        String userToken = userInfo.getString("token","");
        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
        String message="token="+userToken;
        url=url+"?"+message;
        String responseString = serverHttpResponse.getResponse(url);
        System.out.println(responseString);
        // endregion

        if(responseString==null)
        {
            Toast.makeText(History.this, "好像断网了，请检查您的网络设置", Toast.LENGTH_SHORT).show();
//            guideInfoTextView.setText("好像断网了，请检查您的网络设置");
//            guideInfoTextView.setVisibility(View.VISIBLE);
//            listView.setVisibility(View.GONE);
            String userName = userInfo.getString("username","");
            KEntityRepository kEntityRepository =new KEntityRepository(AppDB.getAppDB(History.this, userName));
            List<KEntity> kEntityList = kEntityRepository.getAllKEntities();

            class Entity
            {
                Entity(String _name,String _type, String _uri,String _time,Long _id, String _course)
                {
                    name=_name;
                    type=_type;
                    uri=_uri;
                    time=_time;
                    id=_id;
                    course = _course;
                }
                String name;
                String type;
                String uri;
                String time;
                String course;
                Long id;
            }

            ArrayList<Entity> entityList=new ArrayList<>();
            for(int i=0;i<kEntityList.size();i++)
            {
                KEntity kEntity=kEntityList.get(i);
                String name =kEntity.getLabel();
                String type =kEntity.getType();
                String uri = kEntity.getKEntityUri();
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(kEntity.getCreatedate());
                //TODO:日期的格式
                String course = kEntity.getCourse();
                Long id = 0L;//TODO:已经弄好了吗？
                entityList.add(new Entity(name,type,uri,time,id, course));
            }

            entityList.sort( new Comparator<Entity>(){
                @Override
                public int compare(Entity x , Entity y )
                {
                    if ( x.time== null || y.time== null ) return 0;
                    return y.time.compareTo(x.time); //这是顺序
                }
            });

            class HistoryListAdapter extends BaseAdapter
            {
                class ViewHolder {
                    TextView itemTextView;
                    CheckBox itemCheckBox;
                }

                private Context context;
                private LayoutInflater inflater = null;

                public int getCount() {
                    return entityList.size();
                }

                public Object getItem(int position) {
                    return entityList.get(position);
                }

                public long getItemId(int position) {
                    return position;
                }

                public View getView(final int position, View convertView, ViewGroup parent)
                {
                    final ViewHolder holder;
                    if (convertView == null)
                    {
                        convertView = View.inflate(History.this, R.layout.history_list_item, null);
                        holder = new ViewHolder();
                        holder.itemTextView = (TextView) convertView.findViewById(R.id.history_list_item_text);
                        holder.itemCheckBox = (CheckBox) convertView.findViewById(R.id.history_list_item_checkbox);
                        convertView.setTag(holder);
                    }
                    else
                    {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    Entity entity=entityList.get(position);
                    final String content = "实体类型："+entity.type + "\n实体名称："+entity.name
                            + "\n浏览时间："+entity.time.substring(0,entity.time.length()-3);
                    holder.itemTextView.setText(content);
                    holder.itemCheckBox.setChecked(false);
                    if (selectedIdList.contains(position))    holder.itemCheckBox.setChecked(true);
                    if (isDeleteMode)     holder.itemCheckBox.setVisibility(View.VISIBLE);
                    else    holder.itemCheckBox.setVisibility(View.INVISIBLE);

                    holder.itemCheckBox.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (holder.itemCheckBox.isChecked())
                            {
                                selectedIdList.add(position);
                                Log.d("tag","添加"+position);
                            }
                            else
                            {
                                selectedIdList.remove((Integer)position);
                            }
                            Log.d("tag",position+"被点击");
                        }
                    });
                    return convertView;
                }
            }

            HistoryListAdapter adapter = new HistoryListAdapter();
            listView.setAdapter((ListAdapter) adapter);
            // region 每个list item的点击事件
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent goToEntityDetailsPage = new Intent(History.this,EntityDetails.class);
                    System.out.println("Very important" + entityList.get(position).course);
                    goToEntityDetailsPage.putExtra("entity_name",entityList.get(position).name);
                    goToEntityDetailsPage.putExtra("type",entityList.get(position).type);
                    goToEntityDetailsPage.putExtra("uri",entityList.get(position).uri);
                    goToEntityDetailsPage.putExtra("course",entityList.get(position).course);
                    System.out.println("真正历史记录："+entityList.get(position).name+" "+entityList.get(position).uri);
                    startActivity(goToEntityDetailsPage);
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
            {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                {
                    isDeleteMode = true;
                    selectedIdList.clear();
                    adapter.notifyDataSetChanged();
                    buttonGroup.setVisibility(View.VISIBLE);
                    buttonTextGroup.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            // endregion

            // region 删除按钮
            ImageView deleteButton=(ImageView)findViewById(R.id.history_page_delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(selectedIdList.size()==0)
                    {
                        Toast.makeText(History.this,"您还没有选择历史记录哦",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        AlertDialog.Builder dialog=new AlertDialog.Builder(History.this);
                        dialog.setTitle("删除历史记录");//设置标题
                        dialog.setMessage("确定要删除选中的"+selectedIdList.size()+"项历史记录吗？");//设置信息具体内容
                        dialog.setCancelable(false);//设置是否可取消
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override//确认退出登录
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                for (int j = 0; j < selectedIdList.size(); j++)
                                {
                                    String url = History.this.getString(R.string.backend_ip) + "/request/deleteFromHistory";
                                    SharedPreferences userInfo= History.this.getSharedPreferences("user", 0);
                                    String userToken = userInfo.getString("token","");
                                    ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                                    String message="token="+userToken+"&id="+entityList.get(selectedIdList.get(j)).id;
                                    String responseString = serverHttpResponse.postResponse(url,message);
                                    System.out.println(responseString);
                                }
                                isDeleteMode = false;
                                selectedIdList.clear();
                                adapter.notifyDataSetChanged();
                                buttonGroup.setVisibility(View.GONE);
                                buttonTextGroup.setVisibility(View.GONE);
                                onStart();
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //取消删除，就什么也没有发生
                            }
                        });
                        dialog.show();
                    }

                }
            });

            TextView deleteButtonText=(TextView)findViewById(R.id.history_page_delete_button_text);
            deleteButtonText.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(selectedIdList.size()==0)
                    {
                        Toast.makeText(History.this,"您还没有选择历史记录哦",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        AlertDialog.Builder dialog=new AlertDialog.Builder(History.this);
                        dialog.setTitle("删除历史记录");//设置标题
                        dialog.setMessage("确定要删除选中的"+selectedIdList.size()+"项历史记录吗？");//设置信息具体内容
                        dialog.setCancelable(false);//设置是否可取消
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override//确认退出登录
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                for (int j = 0; j < selectedIdList.size(); j++)
                                {
                                    String url = History.this.getString(R.string.backend_ip) + "/request/deleteFromHistory";
                                    SharedPreferences userInfo= History.this.getSharedPreferences("user", 0);
                                    String userToken = userInfo.getString("token","");
                                    ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                                    String message="token="+userToken+"&id="+entityList.get(selectedIdList.get(j)).id;
                                    String responseString = serverHttpResponse.postResponse(url,message);
                                    System.out.println(responseString);
                                }
                                isDeleteMode = false;
                                selectedIdList.clear();
                                adapter.notifyDataSetChanged();
                                buttonGroup.setVisibility(View.GONE);
                                buttonTextGroup.setVisibility(View.GONE);
                                onStart();
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //取消删除，就什么也没有发生
                            }
                        });
                        dialog.show();
                    }
                }
            });
            // endregion

            // region 取消删除按钮
            ImageView cancelButton=(ImageView)findViewById(R.id.history_page_cancel_button);
            cancelButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    isDeleteMode = false;
                    selectedIdList.clear();
                    adapter.notifyDataSetChanged();
                    buttonGroup.setVisibility(View.GONE);
                    buttonTextGroup.setVisibility(View.GONE);
                }
            });

            TextView cancelButtonText=(TextView)findViewById(R.id.history_page_cancel_button_text);
            cancelButtonText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isDeleteMode = false;
                    selectedIdList.clear();
                    adapter.notifyDataSetChanged();
                    buttonGroup.setVisibility(View.GONE);
                    buttonTextGroup.setVisibility(View.GONE);
                }
            });
            // endregion

            // region 全选按钮
            ImageView checkAllButton=(ImageView)findViewById(R.id.history_page_check_all_button);
            checkAllButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(!allChecked)
                    {
                        allChecked=true;
                        selectedIdList.clear();
                        for(int i=0;i<entityList.size();i++)
                        {
                            selectedIdList.add(i);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        allChecked=false;
                        selectedIdList.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            TextView checkAllButtonText=(TextView)findViewById(R.id.history_page_check_all_button_text);
            checkAllButtonText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!allChecked)
                    {
                        allChecked=true;
                        selectedIdList.clear();
                        for(int i=0;i<entityList.size();i++)
                        {
                            selectedIdList.add(i);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        allChecked=false;
                        selectedIdList.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
            });
            // endregion


        }
        else if(responseString.equals("[]"))
        {
            guideInfoTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else
        {
            try
            {
                JSONArray collectionArray = new JSONArray(responseString);
                System.out.println(collectionArray.get(0));

                class Entity
                {
                    Entity(String _name,String _type, String _uri,String _time,Long _id, String _course)
                    {
                        name=_name;
                        type=_type;
                        uri=_uri;
                        time=_time;
                        id=_id;
                        course = _course;
                    }
                    String name;
                    String type;
                    String uri;
                    String time;
                    String course;
                    Long id;
                }

                ArrayList<Entity> entityList=new ArrayList<>();
                for(int i=0;i<collectionArray.length();i++)
                {
                    JSONObject entity_json=(JSONObject)collectionArray.get(i);
                    String name =entity_json.get("name").toString();
                    String type =entity_json.get("type").toString();
                    String uri = entity_json.get("uri").toString();
                    String time = entity_json.get("time").toString();
                    String course = entity_json.get("course").toString();
                    Long id = Long.parseLong(entity_json.get("id").toString());
                    entityList.add(new Entity(name,type,uri,time,id, course));
                }
                //TODO:给entityList按时间顺序排序
                entityList.sort( new Comparator<Entity>(){
                    @Override
                    public int compare(Entity x , Entity y )
                    {
                        if ( x.time== null || y.time== null ) return 0;
                        return y.time.compareTo(x.time); //这是顺序
                    }
                });

                class HistoryListAdapter extends BaseAdapter
                {
                    class ViewHolder {
                        TextView itemTextView;
                        CheckBox itemCheckBox;
                    }

                    private Context context;
                    private LayoutInflater inflater = null;

                    public int getCount() {
                        return entityList.size();
                    }

                    public Object getItem(int position) {
                        return entityList.get(position);
                    }

                    public long getItemId(int position) {
                        return position;
                    }

                    public View getView(final int position, View convertView, ViewGroup parent)
                    {
                        final ViewHolder holder;
                        if (convertView == null)
                        {
                            convertView = View.inflate(History.this, R.layout.history_list_item, null);
                            holder = new ViewHolder();
                            holder.itemTextView = (TextView) convertView.findViewById(R.id.history_list_item_text);
                            holder.itemCheckBox = (CheckBox) convertView.findViewById(R.id.history_list_item_checkbox);
                            convertView.setTag(holder);
                        }
                        else
                        {
                            holder = (ViewHolder) convertView.getTag();
                        }
                        Entity entity=entityList.get(position);
                        final String content = "实体类型："+entity.type + "\n实体名称："+entity.name
                                + "\n浏览时间："+entity.time.substring(0,entity.time.length()-3);
                        holder.itemTextView.setText(content);
                        holder.itemCheckBox.setChecked(false);
                        if (selectedIdList.contains(position))    holder.itemCheckBox.setChecked(true);
                        if (isDeleteMode)     holder.itemCheckBox.setVisibility(View.VISIBLE);
                        else    holder.itemCheckBox.setVisibility(View.INVISIBLE);

                        holder.itemCheckBox.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (holder.itemCheckBox.isChecked())
                                {
                                    selectedIdList.add(position);
                                    Log.d("tag","添加"+position);
                                }
                                else
                                {
                                    selectedIdList.remove((Integer)position);
                                }
                                Log.d("tag",position+"被点击");
                            }
                        });
                        return convertView;
                    }
                }


                HistoryListAdapter adapter = new HistoryListAdapter();
                listView.setAdapter((ListAdapter) adapter);
                // region 每个list item的点击事件
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Intent goToEntityDetailsPage = new Intent(History.this,EntityDetails.class);
                        System.out.println("Very important" + entityList.get(position).course);
                        goToEntityDetailsPage.putExtra("entity_name",entityList.get(position).name);
                        goToEntityDetailsPage.putExtra("type",entityList.get(position).type);
                        goToEntityDetailsPage.putExtra("uri",entityList.get(position).uri);
                        goToEntityDetailsPage.putExtra("course",entityList.get(position).course);
                        System.out.println("真正历史记录："+entityList.get(position).name+" "+entityList.get(position).uri);
                        startActivity(goToEntityDetailsPage);
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
                {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        isDeleteMode = true;
                        selectedIdList.clear();
                        adapter.notifyDataSetChanged();
                        buttonGroup.setVisibility(View.VISIBLE);
                        buttonTextGroup.setVisibility(View.VISIBLE);
                        return true;
                    }
                });
                // endregion

                // region 删除按钮
                ImageView deleteButton=(ImageView)findViewById(R.id.history_page_delete_button);
                deleteButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(selectedIdList.size()==0)
                        {
                            Toast.makeText(History.this,"您还没有选择历史记录哦",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            AlertDialog.Builder dialog=new AlertDialog.Builder(History.this);
                            dialog.setTitle("删除历史记录");//设置标题
                            dialog.setMessage("确定要删除选中的"+selectedIdList.size()+"项历史记录吗？");//设置信息具体内容
                            dialog.setCancelable(false);//设置是否可取消
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override//确认退出登录
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    for (int j = 0; j < selectedIdList.size(); j++)
                                    {
                                        String url = History.this.getString(R.string.backend_ip) + "/request/deleteFromHistory";
                                        SharedPreferences userInfo= History.this.getSharedPreferences("user", 0);
                                        String userToken = userInfo.getString("token","");
                                        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                                        String message="token="+userToken+"&id="+entityList.get(selectedIdList.get(j)).id;
                                        String responseString = serverHttpResponse.postResponse(url,message);
                                        System.out.println(responseString);
                                    }
                                    isDeleteMode = false;
                                    selectedIdList.clear();
                                    adapter.notifyDataSetChanged();
                                    buttonGroup.setVisibility(View.GONE);
                                    buttonTextGroup.setVisibility(View.GONE);
                                    onStart();
                                }
                            });
                            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //取消删除，就什么也没有发生
                                }
                            });
                            dialog.show();
                        }

                    }
                });

                TextView deleteButtonText=(TextView)findViewById(R.id.history_page_delete_button_text);
                deleteButtonText.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(selectedIdList.size()==0)
                        {
                            Toast.makeText(History.this,"您还没有选择历史记录哦",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            AlertDialog.Builder dialog=new AlertDialog.Builder(History.this);
                            dialog.setTitle("删除历史记录");//设置标题
                            dialog.setMessage("确定要删除选中的"+selectedIdList.size()+"项历史记录吗？");//设置信息具体内容
                            dialog.setCancelable(false);//设置是否可取消
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override//确认退出登录
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    for (int j = 0; j < selectedIdList.size(); j++)
                                    {
                                        String url = History.this.getString(R.string.backend_ip) + "/request/deleteFromHistory";
                                        SharedPreferences userInfo= History.this.getSharedPreferences("user", 0);
                                        String userToken = userInfo.getString("token","");
                                        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                                        String message="token="+userToken+"&id="+entityList.get(selectedIdList.get(j)).id;
                                        String responseString = serverHttpResponse.postResponse(url,message);
                                        System.out.println(responseString);
                                    }
                                    isDeleteMode = false;
                                    selectedIdList.clear();
                                    adapter.notifyDataSetChanged();
                                    buttonGroup.setVisibility(View.GONE);
                                    buttonTextGroup.setVisibility(View.GONE);
                                    onStart();
                                }
                            });
                            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //取消删除，就什么也没有发生
                                }
                            });
                            dialog.show();
                        }
                    }
                });
                // endregion

                // region 取消删除按钮
                ImageView cancelButton=(ImageView)findViewById(R.id.history_page_cancel_button);
                cancelButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        isDeleteMode = false;
                        selectedIdList.clear();
                        adapter.notifyDataSetChanged();
                        buttonGroup.setVisibility(View.GONE);
                        buttonTextGroup.setVisibility(View.GONE);
                    }
                });

                TextView cancelButtonText=(TextView)findViewById(R.id.history_page_cancel_button_text);
                cancelButtonText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isDeleteMode = false;
                        selectedIdList.clear();
                        adapter.notifyDataSetChanged();
                        buttonGroup.setVisibility(View.GONE);
                        buttonTextGroup.setVisibility(View.GONE);
                    }
                });
                // endregion

                // region 全选按钮
                ImageView checkAllButton=(ImageView)findViewById(R.id.history_page_check_all_button);
                checkAllButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(!allChecked)
                        {
                            allChecked=true;
                            selectedIdList.clear();
                            for(int i=0;i<entityList.size();i++)
                            {
                                selectedIdList.add(i);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            allChecked=false;
                            selectedIdList.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

                TextView checkAllButtonText=(TextView)findViewById(R.id.history_page_check_all_button_text);
                checkAllButtonText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!allChecked)
                        {
                            allChecked=true;
                            selectedIdList.clear();
                            for(int i=0;i<entityList.size();i++)
                            {
                                selectedIdList.add(i);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            allChecked=false;
                            selectedIdList.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                // endregion

            }
            catch (JSONException e)
            {
                guideInfoTextView.setText("抱歉，服务器好像出了点问题，请稍后重试");
                guideInfoTextView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }

        // endregion

    }
}
