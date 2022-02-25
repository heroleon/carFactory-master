package com.leon.carfixfactory.presenter;

import android.app.Activity;

import com.leon.carfixfactory.MyApplication;
import com.leon.carfixfactory.bean.WorkerInfo;
import com.leon.carfixfactory.beanDao.WorkerInfoDao;
import com.leon.carfixfactory.contract.WorkerManageContact;

import java.util.List;

public class WorkerManageImp extends BasePresenter<WorkerManageContact.ViewWorkerManage> implements WorkerManageContact.IWorkerManagePresenter {
    public WorkerManageImp(Activity context, WorkerManageContact.ViewWorkerManage view) {
        super(context, view);
    }

    @Override
    public void getWorkerManageData() {
        WorkerInfoDao workerInfoDao = MyApplication.getApplication().getDaoSession().getWorkerInfoDao();
        List<WorkerInfo> workerInfos = workerInfoDao.loadAll();
        mView.getWorkerManageDataSuccess(workerInfos);
    }
}
