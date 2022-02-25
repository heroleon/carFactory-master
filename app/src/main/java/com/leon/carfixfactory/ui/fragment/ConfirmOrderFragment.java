package com.leon.carfixfactory.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.AccessoriesInfo;
import com.leon.carfixfactory.bean.CarPartsInfo;
import com.leon.carfixfactory.bean.DriverInfo;
import com.leon.carfixfactory.bean.RepairRecord;
import com.leon.carfixfactory.contract.OrderConfirmContact;
import com.leon.carfixfactory.presenter.OderConfirmPresenter;
import com.leon.carfixfactory.ui.adapter.ItemPartAdapter;


import java.util.List;

import butterknife.Bind;

public class ConfirmOrderFragment extends BaseFragment<OderConfirmPresenter> implements OrderConfirmContact.ViewOrderConfirm {
    @Bind(R.id.tv_car_card)
    TextView tvCarCard;
    @Bind(R.id.tv_driver_name)
    TextView tvDriverName;
    @Bind(R.id.tv_driver_phone)
    TextView tvDriverPhone;
    @Bind(R.id.tv_repair_mileage)
    TextView tvRepairMileage;
    @Bind(R.id.tv_accessory_fee)
    TextView tvAccessoryFee;
    @Bind(R.id.tv_part_fee)
    TextView tvPartFee;

    @Bind(R.id.ll_repair_desc)
    LinearLayout llRepairDesc;
    @Bind(R.id.cl_accessory)
    ConstraintLayout clAccessory;


    @Bind(R.id.tv_repair_content)
    TextView tvRepairDetail;

    @Bind(R.id.tv_car_duty_person)
    TextView tvDutyPerson;
    @Bind(R.id.tv_repair_time)
    TextView tvRepairTime;
    @Bind(R.id.tv_time_fee)
    TextView tvTimeFee;
    @Bind(R.id.rl_part_view)
    RecyclerView rlPartView;
    @Bind(R.id.rl_accessory_view)
    RecyclerView rlAccessoryView;
    @Bind(R.id.tv_total_fee)
    TextView tvTotalFee;
    private ItemPartAdapter mAdapter;
    private ItemPartAdapter mAccessoryAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new OderConfirmPresenter(getActivity(), this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlPartView.setLayoutManager(linearLayoutManager);
        mAdapter = new ItemPartAdapter(getContext(), R.layout.item_confirm_part);
        rlPartView.setAdapter(mAdapter);
        rlPartView.setNestedScrollingEnabled(false);

        LinearLayoutManager accessoryManager = new LinearLayoutManager(getContext());
        accessoryManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlAccessoryView.setLayoutManager(accessoryManager);
        mAccessoryAdapter = new ItemPartAdapter(getContext(), R.layout.item_confirm_part);
        rlAccessoryView.setAdapter(mAccessoryAdapter);
        rlAccessoryView.setNestedScrollingEnabled(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_confirm_order;
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    public void initData(DriverInfo driverInfo, RepairRecord repairRecord) {
        mPresenter.getPartsList(repairRecord.repairId);
        mPresenter.getAccessoryList(repairRecord.repairId);
        setText(tvCarCard, R.string.car_card_content, getTxtContent(driverInfo.numberPlate));
        setText(tvDriverName, R.string.car_driver_name_content, getTxtContent(driverInfo.driverName));
        setText(tvDriverPhone, R.string.car_driver_phone_content, getTxtContent(driverInfo.driverPhone));
        setText(tvRepairMileage, R.string.car_repair_mileage,
                TextUtils.isEmpty(repairRecord.repairMileage)
                        ? getString(R.string.empty_data)
                        : repairRecord.repairMileage + "KM");

        if (TextUtils.isEmpty(repairRecord.repairDesc)) {
            llRepairDesc.setVisibility(View.GONE);
        } else {
            llRepairDesc.setVisibility(View.VISIBLE);
            tvRepairDetail.setText(repairRecord.repairDesc);
        }

        setText(tvPartFee, R.string.part_price_brackets, repairRecord.totalPartFee);

        if (!TextUtils.isEmpty(repairRecord.totalAccessoryFee)) {
            setText(tvAccessoryFee, R.string.part_price_brackets, repairRecord.totalAccessoryFee);
        }
        setText(tvTotalFee, R.string.part_price, repairRecord.repairTotalFee);
        setText(tvDutyPerson, R.string.car_duty_person_content, getTxtContent(repairRecord.dutyPersonName));
    }

    private void setText(TextView tvView, int resId, String content) {
        tvView.setText(String.format(getString(resId), content));
    }

    @Override
    public void getCarPartsList(List<CarPartsInfo> responses) {
        if (responses != null && responses.size() > 0) {
            mAdapter.clear();
            for (CarPartsInfo carPartsInfo : responses
            ) {
                mAdapter.addItem(carPartsInfo);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getAccessoriesList(List<AccessoriesInfo> responses) {
        if (responses != null && responses.size() > 0) {
            clAccessory.setVisibility(View.VISIBLE);
            mAccessoryAdapter.clear();
            for (AccessoriesInfo accessoriesInfo : responses
            ) {
                mAccessoryAdapter.addItem(accessoriesInfo);
            }
            mAccessoryAdapter.notifyDataSetChanged();
        } else {
            clAccessory.setVisibility(View.GONE);
        }
    }

    @Override
    public void getDriverInfoSuccess(DriverInfo driverInfo) {

    }

    private String getTxtContent(String strContent) {
        return TextUtils.isEmpty(strContent) ? getString(R.string.empty_data) : strContent;
    }

}
