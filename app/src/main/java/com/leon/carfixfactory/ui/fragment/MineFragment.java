package com.leon.carfixfactory.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.HomeData;
import com.leon.carfixfactory.contract.HomeContact;
import com.leon.carfixfactory.presenter.HomeDataPresenterImp;

import java.util.List;

/**
 * Created by leon
 * Date: 2019/7/24
 * Time: 10:00
 * Desc:个人中心
 */
public class MineFragment extends BaseFragment implements HomeContact.ViewHome {
    @Override
    protected void initPresenter() {
        mPresenter = new HomeDataPresenterImp(getActivity(), this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void getHomeDataSuccess(List<HomeData> responses) {

    }

    @Override
    public void ShowToast(String t) {

    }
}
