package io.github.cnzbq.api.impl;

import io.github.cnzbq.api.OpenAiService;
import io.github.cnzbq.api.UserService;
import io.github.cnzbq.bean.user.AuthSceneResult;
import io.github.cnzbq.enums.AiApiUrl;
import io.github.cnzbq.error.AiErrorException;
import io.github.cnzbq.util.AiResponseUtils;
import lombok.RequiredArgsConstructor;

/**
 * @author zbq
 * @since 1.0.6
 */
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final OpenAiService openAiService;

    @Override
    public AuthSceneResult getAuthScene() throws AiErrorException {
        String responseContent = this.openAiService.get(AiApiUrl.User.AUTH_SCENE_URL, null);
        return AiResponseUtils.resultHandler(responseContent, AuthSceneResult.class);
    }
}
