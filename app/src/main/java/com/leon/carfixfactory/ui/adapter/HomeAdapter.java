package com.leon.carfixfactory.ui.adapter;

import android.content.Context;

import android.view.View;


import androidx.recyclerview.widget.RecyclerView;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.HomeData;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerAdapter;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerHolder;

import java.util.List;

public class HomeAdapter extends BaseRecyclerAdapter<HomeData> {
    private OnItemClickListener mItemClickListener;

    public HomeAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, HomeData item, final int position) {
        holder.setImageResource(R.id.iv_icon, getIconRes(item.icon));
        holder.setText(R.id.tv_title, item.title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(recyclerView, view, position);

                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private int getIconRes(String idRes) {
        return context.getResources().getIdentifier(idRes, "mipmap", context.getPackageName());
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView parent, View view, int position);
    }
}
