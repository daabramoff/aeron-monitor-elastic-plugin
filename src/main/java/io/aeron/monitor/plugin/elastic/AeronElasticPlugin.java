package io.aeron.monitor.plugin.elastic;

import io.aeron.monitor.DriverAccess;
import io.aeron.monitor.ext.Plugin;
import io.aeron.monitor.plugin.elastic.indexrotation.IndexRotationStrategyFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class AeronElasticPlugin implements Plugin {
    private ScheduledExecutorService executorService;

    private AnnotationConfigApplicationContext ctx;
    private ElasticSender sender;
    private PluginConfig config;
    private Map<String, DriverAccess> driverAccessMap;

    @Override
    public void init(String[] strings, Map<String, DriverAccess> map) {
        try {
            ctx = new AnnotationConfigApplicationContext(
                    PluginConfig.class,
                    SpringApp.class,
                    IndexRotationStrategyFactory.class);
            executorService = Executors.newSingleThreadScheduledExecutor();
            config = ctx.getBean(PluginConfig.class);
            sender = ctx.getBean(ElasticSender.class);
            driverAccessMap = map;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        ctx.close();
    }

    @Override
    public void run() {
        long timeMs = System.currentTimeMillis();
        TimeUnit unit = TimeUnit.valueOf(config.getSendIntervalUnit());
        long intervalMs = unit.toMillis(config.getSendIntervalValue());
        while (!Thread.currentThread().isInterrupted()) {
            sender.send(driverAccessMap);
            timeMs = timeMs + intervalMs;
            do {
                LockSupport.parkUntil(timeMs);
            } while (timeMs > System.currentTimeMillis());

        }
    }

    public static void main(String args[]) {
        AeronElasticPlugin plugin = new AeronElasticPlugin();

        DriverAccess driverAccess = new DriverAccess("default", "/dev/shm/aeron-denis/");
        Map<String, DriverAccess> map = new HashMap<>();
        map.put("default", driverAccess);

        plugin.init(null, map);
        plugin.run();
    }
}
