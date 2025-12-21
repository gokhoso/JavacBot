package net.javac.log;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
public class LogManager {
    private final HashSet<AbstractLogger<?>> loggers = new HashSet<>();

    public void registerLog(AbstractLogger<?> logger) {
        loggers.add(logger);
    }

    public void deleteLog(AbstractLogger<?> logger) {
        loggers.remove(logger);
    }

    public Set<AbstractLogger<?>> getLoggers() {
        return Collections.unmodifiableSet(loggers);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<AbstractLogger<T>> getLogger(Class<T> type) {
        return loggers.stream()
                .filter(l -> l.getType().equals(type))
                .map(l -> (AbstractLogger<T>) l)
                .findFirst();
    }
}
