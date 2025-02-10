/*
   Copyright 2015 Wang Haomiao<seimimaster@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package cn.wanghaomiao.seimi.http.hc;

import org.apache.hc.core5.http.HttpEntityEnclosingRequest;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.client5.http.HttpRequestRetryHandler;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.protocol.RedirectStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.classic.LaxRedirectStrategy;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author github.com/zhegexiaohuozi seimimaster@gmail.com
 *         Date: 2014/11/13.
 */
public class HttpClientFactory {

    private static final Map<String, HttpClient> hcCache = new ConcurrentHashMap<>();
    public static HttpClient getHttpClient() {
        int defTimeout = 10000;
        return getHttpClient(defTimeout);
    }

    public static HttpClient getHttpClient(int timeout) {
        String defKey = String.valueOf(timeout);
        HttpClient hc = hcCache.get(defKey);
        if (hc == null){
            hc = cliBuilder(timeout).build();
            hcCache.put(defKey, hc);
        }
        return hc;
    }

    public static HttpClient getHttpClient(int timeout, CookieStore cookieStore) {
        return cliBuilder(timeout).setDefaultCookieStore(cookieStore).build();
    }

    public static HttpClientBuilder cliBuilder(int timeout) {
        HttpRequestRetryHandler retryHander = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount > 3) {
                    // Do not retry if over max retry count
                    return false;
                }
                if (exception instanceof java.net.SocketTimeoutException) {
                    //特殊处理
                    return true;
                }
                if (exception instanceof InterruptedIOException) {
                    // Timeout
                    return true;
                }
                if (exception instanceof UnknownHostException) {
                    // Unknown host
                    return false;
                }

                if (exception instanceof SSLException) {
                    // SSL handshake exception
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    // Retry if the request is considered idempotent
                    return true;
                }
                return false;
            }
        };
        RedirectStrategy redirectStrategy = new LaxRedirectStrategy();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout, TimeUnit.MILLISECONDS).setConnectionRequestTimeout(timeout, TimeUnit.MILLISECONDS).setResponseTimeout(timeout, TimeUnit.MILLISECONDS).build();
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = HttpClientConnectionManagerProvider.getHcPoolInstance();
        return HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(poolingHttpClientConnectionManager)
                .setRedirectStrategy(redirectStrategy).setRetryHandler(retryHander);
    }
}
