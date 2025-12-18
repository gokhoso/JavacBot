package net.javac.buffer.impl;

import net.javac.Javac;
import net.javac.buffer.AbstractBuffer;
import net.javac.buffer.BufferRegistry;
import net.javac.entities.EMessage;

public class GuildMessageBuffer extends AbstractBuffer<String, EMessage> {
    public GuildMessageBuffer(int max) {
        super(max);
    }

    @Override
    public EMessage get(String key) {
        return registry.get(key);
    }

    @Override
    public void append(String key, EMessage value) {
        registry.append(key, value);
    }

    @Override
    public String getName() {
        return "GuildMessageBuffer";
    }
}
