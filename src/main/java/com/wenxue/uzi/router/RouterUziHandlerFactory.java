package com.wenxue.uzi.router;

import com.wenxue.uzi.router.annotation.RequestMapping;
import com.yl.vertx.server.utils.ReflectionUtil;
import com.yl.vertx.server.vertx.RouterSingleton;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * @author uzi
 */
@Slf4j
public class RouterUziHandlerFactory {

    private static volatile Reflections reflections;
    private volatile String gatewayPrefix;

    public RouterUziHandlerFactory(String routerScanAddress, String gatewayPrefix) {
        Objects.requireNonNull(routerScanAddress, "The router package address scan is empty.");
        reflections = ReflectionUtil.getReflections(routerScanAddress);
        this.gatewayPrefix = gatewayPrefix;
    }

    public Router createRouter() {
        Router router = RouterSingleton.getInstance();
        router.route().handler((ctx) -> {
            ctx.response().headers().add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
            ctx.response().headers().add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            ctx.response().headers().add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, PUT, DELETE, HEAD");
            ctx.response().headers().add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "X-PINGOTHER, Origin,Content-Type, Accept, X-Requested-With, Dev, Authorization, Version, Token");
            ctx.response().headers().add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "1728000");
            ctx.next();
        });
        Set<HttpMethod> method = new HashSet<HttpMethod>() {
            {
                this.add(HttpMethod.GET);
                this.add(HttpMethod.POST);
                this.add(HttpMethod.OPTIONS);
                this.add(HttpMethod.PUT);
                this.add(HttpMethod.DELETE);
                this.add(HttpMethod.HEAD);
            }
        };
        router.route().handler(CorsHandler.create("*").allowedMethods(method));
        router.route().handler(BodyHandler.create());
        try {
            Set<Class<?>> handlers = reflections.getTypesAnnotatedWith(RequestMapping.class);
            List<Class<?>> sortedHandlers = handlers.stream().sorted(getClassComparator()).collect(Collectors.toList());
            for (Class<?> handler : sortedHandlers) {
                try {
                    this.registerNewHandler(router, handler);
                } catch (Exception var9) {
                    log.error("Error register {}", handler);
                }
            }
        } catch (Exception var10) {
            log.error("Manually Register Handler Fail，Error details：" + var10.getMessage());
        }

        return router;
    }

    private void registerNewHandler(Router router, Class<?> handler) throws Exception {
        String root = this.gatewayPrefix;
        Object instance = handler.newInstance();
        Method[] methods = handler.getMethods();
        List<Method> methodList = Stream.of(methods).filter((method) -> method.isAnnotationPresent(RequestMapping.class)).sorted(getMethodComparator()).collect(Collectors.toList());
        for (Method method : methodList) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (handler.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping routeHandler =handler.getAnnotation(RequestMapping.class);
                    root = root.concat(routeHandler.value());
                }
                String url = root.concat(requestMapping.value());
                Handler<RoutingContext> methodHandler = (Handler)method.invoke(instance);
                log.info("Register New Handler -> {}", url);
                router.route(url).handler(methodHandler);
            }
        }

    }

    private Comparator<Method> getMethodComparator() {
        return (m1, m2) -> {
            RequestMapping mapping1 = (m1.getAnnotation(RequestMapping.class));
            RequestMapping mapping2 = m2.getAnnotation(RequestMapping.class);
                return Integer.compare(mapping2.order(), mapping1.order());
            };
    }

    private Comparator<Class<?>> getClassComparator() {
        return (c1, c2) -> {
            RequestMapping routeHandler1 = c1.getAnnotation(RequestMapping.class);
            RequestMapping routeHandler2 = c2.getAnnotation(RequestMapping.class);
            return Integer.compare(routeHandler2.order(), routeHandler1.order());
        };
    }
}
