package io.github.cnzbq.util.http.apache;

import io.github.cnzbq.bean.OcrInfo;
import io.github.cnzbq.error.AiErrorException;
import io.github.cnzbq.util.AiResponseUtils;
import io.github.cnzbq.util.http.OcrRequestExecutor;
import io.github.cnzbq.util.http.RequestHttp;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Dingwq
 * @since 1.0.0
 */
public class OcrApacheHttpRequestExecutor extends OcrRequestExecutor<CloseableHttpClient, HttpHost> {
    public OcrApacheHttpRequestExecutor(RequestHttp<CloseableHttpClient, HttpHost> requestHttp) {
        super(requestHttp);
    }

    @Override
    public String execute(String uri, OcrInfo ocrInfo, Header[] headers) throws AiErrorException, IOException {
        HttpPost httpPost = new HttpPost(uri);
        if (requestHttp.getRequestHttpProxy() != null) {
            RequestConfig config = RequestConfig.custom().setProxy(requestHttp.getRequestHttpProxy()).build();
            httpPost.setConfig(config);
        } else {
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(120000)
                    .setConnectionRequestTimeout(120000)
                    .setSocketTimeout(120000)
                    .build();
            httpPost.setConfig(config);
        }

        if (Objects.nonNull(ocrInfo)) {
            HttpEntity entity = MultipartEntityBuilder
                    .create()
                    .addTextBody("imgContents", ocrInfo.getImgDataStr())
                    .addTextBody("reportType", String.valueOf(ocrInfo.getReportType()))
                    .setMode(HttpMultipartMode.RFC6532)
                    .build();
            httpPost.setEntity(entity);
            httpPost.setHeaders(headers);
        }
        try (CloseableHttpResponse response = requestHttp.getRequestHttpClient().execute(httpPost)) {
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
            return AiResponseUtils.handleResponse(responseContent);

        } finally {
            httpPost.releaseConnection();
        }
    }
}
