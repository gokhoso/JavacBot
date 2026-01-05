package net.javac.command.general;

import net.javac.command.CommandContext;
import net.javac.command.CommandInformation;
import net.javac.command.ICommand;
import net.javac.config.ConfigLoader;
import net.javac.config.ConfigData;

public class Ping implements ICommand {
    final ConfigData data = ConfigLoader.getData();
    @Override
    public CommandInformation getInformation() {
        return new CommandInformation("ping", "Replies with pong", null, false, data.bot.prefix + "ping");
    }
    @Override
    public void execute(CommandContext ctx) {
        ctx.msg().reply("pong").queue();
    }
}
