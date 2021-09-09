package com.example.renyanyu;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;

import java.util.ArrayList;
import java.util.List;




public class ShareToWeibo extends AppCompatActivity implements WbShareCallback
{
    @Override
    public void onComplete() {
        Toast.makeText(ShareToWeibo.this, "分享成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(UiError error) {
        Toast.makeText(ShareToWeibo.this, "分享失败:" + error.errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(ShareToWeibo.this, "分享取消", Toast.LENGTH_SHORT).show();
    }


    IWBAPI mWBAPI;


    RecyclerView recyclerView;
    List<AppBean> list;
    BottomSheetDialog dialog;

    class Adapter extends RecyclerView.Adapter<ShareToWeibo.ViewHolde>
    {
        Adapter() {
            list = getShareAppList();
            if (list == null) {
                return;
            }
        }

        @Override
        public ViewHolde onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolde(getLayoutInflater().inflate(R.layout.recycler_item, null));
        }

        @Override
        public void onBindViewHolder(ViewHolde holder, @SuppressLint("RecyclerView") int position) {
            holder.appTextView.setText(list.get(position).appName);
            holder.iconImageView.setImageDrawable(list.get(position).icon);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    AppBean appBean = list.get(position);
                    shareIntent.setComponent(new ComponentName(appBean.pkgName, appBean.appLauncherClassName));
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "分享内容");
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(shareIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_to_weibo);

        dialog = new BottomSheetDialog(ShareToWeibo.this);
        View view = View.inflate(ShareToWeibo.this, R.layout.bottom_dialog, null);
        dialog.setContentView(view);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(ShareToWeibo.this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new Adapter());
        dialog.show();


//        // region 分享
//        //在微博开发平台为应用申请的App Key
//        String APP_KY = "771540176";
//        //在微博开放平台设置的授权回调页
//        String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
//        //在微博开放平台为应用申请的高级权限
//        String SCOPE = "email,direct_messages_read,direct_messages_write,"
//                + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
//                + "follow_app_official_microblog," + "invitation_write";
//
//        AuthInfo authInfo = new AuthInfo(this, APP_KY, REDIRECT_URL, SCOPE);
//        mWBAPI = WBAPIFactory.createWBAPI(this);
//        mWBAPI.registerApp(this, authInfo);
//        mWBAPI.setLoggerEnable(true);
//
//        WeiboMultiMessage message = new WeiboMultiMessage();
//        TextObject textObject = new TextObject();
//        String text = "我正在使⽤微博客户端发博器分享⽂字。";
//        text = "这⾥设置您要分享的内容！";
//        textObject.text = text;
//        message.textObject = textObject;
//        mWBAPI.shareMessage(message, true);// true 表示是否指定⽤客户端分享
//
//        //endregion

    }

    @SuppressLint("WrongConstant")
    List<ResolveInfo> getShareApps(Context context)
    {
        List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        PackageManager pManager = context.getPackageManager();
        mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }

    private List<AppBean> getShareAppList() {
        List<AppBean> shareAppInfos = new ArrayList<AppBean>();
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> resolveInfos = getShareApps(ShareToWeibo.this);
        if (null == resolveInfos) {
            return null;
        } else {
            for (ResolveInfo resolveInfo : resolveInfos) {
                AppBean appBean = new AppBean();
                appBean.pkgName = (resolveInfo.activityInfo.packageName);
//              showLog_I(TAG, "pkg>" + resolveInfo.activityInfo.packageName + ";name>" + resolveInfo.activityInfo.name);
                appBean.appLauncherClassName = (resolveInfo.activityInfo.name);
                appBean.appName = (resolveInfo.loadLabel(packageManager).toString());
                appBean.icon = (resolveInfo.loadIcon(packageManager));
                shareAppInfos.add(appBean);
            }
        }
        return shareAppInfos;
    }


    //region 分享的结果返回函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mWBAPI != null) {
            mWBAPI.doResultIntent(data, this);
        }
    }
    //endregion

    class ViewHolde extends RecyclerView.ViewHolder {
        public ImageView iconImageView;
        public TextView appTextView;

        public ViewHolde(View itemView) {
            super(itemView);
            iconImageView = (ImageView) itemView.findViewById(R.id.app_icon_iv);
            appTextView = (TextView) itemView.findViewById(R.id.app_tv);
        }
    }

    class AppBean {
        public Drawable icon;
        public String appName;
        public String pkgName;
        public String appLauncherClassName;
    }

}






