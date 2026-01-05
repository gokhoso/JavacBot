package net.javac.command;

import net.dv8tion.jda.api.Permission;

public record CommandInformation(String name, String description, Permission permission, Boolean isOwnerCommand, String usage) {}
