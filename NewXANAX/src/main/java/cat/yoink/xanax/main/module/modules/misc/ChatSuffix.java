package cat.yoink.xanax.main.module.modules.misc;

import cat.yoink.xanax.main.event.events.PacketEvent;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.setting.BooleanSetting;
import cat.yoink.xanax.main.setting.EnumSetting;
import net.minecraft.network.play.client.CPacketChatMessage;
import cat.yoink.eventmanager.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ChatSuffix extends Module
{
    private final EnumSetting mode = addSetting(new EnumSetting("Mode", "Smooth", "Smooth", "Small", "Normal"));
    private final BooleanSetting blue = addSetting(new BooleanSetting("Blue", false));

    public ChatSuffix()
    {
        super("ChatSuffix", Category.MISC);
    }

    @Listener
    public void onPacket(final PacketEvent event)
    {
        if (!event.getType().equals(PacketEvent.Type.INCOMING) && event.getPacket() instanceof CPacketChatMessage)
        {
            final List<String> prefixes = new ArrayList<>(Arrays.asList("/", ".", "-", ",", ":", ";", "'", "\"", "+", "\\"));
            for (final String prefix : prefixes)
                if (((CPacketChatMessage) event.getPacket()).getMessage().startsWith(prefix)) return;

            ((CPacketChatMessage) event.getPacket()).message += getSuffix();
        }
    }

    private String getSuffix()
    {
        final StringBuilder message = new StringBuilder();

        message.append(" ");
        if (this.blue.getValue()) message.append("`");
        message.append("\u23D0 ");

        switch (this.mode.getValue().toLowerCase())
        {
            case "small":
                message.append("x\u1D00\u0274\u1D00x");
                break;
            case "smooth":
                message.append("\uFF58\uFF41\uFF4E\uFF41\uFF58");
                break;
            case "normal":
                message.append("XANAX");
                break;
            default:
                break;
        }

        return message.toString();
    }
}
