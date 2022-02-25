package com.leon.carfixfactory.ui.adapter;

import android.content.Context;
import android.view.View;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.RepairRecord;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerAdapter;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerHolder;
import com.leon.carfixfactory.utils.DateTimeUtils;

/**
 * Created by leon
 * Date: 2019/11/7
 * Time: 20:13
 * Desc:列表适配器
 */

public class RepairListAdapter extends BaseRecyclerAdapter<RepairRecord> {

    private OnItemOptionClickListener optionClickListener;

    public RepairListAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, RepairRecord item, final int position) {
        holder.setText(R.id.tv_title, String.format(context.getString(R.string.car_repair_order_id), item.repairOrderId));
        holder.setText(R.id.tv_repair_numberPlate, item.numberPlate);
        holder.setText(R.id.tv_total_fee, String.format(context.getString(R.string.part_price), item.repairTotalFee));

        int visible = item.repairState == 2 ? View.GONE : View.VISIBLE;
        holder.setVisibility(R.id.iv_state, visible);
        holder.setVisibility(R.id.tv_first, visible);
        holder.setVisibility(R.id.tv_second, visible);
        holder.setVisibility(R.id.tv_third, visible);

        if (item.repairState != 2) {
            holder.setText(R.id.tv_time, DateTimeUtils.millisecondToDate(item.arrivalTime, DateTimeUtils.YYYY_MM_DD));
            int drawableId = item.repairState == 0 ? R.mipmap.icon_wait_repair : R.mipmap.icon_repairing;
            holder.setImageResource(R.id.iv_state, drawableId);
            if (item.repairState == 0) {
                holder.setVisibility(R.id.tv_third, View.VISIBLE);
                holder.setVisibility(R.id.tv_first, View.GONE);
            } else {
                holder.setVisibility(R.id.tv_third, View.GONE);
                holder.setVisibility(R.id.tv_first, View.VISIBLE);
            }
        } else {
            holder.setText(R.id.tv_time, DateTimeUtils.millisecondToDate(item.deliveryTime, DateTimeUtils.YYYY_MM_DD));
        }
        holder.getView(R.id.tv_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionClickListener != null) {
                    optionClickListener.firstBtnClick(position);
                }
            }
        });

        holder.getView(R.id.tv_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionClickListener != null) {
                    optionClickListener.secondBtnClick(position);
                }
            }
        });

        holder.getView(R.id.tv_third).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionClickListener != null) {
                    optionClickListener.thirdBtnClick(position);
                }
            }
        });

    }

    public void setOnItemOptionClickListener(OnItemOptionClickListener listener) {
        this.optionClickListener = listener;
    }

    public interface OnItemOptionClickListener {
        void firstBtnClick(int position);

        void secondBtnClick(int position);

        void thirdBtnClick(int position);
    }
}
