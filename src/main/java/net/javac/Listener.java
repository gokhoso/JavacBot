package net.javac;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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
            var msgBuilder = new EMessageBuilder(messageEvent);
            messageBuffer.append(messageEvent.getMessageId(), msgBuilder.build());
        }

        if (event instanceof MessageDeleteEvent messageDeleteEvent) {
            EMessage msg = messageBuffer.get(messageDeleteEvent.getMessageId());
            System.out.println(msg.content());
        }
    }
}
