package com.leon.carfixfactory.bean;

import android.text.TextUtils;

import com.leon.carfixfactory.contract.RepairRecordContact;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by leon
 * Date: 2019/9/20
 * Time: 14:41
 * Desc:车辆维修配件信息
 */
@Entity
public class CarPartsInfo implements RepairRecordContact.DialogContentObtain {
    @Id(autoincrement = true)
    public Long partId;
    public Long repairId;
    public String partName;      //项目名称
    public String partPrice;    //项目价格
    public String workTime;     //工时

    @Generated(hash = 382406483)
    public CarPartsInfo() {
    }

    @Generated(hash = 4614629)
    public CarPartsInfo(Long partId, Long repairId, String partName,
                        String partPrice, String workTime) {
        this.partId = partId;
        this.repairId = repairId;
        this.partName = partName;
        this.partPrice = partPrice;
        this.workTime = workTime;
    }

    public Long getPartId() {
        return this.partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public Long getRepairId() {
        return this.repairId;
    }

    public void setRepairId(Long repairId) {
        this.repairId = repairId;
    }

    public String getPartName() {
        return this.partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartPrice() {
        return this.partPrice;
    }

    public void setPartPrice(String partPrice) {
        this.partPrice = partPrice;
    }

    public String getWorkTime() {
        return this.workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    @Override
    public String getFirstContent() {
        return TextUtils.isEmpty(partName) ? "" : partName;
    }

    @Override
    public String getSecondContent() {
        return TextUtils.isEmpty(partPrice) ? "" : partPrice;
    }

    @Override
    public String getThirdContent() {
        return TextUtils.isEmpty(workTime) ? "" : workTime;
    }
}
