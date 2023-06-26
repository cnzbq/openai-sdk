package io.github.cnzbq.api.impl;

import io.github.cnzbq.api.DiseaseService;
import io.github.cnzbq.api.OpenAiService;
import io.github.cnzbq.bean.disease.KnowledgeResult;
import io.github.cnzbq.enums.AiApiUrl;
import io.github.cnzbq.error.AiErrorException;
import io.github.cnzbq.util.AiResponseUtils;
import lombok.RequiredArgsConstructor;

/**
 * @author zbq
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
}
