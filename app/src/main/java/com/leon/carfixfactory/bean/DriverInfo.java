package com.leon.carfixfactory.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by leon
 * Date: 2019/11/5
 * Time: 16:56
 * Desc:车主车辆信息
 */

@Entity
public class DriverInfo implements Serializable {
    private static final long serialVersionUID = 5636116388670610424L;
    @Id(autoincrement = true)
    public Long driverId;
    public String driverName;   //车主姓名
    public String driverPhone;
    public String driverAddress;

    public String numberPlate; //车牌号
    public String carModel;    //车型
    public String frameNum;    //车架号
    public String engineNum;   //发动机号

    public String departmentName;  //单位名称
    public String departmentPhone; //单位电话
    public String departmentAddr;  //单位地址
    @Generated(hash = 1741988379)
    public DriverInfo(Long driverId, String driverName, String driverPhone,
            String driverAddress, String numberPlate, String carModel,
            String frameNum, String engineNum, String departmentName,
            String departmentPhone, String departmentAddr) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.driverAddress = driverAddress;
        this.numberPlate = numberPlate;
        this.carModel = carModel;
        this.frameNum = frameNum;
        this.engineNum = engineNum;
        this.departmentName = departmentName;
        this.departmentPhone = departmentPhone;
        this.departmentAddr = departmentAddr;
    }
    @Generated(hash = 2077275369)
    public DriverInfo() {
    }
    public Long getDriverId() {
        return this.driverId;
    }
    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
    public String getDriverName() {
        return this.driverName;
    }
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    public String getDriverPhone() {
        return this.driverPhone;
    }
    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }
    public String getDriverAddress() {
        return this.driverAddress;
    }
    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }
    public String getNumberPlate() {
        return this.numberPlate;
    }
    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }
    public String getCarModel() {
        return this.carModel;
    }
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
    public String getFrameNum() {
        return this.frameNum;
    }
    public void setFrameNum(String frameNum) {
        this.frameNum = frameNum;
    }
    public String getEngineNum() {
        return this.engineNum;
    }
    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }
    public String getDepartmentName() {
        return this.departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    public String getDepartmentPhone() {
        return this.departmentPhone;
    }
    public void setDepartmentPhone(String departmentPhone) {
        this.departmentPhone = departmentPhone;
    }
    public String getDepartmentAddr() {
        return this.departmentAddr;
    }
    public void setDepartmentAddr(String departmentAddr) {
        this.departmentAddr = departmentAddr;
    }

}
