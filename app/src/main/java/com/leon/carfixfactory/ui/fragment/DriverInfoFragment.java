package com.leon.carfixfactory.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.DriverInfo;
import com.leon.carfixfactory.bean.ItemEditContent;
import com.leon.carfixfactory.contract.ItemEditTextContact;
import com.leon.carfixfactory.presenter.EditContentImp;
import com.leon.carfixfactory.ui.activity.MaintenanceRecordActivity;
import com.leon.carfixfactory.utils.ContentViewSetting;

import java.util.List;

import butterknife.Bind;

public class DriverInfoFragment extends BaseFragment<EditContentImp> implements ItemEditTextContact.ViewEditContent {
    @Bind(R.id.ll_car_id)
    LinearLayout llCarId;
    @Bind(R.id.ll_car_model)
    LinearLayout llCarIdModel;

    @Bind(R.id.ll_chassis_num)
    LinearLayout llChassisNum;
    @Bind(R.id.ll_engine_num)
    LinearLayout llEngineNum;

    @Bind(R.id.ll_driver_name)
    LinearLayout llDriverName;
    @Bind(R.id.ll_driver_address)
    LinearLayout llDriverAddress;
    @Bind(R.id.ll_driver_phone)
    LinearLayout llDriverPhone;

    @Bind(R.id.ll_department_name)
    LinearLayout llDepName;
    @Bind(R.id.ll_department_address)
    LinearLayout llDepAddr;
    @Bind(R.id.ll_department_phone)
    LinearLayout llDepPhone;

    private View[] views;
    private boolean flag = false;

    @Override
    protected void initPresenter() {
        mPresenter = new EditContentImp(getActivity(), this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        views = new View[]{llCarId, llCarIdModel, llChassisNum, llEngineNum,
                llDriverName, llDriverAddress, llDriverPhone,
                llDepName, llDepAddr, llDepPhone};
        mPresenter.initItemData("itemCarInfo.json");
        AppCompatEditText etCarId = ContentViewSetting.getEditText(llCarId);
        etCarId.requestFocus();
        etCarId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (flag) {
                    return;
                }
                flag = true;
                if (s.toString().length() >= 7) {
                    mPresenter.whetherExist(s.toString().toUpperCase());
                }
                flag = false;

            }
        });
    }

    public boolean setDriverInfo(DriverInfo driverInfo) {
        driverInfo.numberPlate = ContentViewSetting.getEditTextContent(llCarId);
        if (TextUtils.isEmpty(driverInfo.numberPlate)) {
            showToast(getString(R.string.notify_empty_car_id));
            return true;
        }
        driverInfo.numberPlate = driverInfo.numberPlate.toUpperCase();
        driverInfo.carModel = ContentViewSetting.getEditTextContent(llCarIdModel);
        driverInfo.frameNum = ContentViewSetting.getEditTextContent(llChassisNum);
        driverInfo.engineNum = ContentViewSetting.getEditTextContent(llEngineNum);
        driverInfo.driverName = ContentViewSetting.getEditTextContent(llDriverName);
        driverInfo.driverAddress = ContentViewSetting.getEditTextContent(llDriverAddress);
        driverInfo.driverPhone = ContentViewSetting.getEditTextContent(llDriverPhone);
        driverInfo.departmentName = ContentViewSetting.getEditTextContent(llDepName);
        driverInfo.departmentAddr = ContentViewSetting.getEditTextContent(llDepAddr);
        driverInfo.departmentPhone = ContentViewSetting.getEditTextContent(llDepPhone);
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_info;
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void getItemDataSuccess(List<ItemEditContent> responses) {
        for (int i = 0; i < responses.size(); i++) {
            ContentViewSetting.setItemContent(views[i], responses.get(i));
        }
    }

    @Override
    public void existData(DriverInfo driverInfo) {
        if (driverInfo != null) {
            ((MaintenanceRecordActivity) mActivity).setDriverInfo(driverInfo);
            initData(driverInfo);
        }
    }

    public void initData(DriverInfo driverInfo) {
        if (driverInfo != null) {
            ContentViewSetting.setEditTextContent(llCarId, driverInfo.numberPlate);
            ContentViewSetting.setEditTextContent(llCarIdModel, driverInfo.carModel);
            ContentViewSetting.setEditTextContent(llChassisNum, driverInfo.frameNum);
            ContentViewSetting.setEditTextContent(llEngineNum, driverInfo.engineNum);
            ContentViewSetting.setEditTextContent(llDriverName, driverInfo.driverName);
            ContentViewSetting.setEditTextContent(llDriverAddress, driverInfo.driverAddress);
            ContentViewSetting.setEditTextContent(llDriverPhone, driverInfo.driverPhone);
            ContentViewSetting.setEditTextContent(llDepName, driverInfo.departmentName);
            ContentViewSetting.setEditTextContent(llDepAddr, driverInfo.departmentAddr);
            ContentViewSetting.setEditTextContent(llDepPhone, driverInfo.departmentPhone);
        }
    }
}
