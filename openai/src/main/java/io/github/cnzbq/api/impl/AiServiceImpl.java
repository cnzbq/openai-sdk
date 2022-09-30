package io.github.cnzbq.api.impl;

import io.github.cnzbq.api.AiService;
import io.github.cnzbq.api.OpenAiService;
import io.github.cnzbq.bean.ai.ExplainerResult;
import io.github.cnzbq.bean.ai.PredictInfo;
import io.github.cnzbq.bean.ai.PredictResult;
import io.github.cnzbq.bean.ai.SceneResult;
import io.github.cnzbq.enums.AiApiUrl;
import io.github.cnzbq.enums.FmtEnum;
import io.github.cnzbq.enums.PlotEnum;
import io.github.cnzbq.error.AiErrorException;
import io.github.cnzbq.util.AiResponseUtils;
import io.github.cnzbq.util.MapUtils;
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
