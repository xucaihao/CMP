package com.cmp.aliadapter.eip.controller;

import com.aliyuncs.exceptions.ClientException;
import com.cmp.aliadapter.common.BaseController;
import com.cmp.aliadapter.eip.modle.EipAddressInfo;
import com.cmp.aliadapter.eip.service.EipAddressService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("/eipAddresses")
public class EipController extends BaseController {

    @Autowired
    private EipAddressService eipAddressService;

    @RequestMapping("/regions/{regionId}")
    @ResponseBody
    public CompletionStage<JsonNode> describeEipAddresses(
            final HttpServletRequest request,
            final HttpServletResponse response,
            @PathVariable String regionId) {
        return getCloudEntity(request)
                .thenApply(cloud -> eipAddressService.describeEipAddresses(cloud, regionId))
                .thenApply(x -> okFormat(OK.value(), x, response))
                .exceptionally(e -> badFormat(e, response));
    }

}
