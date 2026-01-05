package net.javac.command;

@SuppressWarnings("unused")
public interface ICommand {
    CommandInformation getInformation();
    void execute(CommandContext ctx);
}
