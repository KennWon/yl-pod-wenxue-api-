package com.wenxue.uzi.engine;

import com.wenxue.uzi.constant.EngineServiceCons;
import com.wenxue.uzi.router.RouterUziHandlerFactory;
import com.yl.vertx.server.engine.CollectEngine;
import com.yl.vertx.server.engine.EngineInitializer;
import com.yl.vertx.server.enums.EngineType;
import com.yl.vertx.server.handlerfactory.RouterHandlerFactory;
import com.yl.vertx.server.vertx.DeployVertxServer;
import com.yl.vertx.server.vertx.VertxSingleton;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.micrometer.Label;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxJmxMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.EnumSet;

/**
 * @Author:yl
 */
@Slf4j
public class EngineFactory implements CollectEngine {

    @Override
    public EngineInitializer<String, Integer> useEngine(EngineType engineType) { return this::initEngine; }

    public void initEngine(String webApiPackages, Integer httpServerPort, Integer workerPoolSize, Integer eventBusOptionsConnectTimeout) {
        log.info("Vert.x engine init...");

        //启动容器
        EventBusOptions eventBusOptions = new EventBusOptions();
        //便于调试 设定超时等时间较长 生产环境建议适当调整
        eventBusOptions.setConnectTimeout(eventBusOptionsConnectTimeout);

        //Vert.x实例中使用的Event Loop线程的数量，默认值为：2 * Runtime.getRuntime().availableProcessors()
        //可用的处理器个数 * 2
        VertxOptions vertxOptions = new VertxOptions().setEventLoopPoolSize(2 * Runtime.getRuntime().availableProcessors())
                .setEventBusOptions(eventBusOptions)
                //Vert.x实例中支持的Worker线程的最大数量
                .setWorkerPoolSize(workerPoolSize)
                //内部阻塞线程池最大线程数，这个参数主要被Vert.x的一些内部操作使用，默认值为20
                .setInternalBlockingPoolSize(20)
                //阻塞线程检查的时间间隔，默认1000，单位ms，即1秒
                .setBlockedThreadCheckInterval(1000);

        //添加prometheus相关指标监控支持：添加这玩意性能下降30%，有钱加节点就完事了
        log.info("性能指标监控已打开...");
        CollectorRegistry prometheusClientRegistry = new CollectorRegistry();
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT, prometheusClientRegistry, Clock.SYSTEM);

        new ClassLoaderMetrics().bindTo(registry);
        new JvmMemoryMetrics().bindTo(registry);
        new JvmGcMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        new JvmThreadMetrics().bindTo(registry);

        vertxOptions.setMetricsOptions(new MicrometerMetricsOptions()
                .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true)
                                .setStartEmbeddedServer(true)
                                .setEmbeddedServerOptions(new HttpServerOptions().setPort(8071))
                                .setEmbeddedServerEndpoint("/metrics/vertx")
                )
                 //15秒dump一次，和运维请求频次一至，默认10秒
                .setJmxMetricsOptions(new VertxJmxMetricsOptions().setEnabled(true).setStep(15))
                .setMicrometerRegistry(registry)
                .setLabels(EnumSet.of(Label.HTTP_METHOD, Label.HTTP_CODE, Label.HTTP_PATH))
                .setEnabled(true));

        Vertx vertx = Vertx.vertx(vertxOptions);
        VertxSingleton.init(vertx);
        //启动引擎
        try {
            Router router = new RouterUziHandlerFactory(webApiPackages, EngineServiceCons.CONTEXT_PATH).createRouter();
            DeployVertxServer.startDeploy(router, httpServerPort);
            log.info("Vert.x engine started on port: {}", httpServerPort);
        } catch (IOException e) {
            log.error("Vert.x engine start failed：{}", e.getMessage(), e);
        }
    }
}
