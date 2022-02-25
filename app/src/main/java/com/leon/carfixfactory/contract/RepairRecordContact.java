package com.leon.carfixfactory.contract;

import com.leon.carfixfactory.bean.DriverInfo;
import com.leon.carfixfactory.bean.RepairRecord;
import com.leon.carfixfactory.ui.view.IBaseView;

import java.util.List;

public interface RepairRecordContact {
    interface ViewRepairRecord extends IBaseView {
        void getRepairRecordSuccess(List<DriverInfo> responses);
    }

    interface IRepairRecordPresenter {
        void getRepairRecordData();
    }

    interface DialogContentObtain {
        String getFirstContent();

        String getSecondContent();

        String getThirdContent();

    }


    interface  ViewRepairListView extends IBaseView{
        void getRepairRecordSuccess(List<RepairRecord> responses);
    }


}
