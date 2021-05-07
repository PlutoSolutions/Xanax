// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.command.commands;

import cat.yoink.xanax.client.util.ChatUtil;
import cat.yoink.xanax.client.command.CommandManager;
import cat.yoink.xanax.client.command.Command;

public class Prefix extends Command
{
    public Prefix() {
        super("Prefix", new String[] { "prefix" }, "prefix <char>");
    }
    
    @Override
    public void run(final String arguments) {
        if (!this.isArgumentsEmpty(arguments)) {
            CommandManager.INSTANCE.setPrefix(arguments);
            ChatUtil.sendClientMessage("Changed prefix to " + arguments);
            return;
        }
        this.printUsage();
    }
}
