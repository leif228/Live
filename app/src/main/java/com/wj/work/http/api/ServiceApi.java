package com.wj.work.http.api;

import com.wj.work.http.support.PageBean;
import com.wj.work.http.support.TResponse;
import com.wj.work.widget.entity.CreateLiveEntity;
import com.wj.work.widget.entity.LiveRecordEntity;
import com.wj.work.widget.entity.OrderEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ServiceApi {

    // 登录
    @FormUrlEncoded
    @POST("app/user/telLogin")
    Flowable<TResponse<List<OrderEntity>>> reqOderListByType(@Field("tel") String tel, @Field("password") String code);

    // 获取推流地址
    @FormUrlEncoded
    @POST("app/getPushFlowAddress")
    Flowable<TResponse<String>> getPushFlowAddress(@Field("id") String id, @Header("Authorization") String token);

    // 创建直播间
    @FormUrlEncoded
    @POST("app/createLiveStreamOrTrailer")
    Flowable<TResponse<CreateLiveEntity>> reqCreateLive(@FieldMap Map<String, Object> params, @Header("Authorization") String token);

    // 开启录像
    @FormUrlEncoded
    @POST("app/startRecording")
    Flowable<TResponse<Object>> reqStartRecord(@Field("id") String id, @Header("Authorization") String token);

    // 直播记录
    @FormUrlEncoded
    @POST("app/getMyLiveRecording")
    Flowable<TResponse<PageBean<LiveRecordEntity>>> reqLiveRecordList(@Field("currentPage") int currentPage,
                                                                      @Field("pageSize") int pageSize,
                                                                      @Header("Authorization") String token);


}
