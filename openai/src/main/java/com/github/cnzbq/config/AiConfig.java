package com.github.cnzbq.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dingwq
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiConfig {
    /**
     * App_Key
     */
    private String appKey;
    /**
     * App_Secret
     */
    private String appSecret;
    /**
     * 版本号
     */
    private String version = "v1";

    /**
     * 域名配置
     */
    private AiHostConfig hostConfig;
}
