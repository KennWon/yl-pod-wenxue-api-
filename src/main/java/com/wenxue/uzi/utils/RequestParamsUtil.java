package com.wenxue.uzi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yl
 */
@Slf4j
public class RequestParamsUtil {

    public static Map<String, String> getBizContent(RoutingContext rc) {
        Map<String, String> biz = new HashMap<>(32);
        HttpServerRequest request = rc.request();
        MultiMap params = request.params();
        for (Map.Entry<String, String> param : params)
            biz.put(param.getKey(),param.getValue());
        String body = rc.getBodyAsString().trim();
        if (StringUtils.isNotEmpty(body)&&JSON.isValidObject(body))
            biz = JSON.parseObject(body,new TypeReference<Map<String, String>>(){});
        log.info("[API SERVICE].requestId:{},url:{},param:{}", Long.toHexString(System.currentTimeMillis()), request.path(), JSON.toJSONString(biz));
        return biz;
    }
}
