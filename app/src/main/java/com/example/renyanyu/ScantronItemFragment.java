package com.example.renyanyu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ScantronItemFragment extends Fragment {
    LocalBroadcastManager mLocalBroadcastManager;
    public ScantronItemFragment() {

    }

//    int count = Quiz.questionlist.size();
//    int[] mIds = new int[count];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
//        initData();
        System.out.println("IN???");
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        View rootView = inflater.inflate(R.layout.pager_item_scantron, container, false);
//        NoScrollGridView gv = (NoScrollGridView) rootView.findViewById(R.id.gridview);
        GridView gv = (GridView) rootView.findViewById(R.id.gridview);
        TextView tv_submit_result = (TextView) rootView.findViewById(R.id.tv_submit_result);
        tv_submit_result.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ResultReportActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        MyAdapter adapter = new MyAdapter(getActivity());
        gv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent("goToPageI");
                intent.putExtra("index", position);
                mLocalBroadcastManager.sendBroadcast(intent);
            }
        });
        return rootView;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
//        View rootView = getView();//inflater.inflate(R.layout.pager_item_scantron, container, false);
////        NoScrollGridView gv = (NoScrollGridView) rootView.findViewById(R.id.gridview);
//        GridView gv = (GridView) rootView.findViewById(R.id.gridview);
//        TextView tv_submit_result = (TextView) rootView.findViewById(R.id.tv_submit_result);
//        tv_submit_result.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),ResultReportActivity.class);
//                startActivity(intent);
//            }
//        });
//        MyAdapter adapter = new MyAdapter(getActivity());
//        gv.setAdapter(adapter);
////        adapter.getView(10);
//
//        adapter.notifyDataSetChanged();
//        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent("goToPageI");
//                intent.putExtra("index", position);
//                mLocalBroadcastManager.sendBroadcast(intent);
//            }
//        });
//
//    }

    //    private void initData() {
//        for (int i = 0; i < count; i++) {
//            mIds[i] = i + 1;
//        }
//    }

    private class MyAdapter extends BaseAdapter {
        private Context mContext;

        public MyAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return Quiz.questionlist.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(mContext);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new GridView.LayoutParams(100, 100));
            tv.setPadding(8, 8, 8, 8);
            tv.setText(((Integer)(position+1)).toString());
            if(position==9) System.out.println("HERE!!!!");
            if(Quiz.userAnswerList.get(position)!="")
            {
                tv.setBackgroundResource(R.drawable.option_btn_single_checked);
            }
            else
            {
                tv.setBackgroundResource(R.drawable.option_btn_single_normal);
            }
            return tv;
        }

    }

}