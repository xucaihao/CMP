package com.cmp.aliadapter.common;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.cmp.aliadapter.common.Constance.ACCESS_KEY;
import static com.cmp.aliadapter.common.Constance.SECRET;
import static com.cmp.aliadapter.common.ErrorEnum.ERR_AUTH_INFO;

public class AliClient {

    private static final Logger logger = LoggerFactory.getLogger(AliClient.class);

    private static final boolean status = false;

    public static boolean getStatus() {
        return status;
    }

    public static IAcsClient initClient(CloudEntity cloud, String regionId) {
        // 初始化
        JsonNode authInfo = Optional.ofNullable(
                JsonUtil.stringToObject(cloud.getAuthInfo(), JsonNode.class))
                .orElseThrow(() -> new AliException(ERR_AUTH_INFO));
        String accessKey = authInfo.path(ACCESS_KEY).asText();
        String secret = authInfo.path(SECRET).asText();
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKey, secret);
        return new DefaultAcsClient(profile);
    }
}
