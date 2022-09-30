package io.github.cnzbq.api.impl;

import io.github.cnzbq.api.DiseaseService;
import io.github.cnzbq.api.OpenAiService;
import io.github.cnzbq.bean.disease.KnowledgeResult;
import io.github.cnzbq.bean.disease.SubmitSurveyInfo;
import io.github.cnzbq.bean.disease.SurveyResult;
import io.github.cnzbq.bean.disease.SurveySubmitResult;
import io.github.cnzbq.enums.AiApiUrl;
import io.github.cnzbq.error.AiErrorException;
import io.github.cnzbq.util.AiResponseUtils;
import io.github.cnzbq.util.MapUtils;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dingwq
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class DiseaseServiceImpl implements DiseaseService {
    private final OpenAiService openAiService;

    @Override
    public KnowledgeResult knowledge(Integer diseaseCode) throws AiErrorException {
        String responseContent = this.openAiService.post(AiApiUrl.Disease.KNOWLEDGE_URL, String.format("{\"code\": %s}", diseaseCode));
        return AiResponseUtils.resultHandler(responseContent, KnowledgeResult.class);
    }

    @Override
    public SurveyResult survey(Integer diseaseCode, Integer surveyType) throws AiErrorException {
        Map<String, Object> data = new HashMap<>();
        data.put("diseaseCode", diseaseCode);
        MapUtils.putNonNullValue(data, "surveyType", surveyType);
        String responseContent = this.openAiService.post(AiApiUrl.Disease.SURVEY_URL, MapUtils.toJsonStr(data));
        return AiResponseUtils.resultHandler(responseContent, SurveyResult.class);
    }

    @Override
    public SurveySubmitResult surveySubmit(SubmitSurveyInfo info) throws AiErrorException {
        String responseContent = this.openAiService.post(AiApiUrl.Disease.SURVEY_SUBMIT_URL, info.toJsonStr());
        return AiResponseUtils.resultHandler(responseContent, SurveySubmitResult.class);
    }
}
