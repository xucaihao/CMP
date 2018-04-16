package com.cmp.aliadapter.snapshot.controller;

import com.cmp.aliadapter.common.BaseController;
import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.snapshot.service.SnapshotService;
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

}
