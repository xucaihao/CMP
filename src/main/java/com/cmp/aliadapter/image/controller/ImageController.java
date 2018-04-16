package com.cmp.aliadapter.image.controller;

import com.cmp.aliadapter.common.BaseController;
import com.cmp.aliadapter.image.service.ImageService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletionStage;

import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("")
public class ImageController extends BaseController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * 查询镜像列表
     *
     * @param request  http请求 http请求
     * @param response http响应 http响应
     * @return 主机列表
     */
    @RequestMapping("/images")
    @ResponseBody
    public CompletionStage<JsonNode> describeImages(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        return getCloudEntity(request)
                .thenApply(imageService::describeImages)
                .thenApply(x -> okFormat(OK.value(), x, response))
                .exceptionally(e -> badFormat(e, response));
    }
}
