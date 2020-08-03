package com.wj.work.event;

/**
 * MessageEvent
 * 2020/4/22 15:59
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class MessageEvent<T> {

    public static final int LIVE_CLICK_EXIST = 0x101;  //  直播界面 点击了删除

    private int code;
    private String str1;
    private String str2;
    private int int1;
    private int int2;
    private long long1;
    private long long2;
    private T data;

    public MessageEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public int getInt1() {
        return int1;
    }

    public void setInt1(int int1) {
        this.int1 = int1;
    }

    public int getInt2() {
        return int2;
    }

    public void setInt2(int int2) {
        this.int2 = int2;
    }

    public long getLong1() {
        return long1;
    }

    public void setLong1(long long1) {
        this.long1 = long1;
    }

    public long getLong2() {
        return long2;
    }

    public void setLong2(long long2) {
        this.long2 = long2;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
