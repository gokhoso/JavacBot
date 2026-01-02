package net.javac.utils;


public class TextUtils {
    public static String createChannelMention(String channelId) {
        return String.format("<#%s>", channelId);
    }

    public static String createUserMention(String userId) {
        return String.format("<@%s>", userId);
    }

    public static String countOrdinalSuffix(int count) {
        final int mod100 = count % 100;
        if (mod100 >= 11 && mod100 <= 13) {
            return count + "th";
        }
        final int mod10 = count % 10;
        return switch (mod10) {
            case 1 -> count + "st";
            case 2 -> count + "nd";
            case 3 -> count + "rd";
            default -> count + "th";
        };
    }
}
