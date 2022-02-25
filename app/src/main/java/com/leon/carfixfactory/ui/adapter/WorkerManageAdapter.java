package com.leon.carfixfactory.ui.adapter;

import android.content.Context;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.WorkerInfo;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerAdapter;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerHolder;

public class WorkerManageAdapter extends BaseRecyclerAdapter<WorkerInfo> {

    public WorkerManageAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, WorkerInfo item, int position) {
        holder.setCircleImageByUrl(R.id.iv_avatar, item.getAvatarPath());
        holder.setText(R.id.tv_name, item.workerName);
        holder.setText(R.id.tv_phone, item.workerPhone);
        holder.setText(R.id.tv_address, item.workerAddress);
    }

}
