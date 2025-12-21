package net.javac.log;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.javac.buffer.impl.GuildMessageBuffer;
import net.javac.config.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@SuppressWarnings("unused")
public abstract class AbstractLogger<T> {
    private final Logger logger = LoggerFactory.getLogger(AbstractLogger.class);

    private final Class<T> type;
    private final GuildMessageBuffer guildMessageBuffer;

    protected AbstractLogger(Class<T> type, GuildMessageBuffer bufferManager) {
        this.type = type;
        this.guildMessageBuffer = bufferManager;
    }

    public abstract void log(T event);

    public Class<T> getType() {
        return type;
    }

    public TextChannel getLogChannel(Guild guild) {
        return guild.getTextChannelById(ConfigLoader.getData().channels.log);
    }

    protected EmbedBuilder getLoggerEmbedBuilder(Guild guild, String authorName, String authorAvatarUrl, String authorMention) {
        return new EmbedBuilder()
                .setAuthor(authorName, null, authorAvatarUrl)
                .setThumbnail(authorAvatarUrl)
                .addField("Profil:", authorMention, true)
                .setFooter(guild.getName())
                .setTimestamp(Instant.now());
    }

    protected GuildMessageBuffer getGuildMessageBuffer() {
        return guildMessageBuffer;
    }

    public String limitEmbedCharacter(String content) {
        if (content.length() > 200) {
            return content.substring(0, 200) + "... *(end of the character limit)*";
        }

        return content;
    }

    public String createChannelMention(String channelId) {
        return String.format("<#%s>", channelId);
    }

    public String createUserMention(String userId) {
        return String.format("<@%s>", userId);
    }
}
