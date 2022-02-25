package com.leon.carfixfactory.bean;

import android.text.TextUtils;

import com.leon.carfixfactory.contract.RepairRecordContact;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class AccessoriesInfo implements RepairRecordContact.DialogContentObtain {
    @Id(autoincrement = true)
    public Long accessoryId;
    public Long repairId;
    public String accessoryName;
    public String accessoryPrice;
    public int accessoryCount;

    @Generated(hash = 84680927)
    public AccessoriesInfo(Long accessoryId, Long repairId, String accessoryName,
                           String accessoryPrice, int accessoryCount) {
        this.accessoryId = accessoryId;
        this.repairId = repairId;
        this.accessoryName = accessoryName;
        this.accessoryPrice = accessoryPrice;
        this.accessoryCount = accessoryCount;
    }

    @Generated(hash = 531176290)
    public AccessoriesInfo() {
    }

    public Long getAccessoryId() {
        return this.accessoryId;
    }

    public void setAccessoryId(Long accessoryId) {
        this.accessoryId = accessoryId;
    }

    public Long getRepairId() {
        return this.repairId;
    }

    public void setRepairId(Long repairId) {
        this.repairId = repairId;
    }

    public String getAccessoryName() {
        return this.accessoryName;
    }

    public void setAccessoryName(String accessoryName) {
        this.accessoryName = accessoryName;
    }

    public String getAccessoryPrice() {
        return this.accessoryPrice;
    }

    public void setAccessoryPrice(String accessoryPrice) {
        this.accessoryPrice = accessoryPrice;
    }

    public int getAccessoryCount() {
        return this.accessoryCount;
    }

    public void setAccessoryCount(int accessoryCount) {
        this.accessoryCount = accessoryCount;
    }

    @Override
    public String getFirstContent() {
        return TextUtils.isEmpty(accessoryName) ? "" : accessoryName;
    }

    @Override
    public String getSecondContent() {
        return TextUtils.isEmpty(accessoryPrice) ? "" : accessoryPrice;
    }

    @Override
    public String getThirdContent() {
        return String.valueOf(accessoryCount);
    }
}
