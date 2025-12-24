package net.javac.command.general;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.javac.command.CommandInformation;
import net.javac.command.ICommand;
import net.javac.config.ConfigLoader;
import net.javac.config.ModelConfig;

public class Ping implements ICommand {
    final ModelConfig data = ConfigLoader.getData();
    @Override
    public CommandInformation getInformation() {
        return new CommandInformation("ping", "Replies with pong", data.bot.prefix + "ping");
    }
    @Override
    public void execute(MessageReceivedEvent event) {
        event.getMessage().reply("pong").queue();
    }
}
