package com.wenxue.uzi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.base.Objects;
import com.wenxue.uzi.constant.UziKeyPoolCons;
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
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author yl
 */
@Slf4j
public class HttpServerRequestUtil {

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

    public static String getBizContent(Map<String, String> paramMap) {
        String digest = paramMap.get("digest");
        String timestamp = paramMap.get("timestamp");
        String data = paramMap.get("data");
        String key = paramMap.get("key");
        String iv = paramMap.get("iv");
        if (StringUtils.isAnyEmpty(digest, timestamp, data, key, iv)) {
            log.error("paramMap: {} ", JSON.toJSONString(paramMap));
            throw new CommonException(ResultCodeEnum.PARAMS_NOT_COMPLETE);
        }
        long currentTime = System.currentTimeMillis();
        //校验过期时间
        Long requestTime = Long.parseLong(timestamp);
        if (Math.abs(currentTime - requestTime) >= UziKeyPoolCons.TIME_THRESHOLD) {
            log.error("currentTime：{}，requestTime：{}", currentTime, requestTime);
            throw new CommonException(ResultCodeEnum.INTERFACE_REQUEST_TIMEOUT);
        }
        //校验签名
        String waitMd5 = String.format(Md5Utils.MD5_FORMAT, data, iv, key, UziKeyPoolCons.SECRET_KEY, requestTime);
        String md5Sign = Md5Utils.md5(waitMd5);
        if (!Objects.equal(md5Sign, digest)) {
            log.error("waitMd5: {} md5String：{}，digest：{}", waitMd5, md5Sign, digest);
            throw new CommonException(ResultCodeEnum.CHECK_SIGN_FAIL);
        }
        String plaintext = AesUtils.getDecryptText(key, iv, data, UziKeyPoolCons.PRIVATE_KEY);
        if (StringUtils.isEmpty(plaintext)){
            throw new CommonException(ResultCodeEnum.DATA_DECODE_ERROR);
        }
        return plaintext;
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
                .setStatusCode(HttpURLConnection.HTTP_OK)
                .end(result.toString());
    }
    public static void response(RoutingContext ctx, ResponseResult result) {
        ctx.response().putHeader("content-type", "application/json; charset=utf-8")
                .setStatusCode(HttpURLConnection.HTTP_OK)
                .end(JSON.toJSONString(result));
    }
}
