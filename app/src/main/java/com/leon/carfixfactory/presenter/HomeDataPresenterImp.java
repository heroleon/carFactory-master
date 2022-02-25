package com.leon.carfixfactory.presenter;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.leon.carfixfactory.bean.HomeData;

import com.leon.carfixfactory.contract.HomeContact;

import java.util.ArrayList;

public class HomeDataPresenterImp extends BasePresenter<HomeContact.ViewHome> implements HomeContact.IHomePresenter {

    public HomeDataPresenterImp(Activity context, HomeContact.ViewHome view) {
        super(context, view);
    }

    @Override
    public void getHomeData(String fileName) {
        ArrayList<HomeData> mDataList = new ArrayList<>();
        String strByJson = getJson(fileName);
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement indexArr : jsonArray) {
            HomeData menuEntity = gson.fromJson(indexArr, HomeData.class);
            mDataList.add(menuEntity);
        }
        mView.getHomeDataSuccess(mDataList);
    }
}
