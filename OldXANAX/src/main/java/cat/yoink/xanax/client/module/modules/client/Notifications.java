// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.client;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import cat.yoink.xanax.client.event.PacketEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import cat.yoink.xanax.client.notification.NotificationManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cat.yoink.xanax.client.module.Category;
import java.util.HashMap;
import cat.yoink.xanax.client.module.Module;

public class Notifications extends Module
{
    private final HashMap<String, Integer> popList;
    
    public Notifications() {
        super("Notifications", Category.CLIENT);
        this.popList = new HashMap<String, Integer>();
    }
    
    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent event) {
        if (this.nullCheck() || !event.getType().equals((Object)RenderGameOverlayEvent.ElementType.TEXT)) {
            return;
        }
        NotificationManager.INSTANCE.renderNotifications();
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (this.nullCheck() || !event.getType().equals(PacketEvent.Type.INCOMING) || !(event.getPacket() instanceof SPacketEntityStatus)) {
            return;
        }
        final SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
        if (packet.getOpCode() == 35) {
            final Entity entity = packet.getEntity((World)Notifications.mc.world);
            if (this.popList.get(entity.getName()) == null) {
                this.popList.put(entity.getName(), 1);
                NotificationManager.INSTANCE.addNotification(entity.getName() + " popped! [1]", 1000L);
            }
            else if (this.popList.get(entity.getName()) != null) {
                final int popCounter = this.popList.get(entity.getName());
                final int newPopCounter = popCounter + 1;
                this.popList.put(entity.getName(), newPopCounter);
                NotificationManager.INSTANCE.addNotification(entity.getName() + " popped! [" + newPopCounter + "]", 1000L);
            }
        }
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        for (final EntityPlayer player : Notifications.mc.world.playerEntities) {
            if (player.getHealth() <= 0.0f && this.popList.containsKey(player.getName())) {
                NotificationManager.INSTANCE.addNotification(player.getName() + " died after popping " + this.popList.get(player.getName()) + " totems!", 1000L);
                this.popList.remove(player.getName(), this.popList.get(player.getName()));
            }
        }
    }
}
