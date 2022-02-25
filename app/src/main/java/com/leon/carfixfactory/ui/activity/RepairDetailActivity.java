package com.leon.carfixfactory.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.carfixfactory.MyApplication;
import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.AccessoriesInfo;
import com.leon.carfixfactory.bean.CarPartsInfo;
import com.leon.carfixfactory.bean.DriverInfo;
import com.leon.carfixfactory.bean.RepairRecord;
import com.leon.carfixfactory.beanDao.RepairRecordDao;
import com.leon.carfixfactory.contract.OrderConfirmContact;
import com.leon.carfixfactory.presenter.OderConfirmPresenter;
import com.leon.carfixfactory.ui.adapter.ItemPartAdapter;
import com.leon.carfixfactory.utils.DateTimeUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class RepairDetailActivity extends BaseActivity<OderConfirmPresenter> implements OrderConfirmContact.ViewOrderConfirm {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_car_card)
    TextView tvCarCard;
    @Bind(R.id.tv_driver_name)
    TextView tvDriverName;
    @Bind(R.id.tv_driver_phone)
    TextView tvDriverPhone;
    @Bind(R.id.tv_accept_time)
    TextView tvAcceptTime;
    @Bind(R.id.tv_repair_content)
    TextView tvRepairDetail;
    @Bind(R.id.tv_detail_title)
    TextView tvTitleDetail;

    @Bind(R.id.tv_car_type)
    TextView tvCarType;
    @Bind(R.id.tv_car_engine_num)
    TextView tvEngineNum;
    @Bind(R.id.tv_car_frame_num)
    TextView tvFrameNum;
    @Bind(R.id.tv_car_mileage)
    TextView tvCarMileage;
    @Bind(R.id.tv_car_duty_person)
    TextView tvDutyPerson;
    @Bind(R.id.tv_delivery_time)
    TextView tvDeliveryTime;
    @Bind(R.id.tv_part_fee)
    TextView tvPartFee;
    @Bind(R.id.tv_accessory_fee)
    TextView tvAccessoryFee;
    @Bind(R.id.tv_repair_id)
    TextView tvRepairId;
    @Bind(R.id.tv_repair_state)
    TextView tvRepairState;

    @Bind(R.id.btn_delivery)
    TextView btnDelivery;
    @Bind(R.id.btn_repair)
    TextView btnRepair;

    @Bind(R.id.rl_part_view)
    RecyclerView rlPartView;
    @Bind(R.id.rl_accessory_view)
    RecyclerView rlAccessoryView;

    @Bind(R.id.cl_accessory)
    ConstraintLayout clAccessory;

    @Bind(R.id.ll_option)
    LinearLayout llOption;


    @Bind(R.id.tv_total_fee)
    TextView tvTotalFee;
    private ItemPartAdapter mAdapter;
    private ItemPartAdapter mAccessoryAdapter;

    public final static String REPAIR_RECORD = "repair_record";
    private RepairRecord repairRecord;
    private RepairRecordDao repairRecordDao;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new OderConfirmPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_repair_detail;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.title_repair_detail));
        repairRecord = (RepairRecord) getIntent().getSerializableExtra(REPAIR_RECORD);
        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlPartView.setLayoutManager(linearLayoutManager);
        mAdapter = new ItemPartAdapter(this, R.layout.item_confirm_part);
        rlPartView.setAdapter(mAdapter);
        rlPartView.setNestedScrollingEnabled(false);

        LinearLayoutManager accessoryManager = new LinearLayoutManager(this);
        accessoryManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlAccessoryView.setLayoutManager(accessoryManager);
        mAccessoryAdapter = new ItemPartAdapter(this, R.layout.item_confirm_part);
        rlAccessoryView.setAdapter(mAccessoryAdapter);
        rlAccessoryView.setNestedScrollingEnabled(false);
    }

    public void initData() {
        repairRecordDao = MyApplication.getApplication().getDaoSession().getRepairRecordDao();
        initOptionBtn();
        mPresenter.getDetailData(repairRecord.driverId, repairRecord.repairId);
    }

    private void initOptionBtn() {
        int visible = repairRecord.repairState == 2 ? View.GONE : View.VISIBLE;
        llOption.setVisibility(visible);
        if (repairRecord.repairState == 0) {
            btnDelivery.setVisibility(View.GONE);
            btnRepair.setVisibility(View.VISIBLE);
        } else {
            btnDelivery.setVisibility(View.VISIBLE);
            btnRepair.setVisibility(View.GONE);
        }
    }

    private void setText(TextView tvView, int resId, String content) {
        tvView.setText(String.format(getString(resId), content));
    }

    @OnClick(value = {R.id.iv_back, R.id.btn_edit, R.id.btn_delivery, R.id.btn_repair})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_edit:
                //TODO 编辑
                break;
            case R.id.btn_delivery:
                changeRepairState(2);
                break;
            case R.id.btn_repair:
                changeRepairState(1);
                break;
        }
    }

    private void changeRepairState(int type) {
        if (type == 2) {
            repairRecord.deliveryTime = System.currentTimeMillis();
            tvDeliveryTime.setVisibility(View.VISIBLE);
            setText(tvDeliveryTime, R.string.car_delivery_time, DateTimeUtils.millisecondToDate(repairRecord.deliveryTime, DateTimeUtils.YYYY_MM_DD));
        }
        repairRecord.repairState = type;
        repairRecordDao.update(repairRecord);
        initOptionBtn();
        tvRepairState.setText(repairRecord.getRepairState(this));
    }

    @Override
    public void getCarPartsList(List<CarPartsInfo> responses) {
        if (responses != null && responses.size() > 0) {
            setText(tvPartFee, R.string.part_price_brackets, repairRecord.totalPartFee);
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
            setText(tvAccessoryFee, R.string.part_price_brackets, repairRecord.totalAccessoryFee);
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

        setText(tvRepairId, R.string.repair_order_id, repairRecord.repairOrderId);
        tvRepairState.setText(repairRecord.getRepairState(this));
        setText(tvCarCard, R.string.car_card_content, driverInfo.numberPlate);


        initText(tvCarType, R.string.car_card_type, driverInfo.carModel);
        initText(tvEngineNum, R.string.car_card_engine_num, driverInfo.engineNum);
        initText(tvFrameNum, R.string.car_card_frame_num, driverInfo.frameNum);

        initText(tvCarMileage, R.string.car_repair_mileage, repairRecord.repairMileage);
        initText(tvDutyPerson, R.string.car_duty_person_content, repairRecord.dutyPersonName);
        initText(tvDriverName, R.string.car_driver_name_content, driverInfo.driverName);
        initText(tvDriverPhone, R.string.car_driver_phone_content, driverInfo.driverPhone);

        setText(tvAcceptTime, R.string.car_accept_time, DateTimeUtils.millisecondToDate(repairRecord.arrivalTime, DateTimeUtils.YYYY_MM_DD));

        if (repairRecord.repairState == 2) {
            tvDeliveryTime.setVisibility(View.VISIBLE);
            setText(tvDeliveryTime, R.string.car_delivery_time, DateTimeUtils.millisecondToDate(repairRecord.deliveryTime, DateTimeUtils.YYYY_MM_DD));
        } else {
            tvDeliveryTime.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(repairRecord.repairDesc)) {
            tvRepairDetail.setVisibility(View.GONE);
            tvTitleDetail.setVisibility(View.GONE);
        } else {
            tvRepairDetail.setText(repairRecord.repairDesc);
            tvRepairDetail.setVisibility(View.VISIBLE);
            tvTitleDetail.setVisibility(View.VISIBLE);
        }

        tvTotalFee.setText(String.format(getString(R.string.part_price), repairRecord.repairTotalFee));
    }

    @Override
    public void ShowToast(String t) {

    }

    private void initText(TextView view, int strId, String content) {
        String realStr = TextUtils.isEmpty(content) ? getString(R.string.empty_data) : content;
        String str = String.format(getString(strId), realStr);
        view.setText(str);
    }
}
