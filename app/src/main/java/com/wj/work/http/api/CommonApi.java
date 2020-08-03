package com.wj.work.http.api;

import com.wj.work.http.support.TResponse;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * CommonApi
 * 2020/5/7 11:50
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public interface CommonApi {

    /**
     * 单文件上传
     * Single file upload
     *
     * @param body 文件
     * @return 路径
     * @title uploadFile
     * @author tanghu
     * @date 2019/12/11 20:56
     */
    @POST("/api/Upload/uploadImg")
    Flowable<TResponse<String>> uploadFile(@Body MultipartBody body, @Header("Authorization") String token);
}
