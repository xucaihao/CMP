package com.cmp.aliadapter.disk.controller;

import com.cmp.aliadapter.common.BaseController;
import com.cmp.aliadapter.disk.service.DiskService;
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
public class DiskController extends BaseController {

    @Autowired
    private DiskService diskService;

    /**
     * 查询硬盘列表
     *
     * @param request  http请求 http请求
     * @param response http响应 http响应
     * @return 硬盘列表
     */
    @RequestMapping("/disks")
    @ResponseBody
    public CompletionStage<JsonNode> describeDisks(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        return getCloudEntity(request)
                .thenApply(diskService::describeDisks)
                .thenApply(x -> okFormat(OK.value(), x, response))
                .exceptionally(e -> badFormat(e, response));
    }
}
