package net.javac.utils;

import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.javac.entities.EMessage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class EMessageBuilder {
    final String content;
    final String messageId;
    final String authorId;
    final String channelId;
    final String guildId;

    public EMessageBuilder(MessageReceivedEvent e) {
        content = e.getMessage().getContentRaw();
        authorId = Objects.requireNonNull(e.getMember()).getId();
        channelId = e.getChannel().getId();
        messageId = e.getMessageId();
        guildId = e.getGuild().getId();
    }

    public EMessageBuilder(MessageUpdateEvent e) {
        content = e.getMessage().getContentRaw();
        authorId = Objects.requireNonNull(e.getMember()).getId();
        channelId = e.getChannel().getId();
        messageId = e.getMessageId();
        guildId = e.getGuild().getId();
    }

    public EMessage build() {
        return new EMessage(content, authorId, channelId, messageId, guildId);
    }
}
