package io.github.cnzbq.api;

import io.github.cnzbq.bean.ai.ExplainerResult;
import io.github.cnzbq.bean.ai.PredictInfo;
import io.github.cnzbq.bean.ai.PredictResult;
import io.github.cnzbq.bean.ai.SceneResult;
import io.github.cnzbq.enums.FmtEnum;
import io.github.cnzbq.enums.PlotEnum;
import io.github.cnzbq.error.AiErrorException;

/**
 * @author zbq
 * @since 1.0.0
 */
public interface AiService {

    /**
     * 项目列表
     *
     * @param sceneCode 场景code
     */
    SceneResult getSceneItem(Integer sceneCode) throws AiErrorException;

    /**
     * 疾病预测
     *
     * @param info 疾病预测参数
     */
    PredictResult predict(PredictInfo info) throws AiErrorException;

    /**
     * 疾病识别结果分析图
     *
     * @param requestId 请求id
     * @param matchId   模型id
     * @param plot      可选值有 waterfall/force,默认值force
     * @param fmt       可选值有 png/jpg/svg,默认值png
     * @param dpi       图片清晰度
     */
    ExplainerResult explain(String requestId, Long[] matchId, PlotEnum plot, FmtEnum fmt, Integer dpi) throws AiErrorException;

    /**
     * 疾病识别结果分析图
     *
     * @param requestId 请求id
     */
    default ExplainerResult explain(String requestId) throws AiErrorException {
        return explain(requestId, null, null, null, null);
    }

    /**
     * 疾病识别结果分析图
     *
     * @param requestId 请求id
     * @param matchId   模型id
     */
    default ExplainerResult explain(String requestId, Long[] matchId) throws AiErrorException {
        return explain(requestId, matchId, null, null, null);
    }

    /**
     * 疾病识别结果分析图
     *
     * @param requestId 请求id
     * @param matchId   模型id
     * @param plot      可选值有 waterfall/force,默认值force
     */
    default ExplainerResult explain(String requestId, Long[] matchId, PlotEnum plot) throws AiErrorException {
        return explain(requestId, matchId, plot, null, null);
    }

    /**
     * 疾病识别结果分析图
     *
     * @param requestId 请求id
     * @param matchId   模型id
     * @param plot      可选值有 waterfall/force,默认值force
     * @param fmt       可选值有 png/jpg/svg,默认值png
     */
    default ExplainerResult explain(String requestId, Long[] matchId, PlotEnum plot, FmtEnum fmt) throws AiErrorException {
        return explain(requestId, matchId, plot, fmt, null);
    }
}
