package net.javac.systems;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.javac.config.ConfigLoader;
import net.javac.utils.GuildUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.javac.utils.TextUtils.setAllVariables;

public class BumpSystem {
    final Logger logger = LoggerFactory.getLogger(BumpSystem.class);
    private final ScheduledExecutorService scheduledExecutorService;

    public BumpSystem(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    MessageEmbed onBumpEmbed(String guildId, String memberName, String memberId, String guildPhotoUrl) {
        var ob = ConfigLoader.getData().systems.bump.on_bump;
        var embed = new EmbedBuilder();
        // Set embed values
        embed.setAuthor(memberName, null, guildPhotoUrl);
        embed.setColor(Color.magenta);
        embed.setTitle(setAllVariables(ob.title, guildId, memberId, memberName));
        embed.setDescription(setAllVariables(ob.description, guildId, memberId, memberName));
        embed.setFooter(ob.footer);
        if (ob.guild_thumbnail == 1) {
            embed.setThumbnail(guildPhotoUrl);
        }
        return embed.build();
    }

    MessageEmbed bumpReminderEmbed(String guildId, String memberName, String memberId, String iconUrl) {
        var rb = ConfigLoader.getData().systems.bump.reminder_bump;
        var embed = new EmbedBuilder();
        // Set embed values
        embed.setAuthor(memberName, null, iconUrl);
        embed.setColor(Color.magenta);
        embed.setTitle(setAllVariables(rb.title, guildId, memberId, memberName));
        embed.setDescription(setAllVariables(rb.description, guildId, memberId, memberName));
        embed.setFooter(rb.footer);
        if (rb.guild_thumbnail == 1) {
            embed.setThumbnail(iconUrl);
        }
        return embed.build();
    }

    public void send(MessageReceivedEvent e) {
        if (!e.getMessageId().equals("302050872383242240")) {
            return;
        }
        if (e.getMessage().getEmbeds().size() != 1) {
            return;
        }
        var desc = e.getMessage().getEmbeds().getFirst().getDescription();
        if (desc != null && !desc.contains("Öne çıkarma başarılı!")) {
            return;
        }
        final var guild = e.getGuild();
        final String guildId = guild.getId();
        final var member = e.getMember();
        if (member == null) {
            logger.warn("Member is null can not send Bump message");
            return;
        }
        final String memberId = member.getId();
        final String memberName = member.getUser().getName();
        final String iconUrl = guild.getIconUrl();
        final var embed = onBumpEmbed(guildId, memberName, memberId, iconUrl);
        if (embed == null) {
            logger.error("Embed is null, send failed.");
            return;
        }
        final var channel = GuildUtils.getTextChannel(guildId, ConfigLoader.getData().guild.channels.general);
        if (channel == null) {
            logger.error("General channel is null [send method]");
            return;
        }
        e.getMessage().getChannel().asTextChannel().sendMessageEmbeds(embed).queue();
        scheduledExecutorService.schedule(() -> channel.sendMessageEmbeds(bumpReminderEmbed(guildId, memberName, memberId, iconUrl)).queue(), 2, TimeUnit.HOURS);
    }
}
