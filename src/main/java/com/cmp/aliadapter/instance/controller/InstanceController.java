package com.cmp.aliadapter.instance.controller;

import com.alibaba.dubbo.common.utils.IOUtils;
import com.cmp.aliadapter.common.BaseController;
import com.cmp.aliadapter.common.JsonUtil;
import com.cmp.aliadapter.instance.model.req.ReqCloseInstance;
import com.cmp.aliadapter.instance.model.req.ReqModifyInstance;
import com.cmp.aliadapter.instance.model.req.ReqStartInstance;
import com.cmp.aliadapter.instance.service.InstanceService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    /**
     * 查询指定主机
     *
     * @param request    http请求 http请求
     * @param response   http响应 http响应
     * @param regionId   区域id
     * @param instanceId 实例id
     * @return 指定主机
     */
    @RequestMapping("{regionId}/instances/{instanceId}")
    @ResponseBody
    public CompletionStage<JsonNode> describeInstanceAttribute(
            final HttpServletRequest request,
            final HttpServletResponse response,
            @PathVariable final String regionId,
            @PathVariable final String instanceId) {
        return getCloudEntity(request)
                .thenApply(cloud -> instanceService.describeInstance(cloud, regionId, instanceId))
                .thenApply(x -> okFormat(OK.value(), x, response))
                .exceptionally(e -> badFormat(e, response));
    }

    /**
     * 关闭主机
     *
     * @param request  http请求
     * @param response http响应
     * @return 操作结果
     * @throws IOException 异常
     */
    @RequestMapping("/instances/close")
    @ResponseBody
    public CompletionStage<JsonNode> closeInstance(
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String body = IOUtils.read(reader);
        ReqCloseInstance reqCloseInstance = JsonUtil.stringToObject(body, ReqCloseInstance.class);
        return getCloudEntity(request)
                .thenAccept(cloud -> instanceService.closeInstance(cloud, reqCloseInstance))
                .thenApply(x -> okFormat(OK.value(), null, response))
                .exceptionally(e -> badFormat(e, response));
    }

    /**
     * 启动主机
     *
     * @param request  http请求
     * @param response http响应
     * @return 操作结果
     * @throws IOException 异常
     */
    @RequestMapping("/instances/start")
    @ResponseBody
    public CompletionStage<JsonNode> startInstance(
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String body = IOUtils.read(reader);
        ReqStartInstance reqStartInstance = JsonUtil.stringToObject(body, ReqStartInstance.class);
        return getCloudEntity(request)
                .thenAccept(cloud -> instanceService.startInstance(cloud, reqStartInstance))
                .thenApply(x -> okFormat(OK.value(), null, response))
                .exceptionally(e -> badFormat(e, response));
    }

    /**
     * 修改主机名称
     *
     * @param request  http请求
     * @param response http响应
     * @return 操作结果
     * @throws IOException 异常
     */
    @RequestMapping("/instances/modifyName")
    @ResponseBody
    public CompletionStage<JsonNode> modifyInstanceName(
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String body = IOUtils.read(reader);
        ReqModifyInstance reqModifyInstance = JsonUtil.stringToObject(body, ReqModifyInstance.class);
        return getCloudEntity(request)
                .thenAccept(cloud -> instanceService.modifyInstanceName(cloud, reqModifyInstance))
                .thenApply(x -> okFormat(OK.value(), null, response))
                .exceptionally(e -> badFormat(e, response));
    }
}
