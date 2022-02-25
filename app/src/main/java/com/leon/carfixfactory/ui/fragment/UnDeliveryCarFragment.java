package com.leon.carfixfactory.ui.fragment;

import android.os.Bundle;
import android.view.View;

/**
 * Created by leon
 * Date: 2019/11/7
 * Time: 21:24
 * Desc:
 */
public class UnDeliveryCarFragment extends BaseRepairFragment {
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        isDelivery = false;
        super.initView(view, savedInstanceState);
    }
}
