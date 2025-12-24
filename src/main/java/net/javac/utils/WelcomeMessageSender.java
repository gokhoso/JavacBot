package net.javac.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.javac.config.ConfigLoader;
import net.javac.config.ModelConfig.Guild.WelcomeMessage.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import static net.javac.utils.TextUtils.setAllVariables;

public class WelcomeMessageSender {
    static final Logger log = LoggerFactory.getLogger(WelcomeMessageSender.class);

    MessageEmbed embed(String guildId, String memberName, String memberId, String avatarUrl) {
        var wm = ConfigLoader.getData().guild.welcomeMessage;
        var embed = new EmbedBuilder();
        // Set embed values
        embed.setAuthor(memberName, null, avatarUrl);
        embed.setColor(Color.magenta);
        embed.setTitle(setAllVariables(wm.title, guildId, memberId, memberName));
        embed.setThumbnail(avatarUrl);
        embed.setDescription(setAllVariables(wm.description, guildId, memberId, memberName));
        embed.setFooter(wm.footer);
        // Set embed fields
        embed = setFields(embed, wm.fields, guildId, memberId, memberName);
        if (embed == null) {
            return null;
        }
        return embed.build();
    }

    public void send(GuildMemberJoinEvent e) {
        final String guildId = e.getGuild().getId();
        final String memberId = e.getMember().getId();
        final String memberName = e.getMember().getUser().getName();
        final String avatarUrl = e.getMember().getEffectiveAvatarUrl();

        var embed = embed(guildId, memberName, memberId, avatarUrl);

        if (embed == null) {
            log.error("Embed is null, send failed.");
            return;
        }

        var channel = GuildUtils.getTextChannel(guildId, ConfigLoader.getData().channels.general);

        if (channel == null) {
            log.error("General channel is null [send method]");
            return;
        }

        channel.sendMessageEmbeds(embed).queue();
    }

    private EmbedBuilder setFields(EmbedBuilder embed, Fields fields, String guildId, String memberId, String memberName) {
        // Fields disabled
        if (fields.enabled == 0) {
            return embed;
        }
        // Check for valid values
        if (fields.number_of_fields < 1) {
            log.warn("Number of fields must be greater than 0");
            return null;
        }
        if (fields.entries.size() < 2) {
            log.warn("Entries must be greater than 1 (2 entries = 1 field)");
            return null;
        }
        if (fields.entries.size() % 2 != 0) {
            log.warn("Entries must pair (2 entries = 1 field)");
            return null;
        }
        if (fields.entries.size() > 10) {
            log.warn("Field size must be lower than 10");
            return null;
        }
        // Add fields
        for (int i = 0; i < fields.entries.size(); i = i + 2) {
            var title = setAllVariables(fields.entries.get(i), guildId, memberId, memberName);
            var value = setAllVariables(fields.entries.get(i + 1), guildId, memberId, memberName);

            embed.addField(title, value, true);
        }
        // Check if it is empty
        if (fields.suggested_channel_list.isEmpty()) {
            log.warn("Suggested Channel List is empty!");
            return null;
        }
        // Add suggested Channels to list
        String suggestedChannels = " ";
        for (int i = 0; i < fields.suggested_channel_list.size(); i++) {
            suggestedChannels = suggestedChannels.concat(" " + TextUtils.createChannelMention(fields.suggested_channel_list.get(i)));
        }
        // Add suggested channels to field
        embed.addField(fields.suggested_channels_title, suggestedChannels, false);
        return embed;
    }
}
