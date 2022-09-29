package com.github.cnzbq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Dingwq
 * @since 1.0.0
 */
@AllArgsConstructor
public enum PlotEnum {
    WATERFALL("waterfall"),
    FORCE("force");

    @Getter
    private final String value;
}
