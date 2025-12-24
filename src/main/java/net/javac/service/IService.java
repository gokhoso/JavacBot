package net.javac.service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public interface IService {
  Runnable service();
    void run(ScheduledExecutorService executor);
    void stop(boolean mayInterruptIfRunning);
    boolean isServiceLoopStarted();
  int getInitialDelay();
  int getPeriod();
  TimeUnit getTimeType();
}
