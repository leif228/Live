package com.lib.kit.helper;

import android.text.TextUtils;

import com.lib.kit.model.PassCheckParams;
import com.lib.kit.utils.EditCheckUtils;
import com.lib.kit.utils.ToastUtils;

/**
 * @author Ly
 * @describe  用于一些常用的输入框内容检查
 * @date 2017/07/12 9:42
 * 依赖 ToastUtils
 */
public class EditChecker {

    /**
     * TODO 检测数字是否为零
     */
    public static boolean checkIntegerEmpty(int target, String emptyHint) {
        if (target==0){
            ToastUtils.showShort(emptyHint);
            return false;
        }
        return true;
    }

    /**
     * TODO 检测两个数大小  第一个是否大于第二个
     */
    public static boolean checkFloatBigThan(String arg1, String arg2, String illegalHint) {

        if (TextUtils.isEmpty(arg1)|| TextUtils.isEmpty(arg2))return  false;
        float f1= Float.parseFloat(arg1);
        float f2= Float.parseFloat(arg2);
        if (f1<f2){
            ToastUtils.showShort(illegalHint);
            return false;
        }
        return true;
    }

    /**
     * TODO 检测输入字符串是否为空
     */
    public static boolean checkEmpty(String target, String emptyHint) {
        if (TextUtils.isEmpty(target)){
            ToastUtils.showShort(emptyHint);
            return false;
        }
        return true;
    }

    /**
     * TODO 检测对象是否为null
     */
    public static boolean checkEmpty(Object target, String emptyHint) {
        if (target==null){
            ToastUtils.showShort(emptyHint);
            return false;
        }
        return true;
    }

    /**
     * TODO 检测 验证码
     *
     * digit 位数
     */
    public static boolean checkCode(String target,String emptyHint) {
        if (TextUtils.isEmpty(target)){
            ToastUtils.showShort(emptyHint);
            return false;
        }
//        if (target.length()!=digit){
//            ToastUtils.showShort(illegalHint);
//            return true;
//        }
        return true;
    }

    /**
     * TODO 检测多个字符串是否为空
     * 只要有一个为空 返回false
     */
    public static boolean checkEmptys(String emptyHint, String...target) {
        for (String item:target) {
            if (TextUtils.isEmpty(item)){
                ToastUtils.showShort(emptyHint);
                return false;
            }
        }
        return true;
    }

    /**
     * TODO 检测两个字符串是否一样
     * <p>一般用于密码验证是否一致</>
     */
    public static boolean checkSame(String target1, String target2, String illegalHint) {
        if (TextUtils.isEmpty(target1))return false;
        if (TextUtils.isEmpty(target2))return false;
        if (!target1.equals(target2)) {
            ToastUtils.showShort(illegalHint);
            return false;
        }
        return true;
    }

    /**
     * TODO 手机号码检测
     */
    public static boolean checkPhone(String phone, String emptyHint, String illegalHint) {

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort(emptyHint);
            return false;
        }
        return true;
    }

    /**
     * TODO 身份证号码检测
     */
    public static boolean checkIdCard(String target, String emptyHint, String illegalHint) {

        if (TextUtils.isEmpty(target)) {
            ToastUtils.showShort(emptyHint);
            return false;
        }

        if (!EditCheckUtils.isIDCardWeak(target)) {
            ToastUtils.showShort(illegalHint);
            return false;
        }
        return true;
    }

    /**
     * TODO  一般用于密码检测
     *
     * 位数+输入类型
     * <></>
     */
    public static boolean checkText(PassCheckParams params) {

        if (params == null) return false;

        if (TextUtils.isEmpty(params.target)) {
            ToastUtils.showShort(params.emptyHint);
            return false;
        }

        if (params.minLength > 0 && params.target.length() < params.minLength) {
            ToastUtils.showShort(params.minHint);
            return false;
        }

        if (params.maxLength>0&&params.target.length() > params.maxLength){
            ToastUtils.showShort(params.maxHint);
            return false;
        }

        if (params.type!=0){
            switch (params.type){
                case PassCheckParams.DIGIT:
                    if (!EditCheckUtils.isDigitAll(params.target)){
                        ToastUtils.showShort(params.digitHint);
                        return false;
                    }
                    break;
                case PassCheckParams.LETTER:
                    if (!EditCheckUtils.isLetterAll(params.target)){
                        ToastUtils.showShort(params.letterHint);
                        return false;
                    }
                    break;
                case PassCheckParams.DIGIT_OR_LETTER_NO_WHOLE:
                    if (!EditCheckUtils.isDigitOrLetter(params.target)){
                        ToastUtils.showShort(params.digitOrLetter_IllegalHint);
                        return false;
                    }

                    if (EditCheckUtils.isDigitAll(params.target)){
                        ToastUtils.showShort(params.digitOrLetter_AllDigitHint);
                        return false;
                    }

                    if (EditCheckUtils.isLetterAll(params.target)){
                        ToastUtils.showShort(params.digitOrLetter_AllLetterHint);
                        return false;
                    }
                    break;
            }
        }
        return true;
    }
}
