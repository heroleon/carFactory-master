package com.leon.carfixfactory.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.HomeData;
import com.leon.carfixfactory.contract.HomeContact;
import com.leon.carfixfactory.presenter.HomeDataPresenterImp;
import com.leon.carfixfactory.ui.activity.MaintenanceRecordActivity;
import com.leon.carfixfactory.ui.activity.RepairListActivity;
import com.leon.carfixfactory.ui.activity.DriverRecordListActivity;
import com.leon.carfixfactory.ui.activity.WorkerManageActivity;
import com.leon.carfixfactory.ui.adapter.HomeAdapter;
import com.leon.carfixfactory.ui.custom.NoScrollGridLayoutManager;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by leon
 * Date: 2019/7/24
 * Time: 9:47
 * Desc: 首页
 */
public class HomeFragment extends BaseFragment<HomeDataPresenterImp> implements HomeContact.ViewHome, HomeAdapter.OnItemClickListener {
    @Bind(R.id.home_recycler_view)
    RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new HomeDataPresenterImp(getActivity(), this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initRecyclerView();
        mPresenter.getHomeData("itemHomeData.json");
    }

    private void initRecyclerView() {
        NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(getActivity(), 3);
        layoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new HomeAdapter(mActivity, R.layout.item_home_data);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void getHomeDataSuccess(List<HomeData> responses) {
        if (responses != null && responses.size() != 0) {
            mAdapter.updateWithClear(responses);
        } else {
            ShowToast("未取到数据");
        }
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }


    @OnClick({R.id.tv_repair_record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_repair_record:
                Intent intent = new Intent(getActivity(), RepairListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        Intent intent;
        switch (position) {
            case 0:
                showToast("暂未开发，开发中...");
                break;
            case 1:
                intent = new Intent(getActivity(), MaintenanceRecordActivity.class);
                startActivity(intent);
                break;
            case 2:
                showToast("暂未开发，开发中...");
                break;
            case 3:
                showToast("暂未开发，开发中...");
                break;
            case 4:
                intent = new Intent(getActivity(), DriverRecordListActivity.class);
                startActivity(intent);
                break;
            case 5:
                intent = new Intent(getActivity(), WorkerManageActivity.class);
                startActivity(intent);
                break;

        }
    }
}
