package net.javac.buffer.impl;

import net.javac.buffer.AbstractBuffer;
import net.javac.buffer.BufferRegistry;
import net.javac.entities.EMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Supplier;


public class UserMessageBuffer extends AbstractBuffer<String, Deque<EMessage>> {
    private final Logger log = LoggerFactory.getLogger(UserMessageBuffer.class);
    private Supplier<? extends Deque<EMessage>> dequeFactory = ConcurrentLinkedDeque::new;

    public UserMessageBuffer(int max) {
        super(max);
    }

    @Override
    public Deque<EMessage> get(String key) {
        return registry.get(key);
    }

    @Override
    public void append(String key, Deque<EMessage> value) {
        Deque<EMessage> dq = get(key);

        if (dq == null) {
            dq = getInstance();
            registry.append(key, dq);
        }

        dq.addAll(value);
    }

    @Override
    public String getName() {
        return "UserMessageBuffer";
    }

    public Deque<EMessage> getInstance() {
        return dequeFactory.get();
    }

    public void setInstance(Supplier<? extends Deque<EMessage>> instance) {
        this.dequeFactory = instance;
    }
}
