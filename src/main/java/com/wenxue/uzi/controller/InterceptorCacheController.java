package com.wenxue.uzi.controller;

import com.wenxue.uzi.router.annotation.RequestMapping;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yl
 */
@RequestMapping("interceptorOnline")
@Slf4j
public class InterceptorCacheController {


    @RequestMapping(value = "/validate")
    public Handler<RoutingContext> validate() { return InterceptorLocalBloomHelper::validateSingle; }



}
