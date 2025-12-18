package net.javac.entities;

public record EMessage(String content, String authorId, String channelId, String messageId, String guildId) {}

