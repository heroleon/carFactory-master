package com.leon.carfixfactory.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;

import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.quicksidebar.QuickSideBarTipsView;
import com.bigkoo.quicksidebar.QuickSideBarView;
import com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.leon.carfixfactory.MyApplication;
import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.ContactsInfo;
import com.leon.carfixfactory.beanDao.ContactsInfoDao;
import com.leon.carfixfactory.presenter.ContactPresenter;
import com.leon.carfixfactory.ui.adapter.ContactsListWithHeadersAdapter;
import com.leon.carfixfactory.ui.custom.MoreWindow;
import com.leon.carfixfactory.ui.view.MyQuickSideBarView;
import com.leon.carfixfactory.utils.DividerDecoration;
import com.leon.carfixfactory.utils.FileHelp;
import com.leon.carfixfactory.utils.HanziToPinyin;
import com.leon.carfixfactory.utils.ZToast;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ContactActivity extends BaseActivity implements OnQuickSideBarTouchListener {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.quickSideBarTipsView)
    QuickSideBarTipsView quickSideBarTipsView;
    @Bind(R.id.quickSideBarView)
    MyQuickSideBarView quickSideBarView;
    @Bind(R.id.iv_back)
    ImageView back;
    @Bind(R.id.tv_title)
    TextView title;
    @Bind(R.id.ll_contact_root)
    LinearLayout llContactRoot;

    private ContactsInfoDao contactsInfoDao;
    private ContactsListWithHeadersAdapter adapter;
    private HashMap<String, Integer> letters = new HashMap<>();
    private ArrayList<String> customLetters;
    private MoreWindow mMoreWindow;
    private final int REQUEST_CODE = 10000;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};


    private void initData() {
        contactsInfoDao = MyApplication.getApplication().getDaoSession().getContactsInfoDao();
        List<ContactsInfo> contactsInfos = contactsInfoDao.queryBuilder().orderAsc(ContactsInfoDao.Properties.SortKey).build().list();
        customLetters.clear();
        letters.clear();
        int position = 0;
        for (ContactsInfo contact : contactsInfos) {
            String letter = contact.getSortKey();
            //如果没有这个key则加入并把位置也加入
            if (!letters.containsKey(letter)) {
                letters.put(letter, position);
                customLetters.add(letter);
            }
            position++;
        }

        adapter.clear();
        adapter.addAll(contactsInfos);
        //不自定义则默认26个字母
        quickSideBarView.setLetters(customLetters);

    }


    @Override
    protected void initPresenter(Intent intent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_contact;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initView() {
        back.setVisibility(View.GONE);

        mMoreWindow = new MoreWindow(this);
        mMoreWindow.init(llContactRoot);
        title.setText("联系人");
        customLetters = new ArrayList<>();
        //设置监听
        quickSideBarView.setOnQuickSideBarTouchListener(this);
        //设置列表数据和浮动header
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ContactsListWithHeadersAdapter(this);
        // Add decoration for dividers between list items
        recyclerView.addItemDecoration(new DividerDecoration(this));
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));
        // initData();
        recyclerView.setAdapter(adapter);
        quickSideBarView.setLetters(customLetters);
        PermissionX.init(this).permissions(PERMISSIONS_STORAGE).onExplainRequestReason(new ExplainReasonCallback() {
            @Override
            public void onExplainReason(@NonNull ExplainScope explainScope, @NonNull List<String> list) {
                String message = "PermissionX需要您同意以下权限才能正常使用";
                explainScope.showRequestReasonDialog(list, message, "Allow", "Deny");
            }
        }).request((b, list, list1) -> {
            if (!b) {
                ZToast.makeText(ContactActivity.this, "你已禁用存储权限，请到应用设置开启权限", 1000).show();
            }

        });
    }

    @Override
    public void onLetterChanged(String letter, int position, float y) {
        quickSideBarTipsView.setText(letter, position, y);
        //有此key则获取位置并滚动到该位置
        if (letters.containsKey(letter)) {
            recyclerView.scrollToPosition(letters.get(letter));
        }
    }

    @Override
    public void onLetterTouching(boolean touching) {
        //可以自己加入动画效果渐显渐隐
        quickSideBarTipsView.setVisibility(touching ? View.VISIBLE : View.INVISIBLE);
    }

    @OnClick(R.id.iv_scan)
    public void newContact() {
        showMoreWindow();
    }

    private void showMoreWindow() {
        if (null == mMoreWindow) {
            mMoreWindow = new MoreWindow(this);
            mMoreWindow.init(llContactRoot);
        }
        mMoreWindow.showMoreWindow(llContactRoot);
    }


    public void importContact() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        this.startActivityForResult(intent, REQUEST_CODE);
    }

    // 获取文件的真实路径
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            // 用户未选择任何文件，直接返回
            return;
        }
        Uri uri = data.getData();
        // 获取用户选择文件的URI
        FileHelp fileHelp = new FileHelp(this);
        if (uri == null) {
            return;
        }
        String path = FileHelp.getFileAbsolutePath(this, uri);
        if (path == null || !path.contains("contacts.txt")) {
            ZToast.makeText(this, "请选择导出的文件夹", 1000).show();
            return;
        }
        try {
            fileHelp.read(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initData();
    }
}
