package com.wj.work.http.api;

import com.wj.work.http.support.TResponse;
import com.wj.work.widget.entity.RealNameInfo;
import com.wj.work.widget.entity.LoginEntity;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AccountApi {

    // 发送验证码
    @FormUrlEncoded
    @POST("app/getLoginCode")
    Flowable<TResponse<Object>> getLoginCode(@Field("tel") String tel);

    // 登录
    @FormUrlEncoded
    @POST("app/appUserLogin")
    Flowable<TResponse<LoginEntity>> login(@Field("tel") String tel,
                                           @Field("code") String code
    );

    // 实名认证
    @FormUrlEncoded
    @POST("app/userRealNameAuthentication")
    Flowable<TResponse<Object>> userRealNameAuthentication(@Field("idCard") String tel,
                                                           @Field("nationalEmblemImg") String nationalEmblemImg,
                                                           @Field("portraitImg") String portraitImg,
                                                           @Field("realName") String realName,
                                                           @Header("Authorization") String token
    );

    // 实名认证信息
    @POST("app/getCertificationVo")
    Flowable<TResponse<RealNameInfo>> reqCertificationInfo(@Header("Authorization") String token);
}
