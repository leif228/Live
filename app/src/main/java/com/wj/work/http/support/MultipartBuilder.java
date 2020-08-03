package com.wj.work.http.support;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by tanghu on 2018/7/2.
 */
public class MultipartBuilder {

    /**
     * 单文件
     *
     * @param file 文件
     * @param map  其它参数
     * @param name 参数名（name属性）
     */
    public static MultipartBody fileToMultipartBody(File file, Map<String, String> map, String name) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody requestBody = RequestBody.create(MultipartBody.FORM,file);
        builder.addFormDataPart(name, file.getName(), requestBody);
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    /**
     * 多文件
     *
     * @param files 文件
     * @param map   其它参数
     */
    public static MultipartBody filesToMultipartBody(List<File> files, Map<String, String> map) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        int i = 0;
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MultipartBody.FORM,file);
            builder.addFormDataPart("f" + ++i, file.getName(), requestBody);
        }
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }
}
