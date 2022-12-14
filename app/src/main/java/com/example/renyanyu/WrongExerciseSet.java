package com.example.renyanyu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WrongExerciseSet extends AppCompatActivity
{
    ArrayList<Quiz.QuestionBean> questionList = new ArrayList<>();
    Integer currentPageNumber = 0;
    Integer totalPageNumber = 0;

    class QuestionListAdapter extends BaseAdapter
    {
        private Context mContext;
        ListView questionListView ;

        public QuestionListAdapter(Context context, ArrayList<Quiz.QuestionBean> _questionList, ListView questionListView)
        {
            this.mContext = context;
            questionList = _questionList;
            this.questionListView = questionListView;
        }

        public int getCount() {
            return questionList.size();
        }

        class MyOptionsListAdapter extends BaseAdapter
        {
            private Context mContext;
            ListView lv ;
            int correctAnswer;//,int index
            public List<Quiz.QuestionOptionBean> options ;

            public MyOptionsListAdapter(Context context, List<Quiz.QuestionOptionBean> options,
                                        ListView lv,int correctAnswer)
            {
                this.mContext = context;
                this.options = options;
                this.lv = lv;
                this.correctAnswer=correctAnswer;
            }

            public int getCount() {
                return options.size();
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int position) {

                return false;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            public Object getItem(int position) {
                return position;
            }

            public long getItemId(int position) {
                return position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_option, null);
                TextView ctv = (TextView) view.findViewById(R.id.ctv_name);
                TextView option = (TextView) view.findViewById(R.id.tv_option);

                ctv.setText(options.get(position).getName());
                option.setText(options.get(position).getDescription());
                updateBackground(position, ctv);
                return view;

            }

            public void updateBackground(int position, View view)
            {
                int backgroundId;
                if(position==correctAnswer)    backgroundId = R.drawable.option_btn_single_right;
                else    backgroundId = R.drawable.option_btn_single_normal;
                Drawable background = mContext.getResources().getDrawable(backgroundId);
                view.setBackgroundDrawable(background) ;
            }

        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            Quiz.QuestionBean questionBean = questionList.get(position);

            View view = LayoutInflater.from(mContext).inflate(R.layout.question_item, null);
            TextView questionStemTextView = view.findViewById(R.id.question_stem_text);

            HashMap<String,Integer> letterToNumber=new HashMap<String, Integer>();
            letterToNumber.put("A",0);
            letterToNumber.put("B",1);
            letterToNumber.put("C",2);
            letterToNumber.put("D",3);

            NoScrollListview optionsNoScrollListview = view.findViewById(R.id.question_options_list_view);
            MyOptionsListAdapter optionsListAdapter = new MyOptionsListAdapter(WrongExerciseSet.this,
                    questionBean.options,optionsNoScrollListview,letterToNumber.get(questionBean.answer));
            optionsNoScrollListview.setAdapter(optionsListAdapter);
//            tv.setBackgroundResource(R.drawable.option_btn_single_blank);

//            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_option, null);
//            TextView ctv = (TextView) view.findViewById(R.id.ctv_name);
//            TextView option = (TextView) view.findViewById(R.id.tv_option);

//            ctv.setText(options.get(position).getName());

//            option.setText(options.get(position).getDescription());
//            updateBackground(position, ctv);
//            return view;
//
//            public void updateBackground(int position, View view) {
//            int backgroundId;
//            if (lv.isItemChecked(position )) {
//                backgroundId = R.drawable.option_btn_single_checked;
//            } else {
//                backgroundId = R.drawable.option_btn_single_normal;
//            }
//            Drawable background = mContext.getResources().getDrawable(backgroundId);
//            view.setBackgroundDrawable(background) ;
//        }

            optionsNoScrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    String[] letters={"A","B","C","D"};
//                    Quiz.userAnswerList.set(index,letters[position]);
                    System.out.println(position+letters[position]);
                    optionsListAdapter.notifyDataSetChanged();
                }
            });
            questionStemTextView.setText(questionBean.stem);
            return view;
//            questionListView.setAdapter(adapter);
//            //???????????????
//            MyAdapter adapter=new MyAdapter(this);
//            adapter.setList(list);
//            //???????????????ListView??????
//            listview.setAdapter(adapter);
//
//            OptionsListAdapter(Context context, List< Quiz.QuestionOptionBean > options,ListView lv,int index)

//            TextView stemTextView = (TextView) view.findViewById(R.id.question_stem_text);
//            ListView optionsListView = (ListView) view.findViewById(R.id.question_options_list_view);
//            TextView tv_paper_name = (TextView) rootView.findViewById(R.id.tv_paper_name); ???????????????
//            TextView tv_sequence = (TextView) view.findViewById(R.id.tv_sequence); ????????????
//            TextView tv_total_count = (TextView) rootView.findViewById(R.id.tv_total_count);


//            QuestionAdapter adapter = new QuestionAdapter(WrongExerciseSet.this, questions, questionListView, index);
//            lv.setAdapter(adapter);

//            tv_paper_name.setText("???????????????????????????");
//            tv_sequence.setText((index+1)+"");
//            tv_total_count.setText("/"+ Quiz.questionlist.size());
//            .setText(questionBean.stem);
//
//            //????????????????????????(?????????)???(?????????)
//            sb = new StringBuffer();
//            SpannableStringBuilder ssb = new SpannableStringBuilder("(?????????)");
//            ssb.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ssb.append(questionBean.stem);
//            tv_description.setText(ssb);
//            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
//            {
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//                {
//                    String[] letters={"A","B","C","D"};
//                    Quiz.userAnswerList.set(index,letters[position]);
//                    System.out.println(index+letters[position]);
//                    adapter.notifyDataSetChanged();
//                }
//            });

//            View view = LayoutInflater.from(mContext).inflate(R.layout.question_item, null);
//            TextView stem = (TextView) view.findViewById(R.id.ctv_name);
//            TextView option = (TextView) view.findViewById(R.id.tv_option);
//
//            ctv.setText(options.get(position).getName());
//            option.setText(options.get(position).getDescription());
//            updateBackground(position, ctv);
//            return view;

        }

//        public void updateBackground(int position, View view) {
//            int backgroundId;
//            if (lv.isItemChecked(position )) {
//                backgroundId = R.drawable.option_btn_single_checked;
//            } else {
//                backgroundId = R.drawable.option_btn_single_normal;
//            }
//            Drawable background = mContext.getResources().getDrawable(backgroundId);
//            view.setBackgroundDrawable(background) ;
//        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wrong_exercise_set);

        updateData(0);

        TextView nextPageTextView = findViewById(R.id.wrong_exercise_set_page_next_text_view);
        nextPageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPageNumber++;
                updateData(currentPageNumber);
            }
        });
        TextView prevPageTextView = findViewById(R.id.wrong_exercise_set_page_prev_text_view);
        prevPageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPageNumber--;
                updateData(currentPageNumber);
            }
        });

    }

    void updateData(Integer x)
    {

        // region ??????responseString
        String url = WrongExerciseSet.this.getString(R.string.backend_ip) + "/user/wrongex";
        SharedPreferences userInfo= WrongExerciseSet.this.getSharedPreferences("user", 0);
        String userToken = userInfo.getString("token","");
        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
        String message="token="+userToken+"&page="+x;
        url=url+"?"+message;
        String responseString = serverHttpResponse.getResponse(url);
        System.out.println("????????????????????????????????????"+responseString);
        // endregion

        if(responseString==null)
        {
            Toast.makeText(WrongExerciseSet.this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
        }
        else
        {
            questionList.clear();
            try
            {
                JSONObject responseJSONObject = new JSONObject(responseString);
                totalPageNumber=Integer.valueOf((String) responseJSONObject.get("pages"));
                System.out.println("hahaha");
                JSONArray jsonArray= responseJSONObject.getJSONArray("content");
                System.out.println("xixixi");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String answer=jsonObject.get("qanswer").toString();
                    if(answer.equals("A") || answer.equals("B") || answer.equals("C") || answer.equals("D"))
                    {
                        String body=jsonObject.get("qbody").toString();
                        if(numberOfMatch(body,"A.")==1&&numberOfMatch(body,"B.")==1&&
                                numberOfMatch(body,"C.")==1&&numberOfMatch(body,"D.")==1)
                        {
                            int positionOfA=body.indexOf("A.");
                            int positionOfB=body.indexOf("B.");
                            int positionOfC=body.indexOf("C.");
                            int positionOfD=body.indexOf("D.");
                            String stem=body.substring(0,positionOfA);
                            if(stem.indexOf("()")==stem.length()-2)    stem=stem.substring(0,stem.length()-2);
                            String a=body.substring(positionOfA+2,positionOfB);
                            String b=body.substring(positionOfB+2,positionOfC);
                            String c=body.substring(positionOfC+2,positionOfD);
                            String d=body.substring(positionOfD+2);

                            List<Quiz.QuestionOptionBean> options = new ArrayList<Quiz.QuestionOptionBean>();
                            options.add(new Quiz.QuestionOptionBean("A", a));
                            options.add(new Quiz.QuestionOptionBean("B", b));
                            options.add(new Quiz.QuestionOptionBean("C", c));
                            options.add(new Quiz.QuestionOptionBean("D", d));

                            Quiz.QuestionBean question = new Quiz.QuestionBean(jsonObject.getString("qid"),stem,
                                    answer,"?????????",options);
                            questionList.add(question);
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(WrongExerciseSet.this, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            }
            System.out.println(questionList);
        }

        ListView questionListView = findViewById(R.id.wrong_exercise_set_page_list);
        QuestionListAdapter questionListViewAdapter = new QuestionListAdapter(WrongExerciseSet.this, questionList, questionListView);
        questionListView.setAdapter(questionListViewAdapter);

        updateTextViewVisibility();
    }

    void updateTextViewVisibility()
    {
        TextView guideInfoTextView = findViewById(R.id.wrong_exercise_set_page_guide_info_text_view);
        TextView prevPageTextView = findViewById(R.id.wrong_exercise_set_page_prev_text_view);
        TextView nextPageTextView = findViewById(R.id.wrong_exercise_set_page_next_text_view);
        TextView pageNumberTextView = findViewById(R.id.wrong_exercise_set_page_page_number_text_view);
        if(questionList.size()==0)
        {
            guideInfoTextView.setVisibility(View.VISIBLE);
            prevPageTextView.setVisibility(View.GONE);
            nextPageTextView.setVisibility(View.GONE);
            pageNumberTextView.setVisibility(View.GONE);
        }
        else
        {
            guideInfoTextView.setVisibility(View.GONE);
            prevPageTextView.setVisibility(View.VISIBLE);
            nextPageTextView.setVisibility(View.VISIBLE);
            pageNumberTextView.setVisibility(View.VISIBLE);
            if(currentPageNumber==0)    prevPageTextView.setVisibility(View.GONE);
            if(currentPageNumber==totalPageNumber-1)    nextPageTextView.setVisibility(View.GONE);
            pageNumberTextView.setText("??? "+ (currentPageNumber+1) +"/"+totalPageNumber +"???");
        }
    }

    private int numberOfMatch( String parent,String child )
    {
        int count = 0;
        Pattern p = Pattern.compile( child );
        Matcher m = p.matcher(parent);
        while( m.find() )    count++;
        return count;
    }

}