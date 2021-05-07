// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.command;

import java.util.Collection;
import java.util.Arrays;
import java.util.Iterator;
import cat.yoink.xanax.client.util.ChatUtil;
import cat.yoink.xanax.client.command.commands.Friend;
import cat.yoink.xanax.client.command.commands.Prefix;
import cat.yoink.xanax.client.command.commands.Help;
import java.util.ArrayList;
import java.util.List;

public enum CommandManager
{
    INSTANCE;
    
    private final List<Command> commands;
    private String prefix;
    
    private CommandManager() {
        this.commands = new ArrayList<Command>();
        this.prefix = ".";
        this.addCommands(new Help(), new Prefix(), new Friend());
    }
    
    public void runCommand(final String message) {
        boolean found = false;
        final String[] split = message.split(" ");
        final String startCommand = split[0];
        final String arguments = message.substring(startCommand.length()).trim();
        for (final Command command : this.getCommands()) {
            for (final String alias : command.getAlias()) {
                if (startCommand.equals(this.getPrefix() + alias)) {
                    command.run(arguments);
                    found = true;
                }
            }
        }
        if (!found) {
            ChatUtil.sendClientMessage("Unknown command");
        }
    }
    
    public void addCommands(final Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }
    
    public Command getCommand(final String name) {
        for (final Command command : this.commands) {
            if (command.getName().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return null;
    }
    
    public List<Command> getCommands() {
        return this.commands;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
}
