package com.leon.carfixfactory.ui.custom.voice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.aip.asrwakeup3.core.mini.AutoCheck;
import com.baidu.aip.asrwakeup3.core.recog.RecogResult;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.leon.carfixfactory.R;
import com.leon.carfixfactory.utils.ZToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import static android.drm.DrmManagerClient.ERROR_NONE;


public class VoiceDialog extends Dialog implements EventListener {

    private String TAG = "VoiceDialog";


    private Context mContext;

    //显示说话的内容
    private TextView tv_input_text;

    //清空说话内内容按钮
    private TextView tv_cancel;
    //确定说话内容按钮
    private TextView tv_commit;

    //说话按钮
    private DiffuseView diffuseView;

    //取消对话框
    private ImageView iv_cancel;

    EventManager eventManager;

    //语音转文字结果
    private String voiceString;
    private String lastString = "";

    private BottonAnimDialogListener mListener;


    public VoiceDialog(Context context, BottonAnimDialogListener listener) {

        super(context, R.style.VoiceDialog);
        this.mContext = context;
        this.mListener = listener;
        eventManager = EventManagerFactory.create(mContext, "asr");
        eventManager.registerListener(this);
        initView();

    }

    private void initView() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottom_anim_dialog_layout, null);
        setContentView(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM); //显示在底部

        DisplayMetrics dm = new DisplayMetrics();

        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.width = dm.widthPixels; //设置dialog的宽度为当前手机屏幕的宽度
        params.height = dm.heightPixels * 2 / 5;

        getWindow().setAttributes((WindowManager.LayoutParams) params);

        tv_input_text = findViewById(R.id.tv_input_text);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_commit = findViewById(R.id.tv_commit);
        diffuseView = findViewById(R.id.diffuseView);
        iv_cancel = findViewById(R.id.iv_cancel);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText();
            }
        });

        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCommitListener(tv_input_text.getText().toString());
                dismiss();
            }
        });

        diffuseView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        isHasNetWork();
                        break;
                    case MotionEvent.ACTION_UP:
                        diffuseView.stop();
                        stop();
                        //未输入文字时候，显示向下按钮
                        if(TextUtils.isEmpty(tv_input_text.getText()))
                        {
                            iv_cancel.setVisibility(View.VISIBLE);
                            tv_cancel.setVisibility(View.INVISIBLE);
                            tv_commit.setVisibility(View.INVISIBLE);
                        }
                        else {
                            iv_cancel.setVisibility(View.GONE);
                            tv_cancel.setVisibility(View.VISIBLE);
                            tv_commit.setVisibility(View.VISIBLE);
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        //快速划过时候，可能不执行ACTION_UP方法
                        diffuseView.stop();
                        stop();
                        //未输入文字时候，显示向下按钮
                        if(TextUtils.isEmpty(tv_input_text.getText()))
                        {
                            iv_cancel.setVisibility(View.VISIBLE);
                            tv_cancel.setVisibility(View.INVISIBLE);
                            tv_commit.setVisibility(View.INVISIBLE);
                        }
                        else {
                            iv_cancel.setVisibility(View.GONE);
                            tv_cancel.setVisibility(View.VISIBLE);
                            tv_commit.setVisibility(View.VISIBLE);
                        }
                        break;
                }

                return true;
            }
        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceString = "";
                dismiss();
            }
        });

    }


    public void setClickListener(BottonAnimDialogListener listener) {
        this.mListener = listener;
    }


    /**
     * 清空语音转文字结果
     */
    private void clearText() {
        iv_cancel.setVisibility(View.VISIBLE);
        tv_cancel.setVisibility(View.INVISIBLE);
        tv_commit.setVisibility(View.INVISIBLE);
        tv_input_text.setHint(R.string.voice_tips);
        tv_input_text.setText(null);
        lastString = "";
    }


    /**
     * 开始说话时候问题提示以及颜色
     *
     * @param resId
     * @param type  0：hint 1:text
     */
    public void setTv_input_text(int resId, int type) {
        if (type == 0) {
            tv_input_text.setHint(resId);
        } else {
            tv_input_text.setText(resId);

        }
    }

    /**
     * 开始说话时候问题提示以及颜色
     *
     * @param text
     */
    public void setTv_input_text(String text) {

        tv_input_text.setText(lastString+text);
        if(TextUtils.isEmpty(text))
        {
            iv_cancel.setVisibility(View.VISIBLE);
            tv_cancel.setVisibility(View.INVISIBLE);
            tv_commit.setVisibility(View.INVISIBLE);
        }
        else
        {
            iv_cancel.setVisibility(View.GONE);
            tv_cancel.setVisibility(View.VISIBLE);
            tv_commit.setVisibility(View.VISIBLE);
        }

    }


    public interface BottonAnimDialogListener {

        void onCommitListener(String voiceString);
    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)) {
            // 引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了
            setTv_input_text(R.string.voice_please_say, 0);
        }
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
            String aa = "111";
            // 识别结束时候，保存上次存的内容
            if (params != null) {
                try {
                    JSONObject json = new JSONObject(params);
                    int error = json.optInt("error");
                    if (error == ERROR_NONE) {
                        lastString = tv_input_text.getText().toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            // 识别结束
            if (params != null) {
                RecogResult recogResult = RecogResult.parseJson(params);
                // 临时识别结果, 长语音模式需要从此消息中取出结果
                String[] results = recogResult.getResultsRecognition();
                voiceString = results.length > 0 ? results[0] : "";
                setTv_input_text(voiceString);
            }

        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_VOLUME)) {
            Volume vol = parseVolumeJson(params);
            Log.e(TAG, "volumePercent" + vol.volumePercent + "volume:" + vol.volume);
            diffuseView.start(vol.volumePercent);
        }
    }

    /**
     * 基于SDK集成2.2 发送开始事件
     * 点击开始按钮
     * 测试参数填在这里
     */
    private void start() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event

        /*if (enableOffline) {
            params.put(SpeechConstant.DECODER, 2);
        }*/
        // 基于SDK集成2.1 设置识别参数
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // params.put(SpeechConstant.NLU, "enable");
        //params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 作用是静音断句的时长设置，值建议800ms-3000ms可以调节此参数。使用长语音功能时不能使用这个参数。
        //params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        params.put(SpeechConstant.VAD, SpeechConstant.VAD_TOUCH);
        params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, true);
        //请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        (new AutoCheck(mContext.getApplicationContext(), new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
                        Log.e(TAG, message);

                    }
                }
            }
        }, false)).checkAsr(params);
        String json = null; // 可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        eventManager.send(event, json, null, 0, 0);
    }

    /**
     * 点击停止按钮
     * 基于SDK集成4.1 发送停止事件
     */
    private void stop() {
        eventManager.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }

    private Volume parseVolumeJson(String jsonStr) {
        Volume vol = new Volume();
        vol.origalJson = jsonStr;
        try {
            JSONObject json = new JSONObject(jsonStr);
            vol.volumePercent = json.getInt("volume-percent");
            vol.volume = json.getInt("volume");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vol;
    }

    private class Volume {
        private int volumePercent = -1;
        private int volume = -1;
        private String origalJson;
    }


    /**
     * Activity 页面onPause时候使用
     */
    public void onPause() {
        eventManager.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        Log.i(TAG, "On pause");
    }

    /**
     * Activity 页面onDestroy时候使用
     */
    public void onDestroy() {
        // 基于SDK集成4.2 发送取消事件
        eventManager.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        // 基于SDK集成5.2 退出事件管理器
        // 必须与registerListener成TAG对出现，否则可能造成内存泄露
        eventManager.unregisterListener(this);
        Log.i(TAG, "On Destroy");
    }

    private void isHasNetWork() {

        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取代表联网状态的NetWorkInfo对象
        NetworkInfo netInfo = cm.getActiveNetworkInfo(); // getActiveNetworkInfo()
        if (netInfo == null || !netInfo.isAvailable()) { // 提示没有网络
            ZToast.makeText(((Activity) mContext),"网络异常，请稍后重试",1000).show();
        }
        else{
            //todo
            diffuseView.start();
            start();
            iv_cancel.setVisibility(View.INVISIBLE);
            tv_cancel.setVisibility(View.INVISIBLE);
            tv_commit.setVisibility(View.INVISIBLE);
        }
    }
}
