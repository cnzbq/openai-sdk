package io.github.cnzbq.api.impl;

import com.google.gson.Gson;
import io.github.cnzbq.api.AiService;
import io.github.cnzbq.api.DiseaseService;
import io.github.cnzbq.api.OcrService;
import io.github.cnzbq.api.OpenAiService;
import io.github.cnzbq.bean.SignHeader;
import io.github.cnzbq.config.AiConfig;
import io.github.cnzbq.enums.AiApiUrl;
import io.github.cnzbq.error.AiErrorException;
import io.github.cnzbq.error.AiRuntimeException;
import io.github.cnzbq.util.http.RequestExecutor;
import io.github.cnzbq.util.http.RequestHttp;
import io.github.cnzbq.util.http.SimpleGetRequestExecutor;
import io.github.cnzbq.util.http.SimplePostRequestExecutor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;

import java.io.IOException;
import java.util.Objects;

import static io.github.cnzbq.util.Md5Util.md5Hex;

/**
 * @author Dingwq
 * @since 1.0.0
 */
@Slf4j
public abstract class BaseAiServiceImpl<H, P> implements OpenAiService, RequestHttp<H, P> {
    @Getter
    @Setter
    private AiService aiService = new AiServiceImpl(this);
    @Getter
    @Setter
    private DiseaseService diseaseService = new DiseaseServiceImpl(this);
    @Getter
    @Setter
    private OcrService ocrService = new OcrServiceImpl(this);
    @Getter
    private AiConfig aiConfig;
    private int retrySleepMillis = 1000;
    private int maxRetryTimes = 5;


    @Override
    public String get(AiApiUrl url, String queryParam) throws AiErrorException {
        return this.get(url.getUrl(this.aiConfig), queryParam);
    }

    @Override
    public String post(AiApiUrl url, String postData) throws AiErrorException {
        return this.post(url.getUrl(this.aiConfig), postData);
    }

    @Override
    public String get(String url, String queryParam) throws AiErrorException {
        return execute(SimpleGetRequestExecutor.create(this), url, queryParam);
    }

    @Override
    public String post(String url, String postData) throws AiErrorException {
        return execute(SimplePostRequestExecutor.create(this), url, postData);
    }

    @Override
    public RequestHttp<H, P> getRequestHttp() {
        return this;
    }

    @Override
    public SignHeader getSignHeader(String version) {
        long timestamp = System.currentTimeMillis();
        String appKey = this.aiConfig.getAppKey();
        if (StringUtils.isEmpty(appKey)) {
            throw new AiRuntimeException("appKey不能为空");
        }
        String appSecret = this.aiConfig.getAppSecret();
        if (StringUtils.isEmpty(appSecret)) {
            throw new AiRuntimeException("appSecret不能为空");
        }
        String ver = StringUtils.isEmpty(version) ? this.aiConfig.getVersion() : version;
        String auth = appKey + ":" + md5Hex(ver + timestamp + appSecret);
        return SignHeader.builder()
                .authorization(auth)
                .version(ver)
                .timestamp(String.valueOf(timestamp))
                .build();
    }

    @Override
    public void setAiConfig(AiConfig aiConfig) {
        this.aiConfig = aiConfig;
        this.initHttp();
    }

    @Override
    @SuppressWarnings("all")
    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws AiErrorException {
        SignHeader signHeader = getSignHeader();
        int retryTimes = 0;
        do {
            try {
                return this.executeInternal(executor, uri, data, signHeader.toHeaders());
            } catch (AiErrorException e) {
                // 500 系统错误, 1000ms后重试
                if (Objects.isNull(e.getCode()) || (e.getCode() == 500 || e.getCode() == 1)) {
                    if (StringUtils.isNotEmpty(e.getMessage()) && e.getMessage().contains("检验项目")) {
                        throw e;
                    }

                    // 判断是否已经超了最大重试次数
                    if (retryTimes + 1 > this.maxRetryTimes) {
                        log.warn("重试达到最大次数【{}】", maxRetryTimes);
                        log.error("请求异常", e);
                        //最后一次重试失败后，直接抛出异常，不再等待
                        throw e;
                    }

                    int sleepMillis = this.retrySleepMillis * (1 << retryTimes);
                    try {
                        log.warn("openAI系统错误，{} ms 后重试(第{}次)", sleepMillis, retryTimes + 1);
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e1) {
                        throw new AiErrorException(e1);
                    }
                } else {
                    throw e;
                }
            }
        } while (retryTimes++ < this.maxRetryTimes);

        log.warn("重试达到最大次数【{}】", this.maxRetryTimes);
        throw new AiErrorException("openAI服务端异常，超出重试次数");
    }

    protected <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data, Header[] headers) throws AiErrorException {
        try {
            T result = executor.execute(uri, data, headers);
            if (log.isDebugEnabled()) {
                log.debug("\n【请求地址】: {}\n【请求参数】：{}\n【响应数据】：{}", uri, new Gson().toJson(data), result);
            }
            return result;
        } catch (AiErrorException e) {
            log.warn("\n【请求地址】: {}\n【请求参数】：{}\n【错误信息】：", uri, new Gson().toJson(data), e);
            throw e;
        } catch (IOException e) {
            log.warn("\n【请求地址】: {}\n【请求参数】：{}\n【异常信息】：{}", uri, new Gson().toJson(data), e.getMessage());
            throw new AiErrorException(e);
        }
    }
}
