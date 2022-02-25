package com.leon.carfixfactory.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.carfixfactory.MyApplication;
import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.RepairRecord;
import com.leon.carfixfactory.beanDao.RepairRecordDao;
import com.leon.carfixfactory.contract.RepairRecordContact;
import com.leon.carfixfactory.presenter.RepairListImp;
import com.leon.carfixfactory.ui.activity.RepairDetailActivity;
import com.leon.carfixfactory.ui.adapter.RepairListAdapter;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.Bind;

/**
 * Created by leon
 * Date: 2019/11/7
 * Time: 19:55
 * Desc:维修记录基类
 */
public class BaseRepairFragment extends BaseFragment<RepairListImp> implements RepairRecordContact.ViewRepairListView {

    @Bind(R.id.rv_record_view)
    RecyclerView rvRecordView;
    private RepairListAdapter mAdapter;
    protected boolean isDelivery;
    private RepairRecordDao repairRecordDao;

    @Override
    protected void initPresenter() {
        mPresenter = new RepairListImp(mActivity, this);
    }

    @Override
    protected void lazyLoad() {
        refreshData();
    }

    public void refreshData() {
        if (mPresenter != null) {
            mPresenter.getRepairList(isDelivery);
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        repairRecordDao = MyApplication.getApplication().getDaoSession().getRepairRecordDao();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecordView.setLayoutManager(linearLayoutManager);
        mAdapter = new RepairListAdapter(getContext(), R.layout.item_repair_list);
        rvRecordView.setAdapter(mAdapter);
        mAdapter.setOnItemOptionClickListener(new RepairListAdapter.OnItemOptionClickListener() {
            @Override
            public void firstBtnClick(int position) {
                changeRepairState(position, 2);
            }

            @Override
            public void secondBtnClick(int position) {
                //TODO 编辑
            }

            @Override
            public void thirdBtnClick(int position) {
                changeRepairState(position, 1);
            }
        });


        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Intent intent = new Intent(getActivity(), RepairDetailActivity.class);
                intent.putExtra(RepairDetailActivity.REPAIR_RECORD, mAdapter.getList().get(position));
                startActivity(intent);
            }
        });
    }

    private void changeRepairState(int position, int type) {
        RepairRecord repairRecord = mAdapter.getItems(position);
        if (type == 2) {
            repairRecord.deliveryTime = System.currentTimeMillis();
        }
        repairRecord.repairState = type;
        repairRecordDao.update(repairRecord);
        if (type == 1) {
            mAdapter.notifyItemChanged(position);
        } else {
            mAdapter.delete(position);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_repair_record;
    }


    @Override
    public void getRepairRecordSuccess(List<RepairRecord> responses) {
        if (responses != null && responses.size() > 0) {
            mAdapter.updateWithClear(responses);
        }
    }

    @Override
    public void ShowToast(String t) {

    }
}
