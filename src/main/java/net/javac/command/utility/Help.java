package net.javac.command.utility;

import net.dv8tion.jda.api.EmbedBuilder;
import net.javac.command.CommandContext;
import net.javac.command.CommandInformation;
import net.javac.command.CommandRegistry;
import net.javac.command.ICommand;
import net.javac.config.ConfigData;
import net.javac.config.ConfigLoader;
import net.javac.utils.TextVariables;

import java.awt.*;

public class Help implements ICommand {
    private final ConfigData data = ConfigLoader.getData();
    @Override
    public CommandInformation getInformation() {
        return new CommandInformation("help", "Displays available commands.", null, false, ConfigLoader.getData().bot.prefix + "help");
    }

    private String getCommands(CommandRegistry registry) {
        return String.join("\n",
                registry
                        .getCommands()
                        .stream()
                        .map(
                                c -> c.getInformation().usage()+ " " +
                                        "*" + c.getInformation().description() + "*")
                        .toList()
        );
    }

    @Override
    public void execute(CommandContext ctx) {
    final var textVariables = new TextVariables();
    final var id = ctx.author().getId();
    final var name = ctx.author().getUser().getName();
    final var help_command = data.command.help_command;
    textVariables.member(id, name);
    textVariables.guild(ctx.guild().getId());
    textVariables.custom("{commands}", () -> getCommands(ctx.commandRegistry()));

    ctx.msg().replyEmbeds(new EmbedBuilder()
                    .setTitle(textVariables.apply(help_command.title))
                    .setDescription(textVariables.apply(help_command.description))
                    .setThumbnail(help_command.bot_thumbnail == 1 ? ctx.selfUser().getAvatarUrl() : null)
                    .setColor(Color.getColor(help_command.color))
                    .setFooter(textVariables.apply(help_command.footer))
            .build()).queue();
    }
}
