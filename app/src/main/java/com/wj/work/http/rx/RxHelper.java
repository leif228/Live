package com.wj.work.http.rx;

import com.wj.work.http.support.DataException;
import com.wj.work.http.support.HttpResolver;
import com.wj.work.http.support.TResponse;
import com.lib.kit.utils.LL;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tanghu on 2018/6/14.
 */
public class RxHelper {

    public static <T> FlowableTransformer<T, T> io_main() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * .as(mView.<>bindAutoDispose())
     * 对服务器返回数据进行预处理
     *
     * @param <T> T
     */
    public static <T> FlowableTransformer<TResponse<T>, T> handleResult() {
        return upstream -> upstream.flatMap((Function<TResponse<T>, Publisher<T>>) result -> {
            if (HttpResolver.isResponseAvailable(result)) {
                T data = result.getResult();
                if (data != null) {
                    return createData(result.getResult());
                }
                return Flowable.error(new DataException(""));
            }

            LL.V("getErrorMessage="+result.getErrorMessage());
            return Flowable.error(new DataException(result.getErrorMessage()));
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private static <T> Flowable<T> createData(final T data) {
        return Flowable.create(emitter -> {
            emitter.onNext(data);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }
}
