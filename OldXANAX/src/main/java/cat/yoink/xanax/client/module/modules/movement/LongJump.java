// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.movement;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import cat.yoink.xanax.client.util.PlayerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.module.Module;

public class LongJump extends Module
{
    private final NumberSetting speed;
    private final BooleanSetting packet;
    private boolean jumped;
    private boolean canBoost;
    
    public LongJump() {
        super("LongJump", Category.MOVEMENT);
        this.speed = this.newSetting(new NumberSetting("Speed", 30.0, 1.0, 100.0, 0.5));
        this.packet = this.newSetting(new BooleanSetting("Packet", false));
        this.jumped = false;
        this.canBoost = false;
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.jumped) {
            if (LongJump.mc.player.onGround || LongJump.mc.player.capabilities.isFlying) {
                this.jumped = false;
                LongJump.mc.player.motionX = 0.0;
                LongJump.mc.player.motionZ = 0.0;
                if (this.packet.getValue()) {
                    LongJump.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(LongJump.mc.player.posX, LongJump.mc.player.posY, LongJump.mc.player.posZ, LongJump.mc.player.onGround));
                    LongJump.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(LongJump.mc.player.posX + LongJump.mc.player.motionX, 0.0, LongJump.mc.player.posZ + LongJump.mc.player.motionZ, LongJump.mc.player.onGround));
                }
                return;
            }
            if (LongJump.mc.player.movementInput.moveForward == 0.0f && LongJump.mc.player.movementInput.moveStrafe == 0.0f) {
                return;
            }
            final double yaw = PlayerUtil.getDirection();
            LongJump.mc.player.motionX = -Math.sin(yaw) * ((float)Math.sqrt(LongJump.mc.player.motionX * LongJump.mc.player.motionX + LongJump.mc.player.motionZ * LongJump.mc.player.motionZ) * (this.canBoost ? (this.speed.getValue() / 10.0) : 1.0));
            LongJump.mc.player.motionZ = Math.cos(yaw) * ((float)Math.sqrt(LongJump.mc.player.motionX * LongJump.mc.player.motionX + LongJump.mc.player.motionZ * LongJump.mc.player.motionZ) * (this.canBoost ? (this.speed.getValue() / 10.0) : 1.0));
            this.canBoost = false;
        }
    }
    
    @SubscribeEvent
    public void onJump(final LivingEvent.LivingJumpEvent event) {
        if (LongJump.mc.player != null && LongJump.mc.world != null && event.getEntity() == LongJump.mc.player && (LongJump.mc.player.movementInput.moveForward != 0.0f || LongJump.mc.player.movementInput.moveStrafe != 0.0f)) {
            this.jumped = true;
            this.canBoost = true;
        }
    }
}
