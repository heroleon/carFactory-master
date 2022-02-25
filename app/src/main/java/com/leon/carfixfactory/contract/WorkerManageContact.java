package com.leon.carfixfactory.contract;

import com.leon.carfixfactory.bean.WorkerInfo;
import com.leon.carfixfactory.ui.view.IBaseView;

import java.util.List;

public interface WorkerManageContact {
    interface ViewWorkerManage extends IBaseView {
        void getWorkerManageDataSuccess(List<WorkerInfo> responses);
    }

    interface IWorkerManagePresenter {
        void getWorkerManageData();
    }
}
