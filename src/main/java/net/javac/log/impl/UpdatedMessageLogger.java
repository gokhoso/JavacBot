package net.javac.log.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.javac.buffer.impl.GuildMessageBuffer;
import net.javac.entities.EMessage;
import net.javac.log.AbstractLogger;
import net.javac.utils.EMessageBuilder;
import net.javac.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdatedMessageLogger extends AbstractLogger<MessageUpdateEvent> {
    private final Logger log = LoggerFactory.getLogger(UpdatedMessageLogger.class);

    private EMessage oldMessage;
    private Message newMessage;
    private String authorName;
    private Guild guild;
    private String authorAvatarUrl;
    private String authorMention;
    private String channelMention;

    public UpdatedMessageLogger(GuildMessageBuffer bufferManager) {
        super(MessageUpdateEvent.class, bufferManager);
    }

    private MessageEmbed embed() {
        var embed = getLoggerEmbedBuilder(guild, authorName, authorAvatarUrl, authorMention);
        embed.setDescription(":arrows_counterclockwise: **Bir mesaj güncellendi**");
        embed.addField("Kanal:", channelMention, false);
        embed.addField("Eski mesaj:", oldMessage.content(), false);
        embed.addField("Yeni mesaj:", newMessage.getContentRaw(), false);
        return embed.build();
    }

    private void init(Message message) {
        oldMessage = getGuildMessageBuffer().get(message.getId());

        if (oldMessage == null) {
            log.error("OldMessage is null in init!");
            return;
        }

        newMessage = message;
        authorName = message.getAuthor().getName();
        guild = message.getGuild();

        if (message.getMember() != null) {
            authorAvatarUrl = message.getMember().getEffectiveAvatarUrl();
            authorMention = TextUtils.createUserMention(message.getMember().getId());
        }

        channelMention = TextUtils.createChannelMention(message.getChannelId());
    }

    private boolean isEdited() {
        if (oldMessage.content().equals(newMessage.getContentRaw())) return false;
        return !oldMessage.content().isBlank() || !newMessage.getContentRaw().isBlank();
    }

    private void createMessage(MessageUpdateEvent e) {
        var eMessage = new EMessageBuilder(e).build();
        getGuildMessageBuffer().append(e.getMessageId(), eMessage);
    }

    @Override
    public void log(MessageUpdateEvent event) {
        init(event.getMessage());
        if (!isEdited()) return;
        createMessage(event);
        final var embed = embed();
        final var logChannel = getLogChannel(guild);
        logChannel.sendMessageEmbeds(embed).queue();
    }
}
