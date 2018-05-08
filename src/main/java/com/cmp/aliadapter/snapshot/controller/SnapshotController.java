package com.cmp.aliadapter.snapshot.controller;

import com.alibaba.dubbo.common.utils.IOUtils;
import com.cmp.aliadapter.common.BaseController;
import com.cmp.aliadapter.common.JsonUtil;
import com.cmp.aliadapter.snapshot.model.req.ReqCreSnapshot;
import com.cmp.aliadapter.snapshot.service.SnapshotService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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
public class SnapshotController extends BaseController {

    @Autowired
    private SnapshotService snapshotService;

    /**
     * 查询快照列表
     *
     * @param request  http请求 http请求
     * @param response http响应 http响应
     * @return 快照列表
     */
    @RequestMapping("/snapshots")
    @ResponseBody
    public CompletionStage<JsonNode> describeSnapshots(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        return getCloudEntity(request)
                .thenApply(snapshotService::describeSnapshots)
                .thenApply(x -> okFormat(OK.value(), x, response))
                .exceptionally(e -> badFormat(e, response));
    }

    /**
     * 创建快照
     *
     * @param request  http请求
     * @param response http响应
     * @return 操作结果
     * @throws IOException 异常
     */
    @PostMapping("/snapshots")
    @ResponseBody
    public CompletionStage<JsonNode> createSnapshot(
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String body = IOUtils.read(reader);
        ReqCreSnapshot reqCreSnapshot = JsonUtil.stringToObject(body, ReqCreSnapshot.class);
        return getCloudEntity(request)
                .thenAccept(cloud -> snapshotService.createSnapshot(cloud, reqCreSnapshot))
                .thenApply(x -> okFormat(OK.value(), null, response))
                .exceptionally(e -> badFormat(e, response));
    }

}
