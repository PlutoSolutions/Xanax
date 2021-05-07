// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.world;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketAnimation;
import cat.yoink.xanax.client.event.PacketEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.module.Module;

public class NoSwing extends Module
{
    public NoSwing() {
        super("NoSwing", Category.WORLD);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (this.nullCheck() || event.getType() == PacketEvent.Type.INCOMING) {
            return;
        }
        if (event.getPacket() instanceof CPacketAnimation) {
            event.setCanceled(true);
        }
    }
}
