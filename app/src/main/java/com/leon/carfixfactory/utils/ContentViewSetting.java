package com.leon.carfixfactory.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.bean.ItemEditContent;

public class ContentViewSetting {
    public static void setItemContent(View view, ItemEditContent itemContent) {
        TextView leftTitle = view.findViewById(R.id.tv_left_title);
        ImageView ivMustShow = view.findViewById(R.id.iv_necessary_star);
        AppCompatEditText etContent = view.findViewById(R.id.et_content);
        leftTitle.setText(itemContent.title);
        int visible = itemContent.must ? View.VISIBLE : View.GONE;
        ivMustShow.setVisibility(visible);
        etContent.setInputType(itemContent.inputType);
        etContent.setHint(itemContent.hint);
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(itemContent.maxLength) {
        }});
    }

    public static AppCompatEditText getEditText(View view) {
        return view.findViewById(R.id.et_content);
    }

    public static String getEditTextContent(View view) {
        AppCompatEditText etContent = view.findViewById(R.id.et_content);
        return etContent.getText() == null ? null : etContent.getText().toString();
    }

    public static void setEditTextContent(View view, String content) {
        AppCompatEditText etContent = view.findViewById(R.id.et_content);
        etContent.setText(content);
    }

    /**
     * 设置剩余字数
     *
     * @param s        输入框
     * @param textView 右下角字数文本TextView
     * @param maxCount 最大字数
     */
    public static void setRemainingSize(Editable s, TextView textView, int maxCount) {
        if (s.length() > maxCount) {
            s.delete(maxCount, s.length());
        }
        textView.setText(String.valueOf(maxCount - s.length()));
    }
}
