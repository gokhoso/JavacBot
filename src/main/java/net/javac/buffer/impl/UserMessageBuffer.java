package net.javac.buffer.impl;

import net.JavacPlus.buffer.AbstractBuffer;
import net.JavacPlus.buffer.entities.EMessage;
import net.JavacPlus.buffer.registry.BufferDequeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;


public class UserMessageBuffer extends AbstractBuffer<String, Deque<EMessage>> {
    private final Logger log = LoggerFactory.getLogger(UserMessageBuffer.class);

    public UserMessageBuffer(BufferDequeRegistry<String, Deque<EMessage>> registry) {
        super(registry);
    }

    @Override
    public Deque<EMessage> get(String key) {
        return registry.get(key);
    }

    @Override
    public void append(String key, Deque<EMessage> value) {
        registry.append(key, value);
    }

    @Override
    public String getName() {
        return "UserMessageBuffer";
    }
}
