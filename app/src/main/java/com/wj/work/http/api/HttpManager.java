package com.wj.work.http.api;

import android.content.Context;

import com.wj.work.db.SpManager;
import com.wj.work.http.RetrofitManger;
import com.wj.work.http.api.AccountApi;
import com.wj.work.http.api.CommonApi;
import com.wj.work.http.api.ServiceApi;
import com.wj.work.http.rx.RxHelper;
import com.wj.work.http.support.MultipartBuilder;
import com.luck.picture.lib.compress.Luban;

import java.io.File;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * HttpManager
 * 2020/4/26 15:55
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class HttpManager {

    /**
     * 压缩上传
     *
     * @param context
     * @param file
     * @title compressUploadFile
     * @author tanghu
     * @date 2020/4/7 14:02
     */
    public static Flowable<String> compressUploadFile(Context context, String file) {
        return Flowable
                .just(file)
                .observeOn(Schedulers.io())
                .map(s -> Luban.with(context).get(s))
                .flatMap((Function<File, Flowable<String>>) HttpManager::uploadFile)
                .compose(RxHelper.io_main());
    }

    /**
     * 单文件上传
     * Single file upload
     *
     * @param file 文件
     * @return 路径
     * @title uploadFile
     * @author tanghu
     * @date 2019/12/11 20:56
     */
    public static Flowable<String> uploadFile(File file) {
        return commonApi().uploadFile(MultipartBuilder.fileToMultipartBody(file, null, "file")
                , SpManager.getInstance().getLoginSp().getToken()
        )
                .compose(RxHelper.handleResult());
    }

    public static com.wj.work.http.api.AccountApi accountApi() {
        return RetrofitManger.$().createApi(AccountApi.class);
    }

    public static com.wj.work.http.api.ServiceApi serviceApi() {
        return RetrofitManger.$().createApi(ServiceApi.class);
    }

    public static com.wj.work.http.api.CommonApi commonApi() {
        return RetrofitManger.$().createApi(CommonApi.class);
    }

}
