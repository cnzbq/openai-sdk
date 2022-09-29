package com.github.cnzbq.bean.ocr;

import com.github.cnzbq.bean.AiBaseResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Dingwq
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OcrResult extends AiBaseResult<OcrResult.ItemResult> {

    @Data
    public static class ItemResult {
        private List<OcrItem> items;
    }

    @Data
    public static class OcrItem {

        /**
         * 英文名称
         */
        private String enName;

        /**
         * 中文名称
         */
        private String cnName;

        /**
         * 结果
         */
        private String result;

        /**
         * 概率
         */
        private Float prob;

        /**
         * 单位
         */
        private String unit;
    }
}
