package com.leon.carfixfactory.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.WorkerInfo;
import com.leon.carfixfactory.contract.WorkerManageContact;
import com.leon.carfixfactory.presenter.WorkerManageImp;
import com.leon.carfixfactory.ui.adapter.WorkerManageAdapter;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class WorkerManageActivity extends BaseActivity<WorkerManageImp> implements WorkerManageContact.ViewWorkerManage {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private WorkerManageAdapter mAdapter;
    public static final String WORKER_INFO = "worker_info";


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new WorkerManageImp(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_worker_manage;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.title_worker_manage));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new WorkerManageAdapter(this, R.layout.item_worker_manage);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.getWorkerManageData();
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                WorkerInfo workerInfo = mAdapter.getItems(position);
                Intent intent = new Intent();
                intent.putExtra(WORKER_INFO, workerInfo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void getWorkerManageDataSuccess(List<WorkerInfo> responses) {
        if (responses != null && responses.size() > 0) {
            mAdapter.updateWithClear(responses);
        }
    }

    @OnClick(value = {R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void ShowToast(String t) {

    }
}
