package com.example.renyanyu;

import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ResultReportActivity extends FragmentActivity
{

    LocalBroadcastManager mLocalBroadcastManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result_report);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        GridView gv = (GridView) findViewById(R.id.report_gv);
        TextView tv_report_total_question = (TextView) findViewById(R.id.tv_report_total_question);
        RelativeLayout rl_result_panel = (RelativeLayout) findViewById(R.id.rl_result_panel);
        //设置scrollview 自动置顶
        rl_result_panel.setFocusable(true);
        rl_result_panel.setFocusableInTouchMode(true);
        rl_result_panel.requestFocus();

        int correctAnswerCount=0;
        for(int i=0;i<Quiz.questionlist.size();i++)
        {
            System.out.println(Quiz.userAnswerList.get(i)+Quiz.questionlist.get(i).answer);
            if(Quiz.userAnswerList.get(i).equals(Quiz.questionlist.get(i).answer))
            {
                correctAnswerCount++;
            }
        }

        TextView timeCostTextView = findViewById(R.id.result_report_page_time_cost_text);
        timeCostTextView.setText("答题用时： "+Quiz.minute+" min "+Quiz.second+" s ");


        TextView scoreTextView = findViewById(R.id.result_report_page_score_text);
        double score=((double)correctAnswerCount)/((double) Quiz.questionlist.size())*100;
        BigDecimal temp = new BigDecimal(score);
        temp = temp.setScale(2, RoundingMode.HALF_UP);
        scoreTextView.setText("分数："+temp.toString());

        TextView correctAnswerCountTextView = findViewById(R.id.result_report_page_correct_answer_count_text);
        correctAnswerCountTextView.setText(((Integer)correctAnswerCount).toString());

        TextView backTextView = findViewById(R.id.result_report_page_back_text);
        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                ResultReportActivity.this.getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.my_info_fragment, new BlankFragment3(), null)
//                        .addToBackStack(null)
//                        .commit();
            }
        });

        tv_report_total_question.setText("道/"+Quiz.questionlist.size()+"道");
        MyAdapter adapter = new MyAdapter(this);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO跳转到相应的viewpager 页面
                Intent intent = new Intent("goToPageI");
                intent.putExtra("index", position);
                mLocalBroadcastManager.sendBroadcast(intent);
            }
        });

    }


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
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(mContext);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new GridView.LayoutParams(100, 100));
            tv.setPadding(8, 8, 8, 8);

            tv.setText(((Integer)(position+1)).toString());
            if(Quiz.userAnswerList.get(position).equals(""))
            {
                tv.setBackgroundResource(R.drawable.option_btn_single_blank);
            }
            else if(Quiz.userAnswerList.get(position).equals(Quiz.questionlist.get(position).answer))
            {
                tv.setBackgroundResource(R.drawable.option_btn_single_right);
            }
            else if(!Quiz.userAnswerList.get(position).equals(Quiz.questionlist.get(position).answer))
            {
                tv.setBackgroundResource(R.drawable.option_btn_single_wrong);
            }
            return tv;
        }

    }

}

