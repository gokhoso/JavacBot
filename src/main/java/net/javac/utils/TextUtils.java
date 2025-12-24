package net.javac.utils;


public class TextUtils {
    public static String createChannelMention(String channelId) {
        return String.format("<#%s>", channelId);
    }
    public static String createUserMention(String userId) {
        return String.format("<@%s>", userId);
    }
    public static String setCountVariable(String text, String guildId) {
        if (text.contains("{count}")) {
            return text.replace("{count}", Integer.toString(GuildUtils.getCount(guildId)));
        }
        return text;
    }
    public static String setAllVariables(String text, String guildId, String memberId, String memberName) {
        if (text.contains("{count}")) {
            return text.replace("{count}", Integer.toString(GuildUtils.getCount(guildId)));
        }
        if (text.contains("{member.name}")) {
            return text.replace("{member.name}", memberName);
        }
        if (text.contains("{member.mention}")) {
            return text.replace("{member.mention}", TextUtils.createUserMention(memberId));
        }
        return text;
    }
}
