package io.github.cnzbq.api;

import io.github.cnzbq.bean.SignHeader;
import io.github.cnzbq.config.AiConfig;
import io.github.cnzbq.enums.AiApiUrl;
import io.github.cnzbq.error.AiErrorException;
import io.github.cnzbq.util.http.RequestExecutor;
import io.github.cnzbq.util.http.RequestHttp;

/**
 * @author zbq
 * @since 1.0.0
 */
public interface OpenAiService extends io.github.cnzbq.service.AiService {

    /**
     * 返回ai类功能实现类
     */
    AiService getAiService();

    /**
     * 返回疾病类功能实现类
     */
    DiseaseService getDiseaseService();

    /**
     * 返回图像识别类功能实现类
     */
    OcrService getOcrService();

    /**
     * 返回问卷类功能实现类
     */
    SurveyService getSurveyService();

    /**
     * 返回用户类功能实现类
     */
    UserService getUserService();

    /**
     * 初始化http请求对象.
     */
    void initHttp();

    /**
     * 获取RequestHttp对象.
     *
     * @return RequestHttp对象 request http
     */
    RequestHttp getRequestHttp();

    /**
     * 获取授权请求头
     *
     * @param version 版本号
     * @return SignHeader 对象
     */
    SignHeader getSignHeader(String version);

    /**
     * 获取默认版本授权请求头
     *
     * @return SignHeader 对象
     */
    default SignHeader getSignHeader() {
        return getSignHeader("v1");
    }

    void setAiConfig(AiConfig aiConfig);

    /**
     * 获取配置
     */
    AiConfig getAiConfig();

    /**
     * 当本Service没有实现某个API的时候，可以用这个，针对所有的GET请求.
     *
     * @param url        请求接口地址
     * @param queryParam 参数
     * @return 接口响应字符串 string
     * @throws AiErrorException 异常
     */
    String get(AiApiUrl url, String queryParam) throws AiErrorException;

    /**
     * 当本Service没有实现某个API的时候，可以用这个，针对所有的POST请求.
     *
     * @param url      请求接口地址
     * @param postData 请求参数json值
     * @return 接口响应字符串 string
     * @throws AiErrorException 异常
     */
    String post(AiApiUrl url, String postData) throws AiErrorException;

    /**
     * 向开放平台发送请求
     */
    <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws AiErrorException;
}
