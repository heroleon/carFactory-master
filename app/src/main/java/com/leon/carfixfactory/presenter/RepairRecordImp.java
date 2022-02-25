package com.leon.carfixfactory.presenter;

import android.app.Activity;

import com.leon.carfixfactory.MyApplication;
import com.leon.carfixfactory.bean.DriverInfo;
import com.leon.carfixfactory.beanDao.DriverInfoDao;
import com.leon.carfixfactory.contract.RepairRecordContact;

import java.util.List;

public class RepairRecordImp extends BasePresenter<RepairRecordContact.ViewRepairRecord> implements RepairRecordContact.IRepairRecordPresenter {
    public RepairRecordImp(Activity context, RepairRecordContact.ViewRepairRecord view) {
        super(context, view);
    }

    @Override
    public void getRepairRecordData() {
        DriverInfoDao driverInfoDao = MyApplication.getApplication().getDaoSession().getDriverInfoDao();
        List<DriverInfo> driverInfos = driverInfoDao.loadAll();
        mView.getRepairRecordSuccess(driverInfos);
    }
}
