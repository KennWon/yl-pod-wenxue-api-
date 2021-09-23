package com.wenxue.uzi.controller;

import com.yl.vertx.server.anno.RouteHandler;
import com.yl.vertx.server.anno.RouteMapping;
import com.yl.vertx.server.anno.RouteMethod;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yl
 */
@RouteHandler("interceptorOnline")
@Slf4j
public class InterceptorCacheController {


    @RouteMapping(value = "/validate", method = RouteMethod.POST)
    public Handler<RoutingContext> validate() { return InterceptorLocalBloomHelper::validateSingle; }



}
