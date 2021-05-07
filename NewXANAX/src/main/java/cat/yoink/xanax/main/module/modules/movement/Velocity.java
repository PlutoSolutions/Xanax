package cat.yoink.xanax.main.module.modules.movement;

import cat.yoink.xanax.main.event.events.CollisionEvent;
import cat.yoink.xanax.main.event.events.PacketEvent;
import cat.yoink.xanax.main.event.events.WaterPushEvent;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.setting.BooleanSetting;
import cat.yoink.xanax.main.setting.NumberSetting;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import cat.yoink.eventmanager.Listener;

public final class Velocity extends Module
{
    private final NumberSetting horizontal = addSetting(new NumberSetting("Horizontal", 0, 0, 100, 1));
    private final BooleanSetting velocity = addSetting(new BooleanSetting("Velocity", true));
    private final NumberSetting vertical = addSetting(new NumberSetting("Vertical", 0, 0, 100, 1));
    private final BooleanSetting explosions = addSetting(new BooleanSetting("Explosions", true));
    private final BooleanSetting fishable = addSetting(new BooleanSetting("Fishable", false));
    private final BooleanSetting noPush = addSetting(new BooleanSetting("NoPush", true));

    public Velocity()
    {
        super("Velocity", Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onPlayerSPPushOutOfBlocks(final PlayerSPPushOutOfBlocksEvent event)
    {
        if (this.noPush.getValue() && event.getEntity().equals(mc.player)) event.setCanceled(true);
    }

    @Listener
    public void onWaterPush(final WaterPushEvent event)
    {
        if (this.noPush.getValue()) event.setCancelled(true);
    }

    @Listener
    public void onCollision(final CollisionEvent event)
    {
        if (this.noPush.getValue()) event.setCancelled(true);
    }

    @Listener
    public void onPacket(final PacketEvent event)
    {
        if (event.getPacket() instanceof SPacketEntityStatus && !this.fishable.getValue() && ((SPacketEntityStatus) event.getPacket()).getOpCode() == 31 && ((SPacketEntityStatus) event.getPacket()).getEntity(mc.world) instanceof EntityFishHook && ((EntityFishHook) ((SPacketEntityStatus) event.getPacket()).getEntity(mc.world)).caughtEntity.equals(mc.player))
        {
            event.setCancelled(true);
        }

        if (event.getPacket() instanceof SPacketEntityVelocity && this.velocity.getValue() && ((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId())
        {
            final SPacketEntityVelocity packet = (SPacketEntityVelocity) event.getPacket();

            packet.motionX = packet.motionX / 100 * (int) this.horizontal.getValue();
            packet.motionY = packet.motionY / 100 * (int) this.vertical.getValue();
            packet.motionZ = packet.motionZ / 100 * (int) this.horizontal.getValue();
        }

        if (event.getPacket() instanceof SPacketExplosion && this.explosions.getValue())
        {
            final SPacketExplosion packet = ((SPacketExplosion) event.getPacket());

            packet.motionX = packet.motionX / 100 * (int) this.horizontal.getValue();
            packet.motionY = packet.motionY / 100 * (int) this.vertical.getValue();
            packet.motionZ = packet.motionZ / 100 * (int) this.horizontal.getValue();
        }
    }
}
