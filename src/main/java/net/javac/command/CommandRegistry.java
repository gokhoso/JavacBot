package net.javac.command;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandRegistry {
    Map<String, ICommand> commands = new ConcurrentHashMap<>();

    public void addCommand(String name, ICommand command) {
        commands.putIfAbsent(name, command);
    }

    @SuppressWarnings("unused")
    public void removeCommand(String name) {
        commands.remove(name);
    }

    public ICommand getCommand(String name) {
        return commands.get(name);
    }
}
