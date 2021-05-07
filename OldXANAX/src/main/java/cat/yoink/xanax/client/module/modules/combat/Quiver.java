// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.combat;

import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemBow;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import cat.yoink.xanax.client.util.PlayerUtil;
import net.minecraft.init.Items;
import java.util.Objects;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.module.Module;

public class Quiver extends Module
{
    private final NumberSetting bowDelay;
    private final NumberSetting shootDelay;
    private final BooleanSetting strength;
    private final BooleanSetting speed;
    private int lastShot;
    
    public Quiver() {
        super("Quiver", Category.COMBAT);
        this.bowDelay = this.newSetting(new NumberSetting("BowDelay", 4.0, 1.0, 20.0, 1.0));
        this.shootDelay = this.newSetting(new NumberSetting("ShootDelay", 10.0, 2.0, 40.0, 1.0));
        this.strength = this.newSetting(new BooleanSetting("Strength", true));
        this.speed = this.newSetting(new BooleanSetting("Speed", true));
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.lastShot < 50) {
            ++this.lastShot;
        }
        final boolean hasSpeed = Quiver.mc.player.getActivePotionEffect((Potion)Objects.requireNonNull(Potion.getPotionById(1))) != null;
        final boolean hasStrength = Quiver.mc.player.getActivePotionEffect((Potion)Objects.requireNonNull(Potion.getPotionById(5))) != null;
        final int bowSlot = PlayerUtil.getHotbarSlot((Item)Items.BOW);
        final List<Integer> tippedArrowSlots = PlayerUtil.getInventorySlots(Items.TIPPED_ARROW);
        int speedArrowSlot = -1;
        boolean foundSpeedArrowSlot = false;
        int strengthArrowSlot = -1;
        boolean foundStrengthArrowSlot = false;
        for (final Integer slot : tippedArrowSlots) {
            if (!foundSpeedArrowSlot && Objects.requireNonNull(PotionUtils.getPotionFromItem(Quiver.mc.player.inventory.getStackInSlot((int)slot)).getRegistryName()).getPath().contains("swiftness")) {
                speedArrowSlot = slot;
                foundSpeedArrowSlot = true;
            }
            if (!foundStrengthArrowSlot && Objects.requireNonNull(PotionUtils.getPotionFromItem(Quiver.mc.player.inventory.getStackInSlot((int)slot)).getRegistryName()).getPath().contains("strength")) {
                strengthArrowSlot = slot;
                foundStrengthArrowSlot = true;
            }
        }
        if (this.speed.getValue() && !hasSpeed && speedArrowSlot != -1 && (speedArrowSlot < strengthArrowSlot || strengthArrowSlot == -1) && bowSlot != -1 && Quiver.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && Quiver.mc.player.isHandActive() && Quiver.mc.player.getItemInUseMaxCount() >= this.bowDelay.getValue() && this.shootDelay.getValue() < this.lastShot) {
            this.doShooting();
        }
        if (this.speed.getValue() && !hasSpeed && strengthArrowSlot != -1 && speedArrowSlot != -1 && speedArrowSlot > strengthArrowSlot && bowSlot != -1 && Quiver.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && Quiver.mc.player.isHandActive() && Quiver.mc.player.getItemInUseMaxCount() >= 1) {
            PlayerUtil.swapSlots(speedArrowSlot, strengthArrowSlot);
        }
        if (hasSpeed || !this.speed.getValue() || speedArrowSlot == -1) {
            if (this.strength.getValue() && !hasStrength && strengthArrowSlot != -1 && (strengthArrowSlot < speedArrowSlot || speedArrowSlot == -1) && bowSlot != -1 && Quiver.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && Quiver.mc.player.isHandActive() && Quiver.mc.player.getItemInUseMaxCount() >= this.bowDelay.getValue() && this.shootDelay.getValue() < this.lastShot) {
                this.doShooting();
            }
            if (this.strength.getValue() && !hasStrength && strengthArrowSlot != -1 && speedArrowSlot != -1 && strengthArrowSlot > speedArrowSlot && bowSlot != -1 && Quiver.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && Quiver.mc.player.isHandActive() && Quiver.mc.player.getItemInUseMaxCount() >= 1) {
                PlayerUtil.swapSlots(speedArrowSlot, strengthArrowSlot);
            }
        }
    }
    
    private void doShooting() {
        Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(Quiver.mc.player.rotationYaw, -90.0f, Quiver.mc.player.onGround));
        Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Quiver.mc.player.getHorizontalFacing()));
        Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(Quiver.mc.player.getActiveHand()));
        Quiver.mc.player.stopActiveHand();
        this.lastShot = 0;
    }
}
