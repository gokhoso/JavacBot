package net.javac.systems;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.javac.config.ConfigLoader;
import net.javac.utils.GuildUtils;
import net.javac.utils.TextUtils;
import net.javac.utils.TextVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;

public class WelcomeMessageSender {
    static final Logger log = LoggerFactory.getLogger(WelcomeMessageSender.class);
    private final TextVariables textVariables = new TextVariables();

    MessageEmbed embed(String name, String avatarUrl, String image, Color color, String title, String description, String footer) {
        final var embedBuilder = new EmbedBuilder()
                .setAuthor(name, null, avatarUrl)
                .setColor(color)
                .setTitle(title)
                .setImage(image)
                .setDescription(description)
                .setFooter(footer);

        if (avatarUrl != null) embedBuilder.setThumbnail(avatarUrl);

        final var fields = ConfigLoader.getData().systems.welcomeMessage.fields;
        if (fields.enabled == 1) addFields(embedBuilder, fields.entries);
        if (fields.suggested_channels == 1) addSuggestedChannels(embedBuilder, fields.suggested_channels_title, fields.suggested_channel_list);

        return embedBuilder.build();
    }

    void addFields(EmbedBuilder embedBuilder, List<String> entries) {
        for (int i = 0; i < entries.size(); i = i+2) {
            embedBuilder.addField(textVariables.apply(entries.get(i)), textVariables.apply(entries.get(i+1)), true);
        }
    }

    void addSuggestedChannels(EmbedBuilder embedBuilder, String suggestedChannelTitle, List<String> suggestedChannels){
        String finalChannels = "";
        for (String suggestedChannel : suggestedChannels) {
            finalChannels = finalChannels.concat(" " + TextUtils.createChannelMention(suggestedChannel));
        }
        embedBuilder.addField(suggestedChannelTitle, finalChannels, false);
    }

    public void send(GuildMemberJoinEvent e) {
        final String guildId = e.getGuild().getId();
        final String memberId = e.getMember().getId();
        final String name = e.getMember().getUser().getName();
        final String avatarUrl = e.getMember().getEffectiveAvatarUrl();
        final String image = ConfigLoader.getData().systems.welcomeMessage.banner;
        final Color color = Color.CYAN;
        textVariables.member(memberId, name).guild(guildId);

        final var wm = ConfigLoader.getData().systems.welcomeMessage;
        var title = textVariables.apply(wm.title);
        var description = textVariables.apply(wm.description);
        var footer = textVariables.apply(wm.footer);
        var embed = embed(name, avatarUrl, image, color, title, description, footer);

        var channel = GuildUtils.getTextChannel(guildId, ConfigLoader.getData().guild.channels.general);
        if (channel == null) {
            log.error("General channel is null [send method]");
            return;
        }
        channel.sendMessageEmbeds(embed).queue();
    }
}
