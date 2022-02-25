package com.leon.carfixfactory.presenter;

import android.app.Activity;

import com.leon.carfixfactory.MyApplication;
import com.leon.carfixfactory.bean.AccessoriesInfo;
import com.leon.carfixfactory.bean.CarPartsInfo;
import com.leon.carfixfactory.bean.DriverInfo;
import com.leon.carfixfactory.bean.WorkerInfo;
import com.leon.carfixfactory.beanDao.AccessoriesInfoDao;
import com.leon.carfixfactory.beanDao.CarPartsInfoDao;
import com.leon.carfixfactory.beanDao.DriverInfoDao;
import com.leon.carfixfactory.beanDao.WorkerInfoDao;
import com.leon.carfixfactory.contract.HomeContact;
import com.leon.carfixfactory.contract.OrderConfirmContact;
import com.leon.carfixfactory.contract.RepairRecordContact;

import java.util.List;

/**
 * Created by leon
 * Date: 2019/9/21
 * Time: 10:54
 * Desc:
 */
public class OderConfirmPresenter extends BasePresenter<OrderConfirmContact.ViewOrderConfirm> implements OrderConfirmContact.IOrderConfirmPresenter {

    private DriverInfoDao driverInfoDao;
    private AccessoriesInfoDao accessoriesInfoDao;
    private CarPartsInfoDao carPartsInfoDao;

    public OderConfirmPresenter(Activity context, OrderConfirmContact.ViewOrderConfirm view) {
        super(context, view);
        carPartsInfoDao = MyApplication.getApplication().getDaoSession().getCarPartsInfoDao();
        driverInfoDao = MyApplication.getApplication().getDaoSession().getDriverInfoDao();
        accessoriesInfoDao = MyApplication.getApplication().getDaoSession().getAccessoriesInfoDao();
    }

    @Override
    public void getPartsList(Long repairId) {
        List<CarPartsInfo> carParts = carPartsInfoDao.queryBuilder()
                .where(CarPartsInfoDao.Properties.RepairId.eq(repairId))
                .build().list();
        mView.getCarPartsList(carParts);
    }

    @Override
    public void getAccessoryList(Long repairId) {
        List<AccessoriesInfo> accessories = accessoriesInfoDao.queryBuilder()
                .where(AccessoriesInfoDao.Properties.RepairId.eq(repairId))
                .build().list();
        mView.getAccessoriesList(accessories);
    }

    public void getDetailData(Long driverId, Long repairId) {
        DriverInfo driverInfo = driverInfoDao.queryBuilder().where(DriverInfoDao.Properties.DriverId.eq(driverId))
                .unique();
        mView.getDriverInfoSuccess(driverInfo);
        getPartsList(repairId);
        getAccessoryList(repairId);
    }
}
