package com.leon.carfixfactory.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.leon.carfixfactory.MyApplication;
import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.AccessoriesInfo;
import com.leon.carfixfactory.bean.CarPartsInfo;
import com.leon.carfixfactory.bean.DriverInfo;
import com.leon.carfixfactory.bean.ItemEditContent;
import com.leon.carfixfactory.bean.RepairRecord;
import com.leon.carfixfactory.bean.WorkerInfo;
import com.leon.carfixfactory.beanDao.AccessoriesInfoDao;
import com.leon.carfixfactory.beanDao.CarPartsInfoDao;
import com.leon.carfixfactory.beanDao.RepairRecordDao;
import com.leon.carfixfactory.contract.ItemEditTextContact;
import com.leon.carfixfactory.contract.RepairRecordContact;
import com.leon.carfixfactory.presenter.EditContentImp;
import com.leon.carfixfactory.ui.activity.MaintenanceRecordActivity;
import com.leon.carfixfactory.ui.activity.WorkerManageActivity;
import com.leon.carfixfactory.ui.adapter.CarPartsAdapter;
import com.leon.carfixfactory.ui.adapter.base.BaseRecyclerAdapter;
import com.leon.carfixfactory.ui.custom.voice.VoiceView;
import com.leon.carfixfactory.utils.Utils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;


import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.leon.carfixfactory.ui.activity.WorkerManageActivity.WORKER_INFO;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class
MaintenanceRecordFragment extends BaseFragment<EditContentImp> implements ItemEditTextContact.ViewEditContent {

    private static final int DUTY_PERSON = 1000;

    @Bind(R.id.tv_duty_person)
    TextView tvDutyPerson;
    @Bind(R.id.vv_maintenance_content)
    VoiceView maintenanceContent;
    @Bind(R.id.et_repair_time)
    AppCompatEditText etRepairTime;
    @Bind(R.id.et_work_fee)
    AppCompatEditText etWorkFee;

    @Bind(R.id.et_repair_mileage)
    AppCompatEditText etRepairMileage;

    @Bind(R.id.rl_repair_part)
    RecyclerView rlParts;
    @Bind(R.id.rl_parts_accessorise)
    RecyclerView rlAccessorise;


    private CarPartsInfoDao carPartDao;
    private CarPartsAdapter mPartAdapter;
    private CarPartsAdapter mAccessoriesAdapter;
    private RepairRecord repairRecord;

    private AccessoriesInfoDao accessoryDao;
    private WorkerInfo workerInfo;
    private String repairOrderId;

    @Override
    protected void initPresenter() {
        mPresenter = new EditContentImp(getActivity(), this);
    }

    public void initRepairInfo(RepairRecord repairRecord) {
        this.repairRecord = repairRecord;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initAccessoriseRecyclerView();
        initPartRecyclerView();
        repairOrderId = String.format("%s%s", "WX", String.valueOf(System.currentTimeMillis()));
        carPartDao = MyApplication.getApplication().getDaoSession().getCarPartsInfoDao();
        accessoryDao = MyApplication.getApplication().getDaoSession().getAccessoriesInfoDao();
    }

    private void initPartRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlParts.setLayoutManager(linearLayoutManager);
        mPartAdapter = new CarPartsAdapter(getContext(), R.layout.item_car_part);
        rlParts.setAdapter(mPartAdapter);
        mPartAdapter.setOnDelClickListener(new CarPartsAdapter.OnDelClickListener() {
            @Override
            public void onDelete(int position) {
                carPartDao.delete((CarPartsInfo) mPartAdapter.getItems(position));
                mPartAdapter.delete(position);
            }
        });

        mPartAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                showAddRepairDetailDialog(false, mPartAdapter.getItems(position), position);
            }
        });
    }


    public boolean saveData() {
        if (mPartAdapter.getItemCount() <= 0) {
            showToast(getString(R.string.notify_add_part));
            return false;
        }
        List partList = mPartAdapter.getList();
        double totalPartPrice = 0;
        double totalAccessoryPrice = 0;
        for (int i = 0; i < partList.size(); i++) {
            CarPartsInfo carPartsInfo = (CarPartsInfo) partList.get(i);
            totalPartPrice += Double.valueOf(carPartsInfo.workTime) * Double.valueOf(carPartsInfo.partPrice);
        }
        repairRecord.repairOrderId = repairOrderId;
        repairRecord.totalPartFee = totalPartPrice == 0 ? "0" : Utils.getFinalCashValue(totalPartPrice);
        if (mAccessoriesAdapter.getItemCount() > 0) {
            List accessoryList = mAccessoriesAdapter.getList();

            for (int i = 0; i < accessoryList.size(); i++) {
                AccessoriesInfo accessoriesInfo = (AccessoriesInfo) accessoryList.get(i);
                totalAccessoryPrice += accessoriesInfo.accessoryCount * Double.valueOf(accessoriesInfo.accessoryPrice);
            }
            repairRecord.totalAccessoryFee = totalAccessoryPrice == 0 ? "0" : Utils.getFinalCashValue(totalAccessoryPrice);
        } else {
            repairRecord.totalAccessoryFee = null;
        }
        double totalFee = totalAccessoryPrice + totalPartPrice;
        repairRecord.repairTotalFee = totalFee == 0 ? "0" : Utils.getFinalCashValue(totalFee);

        repairRecord.repairDesc = maintenanceContent.getEditText();
        repairRecord.dutyPersonName = workerInfo != null ? workerInfo.workerName : "";
        repairRecord.dutyPersonId = workerInfo != null ? workerInfo.workerId.toString() : "";

        Editable editableMileage = etRepairMileage.getText();
        String mileage = editableMileage != null ? editableMileage.toString() : "";
        repairRecord.repairMileage = TextUtils.isEmpty(mileage) ? "" : mileage;
        return true;
    }

    private void initAccessoriseRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlAccessorise.setLayoutManager(linearLayoutManager);
        mAccessoriesAdapter = new CarPartsAdapter(getContext(), R.layout.item_car_part);
        rlAccessorise.setAdapter(mAccessoriesAdapter);
        mAccessoriesAdapter.setOnDelClickListener(new CarPartsAdapter.OnDelClickListener() {
            @Override
            public void onDelete(int position) {
                accessoryDao.delete((AccessoriesInfo) mAccessoriesAdapter.getItems(position));
                mAccessoriesAdapter.delete(position);
            }
        });

        mAccessoriesAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                showAddRepairDetailDialog(true, mAccessoriesAdapter.getItems(position), position);
            }
        });
    }

    @OnClick(value = {R.id.ll_repair_duty_person, R.id.iv_add_part, R.id.iv_add_accessories})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_repair_duty_person:
                Intent intent = new Intent(getContext(), WorkerManageActivity.class);
                startActivityForResult(intent, DUTY_PERSON);
                break;
            case R.id.iv_add_part:
                showAddRepairDetailDialog(false, null, -1);
                break;
            case R.id.iv_add_accessories:
                showAddRepairDetailDialog(true, null, -1);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_maintenance_content;
    }


    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void getItemDataSuccess(List<ItemEditContent> responses) {

    }

    @Override
    public void existData(DriverInfo driverInfo) {

    }

    private void showAddRepairDetailDialog(final boolean isAccessories, final RepairRecordContact.DialogContentObtain itemInfo, final int position) {
        final InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        QMUIDialog.CustomDialogBuilder builder = new QMUIDialog.CustomDialogBuilder(getContext());
        builder.setLayout(R.layout.dialog_add_car_part);
        QMUIDialog dialog = builder
                .addAction(0, getResources().getString(R.string.cancel), QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog1, int index) {
                        dialog1.dismiss();
                    }
                })
                .addAction(0, getResources().getString(R.string.confirm), QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog12, int index) {
                        AppCompatEditText editName = dialog12.findViewById(R.id.et_part_name);
                        Editable editableName = editName.getText();
                        String partName = editableName != null ? editableName.toString() : "";
                        AppCompatEditText editPrice = dialog12.findViewById(R.id.et_part_price);
                        Editable editablePrice = editPrice.getText();
                        String partPrice = editablePrice != null ? editablePrice.toString() : "";
                        AppCompatEditText editCount = dialog12.findViewById(R.id.et_part_count);
                        Editable editableCount = editCount.getText();
                        String partCount = editableCount != null ? editableCount.toString() : "";
                        if (TextUtils.isEmpty(partName)) {
                            showToast(isAccessories ? getString(R.string.notify_input_accessory_name) : getString(R.string.notify_input_part_name));
                            return;
                        }
                        if (TextUtils.isEmpty(partPrice)) {
                            showToast(isAccessories ? getString(R.string.notify_input_accessory_price) : getString(R.string.notify_input_part_price));
                            return;
                        }
                        if (TextUtils.isEmpty(partCount)) {
                            showToast(isAccessories ? getString(R.string.notify_input_accessory_count) : getString(R.string.notify_input_part_count));
                            return;
                        }
                        if (isAccessories) {
                            AccessoriesInfo accessoriesInfo;
                            if (itemInfo != null) {
                                accessoriesInfo = (AccessoriesInfo) itemInfo;
                                accessoriesInfo.accessoryName = partName;
                                accessoriesInfo.accessoryCount = Integer.parseInt(partCount);
                                accessoriesInfo.accessoryPrice = partPrice;
                                mAccessoriesAdapter.update(accessoriesInfo, position);
                            } else {
                                accessoriesInfo = new AccessoriesInfo();
                                accessoriesInfo.repairId = repairRecord.repairId;
                                accessoriesInfo.accessoryName = partName;
                                accessoriesInfo.accessoryCount = Integer.parseInt(partCount);
                                accessoriesInfo.accessoryPrice = partPrice;
                                mAccessoriesAdapter.addItem(accessoriesInfo);
                                mAccessoriesAdapter.notifyDataSetChanged();
                            }
                            accessoryDao.insertOrReplace(accessoriesInfo);
                        } else {
                            CarPartsInfo carPart;
                            if (itemInfo != null) {
                                carPart = (CarPartsInfo) itemInfo;
                                carPart.partName = partName;
                                carPart.workTime = partCount;
                                carPart.partPrice = partPrice;
                                mPartAdapter.update(carPart, position);
                            } else {
                                carPart = new CarPartsInfo();
                                carPart.repairId = repairRecord.repairId;
                                carPart.partName = partName;
                                carPart.workTime = partCount;
                                carPart.partPrice = partPrice;
                                mPartAdapter.addItem(carPart);
                                mPartAdapter.notifyDataSetChanged();
                            }
                            carPartDao.insertOrReplace(carPart);
                        }
                        dialog12.dismiss();
                    }
                })
                .create(R.style.dialog_theme);
        final AppCompatEditText editName = dialog.findViewById(R.id.et_part_name);
        AppCompatEditText editPrice = dialog.findViewById(R.id.et_part_price);
        AppCompatEditText editCount = dialog.findViewById(R.id.et_part_count);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        TextView tvFirstTitle = dialog.findViewById(R.id.tv_first_title);
        TextView tvThirdTitle = dialog.findViewById(R.id.tv_third_title);

        tv_title.setText(isAccessories ? getString(R.string.new_add_car_part) : getString(R.string.title_new_part));
        tvFirstTitle.setText(isAccessories ? getString(R.string.car_part_name) : getString(R.string.title_repair_part));
        tvThirdTitle.setText(isAccessories ? getString(R.string.car_part_count) : getString(R.string.title_repair_time));

        if (itemInfo != null) {
            editPrice.setText(itemInfo.getSecondContent());
            editName.setText(itemInfo.getFirstContent());
            editName.setSelection(itemInfo.getFirstContent().length());
            editCount.setText(itemInfo.getThirdContent());
        }

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog13) {
                inputMethodManager.hideSoftInputFromWindow(editName.getWindowToken(), 0);
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog14) {
                editName.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        inputMethodManager.showSoftInput(editName, 0);
                    }
                }, 300);
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == DUTY_PERSON) {
                if (data != null) {
                    workerInfo = (WorkerInfo) data.getSerializableExtra(WORKER_INFO);
                    tvDutyPerson.setText(workerInfo.workerName);
                }
            }
        }
    }
}
