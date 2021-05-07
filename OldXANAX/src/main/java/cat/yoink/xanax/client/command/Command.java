// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.command;

import cat.yoink.xanax.client.util.ChatUtil;
import java.util.Objects;

public class Command
{
    private final String name;
    private final String[] alias;
    private final String usage;
    
    public Command(final String name, final String[] alias, final String usage) {
        this.name = name;
        this.alias = alias;
        this.usage = usage;
    }
    
    public void run(final String arguments) {
    }
    
    public boolean isArgumentsEmpty(final String arguments) {
        return Objects.isNull(arguments) || arguments.equals("");
    }
    
    public void printUsage() {
        ChatUtil.sendClientMessage("Usage: " + this.usage);
    }
    
    public String getName() {
        return this.name;
    }
    
    public String[] getAlias() {
        return this.alias;
    }
    
    public String getUsage() {
        return this.usage;
    }
}
