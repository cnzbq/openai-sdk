package io.github.cnzbq.api.impl;

import io.github.cnzbq.util.http.HttpType;
import io.github.cnzbq.util.http.apache.ApacheHttpClientBuilder;
import io.github.cnzbq.util.http.apache.DefaultApacheHttpClientBuilder;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @author zbq
 * @since 1.0.0
 */
public class OpenAiServiceHttpClientImpl extends BaseAiServiceImpl<CloseableHttpClient, HttpHost> {
    private CloseableHttpClient httpClient;
    private HttpHost httpProxy;

    @Override
    public void initHttp() {
        ApacheHttpClientBuilder apacheHttpClientBuilder = DefaultApacheHttpClientBuilder.get();
        this.httpClient = apacheHttpClientBuilder.build();
    }

    @Override
    public CloseableHttpClient getRequestHttpClient() {
        return httpClient;
    }

    @Override
    public HttpHost getRequestHttpProxy() {
        // 目前不支持
        return null;
    }

    @Override
    public HttpType getRequestType() {
        return HttpType.APACHE_HTTP;
    }
}
