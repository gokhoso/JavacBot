package net.javac.utils;

import net.javac.Javac;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TextVariables {
    private final Map<String, Supplier<String>> map = new HashMap<>();

    public TextVariables() {
        map.put("{bot.name}", () -> Javac.getShardManager().getShards().getFirst().getSelfUser().getName());
    }

    @NonNull
    public TextVariables guild(String guildId) {
        map.put("{count}", () -> Integer.toString(GuildUtils.getCount(guildId)));
        map.put("{count.ordinal_suffix}", () -> TextUtils.countOrdinalSuffix(GuildUtils.getCount(guildId)));
        map.put("{guild.name}", () -> GuildUtils.getGuild(guildId).getName());
        map.put("{guild.iconUrl}", () -> GuildUtils.getGuild(guildId).getIconUrl());
        return this;
    }

    @NonNull
    public TextVariables member(String memberId, String memberName) {
        map.put("{member.name}", () -> memberName);
        map.put("{member.mention}", () -> TextUtils.createUserMention(memberId));
        return this;
    }

    @SuppressWarnings("unused")
    public boolean isEmpty() {
        return map.isEmpty();
    }

    public String apply(String text) {
        var changedText = text;
        for (var entry : map.entrySet()) {
            var key = entry.getKey();
            var val = entry.getValue().get();
            if (text.contains(key)) {
                changedText = changedText.replace(key, val);
            }
        }
        return changedText;
    }
}
