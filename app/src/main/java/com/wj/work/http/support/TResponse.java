package com.wj.work.http.support;


/**
 */
public class TResponse<T> implements java.io.Serializable {
    /**
     * 状态码
     */
    private String code ;
    /**
     * 成功失败消息
     */
    private String msg = "";
    /**
     * 数据
     */
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return msg;
    }

    public void setErrorMessage(String errorMessage) {
        this.msg = errorMessage;
    }

    public T getResult() {
        return data;
    }

    public void setResult(T result) {
        this.data = result;
    }
}
