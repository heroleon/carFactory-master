package com.leon.carfixfactory.contract;

import com.leon.carfixfactory.bean.AccessoriesInfo;
import com.leon.carfixfactory.bean.CarPartsInfo;
import com.leon.carfixfactory.bean.DriverInfo;
import com.leon.carfixfactory.bean.HomeData;
import com.leon.carfixfactory.ui.view.IBaseView;

import java.util.List;

/**
 * Created by leon
 * Date: 2019/9/21
 * Time: 10:52
 * Desc:确认维修单
 */
public class OrderConfirmContact {
    public interface ViewOrderConfirm extends IBaseView {
        void getCarPartsList(List<CarPartsInfo> responses);

        void getAccessoriesList(List<AccessoriesInfo> responses);

        void getDriverInfoSuccess(DriverInfo driverInfo);
    }

    public interface IOrderConfirmPresenter {
        void getPartsList(Long repairId);

        void getAccessoryList(Long repairId);
    }
}
