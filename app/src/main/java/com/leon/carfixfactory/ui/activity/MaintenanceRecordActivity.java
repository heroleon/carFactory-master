package com.leon.carfixfactory.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.leon.carfixfactory.MyApplication;
import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.DriverInfo;
import com.leon.carfixfactory.bean.ItemEditContent;
import com.leon.carfixfactory.bean.RepairRecord;
import com.leon.carfixfactory.beanDao.DriverInfoDao;
import com.leon.carfixfactory.beanDao.RepairRecordDao;
import com.leon.carfixfactory.contract.ItemEditTextContact;
import com.leon.carfixfactory.presenter.EditContentImp;
import com.leon.carfixfactory.ui.custom.NoScrollViewPager;
import com.leon.carfixfactory.ui.fragment.ConfirmOrderFragment;
import com.leon.carfixfactory.ui.fragment.DriverInfoFragment;
import com.leon.carfixfactory.ui.fragment.MaintenanceRecordFragment;
import com.leon.carfixfactory.utils.AndroidBug5497Workaround;
import com.leon.carfixfactory.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MaintenanceRecordActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_option)
    LinearLayout llOption;
    @Bind(R.id.tv_last_step)
    TextView tvLastStep;
    @Bind(R.id.tv_next_step)
    TextView tvNextStep;
    @Bind(R.id.vp)
    NoScrollViewPager viewPager;
    private List<Fragment> fragmentList;
    private String[] titles;
    private int currentIndex = 0;
    private boolean isConfirm = false;
    private View rootView;
    private int rootViewVisibleHeight;
    private DriverInfo driverInfo;
    private DriverInfoDao driverInfoDao;
    private RepairRecord repairRecord;
    private RepairRecordDao repairRecordDao;
    private MaintenanceRecordFragment maintenanceRecordFragment;
    private ConfirmOrderFragment confirmOrderFragment;
    private DriverInfoFragment driverInfoFragment;


    @Override
    protected void initPresenter(Intent intent) {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_maintenance_record;
    }

    private void showBottomBtn(int bottomHeight) {
        int height = bottomHeight == 0 ? 0 : DisplayUtil.dip2px(this, bottomHeight);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                height);
        llOption.setLayoutParams(params);
    }

    @Override
    protected void initView() {
        AndroidBug5497Workaround.assistActivity(this);
        tvLastStep.setVisibility(View.GONE);
        initFragmentList();
        addKeyBordListener();
        initCarInfo();
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentIndex = i;
                if (currentIndex == 0) {
                    tvLastStep.setVisibility(View.GONE);
                    tvNextStep.setText(getString(R.string.next_step));
                } else if (currentIndex == 1) {
                    tvLastStep.setVisibility(View.VISIBLE);
                    tvNextStep.setText(getString(R.string.next_step));
                } else {
                    tvNextStep.setText(getString(R.string.confirm));
                }
                isConfirm = currentIndex == 2;
                tvTitle.setText(titles[i]);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initCarInfo() {
        driverInfoDao = MyApplication.getApplication().getDaoSession().getDriverInfoDao();
        repairRecordDao = MyApplication.getApplication().getDaoSession().getRepairRecordDao();
        driverInfo = new DriverInfo();
        repairRecord = new RepairRecord();
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }

    private void addKeyBordListener() {
        //获取activity的根视图
        rootView = getWindow().getDecorView();
        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取当前根视图在屏幕上显示的大小
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int visibleHeight = r.height();
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (rootViewVisibleHeight == visibleHeight) {
                    return;
                }
                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (rootViewVisibleHeight - visibleHeight > 200) {
                    showBottomBtn(0);
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - rootViewVisibleHeight > 200) {
                    showBottomBtn(60);
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
            }
        });
    }

    @OnClick(value = {R.id.iv_back, R.id.tv_last_step, R.id.tv_next_step})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (repairRecord.repairId != null) {
                    repairRecordDao.delete(repairRecord);
                }
                finish();
                break;
            case R.id.tv_last_step:
                currentIndex--;
                if (currentIndex == 0) {
                    driverInfoFragment.initData(driverInfo);
                }
                viewPager.setCurrentItem(currentIndex);
                break;
            case R.id.tv_next_step:
                if (isConfirm) {
                    repairRecord.arrivalTime = System.currentTimeMillis();
                    repairRecordDao.update(repairRecord);
                    finish();
                } else {
                    Fragment fragment = fragmentList.get(currentIndex);
                    if (fragment instanceof DriverInfoFragment) {
                        if (driverInfoFragment.setDriverInfo(driverInfo)) {
                            return;
                        }
                        if (driverInfo.driverId != null) {
                            driverInfoDao.update(driverInfo);
                        } else {
                            driverInfoDao.insert(driverInfo);
                        }
                        repairRecord.driverId = driverInfo.driverId;
                        repairRecord.numberPlate = driverInfo.numberPlate;
                        repairRecordDao.insertOrReplace(repairRecord);
                        maintenanceRecordFragment.initRepairInfo(repairRecord);
                    }

                    if (fragment instanceof MaintenanceRecordFragment) {
                        if (!maintenanceRecordFragment.saveData()) {
                            return;
                        }
                        repairRecordDao.update(repairRecord);
                    }

                    currentIndex++;
                    viewPager.setCurrentItem(currentIndex);
                    if (currentIndex == 2) {
                        confirmOrderFragment.initData(driverInfo, repairRecord);
                    }
                }
                break;
        }
    }

    private void initFragmentList() {
        titles = getResources().getStringArray(R.array.maintenance_title);
        tvTitle.setText(titles[0]);
        fragmentList = new ArrayList<>();
        driverInfoFragment = new DriverInfoFragment();
        maintenanceRecordFragment = new MaintenanceRecordFragment();
        confirmOrderFragment = new ConfirmOrderFragment();
        fragmentList.add(driverInfoFragment);
        fragmentList.add(maintenanceRecordFragment);
        fragmentList.add(confirmOrderFragment);
    }
}
