package io.github.cnzbq.api;

import io.github.cnzbq.bean.user.AuthSceneResult;
import io.github.cnzbq.error.AiErrorException;

/**
 * @author zbq
 * @since 1.0.6
 */
public interface UserService {
    /**
     * 授权场景获取
     */
    AuthSceneResult getAuthScene() throws AiErrorException;
}
