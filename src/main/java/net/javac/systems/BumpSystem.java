package net.javac.systems;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.javac.config.ConfigLoader;
import net.javac.utils.GuildUtils;
import net.javac.utils.TextVariables;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BumpSystem {
    private final ScheduledExecutorService scheduledExecutorService;
    private final TextVariables textVariables = new TextVariables();

    public BumpSystem(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    MessageEmbed onBumpEmbed(String name, String avatarUrl, String image, String title, String description, Color color, String iconUrl, String footer) {
        return new EmbedBuilder()
        .setTitle(title)
        .setAuthor(name, null, avatarUrl)
        .setDescription(description)
        .setColor(color)
        .setImage(image)
        .setFooter(footer)
        .setThumbnail(iconUrl)
        .build();
    }

    MessageEmbed bumpReminderEmbed(String title, String desc, Color color, String iconUrl, String image, String footer) {
        return new EmbedBuilder()
        .setTitle(title)
        .setDescription(desc)
        .setColor(color)
        .setImage(image)
        .setFooter(footer)
        .setThumbnail(iconUrl)
        .build();
    }

    public void send(MessageReceivedEvent event) {
        if (event.getMessage().getEmbeds().size() != 1) return;
        final String desc = event.getMessage().getEmbeds().getFirst().getDescription();
        if (desc == null || !desc.contains("Öne çıkarma başarılı!")) return;

        final var guild = event.getGuild();
        final String guildId = guild.getId();
        final var member = event.getMember();
        if (member == null) return;

        final var interactionMember = Objects.requireNonNull(event.getMessage().getInteractionMetadata()).getUser();
        final String userId = interactionMember.getId();
        final String userName = interactionMember.getName();
        final String userAvatarUrl = interactionMember.getEffectiveAvatarUrl();

        textVariables.member(userId, userName);

        var ob = ConfigLoader.getData().systems.bump.on_bump;
        var ob_title = textVariables.apply(ob.title);
        var ob_desc = textVariables.apply(ob.description);
        var ob_footer = textVariables.apply(ob.footer);
        var ob_color = Color.getColor(ob.color);
        var ob_img = ob.image;
        final String ob_iconUrl = ob.guild_thumbnail == 1 ? guild.getIconUrl() : null;
        final var ob_embed = onBumpEmbed(userName, userAvatarUrl, ob_img, ob_title, ob_desc, ob_color, ob_iconUrl, ob_footer);

        event.getMessage().getChannel().asTextChannel().sendMessageEmbeds(ob_embed).queue();

        var rb = ConfigLoader.getData().systems.bump.reminder_bump;
        var rb_title = textVariables.apply(rb.title);
        var rb_desc = textVariables.apply(rb.description);
        var rb_footer = textVariables.apply(rb.footer);
        var rb_color = Color.getColor(rb.color);
        var rb_img = rb.image;
        var rb_thumbnail = rb.guild_thumbnail == 1 ? guild.getIconUrl() : null;

        final var bumpedChannel = Objects.requireNonNull(GuildUtils.getTextChannel(guildId, ConfigLoader.getData().guild.channels.general));
        scheduledExecutorService.schedule(() -> bumpedChannel.sendMessageEmbeds(bumpReminderEmbed(rb_title, rb_desc, rb_color, rb_thumbnail, rb_img, rb_footer)).queue(), 2, TimeUnit.HOURS);
    }
}
