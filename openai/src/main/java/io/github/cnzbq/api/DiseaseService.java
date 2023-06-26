package io.github.cnzbq.api;

import io.github.cnzbq.bean.disease.KnowledgeResult;
import io.github.cnzbq.error.AiErrorException;

/**
 * @author zbq
 * @since 1.0.0
 */
public interface DiseaseService {

    /**
     * 疾病科普
     *
     * @param diseaseCode 疾病编码
     */
    KnowledgeResult knowledge(Integer diseaseCode) throws AiErrorException;
}
