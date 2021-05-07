// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraftforge.client.event.ClientChatEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.EnumSetting;
import cat.yoink.xanax.client.module.Module;

public class ChatSuffix extends Module
{
    private final EnumSetting mode;
    private final BooleanSetting blue;
    
    public ChatSuffix() {
        super("ChatSuffix", Category.MISC);
        this.mode = this.newSetting(new EnumSetting("Mode", "Smooth", new String[] { "Smooth", "Small", "Normal" }));
        this.blue = this.newSetting(new BooleanSetting("Blue", false));
    }
    
    @SubscribeEvent
    public void onClientChat(final ClientChatEvent event) {
        if (this.nullCheck()) {
            return;
        }
        final List<String> prefixes = new ArrayList<String>(Arrays.asList("/", ".", "-", ",", ":", ";", "'", "\"", "+", "\\"));
        for (final String prefix : prefixes) {
            if (event.getMessage().startsWith(prefix)) {
                return;
            }
        }
        final StringBuilder message = new StringBuilder(event.getMessage());
        message.append(" ");
        if (this.blue.getValue()) {
            message.append("`");
        }
        message.append("\u23d0 ");
        final String lowerCase = this.mode.getValue().toLowerCase();
        switch (lowerCase) {
            case "small": {
                message.append("x\u1d00\u0274\u1d00x");
                break;
            }
            case "smooth": {
                message.append("\uff58\uff41\uff4e\uff41\uff58");
                break;
            }
            case "normal": {
                message.append("XANAX");
                break;
            }
        }
        event.setCanceled(true);
        ChatSuffix.mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
        ChatSuffix.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(message.toString()));
    }
}
