package com.wenxue.uzi.listener;

import com.wenxue.uzi.constant.EngineServiceCons;
import com.yl.vertx.server.engine.CollectEngine;
import com.yl.vertx.server.enums.EngineType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ServiceLoader;

/**
 * @Author:yl
 */
@Component
public class DeployEngineListener {


    @EventListener
    public void startEngine(ApplicationReadyEvent event) {
        ApplicationContext parent = event.getApplicationContext().getParent();
        if (parent == null) {
            //初始化引擎: 使用SPI机制，按需定制高并发引擎(NETTY/VERTX/其他等)
            ServiceLoader<CollectEngine> engineLoader = ServiceLoader.load(CollectEngine.class);
            for (CollectEngine engine : engineLoader ) {
                engine.useEngine(EngineType.VERTX).init(EngineServiceCons.WEB_PACKAGES, EngineServiceCons.HTTP_SERVER_PORT, EngineServiceCons.WORKER_POOL_SIZE, EngineServiceCons.CONNECT_TIME_OUT);
                break;
            }
        }
    }
}
