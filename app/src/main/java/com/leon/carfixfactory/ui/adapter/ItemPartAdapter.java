package com.leon.carfixfactory.ui.adapter;

import android.content.Context;
import android.content.res.Resources;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.CarPartsInfo;
import com.leon.carfixfactory.contract.RepairRecordContact;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerAdapter;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerHolder;

/**
 * Created by leon
 * Date: 2019/9/21
 * Time: 10:39
 * Desc:清单列表
 */
public class ItemPartAdapter extends BaseRecyclerAdapter<RepairRecordContact.DialogContentObtain> {
    private Resources res;

    public ItemPartAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        res = context.getResources();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, RepairRecordContact.DialogContentObtain item, int position) {
        holder.setText(R.id.tv_part_name, item.getFirstContent());
        holder.setText(R.id.tv_part_count, String.format(res.getString(R.string.part_count), item.getThirdContent()));
        holder.setText(R.id.tv_part_price, String.format(res.getString(R.string.part_price), item.getSecondContent()));
    }
}
