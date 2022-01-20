package com.wenxue.uzi.service;

import com.wenxue.uzi.helper.InterceptorLocalBloomHelper;
import com.wenxue.uzi.router.annotation.RequestMapping;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author yl
 */
@RequestMapping("interceptorOnline")
public class InterceptorCacheService {

    @RequestMapping(value = "/validate")
    public Handler<RoutingContext> validate() { return InterceptorLocalBloomHelper::validateSingle; }



}
