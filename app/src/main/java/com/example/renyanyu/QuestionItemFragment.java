package com.example.renyanyu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class QuestionItemFragment extends Fragment {
    Quiz.QuestionBean questionBean;
    int index ;
    private Quiz.OptionsListAdapter adapter;
    private StringBuffer sb;
    private NoScrollListview lv;
    LocalBroadcastManager mLocalBroadcastManager;

    public QuestionItemFragment(int index){
        this.index = index;
        questionBean = Quiz.questionlist.get(index);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        View rootView = inflater.inflate(R.layout.pager_item,
                container, false);
        lv = (NoScrollListview) rootView.findViewById(R.id.lv_options);
        TextView tv_paper_name = (TextView) rootView.findViewById(R.id.tv_paper_name);
        TextView tv_sequence = (TextView) rootView.findViewById(R.id.tv_sequence);
        TextView tv_total_count = (TextView) rootView.findViewById(R.id.tv_total_count);
        TextView tv_description = (TextView) rootView.findViewById(R.id.tv_description);
        Button goToPrevQuestionButton = (Button) rootView.findViewById(R.id.quiz_page_go_to_prev_question_button);
        Button goToNextQuestionButton = (Button) rootView.findViewById(R.id.quiz_page_go_to_next_question_button);
        if(index==0)    goToPrevQuestionButton.setVisibility(View.GONE);
        if(index==Quiz.questionlist.size())    goToNextQuestionButton.setVisibility(View.GONE);

        adapter = new Quiz.OptionsListAdapter(getActivity(), questionBean.options,lv,index);
        lv.setAdapter(adapter);
        //TODO 展开listvie所有子条目使用了自定义Listview，下面的方法有问题
        //setListViewHeightBasedOnChildren(lv);


        tv_paper_name.setText("专项智能练习(言语理解与表达)");
        tv_sequence.setText((index+1)+"");
        tv_total_count.setText("/"+ Quiz.questionlist.size());

        tv_description.setText(questionBean.stem);

        //题干描述前面加上(单选题)或(多选题)
        //判断是单选还是多选
//        int questionType = questionBean.getQuestionType();
        sb = new StringBuffer();

        SpannableStringBuilder ssb = new SpannableStringBuilder("(单选题)");
        ssb.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(questionBean.stem);
        tv_description.setText(ssb);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                adapter.notifyDataSetChanged();
                //TODO 单选题选中后自动跳转到下一页
                Intent intent = new Intent("");
                mLocalBroadcastManager.sendBroadcast(intent);

            }
        });

        goToPrevQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mLocalBroadcastManager.sendBroadcast(new Intent("goToPrevQuestion"));
            }
        });
        goToNextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mLocalBroadcastManager.sendBroadcast(new Intent("goToNextQuestion"));
            }
        });

//        if(questionType == 1){//单选
//
//        }
//        else if(questionType == 2){//多选
//            SpannableStringBuilder ssb = new SpannableStringBuilder("(多选题)");
//            ssb.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ssb.append(questionBean.getDescription());
//            tv_description.setText(ssb);
//            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//
//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View view, int position,
//                                        long id) {
//                    adapter.notifyDataSetChanged();
//                }
//            });
//            btn_submit.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    long[] ids = lv.getCheckedItemIds();
//                    for (int i = 0; i < ids.length; i++) {
//                        long id = ids[i];
//                        sb.append(questionBean.getQuestionOptions().get((int)id).getName()).append(" ");
//                    }
//                    Toast.makeText(getActivity(), "选中的选项为"+sb.toString(), Toast.LENGTH_SHORT).show();
//                    sb.setLength(0);
//                }
//            });
//        }

        return rootView;
    }
    
}
