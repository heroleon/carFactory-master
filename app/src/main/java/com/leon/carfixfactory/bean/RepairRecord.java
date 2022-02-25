package com.leon.carfixfactory.bean;

import android.content.Context;

import com.leon.carfixfactory.R;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by leon
 * Date: 2019/11/5
 * Time: 19:50
 * Desc:维修记录
 */
@Entity
public class RepairRecord implements Serializable {
    private static final long serialVersionUID = 6016907103947932131L;

    @Id(autoincrement = true)
    public Long repairId;
    public Long driverId;
    public String repairOrderId;
    public String numberPlate;

    public long arrivalTime;        //进场时间
    public long deliveryTime;      //交车时间
    public String repairMileage;   //维修里程


    public String repairTotalFee;    //维修总价格
    public String repairDesc;        //维修描述

    public String totalPartFee;       //项目总价格
    public String totalAccessoryFee;  //配件总价格

    public int repairState = 0;        //0未维修 1已维修 2已交车


    public String dutyPersonName;
    public String dutyPersonId;


    @Generated(hash = 1280592811)
    public RepairRecord() {
    }

    @Generated(hash = 1536282064)
    public RepairRecord(Long repairId, Long driverId, String repairOrderId,
                        String numberPlate, long arrivalTime, long deliveryTime, String repairMileage,
                        String repairTotalFee, String repairDesc, String totalPartFee,
                        String totalAccessoryFee, int repairState, String dutyPersonName,
                        String dutyPersonId) {
        this.repairId = repairId;
        this.driverId = driverId;
        this.repairOrderId = repairOrderId;
        this.numberPlate = numberPlate;
        this.arrivalTime = arrivalTime;
        this.deliveryTime = deliveryTime;
        this.repairMileage = repairMileage;
        this.repairTotalFee = repairTotalFee;
        this.repairDesc = repairDesc;
        this.totalPartFee = totalPartFee;
        this.totalAccessoryFee = totalAccessoryFee;
        this.repairState = repairState;
        this.dutyPersonName = dutyPersonName;
        this.dutyPersonId = dutyPersonId;
    }

    public Long getRepairId() {
        return this.repairId;
    }

    public void setRepairId(Long repairId) {
        this.repairId = repairId;
    }

    public Long getDriverId() {
        return this.driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getRepairOrderId() {
        return this.repairOrderId;
    }

    public void setRepairOrderId(String repairOrderId) {
        this.repairOrderId = repairOrderId;
    }

    public long getArrivalTime() {
        return this.arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getDeliveryTime() {
        return this.deliveryTime;
    }

    public void setDeliveryTime(long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getRepairMileage() {
        return this.repairMileage;
    }

    public void setRepairMileage(String repairMileage) {
        this.repairMileage = repairMileage;
    }

    public String getRepairDesc() {
        return this.repairDesc;
    }

    public void setRepairDesc(String repairDesc) {
        this.repairDesc = repairDesc;
    }

    public int getRepairState() {
        return this.repairState;
    }

    public void setRepairState(int repairState) {
        this.repairState = repairState;
    }

    public String getDutyPersonName() {
        return this.dutyPersonName;
    }

    public void setDutyPersonName(String dutyPersonName) {
        this.dutyPersonName = dutyPersonName;
    }

    public String getDutyPersonId() {
        return this.dutyPersonId;
    }

    public void setDutyPersonId(String dutyPersonId) {
        this.dutyPersonId = dutyPersonId;
    }

    public String getRepairTotalFee() {
        return this.repairTotalFee;
    }

    public void setRepairTotalFee(String repairTotalFee) {
        this.repairTotalFee = repairTotalFee;
    }

    public String getTotalPartFee() {
        return this.totalPartFee;
    }

    public void setTotalPartFee(String totalPartFee) {
        this.totalPartFee = totalPartFee;
    }

    public String getTotalAccessoryFee() {
        return this.totalAccessoryFee;
    }

    public void setTotalAccessoryFee(String totalAccessoryFee) {
        this.totalAccessoryFee = totalAccessoryFee;
    }

    public String getNumberPlate() {
        return this.numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getRepairState(Context context) {
        if (repairState == 0) {
            return context.getString(R.string.state_wait_repair);
        } else if (repairState == 1) {
            return context.getString(R.string.state_repairing);
        } else {
            return context.getString(R.string.state_delivery);
        }
    }
}
