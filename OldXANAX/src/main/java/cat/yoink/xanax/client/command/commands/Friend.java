// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.command.commands;

import java.util.Iterator;
import cat.yoink.xanax.client.util.ChatUtil;
import cat.yoink.xanax.client.friend.FriendManager;
import cat.yoink.xanax.client.command.Command;

public class Friend extends Command
{
    public Friend() {
        super("Friend", new String[] { "friend", "f" }, "friend <add|del|list> <name>");
    }
    
    @Override
    public void run(final String arguments) {
        final String[] split = arguments.split(" ");
        if (split[0].equalsIgnoreCase("add")) {
            if (FriendManager.INSTANCE.isFriend(split[1])) {
                ChatUtil.sendClientMessage("User is already a friend");
            }
            else {
                FriendManager.INSTANCE.addFriend(split[1]);
                ChatUtil.sendClientMessage("Added " + split[1]);
            }
            return;
        }
        if (split[0].equalsIgnoreCase("remove") || split[0].equalsIgnoreCase("del")) {
            if (FriendManager.INSTANCE.isFriend(split[1])) {
                FriendManager.INSTANCE.removeFriend(split[1]);
                ChatUtil.sendClientMessage("Removed " + split[1]);
            }
            else {
                ChatUtil.sendClientMessage("User is not your friend");
            }
            return;
        }
        if (!split[0].equalsIgnoreCase("list")) {
            this.printUsage();
            return;
        }
        if (FriendManager.INSTANCE.getFriends().size() == 0) {
            ChatUtil.sendClientMessage("No friends");
            return;
        }
        for (final String friend : FriendManager.INSTANCE.getFriends()) {
            ChatUtil.sendClientMessage(friend);
        }
    }
}
