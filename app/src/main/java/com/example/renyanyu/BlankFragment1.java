package com.example.renyanyu;

import android.app.Activity;
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
import java.util.*;
import java.util.Map;
import android.widget.*;

//import com.example.javeduhw.databinding.FragmentFirstBinding;

public class BlankFragment1 extends Fragment {

    public BlankFragment1() {
        // Required empty public constructor
    }

    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    Button channel_change;
    SearchView search;
    ImageView qa;
    private ListView mLvMsgList;
    private List<Message> mDatas = new ArrayList<>();
    private MessageAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Toast.makeText(getContext(),"点击了：",Toast.LENGTH_LONG).show();

        View view = inflater.inflate(R.layout.fragment_blank_fragment1, container, false);
        qa=(ImageView)view.findViewById(R.id.qa);
        search=view.findViewById(R.id.searc);
        search.setIconifiedByDefault(true);
        //显示搜索按钮
        search.setSubmitButtonEnabled(true);
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
        channel_change=(Button)view.findViewById(R.id.channel);
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
        mLvMsgList = view.findViewById(R.id.listview_MsgList);

        /**
         * 多调用两次，数据会更多
         */
        if(mDatas.size()==0)
            mDatas.addAll(MessageLab.generateMockList());

        mAdapter=new MessageAdapter(getActivity(),getContext(),mDatas);

        mLvMsgList.setAdapter(mAdapter);
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1&&requestCode==0){
            //Toast.makeText(getContext(), "!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getActivity(),MainActivity.class);
            startActivity(i);
        }
    }

    public class MessageAdapter extends BaseAdapter {

        private Context mContext;//上下文环境
        private Activity activity;
        /**
         * 主要用于加载item_msg的布局
         */
        private LayoutInflater mInflater;
        private List<Message> mDatas;

        /**
         * 构造方法
         */
        public MessageAdapter(Activity ac,Context context, List<Message> datas) {

            /**
             * 赋值
             */
            activity=ac;
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mDatas = datas;
        }


        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;

            if (convertView == null){

                convertView=mInflater.inflate(R.layout.item_card,parent,false);

                viewHolder=new ViewHolder();

                /**
                 * 获取子布局中三个控件：ImageView TextView TextView
                 */
                viewHolder.mIvImg=convertView.findViewById(R.id.imageview_Image);
                viewHolder.mTvTitle=convertView.findViewById(R.id.textview_title);
                viewHolder.mTvContent=convertView.findViewById(R.id.textview_content);

                convertView.setTag(viewHolder);
            }

            else {

                viewHolder= (ViewHolder) convertView.getTag();
            }

            Message message=mDatas.get(position);
            viewHolder.mIvImg.setImageResource(message.getImgResId());
            viewHolder.mTvTitle.setText(message.getTitle());
            viewHolder.mTvContent.setText(message.getContent());
            viewHolder.mIvImg.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(position==0){
                        Intent intent=new Intent(activity,QA.class);
                        startActivity(intent);
                    }
                    if(position==1){

                    }
                    if(position==2){
                        getActivity().getSupportFragmentManager().
                                beginTransaction().replace(R.id.fram_con,new BlankFragment2(),null).
                                addToBackStack(null).commit();
                    }
                }
            });
            viewHolder.mTvTitle.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(position==0){
                        Intent intent=new Intent(activity,QA.class);
                        startActivity(intent);
                    }
                    if(position==1){

                    }
                    if(position==2){
                        MainActivity  mainActivity = (MainActivity) getActivity();
                        mainActivity. gotosubject ();
                    }
                }
            });
            viewHolder.mTvContent.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(position==0){
                        Intent intent=new Intent(activity,QA.class);
                        startActivity(intent);
                    }
                    if(position==1){

                    }
                    if(position==2){
                        MainActivity  mainActivity = (MainActivity) getActivity();
                        mainActivity. gotosubject ();
                    }
                }
            });

            return convertView;
        }

        /**
         * 内部类：可省去findViewById的时间
         */
        public  class ViewHolder {

            ImageView mIvImg;
            TextView mTvTitle;
            TextView mTvContent;

        }
    }
}
