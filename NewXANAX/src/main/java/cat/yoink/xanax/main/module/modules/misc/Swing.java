package cat.yoink.xanax.main.module.modules.misc;

import cat.yoink.xanax.main.event.events.PacketEvent;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.setting.EnumSetting;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import cat.yoink.eventmanager.Listener;

public final class Swing extends Module
{
    private final EnumSetting mode = addSetting(new EnumSetting("Hand", "None", "MainHand", "Offhand", "None"));

    public Swing()
    {
        super("Swing", Category.MISC);
    }

    @Listener
    public void onPacket(final PacketEvent event)
    {
        if (event.getType().equals(PacketEvent.Type.INCOMING) || !(event.getPacket() instanceof CPacketAnimation))
            return;

        if (this.mode.getValue().equalsIgnoreCase("None")) event.setCancelled(true);

        ((CPacketAnimation) event.getPacket()).hand = this.mode.is("MainHand") ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
    }
}
