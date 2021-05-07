// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import cat.yoink.xanax.client.event.PacketEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.module.Module;

public class Criticals extends Module
{
    public Criticals() {
        super("Criticals", Category.COMBAT);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (this.nullCheck() || event.getType() == PacketEvent.Type.INCOMING || !(event.getPacket() instanceof CPacketUseEntity) || ((CPacketUseEntity)event.getPacket()).getAction() != CPacketUseEntity.Action.ATTACK || !Criticals.mc.player.onGround) {
            return;
        }
        Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.10000000149011612, Criticals.mc.player.posZ, false));
        Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
    }
}
