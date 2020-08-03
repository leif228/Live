package com.lib.kit.model;

public class PassCheckParams {

    public static final int DIGIT=1001;
    public static final int LETTER=1002;
    public static final int DIGIT_OR_LETTER_NO_WHOLE=1003;

    public String target;
    public int minLength=-1;
    public int maxLength=0;
    public String minHint;
    public String maxHint;
    public String emptyHint;

    // ######  全是数字  ############
    public String digitHint;
    // ######  全是字母  ############
    public String letterHint;
    // ######  字母和数字组合 并且至少有一个 ############
    public String digitOrLetter_IllegalHint;
    public String digitOrLetter_AllDigitHint;
    public String digitOrLetter_AllLetterHint;

    public int type =0;  // 类型验证,支持##字母,数字,字母和数字组合##

    /**
     * 设置目标字符串
     * @param target 目标字符串
     * @param emptyHint 为空时候的提示
     */
    public PassCheckParams target(String target, String emptyHint){
        this.target=target;
        this.emptyHint=emptyHint;
        return this;
    }

    /**
     * 字母或者数字 (并且不能全为字母或者全为数字)
     * @param illegalHint 没有通过时的提示
     * @param allDigitHint 全是数字的提示
     * @param allLetterHint 全是字母的提示
     */
    public PassCheckParams digitOrLetterCantWhole(String illegalHint, String allDigitHint, String allLetterHint){
        this.type=DIGIT_OR_LETTER_NO_WHOLE;
        this.digitOrLetter_IllegalHint=illegalHint;
        this.digitOrLetter_AllDigitHint=allDigitHint;
        this.digitOrLetter_AllLetterHint=allLetterHint;
        return this;
    }

    /**
     * 全由字母组成
     * @param letterHint 没有通过时的提示
     */
    public PassCheckParams letterAll(String letterHint){
        this.type=LETTER;
        this.letterHint=letterHint;
        return this;
    }

    /**
     * 全由数字组成
     * @param digitHint 没有通过时的提示
     */
    public PassCheckParams digitAll(String digitHint){
        this.type=DIGIT;
        this.digitHint=digitHint;
        return this;
    }

    /**
     * 设置目标字符串的最小长度
     * @param minLength 目标字符串最小长度
     * @param minHint  过小的提示
     */
    public PassCheckParams minLength(int minLength, String minHint){
        this.minLength=minLength;
        this.minHint=minHint;
        return this;
    }

    /**
     * 设置目标字符串的最大长度
     * @param maxLength 目标字符串最大长度
     * @param maxHint  过长的提示
     */
    public PassCheckParams maxLength(int maxLength, String maxHint){
        this.maxLength=maxLength;
        this.maxHint=maxHint;
        return this;
    }
}
