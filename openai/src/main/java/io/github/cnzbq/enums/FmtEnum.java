package io.github.cnzbq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Dingwq
 * @since 1.0.0
 */
@AllArgsConstructor
public enum FmtEnum {
    PNG("png"),
    SVG("svg"),
    JPG("jpg");

    @Getter
    private final String value;
}
