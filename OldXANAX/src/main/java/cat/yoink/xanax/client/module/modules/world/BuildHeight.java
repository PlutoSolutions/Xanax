// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.world;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.lang.reflect.Field;
import cat.yoink.xanax.client.handling.Mapping;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import cat.yoink.xanax.client.event.PacketEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.module.Module;

public class BuildHeight extends Module
{
    public BuildHeight() {
        super("BuildHeight", Category.WORLD);
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent event) {
        if (this.nullCheck() || event.getType() == PacketEvent.Type.INCOMING) {
            return;
        }
        final CPacketPlayerTryUseItemOnBlock packet;
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && (packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket()).getPos().getY() >= 255 && packet.getDirection() == EnumFacing.UP) {
            try {
                final Field direction = CPacketPlayerTryUseItemOnBlock.class.getDeclaredField(Mapping.placedBlockDirection);
                direction.setAccessible(true);
                direction.set(packet, EnumFacing.DOWN);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
