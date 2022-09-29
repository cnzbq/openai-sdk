package com.github.cnzbq.api;

import com.github.cnzbq.bean.disease.KnowledgeResult;
import com.github.cnzbq.bean.disease.SubmitSurveyInfo;
import com.github.cnzbq.bean.disease.SurveyResult;
import com.github.cnzbq.bean.disease.SurveySubmitResult;
import com.github.cnzbq.error.AiErrorException;

/**
 * @author Dingwq
 * @since 1.0.0
 */
public interface DiseaseService {

    /**
     * 疾病科普
     *
     * @param diseaseCode 疾病编码
     */
    KnowledgeResult knowledge(Integer diseaseCode) throws AiErrorException;

    /**
     * 获取疾病问卷
     *
     * @param diseaseCode 疾病编码
     */
    default SurveyResult survey(Integer diseaseCode) throws AiErrorException {
        return survey(diseaseCode, null);
    }

    /**
     * 获取疾病问卷
     *
     * @param diseaseCode 疾病编码
     * @param surveyType  问卷类型
     */
    SurveyResult survey(Integer diseaseCode, Integer surveyType) throws AiErrorException;

    /**
     * @param info 问卷提交参数
     */
    SurveySubmitResult surveySubmit(SubmitSurveyInfo info) throws AiErrorException;
}
