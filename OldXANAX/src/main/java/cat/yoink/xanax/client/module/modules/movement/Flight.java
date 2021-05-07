// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.movement;

import java.lang.reflect.Field;
import cat.yoink.xanax.client.handling.Mapping;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import cat.yoink.xanax.client.event.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.module.Module;

public class Flight extends Module
{
    public Flight() {
        super("Flight", Category.MOVEMENT);
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        Flight.mc.player.motionY = 0.0;
        Flight.mc.player.onGround = true;
        final EntityPlayerSP player = Flight.mc.player;
        player.motionX *= 1.01;
        final EntityPlayerSP player2 = Flight.mc.player;
        player2.motionZ *= 1.01;
        if ((Flight.mc.player.movementInput.moveForward != 0.0f || Flight.mc.player.movementInput.moveStrafe != 0.0f) && !Flight.mc.player.isSprinting()) {
            Flight.mc.player.setSprinting(true);
        }
    }
    
    @Override
    public void onEnable() {
        for (int i = 0; i <= 64.0; ++i) {
            Flight.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Flight.mc.player.posX, Flight.mc.player.posY + 0.35, Flight.mc.player.posZ, false));
            Flight.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Flight.mc.player.posX, Flight.mc.player.posY, Flight.mc.player.posZ, i == 64.0));
        }
        final EntityPlayerSP player = Flight.mc.player;
        player.motionX *= 0.2;
        final EntityPlayerSP player2 = Flight.mc.player;
        player2.motionZ *= 0.2;
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (this.nullCheck() || event.getType() == PacketEvent.Type.OUTGOING) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            try {
                final Field yaw = SPacketPlayerPosLook.class.getDeclaredField(Mapping.playerPosLookYaw);
                final Field pitch = SPacketPlayerPosLook.class.getDeclaredField(Mapping.playerPosLookPitch);
                yaw.setAccessible(true);
                pitch.setAccessible(true);
                yaw.set(event.getPacket(), Flight.mc.player.rotationYaw);
                pitch.set(event.getPacket(), Flight.mc.player.rotationPitch);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
