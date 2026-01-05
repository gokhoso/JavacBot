package net.javac.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public record CommandContext(
        MessageReceivedEvent event,
        Member author,
        Message msg,
        CommandRegistry commandRegistry,
        Guild guild,
        SelfUser selfUser
) {}
