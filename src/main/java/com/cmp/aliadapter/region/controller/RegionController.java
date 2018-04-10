package com.cmp.aliadapter.region.controller;

import com.cmp.aliadapter.common.BaseController;
import com.cmp.aliadapter.region.service.RegionService;
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
public class RegionController extends BaseController {

    @Autowired
    private RegionService regionService;

    /**
     * 查询地域列表
     *
     * @param request  http请求 http请求
     * @param response http响应 http响应
     * @return 地域列表
     */
    @RequestMapping("/regions")
    @ResponseBody
    public CompletionStage<JsonNode> describeRegions(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        return getCloudEntity(request)
                .thenApply(cloud -> regionService.describeRegions(cloud))
                .thenApply(x -> okFormat(OK.value(), x, response))
                .exceptionally(e -> badFormat(e, response));
    }

}
