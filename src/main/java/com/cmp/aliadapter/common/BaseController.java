package com.cmp.aliadapter.common;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.cmp.aliadapter.common.Constance.HEADER_CLOUD_INFO;
import static com.cmp.aliadapter.common.ErrorEnum.ERR_CLOUD_INFO_NOT_FOUND;

@Controller
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    private Function<HttpServletRequest, CompletionStage<CloudEntity>> fn = request ->
            CompletableFuture.supplyAsync(() ->
                    Optional.ofNullable(request.getHeader(HEADER_CLOUD_INFO))
                            .map(cloudInfo ->
                                    JsonUtil.stringToObject(cloudInfo, CloudEntity.class))
                            .orElseThrow(() -> new AliException(ERR_CLOUD_INFO_NOT_FOUND))
            );

    /**
     * 从header中获取云实体
     *
     * @param request http请求
     * @return 云实体
     */
    protected CompletionStage<CloudEntity> getCloudEntity(HttpServletRequest request) {
        return fn.apply(request);
    }

    protected static JsonNode okFormat(int code, Object data, HttpServletResponse response) {
        final StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        String method = e.getMethodName().contains("lambda")
                ? e.getMethodName().split("\\$")[1] : e.getMethodName();
        method = "null".equals(method)
                ? "[line num :" + e.getLineNumber() + "]" : method;
        final String log = e.getFileName().replace(".java", "") + "::" + method;
        logger.info("invoke: {}, response code: {}", log, code);
        response.setStatus(code);
        if (null != data) {
            String dateStr = JsonUtil.objectToString(data);
            return JsonUtil.stringToObject(dateStr, JsonNode.class);
        } else {
            return JsonUtil.stringToObject("", JsonNode.class);
        }
    }

    protected static JsonNode badFormat(Throwable e, HttpServletResponse response) {
        final StackTraceElement e1 = Thread.currentThread().getStackTrace()[2];
        String method = e1.getMethodName().contains("lambda")
                ? e1.getMethodName().split("\\$")[1] : e1.getMethodName();
        final String log = e1.getFileName().replace(".java", "") + "::" + method;
        logger.info("invoke: {}, error: {}", log, e);
        return dealThrowable(e, response);
    }

    private static JsonNode dealThrowable(Throwable e, HttpServletResponse response) {
        int code = HttpStatus.BAD_REQUEST.value();
        String msg = "";
        if (null != e) {
            if (e instanceof RestClientResponseException) {
                code = ((RestClientResponseException) e).getRawStatusCode();
                msg = ((RestClientResponseException) e).getResponseBodyAsString();
            }
            if (e.getCause() instanceof RestClientResponseException) {
                code = ((RestClientResponseException) e.getCause()).getRawStatusCode();
                msg = ((RestClientResponseException) e.getCause()).getResponseBodyAsString();
            }
            if (e instanceof HttpClientErrorException) {
                code = ((HttpClientErrorException) e).getRawStatusCode();
                msg = ((HttpClientErrorException) e).getResponseBodyAsString();
            }
            if (e.getCause() instanceof HttpClientErrorException) {
                code = ((HttpClientErrorException) e.getCause()).getRawStatusCode();
                msg = ((HttpClientErrorException) e.getCause()).getResponseBodyAsString();
            }
        }
        response.setStatus(code);
        return JsonUtil.stringToObject(msg, JsonNode.class);
    }

}
