package com.wenxue.uzi.exception;


import com.alibaba.fastjson.JSONException;
import com.wenxue.uzi.constant.origin.ResponseResult;
import com.wenxue.uzi.constant.origin.ResultCodeEnum;
import com.wenxue.uzi.utils.HttpServerRequestUtil;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;

/**
 * @author yl
 */
@Slf4j
public class GlobExceptionHandler implements Handler<RoutingContext> {

    public static GlobExceptionHandler of() { return new GlobExceptionHandler();}

    @Override
    public void handle(RoutingContext ctx) {
        Throwable throwable = ctx.failure();
        if (throwable instanceof JSONException) {
            JSONException e = (JSONException) throwable;
            log.error("[API GLOB EXCEPTION].title:{},message:{},url:{},param:{}", "JSONException", e.getMessage(), ctx.request().path(), HttpServerRequestUtil.getBizContent(ctx), e);
            HttpServerRequestUtil.response(ctx, ResponseResult.genErrorResult(ResultCodeEnum.JSON_PARSING_ERROR));
            return;
        }else if (throwable instanceof ConstraintViolationException) {
            ConstraintViolationException e = (ConstraintViolationException) throwable;
            log.error("[API GLOB EXCEPTION].title:{},message:{},url:{},param:{}", "ConstraintViolationException", e.getMessage(), ctx.request().path(), HttpServerRequestUtil.getBizContent(ctx), e);
            HttpServerRequestUtil.response(ctx, ResponseResult.genErrorResult(ResultCodeEnum.PARAMS_IS_INVALID.getCode(),e.getMessage()));
            return;
        }else if (throwable instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) throwable;
            log.error("[API GLOB EXCEPTION].title:{},message:{},url:{},param:{}", "MethodArgumentNotValidException", e.getMessage(), ctx.request().path(), HttpServerRequestUtil.getBizContent(ctx), e);
            HttpServerRequestUtil.response(ctx, ResponseResult.genErrorResult(ResultCodeEnum.PARAMS_IS_INVALID.getCode(),e.getMessage()));
            return;
        } else if (throwable instanceof CommonException) {
            CommonException e = (CommonException) throwable;
            log.error("[API GLOB EXCEPTION].title:{},code:{},message:{},url:{},param:{}", "CommonException", e.getCode(),e.getMessage(), ctx.request().path(), HttpServerRequestUtil.getBizContent(ctx), e);
            HttpServerRequestUtil.response(ctx, ResponseResult.genErrorResult(e.getCode(),e.getMessage()));
            return;
        }else if (throwable instanceof Exception) {
            Exception e = (Exception) throwable;
            log.error("[API GLOB EXCEPTION].title:{},message:{},url:{},param:{}", "Exception", e.getMessage(), ctx.request().path(), HttpServerRequestUtil.getBizContent(ctx), e);
            HttpServerRequestUtil.response(ctx, ResponseResult.genErrorResult(ResultCodeEnum.FAIL));
            return;
        }
    }

}
