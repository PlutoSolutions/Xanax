// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.movement;

import java.lang.reflect.Field;
import net.minecraft.util.Timer;
import cat.yoink.xanax.client.handling.Mapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.entity.Entity;
import cat.yoink.xanax.client.event.MoveEvent;
import cat.yoink.xanax.client.event.WalkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import cat.yoink.xanax.client.util.PlayerUtil;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.setting.EnumSetting;
import cat.yoink.xanax.client.module.Module;

public class Speed extends Module
{
    private final EnumSetting mode;
    private final NumberSetting speed;
    private final BooleanSetting useTimer;
    private final NumberSetting timerSpeed;
    private final NumberSetting yPortAmount;
    private int currentStage;
    private double currentSpeed;
    private double distance;
    private int cooldown;
    private int jumps;
    
    public Speed() {
        super("Speed", Category.MOVEMENT);
        this.mode = this.newSetting(new EnumSetting("Mode", "Strafe", new String[] { "Strafe", "YPort", "TP" }));
        this.speed = this.newSetting(new NumberSetting("Speed", 9.0, 1.0, 100.0, 1.0));
        this.useTimer = this.newSetting(new BooleanSetting("UseTimer", false));
        this.timerSpeed = this.newSetting(new NumberSetting("TimerSpeed", 7.0, 1.0, 20.0, 0.1));
        this.yPortAmount = this.newSetting(new NumberSetting("YPortAmount", 4.0, 1.0, 10.0, 1.0));
    }
    
    @Override
    public void onEnable() {
        this.currentSpeed = PlayerUtil.vanillaSpeed();
        if (!Speed.mc.player.onGround) {
            this.currentStage = 3;
        }
    }
    
    @Override
    public void onDisable() {
        this.currentSpeed = 0.0;
        this.currentStage = 2;
        this.setTimer(50.0f);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.useTimer.getValue()) {
            this.setTimer(50.0f / (((float)this.timerSpeed.getValue() + 100.0f) / 100.0f));
        }
        else {
            this.setTimer(50.0f);
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final WalkEvent event) {
        this.distance = Math.sqrt((Speed.mc.player.posX - Speed.mc.player.prevPosX) * (Speed.mc.player.posX - Speed.mc.player.prevPosX) + (Speed.mc.player.posZ - Speed.mc.player.prevPosZ) * (Speed.mc.player.posZ - Speed.mc.player.prevPosZ));
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (this.mode.getValue().equalsIgnoreCase("Strafe")) {
            float forward = Speed.mc.player.movementInput.moveForward;
            float strafe = Speed.mc.player.movementInput.moveStrafe;
            float yaw = Speed.mc.player.rotationYaw;
            if (this.currentStage == 1 && PlayerUtil.isMoving()) {
                this.currentStage = 2;
                this.currentSpeed = 1.1799999475479126 * PlayerUtil.vanillaSpeed() - 0.01;
            }
            else if (this.currentStage == 2) {
                this.currentStage = 3;
                if (PlayerUtil.isMoving()) {
                    event.setY(Speed.mc.player.motionY = 0.4);
                    if (this.cooldown > 0) {
                        --this.cooldown;
                    }
                    this.currentSpeed *= this.speed.getValue() / 5.0;
                }
            }
            else if (this.currentStage == 3) {
                this.currentStage = 4;
                this.currentSpeed = this.distance - 0.66 * (this.distance - PlayerUtil.vanillaSpeed());
            }
            else {
                if (Speed.mc.world.getCollisionBoxes((Entity)Speed.mc.player, Speed.mc.player.getEntityBoundingBox().offset(0.0, Speed.mc.player.motionY, 0.0)).size() > 0 || Speed.mc.player.collidedVertically) {
                    this.currentStage = 1;
                }
                this.currentSpeed = this.distance - this.distance / 159.0;
            }
            this.currentSpeed = Math.max(this.currentSpeed, PlayerUtil.vanillaSpeed());
            if (forward == 0.0f && strafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
                this.currentSpeed = 0.0;
            }
            else if (forward != 0.0f) {
                if (strafe >= 1.0f) {
                    yaw += ((forward > 0.0f) ? -45.0f : 45.0f);
                    strafe = 0.0f;
                }
                else if (strafe <= -1.0f) {
                    yaw += ((forward > 0.0f) ? 45.0f : -45.0f);
                    strafe = 0.0f;
                }
                if (forward > 0.0f) {
                    forward = 1.0f;
                }
                else if (forward < 0.0f) {
                    forward = -1.0f;
                }
            }
            final double motionX = Math.cos(Math.toRadians(yaw + 90.0f));
            final double motionZ = Math.sin(Math.toRadians(yaw + 90.0f));
            if (this.cooldown == 0) {
                event.setX(forward * this.currentSpeed * motionX + strafe * this.currentSpeed * motionZ);
                event.setZ(forward * this.currentSpeed * motionZ - strafe * this.currentSpeed * motionX);
            }
            if (forward == 0.0f && strafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
            }
        }
        else if (this.mode.getValue().equalsIgnoreCase("TP") && PlayerUtil.isMoving() && Speed.mc.player.onGround) {
            for (double d = 0.0625; d < this.speed.getValue() / 10.0; d += 0.262) {
                float rotation = Speed.mc.player.prevRotationYaw + (Speed.mc.player.rotationYaw - Speed.mc.player.prevRotationYaw) * Speed.mc.getRenderPartialTicks();
                if (Speed.mc.player.movementInput.moveForward != 0.0f) {
                    if (Speed.mc.player.movementInput.moveStrafe > 0.0f) {
                        rotation += ((Speed.mc.player.movementInput.moveForward > 0.0f) ? -45 : 45);
                    }
                    else if (Speed.mc.player.movementInput.moveStrafe < 0.0f) {
                        rotation += ((Speed.mc.player.movementInput.moveForward > 0.0f) ? 45 : -45);
                    }
                    Speed.mc.player.movementInput.moveStrafe = 0.0f;
                    if (Speed.mc.player.movementInput.moveForward > 0.0f) {
                        Speed.mc.player.movementInput.moveForward = 1.0f;
                    }
                    else if (Speed.mc.player.movementInput.moveForward < 0.0f) {
                        Speed.mc.player.movementInput.moveForward = -1.0f;
                    }
                }
                final double cos = Math.cos(Math.toRadians(rotation + 90.0f));
                final double sin = Math.sin(Math.toRadians(rotation + 90.0f));
                Speed.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Speed.mc.player.posX + (Speed.mc.player.movementInput.moveForward * d * cos + Speed.mc.player.movementInput.moveStrafe * d * sin), Speed.mc.player.posY, Speed.mc.player.posZ + (Speed.mc.player.movementInput.moveForward * d * sin - Speed.mc.player.movementInput.moveStrafe * d * cos), Speed.mc.player.onGround));
            }
            Speed.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Speed.mc.player.posX + Speed.mc.player.motionX, 0.0, Speed.mc.player.posZ + Speed.mc.player.motionZ, Speed.mc.player.onGround));
        }
    }
    
    @SubscribeEvent
    public void onWalk(final WalkEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.mode.getValue().equalsIgnoreCase("YPort")) {
            if (Speed.mc.player.movementInput.moveForward == 0.0f && Speed.mc.player.movementInput.moveStrafe == 0.0f) {
                return;
            }
            if (this.jumps >= this.yPortAmount.getValue() && Speed.mc.player.onGround) {
                this.jumps = 0;
            }
            if (Speed.mc.player.onGround) {
                Speed.mc.player.motionY = ((this.jumps <= 1) ? 0.42 : 0.4);
                final float f = Speed.mc.player.rotationYaw * 0.017453292f;
                final EntityPlayerSP player = Speed.mc.player;
                player.motionX -= Math.sin(f) * 0.20000000298023224;
                final EntityPlayerSP player2 = Speed.mc.player;
                player2.motionZ += Math.cos(f) * 0.20000000298023224;
                ++this.jumps;
            }
            else if (this.jumps <= 1) {
                Speed.mc.player.motionY = -5.0;
            }
            final double yaw = PlayerUtil.getDirection();
            Speed.mc.player.motionX = -Math.sin(yaw) * Math.sqrt(Speed.mc.player.motionX * Speed.mc.player.motionX + Speed.mc.player.motionZ * Speed.mc.player.motionZ);
            Speed.mc.player.motionZ = Math.cos(yaw) * Math.sqrt(Speed.mc.player.motionX * Speed.mc.player.motionX + Speed.mc.player.motionZ * Speed.mc.player.motionZ);
        }
    }
    
    private void setTimer(final float time) {
        try {
            final Field timer = Minecraft.class.getDeclaredField(Mapping.timer);
            timer.setAccessible(true);
            final Field tickLength = Timer.class.getDeclaredField(Mapping.tickLength);
            tickLength.setAccessible(true);
            tickLength.setFloat(timer.get(Speed.mc), time);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
