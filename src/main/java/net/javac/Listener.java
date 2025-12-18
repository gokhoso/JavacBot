package net.javac;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.javac.buffer.impl.GuildMessageBuffer;
import net.javac.entities.EMessage;
import net.javac.utils.EMessageBuilder;
import org.jetbrains.annotations.NotNull;

public class Listener extends ListenerAdapter {
    final GuildMessageBuffer messageBuffer;

    public Listener(GuildMessageBuffer messageBuffer) {
        this.messageBuffer = messageBuffer;
    }

    @Override
    public void onGenericEvent(@NotNull GenericEvent event) {
        if (event instanceof MessageReceivedEvent messageEvent) {
            var msg = new EMessageBuilder(messageEvent).build();
            messageBuffer.append(messageEvent.getMessageId(), msg);
        }

        if (event instanceof MessageDeleteEvent messageDeleteEvent) {
            EMessage msg = messageBuffer.get(messageDeleteEvent.getMessageId());
            System.out.println(msg.content());
        }

        if (event instanceof MessageUpdateEvent messageUpdateEvent) {
            var oldMessage = messageBuffer.get(messageUpdateEvent.getMessageId());
            var newMessage = messageUpdateEvent.getMessage();
            System.out.printf("Old Message: %s%n New Message: %s%n", oldMessage, newMessage);
        }
    }
}
