package net.javac.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ServiceManager {
    private final Logger logger = LoggerFactory.getLogger(ServiceManager.class);
    private final ScheduledExecutorService scheduledExecutorService;
    private final ServiceRegistry serviceRegistry = new ServiceRegistry();

    public ServiceManager(int poolSize) {
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(poolSize);
    }

    public void addService(String name, IService service) {
        serviceRegistry.addCache(name, service);
    }

    public IService getService(String name) {
        final IService service = serviceRegistry.getCache(name);
        if (service == null) {
            logger.error("Service failed, service null: {}", name);
        }
        return service;
    }

    public void start(String name) {
        final IService service = getService(name);
        if (service == null) return;
        service.run(scheduledExecutorService);
    }

    @SuppressWarnings("unused")
    public void stop(String name, boolean mayInterruptIfRunning) {
        final IService service = getService(name);
        if (service == null) return;
        if (!service.isServiceLoopStarted()) {
            logger.warn("Service loop is not started, cancel operation failed.");
            return;
        }
        service.stop(mayInterruptIfRunning);
    }
}
