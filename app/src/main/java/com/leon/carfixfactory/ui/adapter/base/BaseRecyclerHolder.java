package com.leon.carfixfactory.ui.adapter.base;

import android.text.Html;
import android.view.View;

/**
 * Created by 皓然 on 2017/7/19.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.leon.carfixfactory.utils.GlideUtils;


/**
 * 万能的RecyclerView的ViewHolder
 * Created by leon.
 */
public class BaseRecyclerHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;
    private Context context;

    private BaseRecyclerHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        //指定一个初始为8
        views = new SparseArray<>(8);
    }

    /**
     * 取得一个RecyclerHolder对象
     *
     * @param context  上下文
     * @param itemView 子项
     * @return 返回一个RecyclerHolder对象
     */
    public static BaseRecyclerHolder getRecyclerHolder(Context context, View itemView) {
        return new BaseRecyclerHolder(context, itemView);
    }

    public SparseArray<View> getViews() {
        return this.views;
    }

    /**
     * 通过view的id获取对应的控件，如果没有则加入views中
     *
     * @param viewId 控件的id
     * @return 返回一个控件
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置字符串
     */
    public BaseRecyclerHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        if (text == null) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
        return this;
    }

    public BaseRecyclerHolder setHtmlText(int viewId, String text) {
        TextView tv = getView(viewId);
        if (text == null) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setText(Html.fromHtml(text));
        }
        return this;
    }

    /**
     * 设置图片
     */
    public BaseRecyclerHolder setImageResource(int viewId, int drawableId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(drawableId);
        return this;
    }

    /**
     * 设置图片
     */
    public BaseRecyclerHolder setFrameLayoutImageResource(int viewId, int drawableId) {
        FrameLayout iv = getView(viewId);
        iv.setBackgroundResource(drawableId);
        return this;
    }

    /**
     * 设置图片
     */
    public BaseRecyclerHolder setTextViewColor(int viewId, int color) {
        TextView iv = getView(viewId);
        iv.setTextColor(color);
        return this;
    }

    /**
     * 设置图片
     */
    public BaseRecyclerHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 设置图片
     */
    public BaseRecyclerHolder setImageByUrl(int viewId, String url) {
        ImageView iv = getView(viewId);
        GlideUtils.loadDefaultImage(context, url, iv);
        return this;
    }

    /**
     * 设置圆形图片
     */
    public BaseRecyclerHolder setCircleImageByUrl(int viewId, String url) {
        ImageView iv = getView(viewId);
        GlideUtils.loadCenterCropCircleImage(context, url, iv);
        return this;
    }

    public BaseRecyclerHolder setVisibility(int viewId, int visible) {
        View view = getView(viewId);
        view.setVisibility(visible);
        return this;
    }

    public BaseRecyclerHolder setTextViewAppearanceColor(int viewId, int resId) {
        TextView iv = getView(viewId);
        iv.setTextAppearance(context, resId);
        return this;
    }
}
