package net.javac.buffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class BufferManager {
    private final Logger logger = LoggerFactory.getLogger(BufferManager.class);
    private final HashMap<String, AbstractBuffer<?, ?>> buffers = new HashMap<>();

    public void registerBuffer(String bufferId, AbstractBuffer<?, ?> buffer) {
        buffers.put(bufferId, buffer);
    }

    public void deleteBuffer(String bufferId) {
        buffers.remove(bufferId);
    }

    public AbstractBuffer<?, ?> getBuffer(String bufferId) {
        return buffers.get(bufferId);
    }
}
