package com.leon.carfixfactory;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.leon.carfixfactory.beanDao.DaoMaster;
import com.leon.carfixfactory.beanDao.DaoSession;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

public class MyApplication extends Application {
    private DaoSession daoSession;
    private static MyApplication myApplication = null;

    public static MyApplication getApplication() {
        return myApplication;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        initGreenDao();
        initAutoSize();
    }

    private void initAutoSize() {
        AutoSizeConfig.getInstance()
                .getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.MM);
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "contacts.db");
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
