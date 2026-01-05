package net.javac.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.javac.config.ConfigData;
import net.javac.config.ConfigData.Command.Text;
import net.javac.config.ConfigLoader;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class CommandManager {
    final String prefix = ConfigLoader.getData().bot.prefix;
    final CommandRegistry registry = new CommandRegistry();
    final UserCooldown cooldown;
    final ConfigData data = ConfigLoader.getData();
    final Text dataText = ConfigLoader.getData().command.text;

    public CommandManager(int pool) {
        cooldown = new UserCooldown(new ScheduledThreadPoolExecutor(pool));
    }

    public CommandRegistry getRegistry() {
        return registry;
    }

    String[] parseCommand(String text) {
        return text.replace(prefix, "").trim().split("\\s+");
    }

    boolean isCommand(MessageReceivedEvent event) {
        final boolean isBot = event.getAuthor().isBot();
        final String msg = event.getMessage().getContentRaw();
        return !isBot && msg.length() < dataText.max_length && msg.length() > dataText.min_length;
    }

    boolean isOwner(String id) {
        return data.bot.owners.stream().anyMatch(o -> o.equals(id));
    }

    boolean isValidPermission(Member member, ICommand command) {
        return member.hasPermission(command.getInformation().permission());
    }

    public void execute(MessageReceivedEvent event) {
        if (!isCommand(event)) return;
        if (cooldown.isRateLimited(event.getAuthor().getIdLong())) return;
        final Message msg = event.getMessage();
        final String msgContent = msg.getContentRaw();
        if (!msgContent.contains(prefix)) return;
        final String[] args = parseCommand(msgContent);
        final String commandName = args[0];
        final var member = event.getMember();
        final ICommand command = registry.getCommand(commandName);
        if (command == null || member == null) return;
        if (command.getInformation().isOwnerCommand() && !isOwner(member.getId())) return;
        if (cooldown.isOnCooldown(event.getAuthor().getIdLong())) {
            event.getMessage().reply(data.command.on_cooldown).queue();
            return;
        }
        if (command.getInformation().permission() != null && !isValidPermission(member, command)) return;
        command.execute(new CommandContext(event, member, msg, registry, event.getGuild(), event.getJDA().getSelfUser()));
        cooldown.addCooldown(event.getAuthor().getIdLong(), 10);
    }
}
