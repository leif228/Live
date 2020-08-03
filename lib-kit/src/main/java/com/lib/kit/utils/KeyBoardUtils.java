package com.lib.kit.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * KeyBoardUtils
 * 2020/4/22 16:25
 * wj
 * wj
 *
 * @author Ly
 */
public class KeyBoardUtils {

    //关闭软键盘
    public static void hideSoftInputFromWindow(Context context,EditText edit) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        }
    }
}
