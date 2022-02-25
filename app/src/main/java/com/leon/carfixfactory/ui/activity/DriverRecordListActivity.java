package com.leon.carfixfactory.ui.activity;

import android.content.Intent;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.DriverInfo;
import com.leon.carfixfactory.contract.RepairRecordContact;
import com.leon.carfixfactory.presenter.RepairRecordImp;
import com.leon.carfixfactory.ui.adapter.RepairRecordAdapter;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class DriverRecordListActivity extends BaseActivity<RepairRecordImp> implements RepairRecordContact.ViewRepairRecord {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rc_record)
    RecyclerView rcRepairRecord;
    private RepairRecordAdapter mAdapter;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new RepairRecordImp(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_repair_record;
    }

    @Override
    protected void initView() {
        initTile();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcRepairRecord.setLayoutManager(linearLayoutManager);
        mAdapter = new RepairRecordAdapter(this, R.layout.item_repair_record);
        rcRepairRecord.setAdapter(mAdapter);
        mPresenter.getRepairRecordData();
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {

            }
        });
    }

    private void initTile() {
        tvTitle.setText(getString(R.string.title_driver_info));
    }

    @OnClick({R.id.iv_back})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void getRepairRecordSuccess(List<DriverInfo> responses) {
        if (responses != null && responses.size() > 0) {
            mAdapter.updateWithClear(responses);
        }
    }

    @Override
    public void ShowToast(String t) {

    }
}
