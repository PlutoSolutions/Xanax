// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.command.commands;

import cat.yoink.xanax.client.util.ChatUtil;
import cat.yoink.xanax.client.command.CommandManager;
import cat.yoink.xanax.client.command.Command;

public class Help extends Command
{
    public Help() {
        super("Help", new String[] { "help" }, "help");
    }
    
    @Override
    public void run(final String arguments) {
        CommandManager.INSTANCE.getCommands().forEach(command -> ChatUtil.sendClientMessage(String.format("%s - %s%s", command.getName(), CommandManager.INSTANCE.getPrefix(), command.getUsage())));
    }
}
