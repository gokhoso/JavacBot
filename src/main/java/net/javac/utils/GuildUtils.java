package net.javac.utils;

import net.javac.Javac;

public class GuildUtils {
    public static int getCount(String guildId) {
        var guild = Javac.getShardManager().getGuildById(guildId);
        if (guild == null) return 0;
        return guild.getMemberCount();
    }
}
