package net.javac.buffer.impl;

import net.dv8tion.jda.api.entities.Member;
import net.javac.Javac;
import net.javac.buffer.Buffer;
import net.javac.buffer.BufferRegistry;
import net.javac.entities.EMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuildMessageBuffer implements Buffer<String, EMessage> {
    private final Logger logger = LoggerFactory.getLogger(GuildMessageBuffer.class);
    private final BufferRegistry<String, EMessage> registry;

    public GuildMessageBuffer(int max) {
        if (max < 1) {
            throw new IllegalArgumentException("Max parameter must be greater than 0");
        }

        this.registry = new BufferRegistry<>(max);
    }

    @Override
    public EMessage get(String key) {
        return registry.get(key);
    }

    @Override
    public void append(String key, EMessage value) {
        registry.append(key, value);
    }

    public Member getMember(String guildId, String messageId) {
        var guild = Javac.getShardManager().getGuildById(guildId);

        if (guild == null) {
            logger.warn("Guild not found in getMember method");
            return null;
        }

        return guild.getMemberById(get(messageId).authorId());
    }
}

