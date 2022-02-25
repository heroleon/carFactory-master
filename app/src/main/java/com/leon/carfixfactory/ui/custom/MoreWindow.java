package com.leon.carfixfactory.ui.custom;

import static android.os.Environment.DIRECTORY_DCIM;
import static com.leon.carfixfactory.ui.activity.ContactActivity.PERMISSIONS_STORAGE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.leon.carfixfactory.MyApplication;
import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.ContactsInfo;
import com.leon.carfixfactory.bean.WorkerInfo;
import com.leon.carfixfactory.beanDao.ContactsInfoDao;
import com.leon.carfixfactory.ui.activity.AddWorkerActivity;
import com.leon.carfixfactory.ui.activity.BaseActivity;
import com.leon.carfixfactory.ui.activity.ContactActivity;
import com.leon.carfixfactory.utils.DisplayUtil;
import com.leon.carfixfactory.utils.FileHelp;
import com.leon.carfixfactory.utils.ZToast;
import com.ms_square.etsyblur.BlurringView;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.List;

import io.reactivex.functions.Consumer;

public class MoreWindow extends PopupWindow implements OnClickListener {

    private Activity mContext;
    private RelativeLayout layout;
    private ImageView close;
    private View bgView;
    private BlurringView blurringView;
    private int mWidth;
    private int mHeight;
    private int statusBarHeight;
    private Handler mHandler = new Handler();

    public MoreWindow(Activity context) {
        mContext = context;
    }

    /**
     * 初始化
     *
     * @param view 要显示的模糊背景View,一般选择跟布局layout
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void init(View view) {
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        mWidth = metrics.widthPixels;
        int statusHEIGHT = getStatusBarHeight(mContext);
        int navarBar = getNavigationBarHeight(mContext);
        mHeight = metrics.heightPixels;

        setWidth(mWidth);
        setHeight(mHeight);

        layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.more_window, null);

        setContentView(layout);

        close = (ImageView) layout.findViewById(R.id.iv_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    closeAnimation();
                }
            }

        });

        blurringView = (BlurringView) layout.findViewById(R.id.blurring_view);
        blurringView.blurredView(view);//模糊背景

        bgView = layout.findViewById(R.id.rel);
        setOutsideTouchable(true);
        setFocusable(true);
        setClippingEnabled(false);//使popupwindow全屏显示
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public  int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 显示window动画
     *
     * @param anchor
     */
    public void showMoreWindow(View anchor) {

        showAtLocation(anchor, Gravity.TOP | Gravity.START, 0, 0);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //圆形扩展的动画
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        int x = mWidth / 2;
                        int y = (int) (mHeight - DisplayUtil.dip2px(mContext,25));
                        Animator animator = ViewAnimationUtils.createCircularReveal(bgView, x,
                                y, 0, bgView.getHeight());
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
//                                bgView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                //							layout.setVisibility(View.VISIBLE);
                            }
                        });
                        animator.setDuration(300);
                        animator.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        showAnimation(layout);

    }

    private void showAnimation(ViewGroup layout) {
        try {
            LinearLayout linearLayout = layout.findViewById(R.id.lin);
            mHandler.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
                @Override
                public void run() {
                    //＋ 旋转动画
                    close.animate().rotation(90).setDuration(400);
                }
            });
            //菜单项弹出动画
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                final View child = linearLayout.getChildAt(i);
                child.setOnClickListener(this);
                child.setVisibility(View.INVISIBLE);
                mHandler.postDelayed(new Runnable() {

                    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void run() {
                        child.setVisibility(View.VISIBLE);
                        ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
                        fadeAnim.setDuration(200);
                        KickBackAnimator kickAnimator = new KickBackAnimator();
                        kickAnimator.setDuration(150);
                        fadeAnim.setEvaluator(kickAnimator);
                        fadeAnim.start();
                    }
                }, i * 50 + 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭window动画
     */
    private void closeAnimation() {
        mHandler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override
            public void run() {
                close.animate().rotation(-90).setDuration(400);
            }
        });

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                int x = mWidth / 2;
                int y = (int) (mHeight - DisplayUtil.dip2px(mContext,25));
                Animator animator = ViewAnimationUtils.createCircularReveal(bgView, x,
                        y, bgView.getHeight(), 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        //							layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
//                        bgView.setVisibility(View.GONE);
                        dismiss();
                    }
                });
                animator.setDuration(300);
                animator.start();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (isShowing()) {
            closeAnimation();
        }
        switch (v.getId()) {
            case R.id.tv_add_worker:
                Intent intent = new Intent(mContext,AddWorkerActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.tv_search://导入
                PermissionX.init((BaseActivity) mContext).permissions(PERMISSIONS_STORAGE).onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(@NonNull ExplainScope explainScope, @NonNull List<String> list) {
                        String message = "PermissionX需要您同意以下权限才能正常使用";
                        explainScope.showRequestReasonDialog(list, message, "Allow", "Deny");
                    }
                }).request((b, list, list1) -> {
                    if (b) {
                        ((ContactActivity) mContext).importContact();
                    } else {
                        ZToast.makeText(mContext, "你已禁用存储权限，请到应用设置开启权限", 1000).show();
                    }
                });
                break;
            case R.id.tv_course://导出
                PermissionX.init((BaseActivity) mContext).permissions(PERMISSIONS_STORAGE).onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(@NonNull ExplainScope explainScope, @NonNull List<String> list) {
                        String message = "PermissionX需要您同意以下权限才能正常使用";
                        explainScope.showRequestReasonDialog(list, message, "Allow", "Deny");
                    }
                }).request((b, list, list1) -> {
                    if (b) {
                        outputContacts();
                    } else {
                        ZToast.makeText(mContext, "你已禁用存储权限，请到应用设置开启权限", 1000).show();
                    }
                });
                break;
        }

    }

    private void outputContacts() {
        ContactsInfoDao contactsInfoDao =  MyApplication.getApplication().getDaoSession().getContactsInfoDao();
        List<ContactsInfo> contactsInfos = contactsInfoDao.queryBuilder().build().list();
        if(contactsInfos==null||contactsInfos.size()==0){
            ZToast.makeText(mContext,"暂无联系人",1000).show();
            return;
        }
        String backupPath = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).getAbsolutePath();
        String fileName = backupPath+File.separator+"contacts.txt";
        File file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
       FileHelp fileHelp =  new FileHelp(mContext);
        for (int i = 0; i <contactsInfos.size() ; i++) {
            ContactsInfo contactsInfo = contactsInfos.get(i);
            String content = contactsInfo.getName()
                    +"#"+contactsInfo.getPhone_number()
                    +"#"+contactsInfo.getHome_number()
                    +"#"+contactsInfo.getDuty()
                    +"#"+contactsInfo.getNote()
                    +"#"+contactsInfo.getSortKey();
            fileHelp.save(content,file.getAbsolutePath());
        }
        ZToast.makeText(mContext,"导出成功，请在"+fileName+"查看",1000).show();
    }
}
