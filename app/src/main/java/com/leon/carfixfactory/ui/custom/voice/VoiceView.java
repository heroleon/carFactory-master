package com.leon.carfixfactory.ui.custom.voice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.utils.ContentViewSetting;
import com.leon.carfixfactory.utils.ZToast;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class VoiceView extends LinearLayout implements VoiceDialog.BottonAnimDialogListener{

    private boolean isShowMustTextView;

    private String textViewTips;

    private String editTextHint ;

    private int editTextMaxSize = 500;

    private View view;

    private TextView tv_must_input;
    private TextView tv_voice_describe;
    private TextView voice_input;
    private TextView tv_remaining;
    private EditText et_remark;

    private Context mContext;


    public VoiceView(Context context) {
        this(context, null);
    }

    public VoiceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);

    }

    public VoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VoiceView,defStyleAttr,0);
        isShowMustTextView = ta.getBoolean(R.styleable.VoiceView_is_must_show,isShowMustTextView);
        textViewTips = ta.getString(R.styleable.VoiceView_textview_tips);
        editTextHint = ta.getString(R.styleable.VoiceView_edittext_hint);
        editTextMaxSize = ta.getInt(R.styleable.VoiceView_edittext_maxsize, editTextMaxSize);
        ta.recycle();

        view = LayoutInflater.from(context).inflate(R.layout.view_voice_input, this);
        tv_must_input = view.findViewById(R.id.tv_must_input);
        tv_voice_describe = view.findViewById(R.id.tv_voice_describe);
        et_remark = view.findViewById(R.id.et_remark);
        tv_remaining = view.findViewById(R.id.tv_remaining);
        voice_input = view.findViewById(R.id.voice_input);

        if(isShowMustTextView)
        {
            tv_must_input.setVisibility(VISIBLE);
        }
        else {
            tv_must_input.setVisibility(GONE);
        }

        if(!TextUtils.isEmpty(textViewTips))
        {
            tv_voice_describe.setText(textViewTips);
        }
        if(!TextUtils.isEmpty(editTextHint))
        {
            et_remark.setHint(editTextHint);
        }

        //设置最大字数
        InputFilter[] filters={new InputFilter.LengthFilter(editTextMaxSize)};
        et_remark.setFilters(filters);
        tv_remaining.setText(String.valueOf(editTextMaxSize));
        et_remark.clearFocus();//清除焦点
        TextWatcher remarkWatcher= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //改变数据源
            }

            @Override
            public void afterTextChanged(Editable s) {
                ContentViewSetting.setRemainingSize(s,tv_remaining,editTextMaxSize);
            }
        };
        et_remark.addTextChangedListener(remarkWatcher);
        et_remark.setTag(remarkWatcher);

        voice_input.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startAndRegister();
            }
        });

    }

    /**
     * 获取文本框文字
     * @return
     */
    public String getEditText()
    {
        return  et_remark.getText().toString();
    }


    /**
     * 设置右边语音输入是否显示，以及编辑框是否可以编辑
     * @return
     */
    public void setRightShow(boolean flag,boolean isEdit)
    {
        if(flag)
        {
            voice_input.setVisibility(VISIBLE);
            tv_remaining.setVisibility(VISIBLE);

        }
        else {
            voice_input.setVisibility(GONE);
            tv_remaining.setVisibility(GONE);

        }

        if(isEdit)
        {
            et_remark.setFocusableInTouchMode(true);
            et_remark.setFocusable(true);
            et_remark.requestFocus();
        }
        else {
            et_remark.setFocusable(false);
            et_remark.setFocusableInTouchMode(false);
        }


    }


    /**
     * 设置文本框文字
     * @return
     */
    public void setEditText(String text)
    {
        et_remark.setText(text);
    }

    public void setEditTextHint(String text)
    {
        et_remark.setHint(text);
    }

    @Override
    public void onCommitListener(String voiceString) {
        if(TextUtils.isEmpty(voiceString))
        {
            return;
        }
        et_remark.setText(et_remark.getText().toString()+voiceString);
        et_remark.setSelection(et_remark.getText().length());
    }

    /**
     * 请求权限并且开始语音功能
     */
    @SuppressLint("CheckResult")
    public void startAndRegister(){
        RxPermissions rxPermissions = new RxPermissions((FragmentActivity) mContext);

        //合并查询结果
        rxPermissions.requestEachCombined(Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if(permission.granted){
                            // Storage permission are allowed.
                            new VoiceDialog(mContext,VoiceView.this).show();
                        }else if(permission.shouldShowRequestPermissionRationale){
                            //  Denied permission without ask never again
                            ZToast.makeText(((Activity) mContext),"请打开麦克风权限",1000).show();
                        }else{
                            // Denied permission with ask never again
                            // Need to go to the settings
                            ZToast.makeText(((Activity) mContext),"请打开麦克风权限",1000).show();
                        }
                    }
                });

    }

    public void setTips(String text) {
        tv_voice_describe.setText(text);
    }
}
