package com.leon.carfixfactory.ui.view;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.bigkoo.quicksidebar.R.array;
import com.bigkoo.quicksidebar.R.dimen;
import com.bigkoo.quicksidebar.R.styleable;
import com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.leon.carfixfactory.R;

import java.util.Arrays;
import java.util.List;

public class MyQuickSideBarView extends View {
    private OnQuickSideBarTouchListener listener;
    private List<String> mLetters;
    private int mChoose;
    private Paint mPaint;
    private float mTextSize;
    private float mTextSizeChoose;
    private int mTextColor;
    private int mTextColorChoose;
    private int mWidth;
    private int mHeight;
    private float mItemHeight;
    private float mItemStartY;

    public MyQuickSideBarView(Context context) {
        this(context, (AttributeSet)null);
    }

    public MyQuickSideBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyQuickSideBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mChoose = -1;
        this.mPaint = new Paint();
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mLetters = Arrays.asList(context.getResources().getStringArray(array.quickSideBarLetters));
        this.mTextSize = (float)context.getResources().getDimensionPixelSize(dimen.textSize_quicksidebar);
        this.mTextSizeChoose = (float)context.getResources().getDimensionPixelSize(dimen.textSize_quicksidebar_choose);
        this.mItemHeight = context.getResources().getDimension(dimen.height_quicksidebaritem);
        if (attrs != null) {
            TypedArray a = this.getContext().obtainStyledAttributes(attrs, styleable.QuickSideBarView);
            this.mTextColor = a.getColor(styleable.QuickSideBarView_sidebarTextColor, this.mTextColor);
            this.mTextColorChoose = a.getColor(styleable.QuickSideBarView_sidebarTextColorChoose, this.mTextColorChoose);
            this.mTextSize = a.getDimension(styleable.QuickSideBarView_sidebarTextSize, this.mTextSize);
            this.mTextSizeChoose = a.getDimension(styleable.QuickSideBarView_sidebarTextSizeChoose, this.mTextSizeChoose);
            this.mItemHeight = a.getDimension(styleable.QuickSideBarView_sidebarItemHeight, this.mItemHeight);
            a.recycle();
        }

        this.post(new Runnable() {
            @Override
            public void run() {
                mHeight =getHeight();
                mWidth = getWidth();
                mItemStartY = ((float)mHeight - (float)mLetters.size() *mItemHeight) / 2.0F;
                invalidate();
            }
        });

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i = 0; i < this.mLetters.size(); ++i) {
            this.mPaint.setColor(this.mTextColor);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setTextSize(this.mTextSize);
            if (i == this.mChoose) {
                this.mPaint.setColor(this.mTextColorChoose);
                this.mPaint.setFakeBoldText(true);
                this.mPaint.setTypeface(Typeface.DEFAULT_BOLD);
                this.mPaint.setTextSize(this.mTextSizeChoose);
            }

            Rect rect = new Rect();
            this.mPaint.getTextBounds((String)this.mLetters.get(i), 0, ((String)this.mLetters.get(i)).length(), rect);
            float xPos = (float)((int)((double)(this.mWidth - rect.width()) * 0.5D));
            float yPos = this.mItemHeight * (float)i + (float)((int)((double)(this.mItemHeight - (float)rect.height()) * 0.5D)) + this.mItemStartY;
            canvas.drawText((String)this.mLetters.get(i), xPos, yPos, this.mPaint);
            this.mPaint.reset();
        }


    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        int oldChoose = this.mChoose;
        int newChoose = (int)((y - this.mItemStartY) / this.mItemHeight);
        switch(action) {
            case 1:
                this.mChoose = -1;
                if (this.listener != null) {
                    this.listener.onLetterTouching(false);
                }

                this.invalidate();
                break;
            default:
                if (oldChoose != newChoose) {
                    if (newChoose >= 0 && newChoose < this.mLetters.size()) {
                        this.mChoose = newChoose;
                        if (this.listener != null) {
                            Rect rect = new Rect();
                            this.mPaint.getTextBounds((String)this.mLetters.get(this.mChoose), 0, ((String)this.mLetters.get(this.mChoose)).length(), rect);
                            float yPos = this.mItemHeight * (float)this.mChoose + (float)((int)((double)(this.mItemHeight - (float)rect.height()) * 0.5D)) + this.mItemStartY;
                            this.listener.onLetterChanged((String)this.mLetters.get(newChoose), this.mChoose, yPos);
                        }
                    }

                    this.invalidate();
                }

                if (event.getAction() == 3) {
                    if (this.listener != null) {
                        this.listener.onLetterTouching(false);
                    }
                } else if (event.getAction() == 0 && this.listener != null) {
                    this.listener.onLetterTouching(true);
                }
        }

        return true;
    }

    public OnQuickSideBarTouchListener getListener() {
        return this.listener;
    }

    public void setOnQuickSideBarTouchListener(OnQuickSideBarTouchListener listener) {
        this.listener = listener;
    }

    public List<String> getLetters() {
        return this.mLetters;
    }

    public void setLetters(List<String> letters) {
        this.mLetters = letters;
        this.invalidate();
    }
}
