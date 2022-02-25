package com.leon.carfixfactory.presenter;

import android.app.Activity;

import com.leon.carfixfactory.MyApplication;
import com.leon.carfixfactory.bean.AccessoriesInfo;
import com.leon.carfixfactory.bean.RepairRecord;
import com.leon.carfixfactory.beanDao.AccessoriesInfoDao;
import com.leon.carfixfactory.beanDao.DriverInfoDao;
import com.leon.carfixfactory.beanDao.RepairRecordDao;
import com.leon.carfixfactory.contract.ItemEditTextContact;
import com.leon.carfixfactory.contract.RepairRecordContact;

import java.util.List;

/**
 * Created by leon
 * Date: 2019/11/7
 * Time: 21:15
 * Desc:
 */
public class RepairListImp extends BasePresenter<RepairRecordContact.ViewRepairListView> {
    private RepairRecordDao repairRecordDao;

    public RepairListImp(Activity context, RepairRecordContact.ViewRepairListView view) {
        super(context, view);
        repairRecordDao = MyApplication.getApplication().getDaoSession().getRepairRecordDao();
    }

    public void getRepairList(boolean isDeliveryCar) {
        List<RepairRecord> repairRecords;
        if (isDeliveryCar) {
            repairRecords = repairRecordDao.queryBuilder()
                    .where(RepairRecordDao.Properties.RepairState.eq(2))
                    .orderDesc(RepairRecordDao.Properties.DeliveryTime)
                    .build().list();
        } else {
            repairRecords = repairRecordDao.queryBuilder()
                    .where(RepairRecordDao.Properties.RepairState.lt(2))
                    .orderDesc(RepairRecordDao.Properties.ArrivalTime)
                    .build().list();
        }
        mView.getRepairRecordSuccess(repairRecords);
    }
}
