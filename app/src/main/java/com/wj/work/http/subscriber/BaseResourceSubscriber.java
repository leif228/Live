package com.wj.work.http.subscriber;

import com.wj.work.http.support.DataException;
import com.wj.work.http.support.HttpResolver;
import com.lib.kit.utils.LL;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;

/**
 * @author tanghu
 * @title BaseResourceSubscriber
 * @package com.duoqio.gonet.http.subscriber
 * @date 2019/12/3 16:20
 */
public abstract class BaseResourceSubscriber<T> extends ResourceSubscriber<T> {
    private static final String TAG = "BaseResourceSubscriber";

    @Override
    public void onNext(T t) {
        if (isActivityDestroyed()) {
            return;
        }
        _onNext(t);
    }

    /**
     * if (isDestroyed()) {
     * return;
     * }
     *
     * @title _onNext
     * @author tanghu
     * @date 2020/4/2 10:37
     */
    protected abstract void _onNext(T t);

    /**
     * if (isDestroyed()) {
     * return;
     * }
     *
     * @title _onError
     * @author tanghu
     * @date 2020/4/2 10:37
     */
    protected abstract void _onError(Throwable t, String erroMsg);

    @Override
    public void onError(Throwable t) {
        LL.V("onError = "+t.getMessage());
        t.printStackTrace();


        if (isActivityDestroyed()) {
            return;
        }
        dispose();
        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            _onError(t, HttpResolver.getErrorMsg(HttpResolver.ERR_APP_NET));
        } else if (t instanceof HttpException) {
            _onError(t, HttpResolver.getErrorMsg(HttpResolver.ERR_APP_HTTP));
        } else if (t instanceof SocketTimeoutException) {
            _onError(t, HttpResolver.getErrorMsg(HttpResolver.ERR_APP_SOCKET_TIME_OUT));
        } else if (t instanceof SSLHandshakeException) {
            _onError(t, HttpResolver.getErrorMsg(HttpResolver.ERR_APP_SSL));
        } else if (t instanceof RuntimeException) {
            _onError(t, HttpResolver.getErrorMsg(HttpResolver.ERR_APP_RUNTIME));
        } else if (t instanceof JSONException) {
            _onError(t, HttpResolver.getErrorMsg(HttpResolver.ERR_APP_JSON));
        }  else if (t instanceof DataException) {
            _onError(t,((DataException) t).getMsg());
        }
        else {
            _onError(t, HttpResolver.getErrorMsg(HttpResolver.ERR_APP_UNKNOWN));
        }
    }

    protected abstract boolean isActivityDestroyed();

    @Override
    public void onComplete() {
    }

}
