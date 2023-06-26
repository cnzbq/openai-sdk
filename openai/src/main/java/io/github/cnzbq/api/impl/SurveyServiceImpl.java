package io.github.cnzbq.api.impl;

import io.github.cnzbq.api.OpenAiService;
import io.github.cnzbq.api.SurveyService;
import io.github.cnzbq.bean.survey.SubmitSurveyInfo;
import io.github.cnzbq.bean.survey.SurveyResult;
import io.github.cnzbq.bean.survey.SurveySubmitResult;
import io.github.cnzbq.enums.AiApiUrl;
import io.github.cnzbq.error.AiErrorException;
import io.github.cnzbq.util.AiResponseUtils;
import io.github.cnzbq.util.MapUtils;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zbq
 * @since 1.0.6
 */
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {
    private final OpenAiService openAiService;

    @Override
    public SurveyResult survey(Integer diseaseCode, Integer surveyType) throws AiErrorException {
        Map<String, Object> data = new HashMap<>();
        data.put("diseaseCode", diseaseCode);
        MapUtils.putNonNullValue(data, "surveyType", surveyType);
        String responseContent = this.openAiService.post(AiApiUrl.Survey.SURVEY_URL, MapUtils.toJsonStr(data));
        return AiResponseUtils.resultHandler(responseContent, SurveyResult.class);
    }

    @Override
    public SurveySubmitResult surveySubmit(SubmitSurveyInfo info) throws AiErrorException {
        String responseContent = this.openAiService.post(AiApiUrl.Survey.SURVEY_SUBMIT_URL, info.toJsonStr());
        return AiResponseUtils.resultHandler(responseContent, SurveySubmitResult.class);
    }
}
