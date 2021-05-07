package cat.yoink.xanax.main.util;

import cat.yoink.xanax.main.MinecraftInstance;
import net.minecraft.util.text.TextComponentString;

public final class ChatUtil implements MinecraftInstance
{
    public static void sendPublicMessage(final String message)
    {
        mc.player.sendChatMessage(message);
    }

    public static void sendPrivateMessage(final String message)
    {
        final String s = String.format("&7[&cXANAX&7]&7 %s", message);
        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(s.replace("&", "\u00A7")));
    }
}
