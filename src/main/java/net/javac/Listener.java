package net.javac;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.javac.buffer.impl.GuildMessageBuffer;
import net.javac.command.CommandManager;
import net.javac.log.LogManager;
import net.javac.utils.EMessageBuilder;
import net.javac.utils.WelcomeMessageSender;
import org.jetbrains.annotations.NotNull;

public class Listener extends ListenerAdapter {
    final CommandManager commandManager;
    final LogManager logManager;
    final GuildMessageBuffer guildMessageBuffer;
    final WelcomeMessageSender welcomeMessageSender = new WelcomeMessageSender();

    public Listener(CommandManager commandManager, LogManager logManager, GuildMessageBuffer guildMessageBuffer) {
        this.commandManager = commandManager;
        this.logManager = logManager;
        this.guildMessageBuffer = guildMessageBuffer;
    }

    @Override
    public void onGenericEvent(@NotNull GenericEvent event) {
        if (event instanceof MessageReceivedEvent messageEvent) {
            guildMessageBuffer.append(messageEvent.getMessageId(), new EMessageBuilder(messageEvent).build());
            commandManager.execute(messageEvent);
        }

        if (event instanceof MessageDeleteEvent messageDeleteEvent) {
            logManager.getLogger(MessageDeleteEvent.class).ifPresent(logger -> logger.log(messageDeleteEvent));
        }

        if (event instanceof MessageUpdateEvent messageUpdateEvent) {
            logManager.getLogger(MessageUpdateEvent.class).ifPresent(logger -> logger.log(messageUpdateEvent));
        }

        if (event instanceof GuildMemberJoinEvent guildMemberJoinEvent) {
            welcomeMessageSender.send(guildMemberJoinEvent);
        }
    }
}
