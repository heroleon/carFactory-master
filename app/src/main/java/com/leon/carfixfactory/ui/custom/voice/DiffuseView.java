package com.leon.carfixfactory.ui.custom.voice;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.leon.carfixfactory.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangyi
 * 自定义圆圈扩散View
 */
public class DiffuseView extends View {

    /**
     * 扩散圆圈颜色
     */
    private int mColor = getResources().getColor(R.color.colorAccent);
    /**
     * 圆圈中心颜色
     */
    private int mCoreColor = getResources().getColor(R.color.colorPrimary);
    /**
     * 圆圈中心图片
     */
    private Bitmap mBitmap;

    /**
     * 圆圈中心图片
     */
    private Bitmap mBitmapPress;
    /**
     * 中心圆半径
     */
    private float mCoreRadius = 150;
    /**
     * 扩散圆宽度
     */
    private int mDiffuseWidth = 3;
    /**
     * 最大宽度
     */
    private Integer mMaxWidth = 255;
    /**
     * 是否正在扩散中
     */
    private volatile boolean mIsDiffuse = false;
    // 透明度集合
    private List<Integer> mAlphas = new ArrayList<>();
    // 扩散圆半径集合
    private List<Integer> mWidths = new ArrayList<>();
    private Paint mPaint;

    //根据声音大小画圆环大小
    private int voiceWidth;

    public DiffuseView(Context context) {
        this(context, null);
    }

    public DiffuseView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DiffuseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DiffuseView, defStyleAttr, 0);
        mColor = a.getColor(R.styleable.DiffuseView_diffuse_color, mColor);
        mCoreColor = a.getColor(R.styleable.DiffuseView_diffuse_coreColor, mCoreColor);
        mCoreRadius = a.getFloat(R.styleable.DiffuseView_diffuse_coreRadius, mCoreRadius);
        mDiffuseWidth = a.getInt(R.styleable.DiffuseView_diffuse_width, mDiffuseWidth);
        mMaxWidth = a.getInt(R.styleable.DiffuseView_diffuse_maxWidth, mMaxWidth);
        mBitmapPress = BitmapFactory.decodeResource(getResources(), R.mipmap.voice_input_press);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.voice_input);
        a.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mAlphas.add(255);
        mWidths.add(0);
    }

    @Override
    public synchronized void invalidate() {
        if (hasWindowFocus()) {
            super.invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {

        if (!mIsDiffuse) {
            if (mBitmap != null) {
                canvas.drawBitmap(mBitmap, getWidth() / 2 - mBitmap.getWidth() / 2
                        , getHeight() / 2 - mBitmap.getHeight() / 2, mPaint);
            }
            return;

        }

        // 绘制扩散圆
        mPaint.setColor(mColor);
        for (int i = 0; i < mAlphas.size(); i++) {
            // 设置透明度
            Integer alpha = mAlphas.get(i);
            mPaint.setAlpha(alpha);
            // 绘制扩散圆
           // Integer width = mWidths.get(i);
            canvas.drawCircle(getWidth() / 2 , getHeight() / 2, getWidth() / 2-(50-voiceWidth/2), mPaint);

           /* if (alpha > 0 && width < mMaxWidth) {
                mAlphas.set(i, alpha - 1);
                mWidths.set(i, width + 1);
            }*/
        }
        // 判断当扩散圆扩散到指定宽度时添加新扩散圆
        /*if (mWidths.get(mWidths.size() - 1) == mMaxWidth / mDiffuseWidth) {
            mAlphas.add(255);
            mWidths.add(0);
        }*/
        // 超过10个扩散圆，删除最外层
        /*if(mWidths.size() >= 10){
            mWidths.remove(0);
            mAlphas.remove(0);
        }*/

        // 绘制中心圆及图片
        mPaint.setAlpha(255);
        mPaint.setColor(mCoreColor);
        //canvas.drawCircle(getWidth() / 2, getHeight() / 2, mCoreRadius, mPaint);

        if (mBitmapPress != null) {
            canvas.drawBitmap(mBitmapPress, getWidth() / 2 - mBitmapPress.getWidth() / 2
                    , getHeight() / 2 - mBitmapPress.getHeight() / 2, mPaint);
        }

        /*if(mIsDiffuse){
            invalidate();
        }*/

    }

    /**
     * 开始扩散
     */
    public void start() {
        mIsDiffuse = true;
        invalidate();
    }

    /**
     * 根据声音大小改变 圆的大小 ** 有时候在已经没有按住说话按钮时候，仍然回调该方案，故需要加判断
     */
    public void start(int width) {
        if(mIsDiffuse)
        {
            voiceWidth = width;
            invalidate();
        }

    }

    /**
     * 停止扩散
     */
    public void stop() {
        mIsDiffuse = false;
        invalidate();
    }

    /**
     * 是否扩散中
     */
    public boolean isDiffuse() {
        return mIsDiffuse;
    }

    /**
     * 设置扩散圆颜色
     */
    public void setColor(int colorId) {
        mColor = colorId;
    }

    /**
     * 设置中心圆颜色
     */
    public void setCoreColor(int colorId) {
        mCoreColor = colorId;
    }

    /**
     * 设置中心圆图片
     */
    public void setCoreImage(int imageId) {
        mBitmap = BitmapFactory.decodeResource(getResources(), imageId);
    }

    /**
     * 设置中心圆半径
     */
    public void setCoreRadius(int radius) {
        mCoreRadius = radius;
    }

    /**
     * 设置扩散圆宽度(值越小宽度越大)
     */
    public void setDiffuseWidth(int width) {
        mDiffuseWidth = width;
    }

    /**
     * 设置最大宽度
     */
    public void setMaxWidth(int maxWidth) {
        mMaxWidth = maxWidth;
    }

}