package com.leon.carfixfactory.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by lenovo on 2018/5/13.
 */

public class DividerDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private Context context;

    public DividerDecoration(Context context) {
        this.context = context;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    private int getOrientation(RecyclerView parent) {
        LinearLayoutManager layoutManager;
        try {
            layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        } catch (ClassCastException e) {
            throw new IllegalStateException("DividerDecoration can only be used with a " +
                    "LinearLayoutManager.", e);
        }
        return layoutManager.getOrientation();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        if (getOrientation(parent) == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int recyclerViewTop = parent.getPaddingTop();
        final int recyclerViewBottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = Math.max(recyclerViewTop, child.getBottom() + params.bottomMargin);
            final int bottom = Math.min(recyclerViewBottom, top + mDivider.getIntrinsicHeight());
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int recyclerViewLeft = parent.getPaddingLeft();
        final int recyclerViewRight = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = Math.max(recyclerViewLeft, child.getRight() + params.rightMargin);
            final int right = Math.min(recyclerViewRight, left + mDivider.getIntrinsicHeight());
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public int dpToPx(int dpValue) {
        return (int) (dpValue * screenRate(context) + 0.5f);
    }

    //?????????????????????
    public static float screenRate(Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return scale;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (getOrientation(parent) == VERTICAL_LIST) {
            outRect.set(0, 0, 0, 1);
        } else {
            outRect.set(0, 0, 1, 0);
        }
    }
}