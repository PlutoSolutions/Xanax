// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.lang.reflect.Field;
import net.minecraft.network.play.server.SPacketExplosion;
import cat.yoink.xanax.client.handling.Mapping;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import cat.yoink.xanax.client.event.PacketEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.module.Module;

public class Velocity extends Module
{
    public Velocity() {
        super("Velocity", Category.MOVEMENT);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (this.nullCheck() || event.getType() == PacketEvent.Type.OUTGOING) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            if (((SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                try {
                    final Field motionX = SPacketEntityVelocity.class.getDeclaredField(Mapping.sPacketEntityVelocityMotionX);
                    final Field motionY = SPacketEntityVelocity.class.getDeclaredField(Mapping.sPacketEntityVelocityMotionY);
                    final Field motionZ = SPacketEntityVelocity.class.getDeclaredField(Mapping.sPacketEntityVelocityMotionZ);
                    motionX.setAccessible(true);
                    motionY.setAccessible(true);
                    motionZ.setAccessible(true);
                    motionX.set(event.getPacket(), 0);
                    motionY.set(event.getPacket(), 0);
                    motionZ.set(event.getPacket(), 0);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (event.getPacket() instanceof SPacketExplosion) {
            try {
                final Field motionX = SPacketExplosion.class.getDeclaredField("motionX");
                final Field motionY = SPacketExplosion.class.getDeclaredField("motionY");
                final Field motionZ = SPacketExplosion.class.getDeclaredField("motionZ");
                motionX.setAccessible(true);
                motionY.setAccessible(true);
                motionZ.setAccessible(true);
                motionX.set(event.getPacket(), 0);
                motionY.set(event.getPacket(), 0);
                motionZ.set(event.getPacket(), 0);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
