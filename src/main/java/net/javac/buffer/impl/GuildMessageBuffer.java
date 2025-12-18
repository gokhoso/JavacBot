package net.javac.buffer.impl;

import net.JavacPlus.buffer.AbstractBuffer;
import net.JavacPlus.buffer.entities.EMessage;
import net.JavacPlus.buffer.registry.BufferRegistryPolicy;

public class GuildMessageBuffer extends AbstractBuffer<String, EMessage> {
    public GuildMessageBuffer(BufferRegistryPolicy<String, EMessage> registry) {
        super(registry);
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
