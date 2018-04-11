package com.cmp.aliadapter.instance.controller;

import com.cmp.aliadapter.common.BaseController;
import com.cmp.aliadapter.instance.service.InstanceService;
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
public class InstanceController extends BaseController {

    private final InstanceService instanceService;

    @Autowired
    public InstanceController(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    /**
     * 查询主机列表
     *
     * @param request  http请求 http请求
     * @param response http响应 http响应
     * @return 主机列表
     */
    @RequestMapping("/instances")
    @ResponseBody
    public CompletionStage<JsonNode> describeInstances(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        return getCloudEntity(request)
                .thenApply(instanceService::describeInstances)
                .thenApply(x -> okFormat(OK.value(), x, response))
                .exceptionally(e -> badFormat(e, response));
    }
}
