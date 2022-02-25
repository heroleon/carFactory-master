package com.leon.carfixfactory.presenter;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.leon.carfixfactory.MyApplication;
import com.leon.carfixfactory.bean.AccessoriesInfo;
import com.leon.carfixfactory.bean.DriverInfo;
import com.leon.carfixfactory.bean.ItemEditContent;
import com.leon.carfixfactory.beanDao.AccessoriesInfoDao;
import com.leon.carfixfactory.beanDao.DriverInfoDao;
import com.leon.carfixfactory.contract.ItemEditTextContact;

import java.util.ArrayList;
import java.util.List;

public class EditContentImp extends BasePresenter<ItemEditTextContact.ViewEditContent> implements ItemEditTextContact.IEditContent {

    private DriverInfoDao driverInfoDao;

    public EditContentImp(Activity context, ItemEditTextContact.ViewEditContent view) {
        super(context, view);
        driverInfoDao = MyApplication.getApplication().getDaoSession().getDriverInfoDao();
    }

    @Override
    public void initItemData(String fileName) {
        ArrayList<ItemEditContent> mDataList = new ArrayList<>();
        String strByJson = getJson(fileName);
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();
        Gson gson = new Gson();

        for (JsonElement indexArr : jsonArray) {
            ItemEditContent menuEntity = gson.fromJson(indexArr, ItemEditContent.class);
            mDataList.add(menuEntity);
        }
        mView.getItemDataSuccess(mDataList);
    }

    @Override
    public void whetherExist(String carId) {
        DriverInfo driverInfo = driverInfoDao.queryBuilder()
                .where(DriverInfoDao.Properties.NumberPlate.eq(carId)).unique();
        mView.existData(driverInfo);
    }
}
