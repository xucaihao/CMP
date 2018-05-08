package com.cmp.aliadapter.disk.controller;

import com.alibaba.dubbo.common.utils.IOUtils;
import com.cmp.aliadapter.common.BaseController;
import com.cmp.aliadapter.common.JsonUtil;
import com.cmp.aliadapter.disk.model.req.ReqModifyDisk;
import com.cmp.aliadapter.disk.service.DiskService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    /**
     * 修改硬盘名称
     *
     * @param request  http请求
     * @param response http响应
     * @return 操作结果
     * @throws IOException 异常
     */
    @RequestMapping("/disks/modifyName")
    @ResponseBody
    public CompletionStage<JsonNode> modifyDiskName(
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String body = IOUtils.read(reader);
        ReqModifyDisk reqModifyDisk = JsonUtil.stringToObject(body, ReqModifyDisk.class);
        return getCloudEntity(request)
                .thenAccept(cloud -> diskService.modifyDiskName(cloud, reqModifyDisk))
                .thenApply(x -> okFormat(OK.value(), null, response))
                .exceptionally(e -> badFormat(e, response));
    }
}
