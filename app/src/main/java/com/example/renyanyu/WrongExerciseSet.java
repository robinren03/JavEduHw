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

//    class QuestionAdapter extends BaseAdapter
//    {
//        private Context mContext;
//
//        public QuestionAdapter(Context context, List<Quiz.QuestionBean> questions, ListView questionListView, int index) {
//            this.mContext = context;
//            this.questions = questions;
//            this.questionListView = questionListView;
//        }
//
//        public int getCount() {
//            return questions.size();
//        }
//
//        @Override
//        public boolean areAllItemsEnabled() {
//            return false;
//        }
//
//        @Override
//        public boolean isEnabled(int position) {
//            return true;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return true;
//        }
//
//        public Object getItem(int position) {
//            return position;
//        }
//
//        public long getItemId(int position) {
//            return position;
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent)
//        {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.question_item, null);
//
//            TextView stemTextView = (TextView) view.findViewById(R.id.question_stem_text);
//            ListView optionsListView = (ListView) view.findViewById(R.id.question_options_list_view);
////            TextView tv_paper_name = (TextView) rootView.findViewById(R.id.tv_paper_name); 对应知识点
////            TextView tv_sequence = (TextView) view.findViewById(R.id.tv_sequence); 题目序号
////            TextView tv_total_count = (TextView) rootView.findViewById(R.id.tv_total_count);
//
////            QuestionAdapter adapter = new QuestionAdapter(WrongExerciseSet.this, questions, questionListView, index);
////            lv.setAdapter(adapter);
//
////            tv_paper_name.setText("相关知识点：叶绿体");
////            tv_sequence.setText((index+1)+"");
////            tv_total_count.setText("/"+ Quiz.questionlist.size());
//            .setText(questionBean.stem);
//
//            //题干描述前面加上(单选题)或(多选题)
//            sb = new StringBuffer();
//            SpannableStringBuilder ssb = new SpannableStringBuilder("(单选题)");
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
//
//            View view = LayoutInflater.from(mContext).inflate(R.layout.question_item, null);
//            TextView stem = (TextView) view.findViewById(R.id.ctv_name);
//            TextView option = (TextView) view.findViewById(R.id.tv_option);
//
//            ctv.setText(options.get(position).getName());
//            option.setText(options.get(position).getDescription());
//            updateBackground(position, ctv);
//            return view;
//
//        }
//
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
//
//    }

    class QuestionListAdapter extends BaseAdapter
    {
        private Context mContext;
        ListView questionListView ;
//        int index;

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

                View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_option, null);//(R., null);
                TextView ctv = (TextView) view.findViewById(R.id.ctv_name);
                TextView option = (TextView) view.findViewById(R.id.tv_option);

                ctv.setText(options.get(position).getName());
                option.setText(options.get(position).getDescription());
                updateBackground(position, ctv);
                return view;

            }

            public void updateBackground(int position, View view)
            {

                System.out.println("I am here");
                int backgroundId;
                if(position==correctAnswer)
                {
                    backgroundId = R.drawable.option_btn_single_right;
                }
                else
                {
                    backgroundId = R.drawable.option_btn_single_normal;
                }

//                if (lv.isItemChecked(position )) {
//                    System.out.println("123");
//                    backgroundId = R.drawable.option_btn_single_checked;
//                } else {
//                    System.out.println("456");
//                    backgroundId = R.drawable.option_btn_single_normal;
//                }
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
//            //准备适配器
//            MyAdapter adapter=new MyAdapter(this);
//            adapter.setList(list);
//            //将适配器与ListView关联
//            listview.setAdapter(adapter);
//
//            OptionsListAdapter(Context context, List< Quiz.QuestionOptionBean > options,ListView lv,int index)

//            TextView stemTextView = (TextView) view.findViewById(R.id.question_stem_text);
//            ListView optionsListView = (ListView) view.findViewById(R.id.question_options_list_view);
//            TextView tv_paper_name = (TextView) rootView.findViewById(R.id.tv_paper_name); 对应知识点
//            TextView tv_sequence = (TextView) view.findViewById(R.id.tv_sequence); 题目序号
//            TextView tv_total_count = (TextView) rootView.findViewById(R.id.tv_total_count);


//            QuestionAdapter adapter = new QuestionAdapter(WrongExerciseSet.this, questions, questionListView, index);
//            lv.setAdapter(adapter);

//            tv_paper_name.setText("相关知识点：叶绿体");
//            tv_sequence.setText((index+1)+"");
//            tv_total_count.setText("/"+ Quiz.questionlist.size());
//            .setText(questionBean.stem);
//
//            //题干描述前面加上(单选题)或(多选题)
//            sb = new StringBuffer();
//            SpannableStringBuilder ssb = new SpannableStringBuilder("(单选题)");
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

        // region 获取responseString
        //TODO:测试是否能正常获取数据
        String url = WrongExerciseSet.this.getString(R.string.backend_ip) + "/user/wrongex";
        SharedPreferences userInfo= WrongExerciseSet.this.getSharedPreferences("user", 0);
        String userToken = userInfo.getString("token","");
        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
        String message="token="+userToken+"&page="+x;
        url=url+"?"+message;
        String responseString = serverHttpResponse.getResponse(url);
        System.out.println("错题本接口收到的内容是："+responseString);

//        String responseString="[{\"qAnswer\":\"D\",\"id\":37964,\"qBody\":\"下列哪种现象不是生物对环境的适应()" +
//                "A.河边垂柳的树枝长向了河心B.仙人掌的叶变成刺C.秋天大雁南飞越冬D.蚯蚓在土壤中括动,可使土壤疏松\"}," +
//                "{\"qAnswer\":\"B\",\"id\":38086,\"qBody\":\"松树和苹果树这两种植物最主要的区别在于:()" +
//                "A.松树的种子外面有果皮包被着B.松树没有果实,种子裸露着C.松树比苹果树高大D.松树有果实,苹果树的种子裸露\"}," +
//                "{\"qAnswer\":\"B\",\"id\":38108,\"qBody\":\"下列属于裸子植物的一组是:()" +
//                "A.小麦和水稻B.银杏和松树C.玉米和杉树D.杨树和柳树\"}," +
//                "{\"qAnswer\":\"A\",\"id\":38226,\"qBody\":\"在哺乳动物的骨中,对骨的长粗和骨折修复起着重要作用的结构是()" +
//                "A.骨膜B.骨松质C.骨密质D.骨髓腔\"},{\"qAnswer\":\"B\",\"id\":38256,\"qBody\":\"下列说法中,正确的是" +
//                "A.松的球果和桃子一样都是果实B.裸子植物和被子植物的种子中都有胚C.种子中的胚乳能发育成新植株" +
//                "D.裸子植物的种子比被子植物的种子能得到更好的保护\"}," +
//                "{\"qAnswer\":\"B\",\"id\":38274,\"qBody\":\"蚯蚓在土壤中生活,可以使土壤疏松,排出的粪便可增加土壤肥力。这说明了:" +
//                "A.环境影响生物B.生物影响环境C.生物依赖环境D.生物适应环境\"}," +
//                "{\"qAnswer\":\"D\",\"id\":38354,\"qBody\":\"与桃树相比,松树种子最主要的不同是" +
//                "A.果实内有种子B.球果是由果皮和种子组成C.胚珠外有子房壁D.没果皮包裹,种子裸露在外\"}," +
//                "{\"qAnswer\":\"B\",\"id\":38571,\"qBody\":\"下列农业生产措施中,能提高光合作用效率的是()" +
//                "A.常松士,勤施肥B.合理密植充分利用光照C.温室大棚夜间适当降低室温D.移栽树苗时,剪去树苗的部分叶片\"}," +
//                "{\"qAnswer\":\"A\",\"id\":38576,\"qBody\":\"下列各组植物中,生活环境和繁殖方式最相似的是()" +
//                "A.墙藓、肾蕨B.水绵、水稻C.海带、雪松D.白菜、紫菜\"}," +
//                "{\"qAnswer\":\"A\",\"id\":38640,\"qBody\":\"下列生物防治的方案不可行的是" +
//                "A.鸡防治菜青虫B.灰喜鹊防治松毛虫C.七星瓢虫防治棉蚜虫D.啄木鸟防治林业害虫\"}]";

        // endregion

        if(responseString!=null)
        {
            questionList.clear();
//            currentPageNumber = x+1;

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
                                    answer,"叶绿体",options);
                            questionList.add(question);
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
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
            pageNumberTextView.setText("第 "+ (currentPageNumber+1) +"/"+totalPageNumber +"页");
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