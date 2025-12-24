package net.javac.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@SuppressWarnings("unused")
public interface ICommand {
    CommandInformation getInformation();
    void execute(MessageReceivedEvent event);
}
