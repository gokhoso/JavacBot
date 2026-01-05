package net.javac.command;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandRegistry {
    Map<String, ICommand> commands = new ConcurrentHashMap<>();

    public void addCommand(ICommand command) {
        commands.putIfAbsent(command.getInformation().name(), command);
    }

    @SuppressWarnings("unused")
    public void removeCommand(String name) {
        commands.remove(name);
    }

    public ICommand getCommand(String name) {
        return commands.get(name);
    }

    public List<ICommand> getCommands() {
        return commands.values().stream().toList();
    }
}
