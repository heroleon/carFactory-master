package com.leon.carfixfactory.ui.adapter;

import android.content.Context;
import android.content.res.Resources;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.DriverInfo;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerAdapter;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerHolder;

public class RepairRecordAdapter extends BaseRecyclerAdapter<DriverInfo> {

    private Resources res;

    public RepairRecordAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        this.res = context.getResources();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, DriverInfo item, int position) {
        holder.setText(R.id.tv_car_card, item.numberPlate);
        holder.setText(R.id.tv_car_owner_name, String.format(res.getString(R.string.car_owner_name), item.driverName));
        holder.setText(R.id.tv_car_owner_phone, String.format(res.getString(R.string.car_owner_phone), item.driverPhone));
        holder.setText(R.id.tv_driver_address, item.driverAddress);
    }
}
