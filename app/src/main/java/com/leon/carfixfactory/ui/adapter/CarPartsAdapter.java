package com.leon.carfixfactory.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.CarPartsInfo;
import com.leon.carfixfactory.contract.RepairRecordContact;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerAdapter;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerHolder;

/**
 * Created by leon
 * Date: 2019/9/20
 * Time: 14:48
 * Desc: 维修项目适配器
 */
public class CarPartsAdapter extends BaseRecyclerAdapter<RepairRecordContact.DialogContentObtain> {
    private Resources res;
    private OnDelClickListener onDelClickListener;

    public CarPartsAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        res = context.getResources();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, RepairRecordContact.DialogContentObtain item, final int position) {
        holder.setText(R.id.tv_part_name, item.getFirstContent());
        holder.setText(R.id.tv_part_count, String.format(res.getString(R.string.part_count), item.getThirdContent()));
        holder.setText(R.id.tv_part_price, String.format(res.getString(R.string.part_price), item.getSecondContent()));
        holder.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDelClickListener != null) {
                    onDelClickListener.onDelete(position);
                }
            }
        });
    }

    public void setOnDelClickListener(OnDelClickListener listener) {
        onDelClickListener = listener;
    }

    public interface OnDelClickListener {
        void onDelete(int position);
    }
}
