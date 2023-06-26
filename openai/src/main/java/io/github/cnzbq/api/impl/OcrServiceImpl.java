package io.github.cnzbq.api.impl;

import io.github.cnzbq.api.OcrService;
import io.github.cnzbq.api.OpenAiService;
import io.github.cnzbq.bean.ocr.OcrResult;
import io.github.cnzbq.enums.AiApiUrl;
import io.github.cnzbq.bean.OcrInfo;
import io.github.cnzbq.error.AiErrorException;
import io.github.cnzbq.util.AiResponseUtils;
import io.github.cnzbq.util.Base64Encoder;
import io.github.cnzbq.util.FileUtils;
import io.github.cnzbq.util.http.OcrRequestExecutor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zbq
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class OcrServiceImpl implements OcrService {
    private final OpenAiService openAiService;

    @Override
    public OcrResult ocr(String[] imgContents, Integer reportType) throws AiErrorException {
        List<String> data = new ArrayList<>();
        for (String content : imgContents) {
            if (StringUtils.isNotBlank(content)) {
                data.add(content);
            }
        }
        OcrInfo ocrInfo = new OcrInfo();
        if (Objects.nonNull(reportType)) {
            ocrInfo.setReportType(reportType);
        }
        ocrInfo.setImgContents(data.toArray(new String[0]));
        String responseContent = this.openAiService.execute(OcrRequestExecutor.create(this.openAiService.getRequestHttp()),
                AiApiUrl.Ocr.CBC_URL.getUrl(this.openAiService.getAiConfig()), ocrInfo);
        return AiResponseUtils.resultHandler(responseContent, OcrResult.class);
    }

    @Override
    public OcrResult ocr(File[] files, Integer reportType) throws IOException, AiErrorException {
        String[] imgContents = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            // file length 1M
            if (file.length() > 1048576L) {
                imgContents[i] = Base64Encoder.encode(FileUtils.getImageCom(file));
            } else {
                imgContents[i] = Base64Encoder.encode(FileUtils.copyToByteArray(file));
            }
        }
        return ocr(imgContents, reportType);
    }

    @Override
    public OcrResult ocr(Integer reportType, String... imgUrl) throws IOException, AiErrorException {
        String[] imgContents = new String[imgUrl.length];
        for (int i = 0; i < imgUrl.length; i++) {
            byte[] bytes = FileUtils.imageUrlToBase64(imgUrl[i]);
            // file length 1M
            if (bytes.length > 1048576) {
                imgContents[i] = Base64Encoder.encode(FileUtils.getImageCom(bytes));
            } else {
                imgContents[i] = Base64Encoder.encode(bytes);
            }
        }

        return ocr(imgContents, reportType);
    }
}
