package com.github.cnzbq.api.impl;

import com.github.cnzbq.api.AiService;
import com.github.cnzbq.api.OpenAiService;
import com.github.cnzbq.bean.ai.ExplainerResult;
import com.github.cnzbq.bean.ai.PredictInfo;
import com.github.cnzbq.bean.ai.PredictResult;
import com.github.cnzbq.bean.ai.SceneResult;
import com.github.cnzbq.enums.AiApiUrl;
import com.github.cnzbq.enums.FmtEnum;
import com.github.cnzbq.enums.PlotEnum;
import com.github.cnzbq.error.AiErrorException;
import com.github.cnzbq.util.AiResponseUtils;
import com.github.cnzbq.util.MapUtils;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Dingwq
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {
    private final OpenAiService openAiService;

    @Override
    public SceneResult getSceneItem(Integer sceneCode) throws AiErrorException {
        String responseContent = this.openAiService.post(AiApiUrl.AiOpen.SCENE_URL, String.format("{\"code\": %s}", sceneCode));
        return AiResponseUtils.resultHandler(responseContent, SceneResult.class);
    }

    @Override
    public PredictResult predict(PredictInfo info) throws AiErrorException {
        String responseContent = this.openAiService.post(AiApiUrl.AiOpen.PREDICT_URL, info.toJsonStr());
        return AiResponseUtils.resultHandler(responseContent, PredictResult.class);
    }

    @Override
    public ExplainerResult explain(String requestId, Long[] matchId, PlotEnum plot, FmtEnum fmt, Integer dpi) throws AiErrorException {
        Map<String, Object> data = new HashMap<>();
        data.put("requestId", requestId);
        data.put("plot", Objects.nonNull(plot) ? plot.getValue() : "force");
        data.put("fmt", Objects.nonNull(fmt) ? fmt.getValue() : "png");
        MapUtils.putNonNullValue(data, "matchId", matchId);
        String responseContent = this.openAiService.post(AiApiUrl.AiOpen.EXPLAINER_URL, MapUtils.toJsonStr(data));
        return AiResponseUtils.resultHandler(responseContent, ExplainerResult.class);
    }
}
