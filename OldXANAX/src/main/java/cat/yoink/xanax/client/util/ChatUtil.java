// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.util;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import cat.yoink.xanax.client.MinecraftInstance;

public class ChatUtil implements MinecraftInstance
{
    public static void sendClientMessage(final String message) {
        sendClientMessage(message, true);
    }
    
    public static void sendClientMessage(final String message, final boolean watermark) {
        final StringBuilder messageBuilder = new StringBuilder();
        if (watermark) {
            messageBuilder.append("&7[&cXANAX&7]").append("&7 ");
        }
        messageBuilder.append(message);
        ChatUtil.mc.ingameGUI.getChatGUI().printChatMessage((ITextComponent)new TextComponentString(messageBuilder.toString().replace("&", "§")));
    }
    
    public static void sendPublicMessage(final String message) {
        sendPublicMessage(message, true);
    }
    
    public static void sendPublicMessage(final String message, final boolean packet) {
        if (packet) {
            ChatUtil.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(message));
        }
        else {
            ChatUtil.mc.player.sendChatMessage(message);
        }
    }
}
