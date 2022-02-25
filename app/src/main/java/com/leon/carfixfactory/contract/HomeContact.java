package com.leon.carfixfactory.contract;

import com.leon.carfixfactory.bean.HomeData;
import com.leon.carfixfactory.bean.ItemEditContent;
import com.leon.carfixfactory.ui.view.IBaseView;

import java.util.List;

/**
 * Created by leon
 * Date: 2019/7/24
 * Time: 10:01
 * Desc:首页
 */
public interface HomeContact {
    interface ViewHome extends IBaseView {
        void getHomeDataSuccess(List<HomeData> responses);
    }

    interface IHomePresenter {
        void getHomeData(String fileName);
    }
}
