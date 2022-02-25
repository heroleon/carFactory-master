package com.leon.carfixfactory.ui.activity;

import static com.leon.carfixfactory.ui.activity.ContactActivity.PERMISSIONS_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.leon.carfixfactory.MyApplication;
import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.ContactsInfo;
import com.leon.carfixfactory.bean.DriverInfo;
import com.leon.carfixfactory.bean.ItemEditContent;
import com.leon.carfixfactory.bean.WorkerInfo;
import com.leon.carfixfactory.beanDao.ContactsInfoDao;
import com.leon.carfixfactory.contract.ItemEditTextContact;
import com.leon.carfixfactory.presenter.EditContentImp;
import com.leon.carfixfactory.utils.ContentViewSetting;
import com.leon.carfixfactory.utils.GlideUtils;
import com.leon.carfixfactory.utils.HanziToPinyin;
import com.leon.carfixfactory.utils.ZToast;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.winfo.photoselector.PhotoSelector;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;


import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class AddWorkerActivity extends BaseActivity<EditContentImp> implements ItemEditTextContact.ViewEditContent {
    private static final int PHOTO_CODE = 10000;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_name)
    LinearLayout llName;
    @Bind(R.id.ll_birthday)
    LinearLayout llBirthday;
    @Bind(R.id.ll_phone)
    LinearLayout llPhone;
    @Bind(R.id.ll_address)
    LinearLayout llAddress;
    @Bind(R.id.ll_remark)
    LinearLayout llRemark;
    @Bind(R.id.iv_worker_avatar)
    ImageView ivWorkerAvatar;
    @Bind(R.id.btn_call)
    Button btnCall;

    private View[] views;
    private ContactsInfo contactsInfo;
    private boolean isEdit;
    private ContactsInfoDao contactsInfoDao;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new EditContentImp(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_add_worker;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        contactsInfoDao = MyApplication.getApplication().getDaoSession().getContactsInfoDao();
        Long contactId = intent.getLongExtra("id", -1);
        isEdit = contactId == -1;
        if(!isEdit){
            contactsInfo = contactsInfoDao.queryBuilder().where(ContactsInfoDao.Properties.Id.eq(contactId)).list().get(0);
        }else{
            contactsInfo = new ContactsInfo();
        }
        tvTitle.setText(isEdit ? "新增联系人" : contactsInfo.getName());
        if (!isEdit && !TextUtils.isEmpty(contactsInfo.getPhone_number())) {
            btnCall.setVisibility(View.VISIBLE);
        }
        views = new View[]{llName, llPhone, llBirthday, llAddress, llRemark};
        mPresenter.initItemData("itemAddWorker.json");
    }

    @OnClick(value = {R.id.iv_back, R.id.btn_confirm, R.id.rl_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_avatar:
                choosePhoto();
                break;
            case R.id.btn_confirm:
                contactsInfo.setName(ContentViewSetting.getEditTextContent(llName));
                contactsInfo.setPhone_number(ContentViewSetting.getEditTextContent(llPhone));
                contactsInfo.setHome_number(ContentViewSetting.getEditTextContent(llBirthday));
                contactsInfo.setDuty(ContentViewSetting.getEditTextContent(llAddress));
                contactsInfo.setNote(ContentViewSetting.getEditTextContent(llRemark));
                String errNotify = contactsInfo.checkData(this);
                if (TextUtils.isEmpty(errNotify)) {
                    if(isHasContacts(contactsInfo)){
                        ZToast.makeText(this,"该用户信息已存在",1000).show();
                        return;
                    }
                    long position = MyApplication.getApplication().getDaoSession().getContactsInfoDao().insertOrReplace(contactsInfo);
                    if (position != -1) {
                        showToast(isEdit?"新增成功":"编辑成功");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);
                    } else {
                        showToast("新增失败");
                    }
                } else {
                    showToast(errNotify);
                }
                break;
        }
    }

    private void choosePhoto() {
        PhotoSelector.builder()
                .setShowCamera(true)//显示拍照
                .setMaxSelectCount(1)//最大选择9 默认9，如果这里设置为-1则是不限数量
                .setGridColumnCount(3)//列数
                .setMaterialDesign(true)//design风格
                .setToolBarColor(ContextCompat.getColor(this, R.color.colorPrimary))//toolbar的颜色
                .setBottomBarColor(ContextCompat.getColor(this, R.color.colorPrimary))//底部bottombar的颜色
                .setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))//状态栏的颜色
                .start(this, PHOTO_CODE);
    }

    @Override
    public void getItemDataSuccess(List<ItemEditContent> responses) {
        for (int i = 0; i < responses.size(); i++) {
            ContentViewSetting.setItemContent(views[i], responses.get(i));
        }
        if (!isEdit) {
            ContentViewSetting.setEditTextContent(llName, contactsInfo.getName());
            ContentViewSetting.setEditTextContent(llPhone, contactsInfo.getPhone_number());
            if (!TextUtils.isEmpty(contactsInfo.getHome_number())) {
                ContentViewSetting.setEditTextContent(llBirthday, contactsInfo.getHome_number());
            }
            if (!TextUtils.isEmpty(contactsInfo.getDuty())) {
                ContentViewSetting.setEditTextContent(llAddress, contactsInfo.getDuty());
            }
            if (!TextUtils.isEmpty(contactsInfo.getNote())) {
                ContentViewSetting.setEditTextContent(llRemark, contactsInfo.getNote());
            }
        }
    }

    @Override
    public void existData(DriverInfo driverInfo) {

    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == PHOTO_CODE) {
            if (data != null) {
                ArrayList<String> imgs = data.getStringArrayListExtra(PhotoSelector.SELECT_RESULT);
                workerInfo.avatarPath = imgs.get(0);
                GlideUtils.loadCenterCropCircleImage(this, workerInfo.avatarPath, ivWorkerAvatar);
            }
        }*/
    }

    public void phoneCall(View view) {
        PermissionX.init(this).permissions(Manifest.permission.CALL_PHONE).onExplainRequestReason(new ExplainReasonCallback() {
            @Override
            public void onExplainReason(@NonNull ExplainScope explainScope, @NonNull List<String> list) {
                String message = "需要您同意以下权限才能正常使用";
                explainScope.showRequestReasonDialog(list, message, "Allow", "Deny");
            }
        }).request((b, list, list1) -> {
            if (b) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ContentViewSetting.getEditTextContent(llPhone)));
                startActivity(intent);
            } else {
                ZToast.makeText(AddWorkerActivity.this, "你已禁用拨打电话权限，请到应用设置开启权限", 2000).show();
            }
        });
    }

    public boolean isHasContacts(ContactsInfo contactsInfo) {
        List<ContactsInfo> contactsInfos = contactsInfoDao.queryBuilder().where(ContactsInfoDao.Properties.Name.eq(contactsInfo.getName()), ContactsInfoDao.Properties.Phone_number.eq(contactsInfo.getPhone_number())).build().list();
        return contactsInfos != null && contactsInfos.size() > 0;
    }
}
