package net.javac.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.javac.config.ConfigLoader;
import net.javac.config.ModelConfig.Commands.TextCommands;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class CommandManager {
    final String prefix = ConfigLoader.getData().bot.prefix;
    final CommandRegistry registry = new CommandRegistry();
    final UserCooldown cooldown;
    final TextCommands data = ConfigLoader.getData().commands.text_commands;

    public CommandManager(int pool) {
        cooldown = new UserCooldown(new ScheduledThreadPoolExecutor(pool));
    }

    public CommandRegistry getRegistry() {
        return registry;
    }

    String[] parseCommand(String text) {
        return text.replace(prefix, "").trim().split("\\s+");
    }

    boolean canPass(MessageReceivedEvent event) {
        final boolean isBot = event.getAuthor().isBot();
        final String msg = event.getMessage().getContentRaw();
        return !isBot && msg.length() < data.max_length && msg.length() > data.min_length;
    }

    public void execute(MessageReceivedEvent event) {
        if (!canPass(event)) return;
        if (cooldown.isRateLimited(event.getAuthor().getIdLong())) return;
        if (cooldown.isOnCooldown(event.getAuthor().getIdLong())) {
            event.getMessage().reply("Zaman aşımındasın! biraz bekle.").queue();
            return;
        }
        final Message msg = event.getMessage();
        final String msgContent = msg.getContentRaw();
        if (!msgContent.contains(prefix)) return;
        final String[] args = parseCommand(msgContent);
        final String commandName = args[0];
        final ICommand command = registry.getCommand(commandName);
        command.execute(event);
        cooldown.addCooldown(event.getAuthor().getIdLong(), 10);
    }
}
