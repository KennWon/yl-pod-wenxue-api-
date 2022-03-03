package com.wenxue.uzi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wenxue.uzi.constant.origin.ResponseResult;
import com.wenxue.uzi.constant.origin.ResultCodeEnum;
import com.wenxue.uzi.exception.CommonException;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * @author yl
 */
@Slf4j
public class ServerRequestUtil {

    public static Map<String, String> getBizContent(RoutingContext rc) {
        Map<String, String> biz = new HashMap<>(32);
        HttpServerRequest request = rc.request();
        MultiMap params = request.params();
        for (Map.Entry<String, String> param : params)
            biz.put(param.getKey(),param.getValue());
        String body = rc.getBodyAsString().trim();
        if (StringUtils.isNotEmpty(body)&&JSON.isValidObject(body))
            biz = JSON.parseObject(body,new TypeReference<Map<String, String>>(){});
        return biz;
    }

    public static <T> T getBizContent(RoutingContext rc,Class<T> clazz){
        T value = JSON.parseObject(JSON.toJSONString(getBizContent(rc)), clazz);
        Set<ConstraintViolation<T>> constraintValidations = Validation.buildDefaultValidatorFactory().getValidator().validate(value);
        if (CollectionUtils.isNotEmpty(constraintValidations)) {
            for (ConstraintViolation<T> constraintValidation : constraintValidations) {
                String message = constraintValidation.getMessage();
                if (StringUtils.isNotEmpty(message))
                    throw new CommonException(ResultCodeEnum.PARAMS_IS_INVALID.getCode(),message);
            }
        }
        return value;
    }

    public static void response(RoutingContext ctx,Object result) {
        ctx.response().putHeader("content-type", "application/json; charset=utf-8")
                .setStatusCode(HTTP_OK)
                .end(result.toString());
    }
    public static void response(RoutingContext ctx, ResponseResult result) {
        ctx.response().putHeader("content-type", "application/json; charset=utf-8")
                .setStatusCode(HTTP_OK)
                .end(JSON.toJSONString(result));
    }
}
