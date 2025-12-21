package net.javac.log.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.javac.buffer.impl.GuildMessageBuffer;
import net.javac.entities.EMessage;
import net.javac.log.AbstractLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeletedMessageLogger extends AbstractLogger<MessageDeleteEvent> {
    private final Logger log = LoggerFactory.getLogger(DeletedMessageLogger.class);

    private EMessage message;
    private String authorName;
    private Guild guild;
    private String authorAvatarUrl;
    private String authorMention;
    private String channelMention;

    @SuppressWarnings("unused")
    public DeletedMessageLogger(GuildMessageBuffer bufferManager) {
        super(MessageDeleteEvent.class, bufferManager);
    }

    private MessageEmbed embed() {
        return getLoggerEmbedBuilder(guild, authorName, authorAvatarUrl, authorMention)
                .setDescription(":wastebasket: **Bir mesaj silindi**")
                .addField("Kanal:", channelMention, true)
                .addField("Mesaj:", message.content(), false)
                .build();
    }

    private void init(String guildId, String messageId) {
        final var member = getGuildMessageBuffer().getMember(guildId, messageId);

        if (member == null) {
            log.warn("Member is not found in init method");
            return;
        }

        message = getGuildMessageBuffer().get(messageId);
        authorName = member.getUser().getName();
        guild = member.getGuild();
        authorAvatarUrl = member.getEffectiveAvatarUrl();
        authorMention = createUserMention(member.getId());
        channelMention = createChannelMention(message.channelId());
    }

    @Override
    public void log(MessageDeleteEvent event) {
        try {
            init(event.getGuild().getId(), event.getMessageId());
            final var embed = embed();
            final var logChannel = getLogChannel(guild);
            logChannel.sendMessageEmbeds(embed).queue();
        } catch (NullPointerException | IndexOutOfBoundsException npe) {
            log.error(npe.getMessage());
        }
    }
}
