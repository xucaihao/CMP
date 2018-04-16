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

    //true(使用阿里云数据) false(使用模拟数据)
    private static final boolean status = true;

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
//        String accessKey = "LTAIRmS5yVTpnl5m";
//        String secret = "uroHDLzk1Ln8HoR9d1NYEt3e4bJLUR";
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKey, secret);
        return new DefaultAcsClient(profile);
    }
}
