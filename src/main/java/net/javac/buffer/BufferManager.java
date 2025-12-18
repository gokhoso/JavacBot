package net.javac.buffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;

public class BufferManager {
    private final Logger logger = LoggerFactory.getLogger(BufferManager.class);

    private final HashSet<AbstractBuffer<?, ?>> buffers = new HashSet<>();

    public void registerBuffer(AbstractBuffer<?, ?> buffer) {
        buffers.add(buffer);
    }

    public void deleteBuffer(AbstractBuffer<?, ?> buffer) {
        buffers.remove(buffer);
    }

    public Optional<? extends AbstractBuffer<?, ?>> getBuffer(String name) {
        return buffers.stream()
                .filter(l -> l.getName().equals(name))
                .map(l -> (AbstractBuffer<?, ?>) l)
                .findFirst();
    }
}
