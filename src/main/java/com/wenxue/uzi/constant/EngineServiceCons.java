package com.wenxue.uzi.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yl
 * 业务常量配置
 */
@Component
public class EngineServiceCons {

    public static Integer HTTP_SERVER_PORT;
    public static String WEB_PACKAGES;
    public static Integer WORKER_POOL_SIZE;
    public static Integer CONNECT_TIME_OUT;
    public static String CONTEXT_PATH;

    @Value("${httpServerPort:9999}")
    public void setHttpServerPort(Integer httpServerPort) { HTTP_SERVER_PORT = httpServerPort; }

    @Value("${webPackages:com.wenxue.uzi.service}")
    public void setWebPackages(String webPackages) { WEB_PACKAGES = webPackages; }

    @Value("${workerPoolSize:20}")
    public void setWorkerPoolSize(Integer workerPoolSize) { WORKER_POOL_SIZE = workerPoolSize; }

    @Value("${connectTimeOut:10000}")
    public void setConnectTimeOut(Integer connectTimeOut) { CONNECT_TIME_OUT = connectTimeOut; }

    @Value("${contextPath:/}")
    public void setContextPath(String contextPath) { CONTEXT_PATH = contextPath; }
}
