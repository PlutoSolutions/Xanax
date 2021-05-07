// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.util;

import java.lang.reflect.Field;
import cat.yoink.xanax.client.handling.Mapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.inventory.ClickType;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import java.util.Objects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.entity.EntityLivingBase;
import cat.yoink.xanax.client.friend.FriendManager;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import cat.yoink.xanax.client.MinecraftInstance;

public class PlayerUtil implements MinecraftInstance
{
    public static EntityPlayer getClosestPlayer() {
        float range = 1000000.0f;
        EntityPlayer closest = null;
        for (final EntityPlayer player : PlayerUtil.mc.world.playerEntities) {
            if (player == PlayerUtil.mc.player) {
                continue;
            }
            final float distance = PlayerUtil.mc.player.getDistance((Entity)player);
            if (distance >= range) {
                continue;
            }
            closest = player;
            range = distance;
        }
        return closest;
    }
    
    public static EntityPlayer getClosestPlayer(final boolean getFriends) {
        float range = 1000000.0f;
        EntityPlayer closest = null;
        for (final EntityPlayer player : PlayerUtil.mc.world.playerEntities) {
            if (player != PlayerUtil.mc.player) {
                if (!getFriends && FriendManager.INSTANCE.isFriend(player.getName())) {
                    continue;
                }
                final float distance = PlayerUtil.mc.player.getDistance((Entity)player);
                if (distance >= range) {
                    continue;
                }
                closest = player;
                range = distance;
            }
        }
        return closest;
    }
    
    public static Entity getClosestEntity() {
        float range = 1000000.0f;
        Entity closest = null;
        for (final Entity e : PlayerUtil.mc.world.loadedEntityList) {
            if (e instanceof EntityLivingBase && e != PlayerUtil.mc.player && !e.isDead) {
                if (PlayerUtil.mc.player == null) {
                    continue;
                }
                final float distance = PlayerUtil.mc.player.getDistance(e);
                if (distance >= range) {
                    continue;
                }
                closest = e;
                range = distance;
            }
        }
        return closest;
    }
    
    public static Entity getClosestEntity(final boolean getFriends) {
        float range = 1000000.0f;
        Entity closest = null;
        for (final Entity e : PlayerUtil.mc.world.loadedEntityList) {
            if (e instanceof EntityLivingBase && e != PlayerUtil.mc.player && !e.isDead && PlayerUtil.mc.player != null) {
                if (!getFriends && FriendManager.INSTANCE.isFriend(e.getName())) {
                    continue;
                }
                final float distance = PlayerUtil.mc.player.getDistance(e);
                if (distance >= range) {
                    continue;
                }
                closest = e;
                range = distance;
            }
        }
        return closest;
    }
    
    public static double vanillaSpeed() {
        double baseSpeed = 0.272;
        if (PlayerUtil.mc.player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = Objects.requireNonNull(PlayerUtil.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * amplifier;
        }
        return baseSpeed;
    }
    
    public static boolean isMoving() {
        return PlayerUtil.mc.player.moveForward != 0.0 || PlayerUtil.mc.player.moveStrafing != 0.0;
    }
    
    public static int getHotbarSlot(final Item item) {
        for (int i = 0; i < 9; ++i) {
            final Item item2 = PlayerUtil.mc.player.inventory.getStackInSlot(i).getItem();
            if (item.equals(item2)) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getHotbarSlot(final Block block) {
        for (int i = 0; i < 9; ++i) {
            final Item item = PlayerUtil.mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock && ((ItemBlock)item).getBlock().equals(block)) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getInventorySlot(final Item item) {
        for (int i = 9; i < 36; ++i) {
            final Item item2 = PlayerUtil.mc.player.inventory.getStackInSlot(i).getItem();
            if (item.equals(item2)) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getInventorySlot(final Block block) {
        for (int i = 9; i < 36; ++i) {
            final Item item = PlayerUtil.mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock && ((ItemBlock)item).getBlock().equals(block)) {
                return i;
            }
        }
        return -1;
    }
    
    public static List<Integer> getInventorySlots(final Item item) {
        final List<Integer> slots = new ArrayList<Integer>();
        for (int i = 9; i < 36; ++i) {
            final Item item2 = PlayerUtil.mc.player.inventory.getStackInSlot(i).getItem();
            if (item.equals(item2)) {
                slots.add(i);
            }
        }
        return slots;
    }
    
    public static void swapSlots(final int slot1, final int slot2) {
        PlayerUtil.mc.playerController.windowClick(0, slot1, 0, ClickType.PICKUP, (EntityPlayer)PlayerUtil.mc.player);
        PlayerUtil.mc.playerController.windowClick(0, slot2, 0, ClickType.PICKUP, (EntityPlayer)PlayerUtil.mc.player);
        PlayerUtil.mc.playerController.windowClick(0, slot1, 0, ClickType.PICKUP, (EntityPlayer)PlayerUtil.mc.player);
    }
    
    public static void rotatePacket(final double x, final double y, final double z) {
        final double diffX = x - PlayerUtil.mc.player.posX;
        final double diffY = y - (PlayerUtil.mc.player.posY + PlayerUtil.mc.player.getEyeHeight());
        final double diffZ = z - PlayerUtil.mc.player.posZ;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        PlayerUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(yaw, pitch, PlayerUtil.mc.player.onGround));
    }
    
    public static double getDirection() {
        float rotationYaw = PlayerUtil.mc.player.rotationYaw;
        if (PlayerUtil.mc.player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (PlayerUtil.mc.player.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (PlayerUtil.mc.player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (PlayerUtil.mc.player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (PlayerUtil.mc.player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    public static void setRightClickDelayTimer(final int delayTimer) {
        try {
            final Field rightClickDelayTimer = Minecraft.class.getDeclaredField(Mapping.rightClickDelayTimer);
            rightClickDelayTimer.setAccessible(true);
            rightClickDelayTimer.set(PlayerUtil.mc, delayTimer);
        }
        catch (Exception ex) {}
    }
}
