package cat.yoink.xanax.main.module.modules.world;

import cat.yoink.xanax.main.event.events.PacketEvent;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import cat.yoink.eventmanager.Listener;

public final class BuildHeight extends Module
{
    public BuildHeight()
    {
        super("BuildHeight", Category.WORLD);
    }

    @Listener
    public void packetSend(final PacketEvent event)
    {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && ((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getPos().getY() >= 255 && ((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getDirection().equals(EnumFacing.UP))
        {
            ((CPacketPlayerTryUseItemOnBlock) event.getPacket()).placedBlockDirection = EnumFacing.DOWN;
        }
    }
}
