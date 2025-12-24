package net.javac.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.javac.Javac;

public class GuildUtils {
    public static Guild getGuild(String id) {
        return Javac.getShardManager().getGuildById(id);
    }

    public static int getCount(String guildId) {
        var guild = getGuild(guildId);
        if (guild == null) return 0;
        return guild.getMemberCount();
    }

    public static TextChannel getTextChannel(String guildId, String id) {
        var guild = Javac.getShardManager().getGuildById(guildId);
        if (guild == null) return null;
        return guild.getTextChannelById(id);
    }
}
