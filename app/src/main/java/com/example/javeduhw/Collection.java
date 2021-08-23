//收藏页面
package com.example.javeduhw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Collection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ListView lv=(ListView)findViewById(R.id.lv_expense);
        //View v = inflater.inflate(R.layout.activity_collection, container, false);
//        List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>(); //存储数据的数组列表
//                //写死的数据，用于测试
//        String[] expense_category = new String[] {"发工资", "买衣服"};
//        String[] expense_money = new String[] {"30000.00", "1500.00"};
//        for (int i = 0; i < 2; i++)
//        {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("expense_category", expense_category[i]);
//            map.put("expense_money", expense_money[i]);
//            listitem.add(map);
//        }
//
//        SimpleAdapter adapter = new SimpleAdapter(Collection.this
//                , listitem
//                , R.layout.collection_item
//                , new String[]{"expense_category", "expense_money", "image_expense"}
//                , new int[]{R.id.tv_expense_category, R.id.tv_expense_money, R.id.image_expense});
        // 第一个参数是上下文对象
        // 第二个是listitem
        // 第三个是指定每个列表项的布局文件
        // 第四个是指定Map对象中定义的两个键（这里通过字符串数组来指定）
        // 第五个是用于指定在布局文件中定义的id（也是用数组来指定）

//        ListView listView = (ListView) v.findViewById(R.id.lv_expense);
//        listView.setAdapter(adapter);
    }
}





///** A simple single line list item. */
//public class Collection extends ViewHolder {
//
//    public final ImageView icon;
//    public final TextView text;
//
//    public Collection(@NonNull View view) {
//        super(view);
////        this.icon = itemView.findViewById(R.id.icon_);
////        this.text = itemView.findViewById(R.id.mtrl_list_item_text);
//    }
//
//    @NonNull
//    public static Collection create(@NonNull ViewGroup parent) {
//        return new Collection(LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.material_list_item_single_line, parent, false));
//    }
//}

//package com.hyl.listviewdemo;

//import com.hyl.dao.DBOpenHelper;

//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
////import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

/**
 * @programName: OneFragment.java
 * @programFunction:
 * @createDate: 2018/09/25
 * @author: AnneHan
 * @version:
 * xx.   yyyy/mm/dd   ver    author    comments
 * 01.   2018/09/25   1.00   AnneHan   New Create
 */

//public class  {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_collection);
//    }
//}

//public class Collection extends AppCompatActivity
//{
//
//    List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>(); //存储数据的数组列表
//
//    public Collection() {
//        // Required empty public constructor
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_collection);
//
//        //写死的数据，用于测试
//        String[] expense_category = new String[] {"发工资", "买衣服"};
//        String[] expense_money = new String[] {"30000.00", "1500.00"};
//        for (int i = 0; i < 2; i++)
//        {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("expense_category", expense_category[i]);
//            map.put("expense_money", expense_money[i]);
//            listitem.add(map);
//        }
//        //getData(); //query data from a database
//
////        SimpleAdapter adapter = new SimpleAdapter(getActivity()
////                , listitem
////                , R.layout.fragment_one_item
////                , new String[]{"expense_category", "expense_money", "image_expense"}
////                , new int[]{R.id.tv_expense_category, R.id.tv_expense_money, R.id.image_expense});
////        // 第一个参数是上下文对象
////        // 第二个是listitem
////        // 第三个是指定每个列表项的布局文件
////        // 第四个是指定Map对象中定义的两个键（这里通过字符串数组来指定）
////        // 第五个是用于指定在布局文件中定义的id（也是用数组来指定）
////
////        ListView listView = (ListView) v.findViewById(R.id.lv_expense);
////        listView.setAdapter(adapter);
////
////        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置监听器
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
////                //在点击某笔明细的时候，Tip出明细内容
////                Toast.makeText(getActivity(), map.get("expense_category").toString(), Toast.LENGTH_LONG).show();
////            }
////        });
////
////        return v;
//    }
//
//}