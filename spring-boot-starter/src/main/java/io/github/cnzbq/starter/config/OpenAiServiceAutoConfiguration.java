package io.github.cnzbq.starter.config;

import io.github.cnzbq.starter.properties.OpenAiProperties;
import io.github.cnzbq.api.OpenAiService;
import io.github.cnzbq.api.impl.OpenAiServiceHttpClientImpl;
import io.github.cnzbq.config.AiConfig;
import io.github.cnzbq.config.AiHostConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dingwq
 * @since 1.0.0
 */
@Configuration
@RequiredArgsConstructor
public class OpenAiServiceAutoConfiguration {
    private final OpenAiProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public OpenAiService openAiService() {
        AiConfig config = new AiConfig();
        config.setAppKey(properties.getAppKey());
        config.setAppSecret(properties.getAppSecret());
        config.setVersion(properties.getVersion());
        AiHostConfig hostConfig = new AiHostConfig();
        hostConfig.setOpenHost(properties.getOpenHost());
        config.setHostConfig(hostConfig);

        OpenAiService openAiService = new OpenAiServiceHttpClientImpl();
        openAiService.setAiConfig(config);
        return openAiService;
    }
}
