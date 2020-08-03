package com.wj.work.http.support;

/**
 * DataException
 * 2020/4/26 17:36
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class DataException extends Exception{

    private String msg;

    public DataException(String errorMessage) {
        this.msg=errorMessage;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
