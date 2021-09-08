package com.example.renyanyu;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
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

    class BaseBean {

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

    class QuestionOptionBean extends BaseBean {
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

    class QuestionBean extends BaseBean {

        // 1�?题目ID：questionId
        // 2�?题目描述：description
        // a) 填空题时：填空位置标记为：�?{1}”�?”{N}”，同时选项的名称为：�?{1}”�?”{N}”，
        // 对应选项的描述为：正确答案字节数的长度加4
        // b) 题目包含有图片时：图片的位置标识为：”{image1}”�?”{imageN}”，
        // 同时增加key”{image1}”�?”{image}”，它们的�?为：图片的二进制内容
        // 3�?题目类型：questionType�?-单�? 2-多�? 3-填空 4-问答�?
        // 4�?知识点名称：knowledgePointName
        // 5�?知识点ID：knowledgePointId
        // 6�?题目选项集合：questionOptions
        // a) 选项名称：name
        // b) 选项描述：description

        String id;// 题目ID
        String stem;// 题目描述
        String relatedKnowledgePointName; // 相关知识点名称
        List<QuestionOptionBean> options; // 选项集合
//        private int questionType;// 题目类型

//        private String knowledgePointId; // 知识点id


        public QuestionBean() {
            super();
        }

        public QuestionBean(String questionId, String description,
                            int questionType, String knowledgePointName,
                            String knowledgePointId, List<QuestionOptionBean> questionOptions) {
            super();
            this.id = questionId;
            this.stem = description;
            this.relatedKnowledgePointName = knowledgePointName;
            this.options = questionOptions;
//            this.questionType = questionType;

//            this.knowledgePointId = knowledgePointId;

        }

//        public String getQuestionId() {
//            return questionId;
//        }
//
//        public void setQuestionId(String questionId) {
//            this.questionId = questionId;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public void setDescription(String description) {
//            this.description = description;
//        }
//
//        public int getQuestionType() {
//            return questionType;
//        }
//
//        public void setQuestionType(int questionType) {
//            this.questionType = questionType;
//        }
//
//        public String getKnowledgePointName() {
//            return knowledgePointName;
//        }
//
//        public void setKnowledgePointName(String knowledgePointName) {
//            this.knowledgePointName = knowledgePointName;
//        }

//        public String getKnowledgePointId() {
//            return knowledgePointId;
//        }
//
//        public void setKnowledgePointId(String knowledgePointId) {
//            this.knowledgePointId = knowledgePointId;
//        }

//        public List<QuestionOptionBean> getQuestionOptions() {
//            return questionOptions;
//        }
//
//        public void setQuestionOptions(List<QuestionOptionBean> questionOptions) {
//            this.questionOptions = questionOptions;
//        }

//        @Override
//        public String toString() {
//            return "QuestionBean [questionId=" + questionId + ", description="
//                    + description + ", questionType=" + questionType
//                    + ", knowledgePointName=" + knowledgePointName
////                    + ", knowledgePointId=" + knowledgePointId
//                    + ", questionOptions=" + questionOptions + "]";
//        }

    }

    class ItemAdapter extends FragmentStatePagerAdapter {
        Context context;
        public ItemAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int arg0) {
            if(arg0 == Quiz.questionlist.size()){
                return new ScantronItemFragment();
            }
            return new QuestionItemFragment(arg0);
        }


        @Override
        public int getCount() {

            return Quiz.questionlist.size()+1;
        }



    }

    static class OptionsListAdapter extends BaseAdapter {
        private Context mContext;
        ListView lv ;
        int index;
        public List<QuestionOptionBean> options ;

        public OptionsListAdapter(Context context, List<QuestionOptionBean> options,ListView lv,int index) {
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

            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.list_item_option, null);
            CheckedTextView ctv = (CheckedTextView) view.findViewById(R.id.ctv_name);
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
    public static QuestionBean question;

    public static QuestionOptionBean option;
    private ViewPager vp;
    private ItemAdapter pagerAdapter;
    View pager_item;
    public static int currentIndex = 0;
    private TextView tv_time;
    private TextView tv_share;
    private TextView tv_answercard;
    private TextView tv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quiz);


//        String url = EntityDetails.this.getString(R.string.backend_ip) + "/request/exercise";
//        String msg="?uriName="+entity_name;
//        //System.out.println("msg:"+msg);
//        String res= serverHttpResponse.getResponse(url+msg);
//        try{
//            JSONObject answer_json = new JSONObject(res);
//            //System.out.println(((JSONArray) answer_json.opt("data")).length());
//            for(int i = 0; i<((JSONArray) answer_json.opt("data")).length(); i++){
//                JSONObject data1 = ((JSONArray) answer_json.opt("data")).optJSONObject(i);
//                String stemall=data1.opt("qBody").toString();
//                String answer=data1.opt("qAnswer").toString();
//                int index_a=stemall.indexOf("A.");
//                int index_b=stemall.indexOf("B.");
//                int index_c=stemall.indexOf("C.");
//                int index_d=stemall.indexOf("D.");
//                if(index_a==-1&&index_b==-1&&index_c==-1&&index_d==-1){
//                    Exercise e=new Exercise(stemall,answer);
//                    mex.add(e);
//                }
//                else{
//                    String stem=stemall.substring(0,index_a);
//                    String text_a=stemall.substring(index_a+2,index_b);
//                    String text_b=stemall.substring(index_b+2,index_c);
//                    String text_c=stemall.substring(index_c+2,index_d);
//                    String text_d=stemall.substring(index_d+2);
//                    Exercise e=new Exercise(stem,text_a,text_b,text_c,text_d,answer);
//                    mex.add(e);
//                }
//            }
//        }catch(Exception e){}


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
                questionlist.add(new QuestionBean(((Integer)i).toString(), question.stem, 1,
                        "常识判断", "001", options));
            }
            System.out.println(questionlist);

        }




        Log.e("测试数据", questionlist.get(0).toString());
        Log.e("测试数据", questionlist.get(1).toString());

        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_answercard = (TextView) findViewById(R.id.tv_answercard);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_share = (TextView) findViewById(R.id.tv_share);
//        startCounter();
        tv_back.setOnClickListener(this);
        tv_answercard.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_share.setOnClickListener(this);

        vp = (ViewPager) findViewById(R.id.vp);

        vp.setCurrentItem(0);
        pagerAdapter = new ItemAdapter(getSupportFragmentManager());
        vp.setAdapter(pagerAdapter);
//        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int arg0) {
//
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int position) {
//                currentIndex = position;
//            }
//        });

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("goToPrevQuestion");
        filter.addAction("goToNextQuestion");
        filter.addAction("com.leyikao.jumptopage");
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back://点击头部返回

                break;
            case R.id.tv_answercard://点击头部答题卡

                jumpToPage(questionlist.size());
//                int index = intent.getIntExtra("index", 0);
//                vp.setCurrentItem(questionlist.size());

                break;
            case R.id.tv_time://点击头部计时器
                //TODO计时器停止计时
//                stopCounter();
                final ConfirmDialog confirmDialog = new ConfirmDialog(this, "共4道题，还剩4道题未做");
                confirmDialog.setCancelable(false);
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {

                    @Override
                    public void doProceed() {
                        //TODO计时器继续计时
                        confirmDialog.dismiss();
//                        startCounter();
                    }

                });
                break;
            case R.id.tv_share://点击头部分享

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
            else if (intent.getAction().equals("com.leyikao.jumptopage")) {
                int index = intent.getIntExtra("index", 0);
                jumpToPage(index);
            }
        }
    };

    public void jumpToNext() {


    }
    public void jumpToPage(int index) {
        vp.setCurrentItem(index);
    }

//    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
//    {
//        @Override
//        public void onReceive(Context context, Intent intent)
//        {
//            if (intent.getAction().equals("com.example.goToPrevQuestion"))
//            {
//                System.out.println("intent.getAction().equals(\"goToPrevQuestion\")");
//                int position = vp.getCurrentItem();
//                vp.setCurrentItem(position - 1);
//            }
//            else if (intent.getAction().equals("goToNextQuestion"))
//            {
//                int position = vp.getCurrentItem();
//                vp.setCurrentItem(position + 1);
//            }
//            else if (intent.getAction().equals("com.leyikao.jumptopage"))
//            {
//                int index = intent.getIntExtra("index", 0);
//                vp.setCurrentItem(index);
//            }
//        }
//    };


//    private void loadData()
//    {
//
//
//        // 初始化数据
//        option = new QuestionOptionBean("A", "这个男的头有病");
//        options1.add(option);
//        option = new QuestionOptionBean("B", "这个男的头比较大");
//        options1.add(option);
//        option = new QuestionOptionBean("C", "这个男的看见的是鬼");
//        options1.add(option);
//        option = new QuestionOptionBean("D", "这个女的有点吃醋");
//        options1.add(option);
//        option = new QuestionOptionBean("E", "这个男的看见的是鬼");
//        options1.add(option);
//        question = new QuestionBean("0001", "男：看那个妹妹，好靓哦！\n女：看你个大头鬼！"
//                + "\n问：这个女的是什么意思？", 2, "常识判断", "001", options1);
//        questionlist.add(question);
//
//        // 初始化数据
//        option = new QuestionOptionBean("A", "河北");
//        options2.add(option);
//        option = new QuestionOptionBean("B", "通州");
//        options2.add(option);
//        option = new QuestionOptionBean("C", "石家庄");
//        options2.add(option);
//        option = new QuestionOptionBean("D", "北京");
//        options2.add(option);
//        question = new QuestionBean("0002", "中国的首都在哪？", 1, "常识判断", "001",
//                options2);
//        questionlist.add(question);
//
//        // 初始化数据
//        option = new QuestionOptionBean("A",
//                "中台办国台办宣布五项促进两岸交往新举措，大陆13省市居民可赴金门旅游。");
//        options3.add(option);
//        option = new QuestionOptionBean("B",
//                "说起去年发生的那件事，两个人脸上依如往常，目光中带着幽怨和冷漠，相对许久许久。");
//        options3.add(option);
//        option = new QuestionOptionBean("C",
//                "明年，他只打算完成一部电视剧本，其他的事不想做。关于电视剧本的详细情况，他说，不易过早泄密。");
//        options3.add(option);
//        option = new QuestionOptionBean("D",
//                "她把海南的荔枝、芒果，新疆的哈蜜瓜、紫葡萄等珍果和自家产的黄橙橙的菠萝放在一起，装满了一篮子。");
//        options3.add(option);
//        question = new QuestionBean("0003", "下列语句中，没有错别字的一项是 ", 1, "常识判断",
//                "001", options3);
//        questionlist.add(question);
//
//
//
//    }
    //计时器任务
    int time = 0;
    int second = 0;
    int minute = 0;
    String timeStr  ="00:00";
    int[] iTime = new int[]{0,0,0,0};



//    Handler handler = new Handler(){
//
//
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case 1:
//                    time++;
//                    second = time %60;
//                    minute = time /60;
//                    if(minute>99){
//                        break;
//                    }
//                    //Log.e("秒数", ""+second);
//                    //Log.e("分钟数", ""+minute);
//                    if(second < 10 && minute < 10){
//                        iTime[0]=0;
//                        iTime[1]=minute;
//                        iTime[2]=0;
//                        iTime[3]=second;
//
//                    }else if(second >= 10 && minute < 10){
//                        iTime[0]=0;
//                        iTime[1]=minute;
//                        iTime[2]=(second+"").charAt(0)-48;
//                        iTime[3]=(second+"").charAt(1)-48;
//
//                    }else if(second < 10 && minute >= 10){
//                        iTime[0]=(minute+"").charAt(0)-48;
//                        iTime[1]=(minute+"").charAt(1)-48;
//                        iTime[2]=0;
//                        iTime[3]=second;
//
//                    }else if(second >= 10 && minute >= 10){
//                        iTime[0]=(minute+"").charAt(0)-48;
//                        iTime[1]=(minute+"").charAt(1)-48;
//                        iTime[2]=(second+"").charAt(0)-48;
//                        iTime[3]=(second+"").charAt(1)-48;
//
//                    }
//                    tv_time.setText(""+iTime[0]+iTime[1]+":"+iTime[2]+iTime[3]);
//                    handler.sendEmptyMessageDelayed(1, 1000);
//                    break;
//
//                default:
//                    break;
//            }
//
//        };
//    };
//
//
//    // 开始计时
//    public void startCounter() {
//        handler.sendEmptyMessageDelayed(1, 1000);
//    }
//
//    // 暂停计时
//    public void stopCounter() {
//        handler.removeCallbacksAndMessages(null);
//    }

    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        super.onDestroy();
    }

}