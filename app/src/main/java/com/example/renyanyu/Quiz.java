package com.example.renyanyu;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Quiz extends FragmentActivity implements View.OnClickListener
{

    static class BaseBean {

        /**
         * 对应{"result":"1", 1:请求成功, 0:请求失败�?
         */
        public int result;//
        public String message;
        public int flag;

        /**
         * 下面是message常量值，用于在其它类做判断用
         */
        // 1:请求成功
        public final static int MESSAGE_SUCCESS = 1;
        // 0:请求失败
        public final static int MESSAGE_FAILED = 0;

        // -1:参数解密错误
        public final static int MESSAGE_DECODE_WRONG = -1;

        public void setResult(int result) {
            this.result = result;
        }

        public int getResult() {
            return result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

    }

    public static class QuestionOptionBean extends BaseBean {
        // 题目选项
        // 1 选项名称：name
        // 2 选项描述：description

        private String name;// 选项名称
        private String description;// 选项描述

        public QuestionOptionBean() {
            super();
        }

        public QuestionOptionBean(String name, String description) {
            super();
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "QuestionOptionBean [name=" + name + ", description="
                    + description + "]";
        }

    }

    static class QuestionBean extends BaseBean
    {
        String id;// 题目ID
        String stem;// 题目描述
        String answer;
        String relatedKnowledgePointName; // 相关知识点名称
        List<QuestionOptionBean> options; // 选项集合

        public QuestionBean() {
            super();
        }

        public QuestionBean(String id, String stem, String answer, String relatedKnowledgePointName,
                            List<QuestionOptionBean> questionOptions)
        {
            super();
            this.id = id;
            this.stem = stem;
            this.answer=answer;
            this.relatedKnowledgePointName = relatedKnowledgePointName;
            this.options = questionOptions;
        }
    }

    class ItemAdapter extends FragmentStatePagerAdapter {
        Context context;
        public ItemAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int arg0) {
            if(arg0 == Quiz.questionlist.size()){
                return new UseLessFragment();
            }
            if(arg0 == Quiz.questionlist.size()+1){
                return new ScantronItemFragment();
            }
            return new QuestionItemFragment(arg0);
        }


        @Override
        public int getCount() {

            return Quiz.questionlist.size()+2;
        }



    }

    static class OptionsListAdapter extends BaseAdapter {
        private Context mContext;
        ListView lv ;
        //        int index;,int index
        public List<QuestionOptionBean> options ;

        public OptionsListAdapter(Context context, List<QuestionOptionBean> options,ListView lv) {
            this.mContext = context;
            this.options = options;
            this.lv = lv;

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

        public View getView(int position, View convertView, ViewGroup parent) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_option, null);
            TextView ctv = (TextView) view.findViewById(R.id.ctv_name);
            TextView option = (TextView) view.findViewById(R.id.tv_option);

            ctv.setText(options.get(position).getName());
            option.setText(options.get(position).getDescription());
            updateBackground(position, ctv);
            return view;

        }

        public void updateBackground(int position, View view) {
            int backgroundId;
            if (lv.isItemChecked(position )) {
                backgroundId = R.drawable.option_btn_single_checked;
            } else {
                backgroundId = R.drawable.option_btn_single_normal;
            }
            Drawable background = mContext.getResources().getDrawable(backgroundId);
            view.setBackgroundDrawable(background) ;
        }

    }



    List<View> list = new ArrayList<View>();
    public static List<QuestionBean> questionlist = new ArrayList<QuestionBean>();
    public static ArrayList<String> userAnswerList=new ArrayList<>();
    public static int minute=0;
    public static int second=0;

    public static QuestionBean question;

    public static QuestionOptionBean option;
    private ViewPager vp;
    private ItemAdapter pagerAdapter;
    View pager_item;
    public static int currentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quiz);

        questionlist.clear();
        userAnswerList.clear();


        // region 获取responseString
        String url = Quiz.this.getString(R.string.backend_ip) + "/user/quiz";
        SharedPreferences userInfo= Quiz.this.getSharedPreferences("user", 0);
        String userToken = userInfo.getString("token","");
        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
        String message="token="+userToken;
        url=url+"?"+message;
        //com.alibaba.fastjson.JSONArray questionJsonArray=(com.alibaba.fastjson.JSONArray)serverHttpResponse.getResponse(url);
        String responseString = serverHttpResponse.getResponse(url);
        System.out.println(responseString);
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

        class Question
        {
            String id;
            String answer;
            String body;
            String stem;
            String a;
            String b;
            String c;
            String d;
            Question(String _id,String _answer,String _body,String _stem,String _a,String _b,String _c,String _d)
            {
                id=_id;
                answer=_answer;
                body=_body;
                stem=_stem;
                a=_a;
                b=_b;
                c=_c;
                d=_d;
            }
        }

        ArrayList<Question> questionList = new ArrayList<>();
        if(responseString!=null)
        {
            try
            {
                JSONArray jsonArray=new JSONArray(responseString);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String answer=jsonObject.get("qAnswer").toString();
                    if(answer.equals("A") || answer.equals("B") || answer.equals("C") || answer.equals("D"))
                    {
                        String body=jsonObject.get("qBody").toString();
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
                            Question question = new Question(jsonObject.getString("id"),answer,body,stem, a,b,c,d);
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

            for(int i=0;i<questionList.size();i++)
            {
                Question question=questionList.get(i);
                List<QuestionOptionBean> options = new ArrayList<QuestionOptionBean>();
                options.add(new QuestionOptionBean("A", question.a));
                options.add(new QuestionOptionBean("B", question.b));
                options.add(new QuestionOptionBean("C", question.c));
                options.add(new QuestionOptionBean("D", question.d));
                questionlist.add(new QuestionBean(((Integer)i).toString(), question.stem, question.answer,
                        "叶绿体", options));
            }
            System.out.println(questionlist);

        }

        for(int i=0;i<questionlist.size();i++)
        {
            userAnswerList.add("");
        }

//        Log.e("测试数据", questionlist.get(0).toString());
//        Log.e("测试数据", questionlist.get(1).toString());

//        TextView backButton = (TextView) findViewById(R.id.quiz_page_back_button);
        TextView timerButton= (TextView) findViewById(R.id.quiz_page_timer_button);
        TextView answerBoardButton = (TextView) findViewById(R.id.quiz_page_answer_board_button);
//        backButton.setOnClickListener(this);
        timerButton.setOnClickListener(this);
        answerBoardButton.setOnClickListener(this);

        handler.sendEmptyMessageDelayed(1, 1000);//startTimer




        vp = (ViewPager) findViewById(R.id.vp);

        vp.setCurrentItem(0);
        pagerAdapter = new ItemAdapter(getSupportFragmentManager());
        vp.setAdapter(pagerAdapter);

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("goToPrevQuestion");
        filter.addAction("goToNextQuestion");
        filter.addAction("goToPageI");
        lbm.registerReceiver(mMessageReceiver, filter);


    }


    private int numberOfMatch( String parent,String child )
    {
        int count = 0;
        Pattern p = Pattern.compile( child );
        Matcher m = p.matcher(parent);
        while( m.find() )    count++;
        return count;
    }



    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.quiz_page_answer_board_button://点击头部答题卡
                pagerAdapter.notifyDataSetChanged();
                vp.setCurrentItem(questionlist.size()+1);

                break;
            case R.id.quiz_page_timer_button://点击头部计时器
                handler.removeCallbacksAndMessages(null);//stop
                AlertDialog.Builder dialog=new AlertDialog.Builder(Quiz.this);
                dialog.setTitle("休息一下");//设置标题
                int answeredCount=0;
                for(int i=0;i<userAnswerList.size();i++)
                {
                    if(userAnswerList.get(i)!="")    answeredCount++;
                }
                dialog.setMessage("计时已停止，你可以休息一会儿再继续作答^_^，目前已答完"+answeredCount+"题，" +
                        "还剩"+(userAnswerList.size()-answeredCount)+"题未答。");//设置信息具体内容
                dialog.setCancelable(false);//设置是否可取消
                dialog.setPositiveButton("继续答题", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }
                });
                dialog.setNegativeButton("结束答题", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handler.sendEmptyMessageDelayed(1, 1000);
                        vp.setCurrentItem(questionlist.size()+1);
                    }
                });
                dialog.show();

                break;
            default:
                break;
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals("goToPrevQuestion"))
            {
                vp.setCurrentItem(vp.getCurrentItem() - 1);
            }
            else if(intent.getAction().equals("goToNextQuestion"))
            {
                vp.setCurrentItem(vp.getCurrentItem() + 1);
            }
            else if (intent.getAction().equals("goToPageI")) {
                vp.setCurrentItem(intent.getIntExtra("index", 0));
            }
        }
    };



    Handler handler = new Handler()
    {
        int time = 0;
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    time++;
                    second = time %60;
                    minute = time /60;
                    if(minute>99)
                    {
                        //TODO:自动交卷
                        break;
                    }
                    TextView timerButton= (TextView) findViewById(R.id.quiz_page_timer_button);
                    timerButton.setText(""+minute/10+minute%10+":"+second/10+second%10);
                    handler.sendEmptyMessageDelayed(1, 1000);
                    break;
                default:
                    break;
            }

        };
    };

    protected void onDestroy()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

}