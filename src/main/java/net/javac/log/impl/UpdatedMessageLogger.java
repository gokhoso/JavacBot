package net.javac.log.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.javac.buffer.impl.GuildMessageBuffer;
import net.javac.entities.EMessage;
import net.javac.log.AbstractLogger;
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
        return getLoggerEmbedBuilder(guild, authorName, authorAvatarUrl, authorMention).setDescription(":arrows_counterclockwise: **Bir mesaj güncellendi**").addField("Kanal:", channelMention, false).addField("Eski mesaj:", oldMessage.content(), false).addField("Yeni mesaj:", newMessage.getContentRaw(), false).build();
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

    @Override
    public void log(MessageUpdateEvent event) {
        init(event.getMessage());
        final var embed = embed();
        final var logChannel = getLogChannel(guild);
        logChannel.sendMessageEmbeds(embed).queue();
    }
}
