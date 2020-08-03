package com.wj.work.http.support;

import com.wj.work.http.support.TResponse;

/**
 * HttpResolver
 * 处理返回的提示字符串
 * 2020/4/26 16:58
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class HttpResolver {

    public static final int ERR_APP_NET = 1;  // 网络异常
    public static final int ERR_APP_SSL = 2; // 证书异常
    public static final int ERR_APP_JSON = 3; // JSON 解析异常
    public static final int ERR_APP_SOCKET_TIME_OUT = 4; // 请求超时
    public static final int ERR_APP_RUNTIME = 5; // 运行异常
    public static final int ERR_APP_HTTP = 6; // 访问异常
    public static final int ERR_APP_UNKNOWN = 7; // 未知异常

    public static String getErrorMsg(int errorCode) {
        String result = "error";
        switch (errorCode) {
            case ERR_APP_NET:
                result = "连接不到服务器,请稍后重试!";
                break;
            case ERR_APP_SSL:
                result = "ERR_APP_SSL";
                break;
            case ERR_APP_JSON:
                result = "ERR_APP_JSON";
                break;
            case ERR_APP_SOCKET_TIME_OUT:
                result = "连接超时,请稍后重试!";
                break;
            case ERR_APP_RUNTIME:
                result = "ERR_APP_RUNTIME!";
                break;
            case ERR_APP_HTTP:
                result = "网络异常!";
                break;
            case ERR_APP_UNKNOWN:
                result = "未知错误!";
                break;
        }
        return result;
    }

    public static <T> boolean isResponseAvailable(TResponse<T> result) {
        return result != null && "0".equals(result.getCode());
    }
}
