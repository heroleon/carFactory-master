package com.leon.carfixfactory.ui.fragment;

import android.os.Bundle;
import android.view.View;

/**
 * Created by leon
 * Date: 2019/11/7
 * Time: 21:23
 * Desc:
 */
public class DeliveryCarFragment extends BaseRepairFragment {
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        isDelivery = true;
        super.initView(view, savedInstanceState);
    }
}
