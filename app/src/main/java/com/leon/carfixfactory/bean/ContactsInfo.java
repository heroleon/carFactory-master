package com.leon.carfixfactory.bean;

import android.content.Context;
import android.text.TextUtils;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.ui.activity.AddWorkerActivity;
import com.leon.carfixfactory.utils.HanziToPinyin;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/5/13.
 * 联系人属性
 */

@Entity
public class ContactsInfo {
    //联系人ID
    @Id(autoincrement = true)
    private Long id;
    //联系人姓名
    private String name;
    //手机号
    private String phone_number;
    //家庭电话
    private String home_number;
    //职位
    private String duty;
    //备注
    private String note;
    //排序字母
    private String sortKey;



    @Generated(hash = 1681916646)
    public ContactsInfo(Long id, String name, String phone_number,
            String home_number, String duty, String note, String sortKey) {
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
        this.home_number = home_number;
        this.duty = duty;
        this.note = note;
        this.sortKey = sortKey;
    }



    @Generated(hash = 9726432)
    public ContactsInfo() {
    }



    public String checkData(Context context) {
        if (TextUtils.isEmpty(name)) {
            return "请输入姓名";
        }
        sortKey = getPinYin(name.substring(0, 1));
        if (TextUtils.isEmpty(phone_number)) {
            return "请输入手机号";
        }
        return null;
    }



    public Long getId() {
        return this.id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getName() {
        return this.name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public String getPhone_number() {
        return this.phone_number;
    }



    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }



    public String getHome_number() {
        return this.home_number;
    }



    public void setHome_number(String home_number) {
        this.home_number = home_number;
    }



    public String getDuty() {
        return this.duty;
    }



    public void setDuty(String duty) {
        this.duty = duty;
    }



    public String getNote() {
        return this.note;
    }



    public void setNote(String note) {
        this.note = note;
    }



    public String getSortKey() {
        return this.sortKey;
    }



    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    /**
     * 输入汉字返回拼音的通用方法函数
     *
     * @param hanzi 数据库中读取出的sort key
     * @return 拼音
     */
    private String getPinYin(String hanzi) {
        if (hanzi == null || hanzi.matches("\\d*")) {
            return "#";
        }
        ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(hanzi);
        StringBuilder sb = new StringBuilder();
        if (tokens != null && tokens.size() > 0) {
            for (HanziToPinyin.Token token : tokens) {
                if (HanziToPinyin.Token.PINYIN == token.type) {
                    sb.append(token.target.charAt(0));
                } else {
                    sb.append(token.source);
                }
            }
        }
        return sb.toString().toUpperCase();
    }
}