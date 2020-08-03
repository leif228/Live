package com.wj.work.http.Interceptor;

import okhttp3.Interceptor;

/**
 * Created by tanghu on 2018/6/14.
 */
public interface BaseInterceptor extends Interceptor {

    long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

    // Content-Type
    String HEADER_CONTENT_TYPE_NAME = "Content-Type";
    String HEADER_CONTENT_TYPE_VALUE = "application/json";

    // Cache-Control
    String HEADER_CACHE_CONTROL_NAME = "Cache-Control";
    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    String HEADER_CACHE_CONTROL_VALUE_CACHE = "public, only-if-cached, max-stale=" + CACHE_STALE_SEC;
    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    String HEADER_CACHE_CONTROL_VALUE_AGE = "public, max-age=0";
    // Pragma 、 token
    String HEADER_PRAGMA_NAME = "Pragma";
    String HEADER_TOKEN_NAME = "token";
}
