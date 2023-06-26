package io.github.cnzbq.util.http;

import io.github.cnzbq.bean.OcrInfo;
import io.github.cnzbq.error.AiErrorException;
import io.github.cnzbq.util.http.apache.OcrApacheHttpRequestExecutor;
import org.apache.http.Header;

import java.io.IOException;

/**
 * @author zbq
 * @since 1.0.0
 */
@SuppressWarnings("all")
public abstract class OcrRequestExecutor<H, P> implements RequestExecutor<String, OcrInfo> {
    protected RequestHttp<H, P> requestHttp;

    public OcrRequestExecutor(RequestHttp<H, P> requestHttp) {
        this.requestHttp = requestHttp;
    }

    @Override
    public void execute(String uri, OcrInfo ocrInfo, Header[] headers, ResponseHandler<String> handler) throws AiErrorException, IOException {
        handler.handle(this.execute(uri, ocrInfo, headers));
    }

    public static RequestExecutor<String, OcrInfo> create(RequestHttp requestHttp) {
        switch (requestHttp.getRequestType()) {
            case APACHE_HTTP:
                return new OcrApacheHttpRequestExecutor(requestHttp);
            default:
                return null;
        }
    }
}
