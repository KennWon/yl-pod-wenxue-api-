package com.wenxue.uzi.controller;

import com.wenxue.uzi.router.annotation.RequestMapping;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author yl
 */
@RequestMapping("interceptorOnline")
public class InterceptorCacheController {


    @RequestMapping(value = "/validate")
    public Handler<RoutingContext> validate() { return InterceptorLocalBloomHelper::validateSingle; }



}
