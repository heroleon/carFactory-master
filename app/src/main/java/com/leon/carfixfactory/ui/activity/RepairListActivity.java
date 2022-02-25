package com.leon.carfixfactory.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.ui.adapter.base.BaseViewPagerAdapter;
import com.leon.carfixfactory.ui.fragment.BaseFragment;
import com.leon.carfixfactory.ui.fragment.DeliveryCarFragment;
import com.leon.carfixfactory.ui.fragment.UnDeliveryCarFragment;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by leon
 * Date: 2019/11/7
 * Time: 19:27
 * Desc:维修记录列表
 */
public class RepairListActivity extends BaseActivity {
    @Bind(R.id.tab)
    QMUITabSegment tabSegment;
    @Bind(R.id.view_pager)
    QMUIViewPager view_pager;

    @Bind(R.id.tv_title)
    TextView tvTitle;

    private List<BaseFragment> mFragments;

    @Override
    protected void initPresenter(Intent intent) {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_repair_list;
    }

    @Override
    protected void initView() {
        initSupportToolBar();
        initFragments();
        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((UnDeliveryCarFragment) mFragments.get(0)).refreshData();
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new UnDeliveryCarFragment());
        mFragments.add(new DeliveryCarFragment());
    }

    private void initViewPager() {
        tabSegment.setHasIndicator(true);
        tabSegment.setIndicatorPosition(false);
        tabSegment.setDefaultSelectedColor(getResources().getColor(R.color.colorPrimary));
        tabSegment.setupWithViewPager(view_pager);
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), mFragments, Arrays.asList(getResources().getStringArray(R.array.tab_repair)));
        view_pager.setAdapter(adapter);
    }

    private void initSupportToolBar() {
        tvTitle.setText(getString(R.string.title_repair_record));
    }

    @OnClick(R.id.iv_back)
    public void back(View view) {
        finish();
    }
}
