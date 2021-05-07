// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import cat.yoink.xanax.client.util.WorldUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import cat.yoink.xanax.client.util.PlayerUtil;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import cat.yoink.xanax.client.module.Category;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.module.Module;

public class AutoTrap extends Module
{
    private final NumberSetting blocksPerTick;
    private final BooleanSetting disable;
    List<Vec3d> positions;
    
    public AutoTrap() {
        super("AutoTrap", Category.COMBAT);
        this.blocksPerTick = this.newSetting(new NumberSetting("BTP", 1.0, 1.0, 5.0, 1.0));
        this.disable = this.newSetting(new BooleanSetting("Disable", true));
        this.positions = new ArrayList<Vec3d>(Arrays.asList(new Vec3d(0.0, -1.0, -1.0), new Vec3d(1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, 0.0)));
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        int blocksPlaced = 0;
        final EntityPlayer closestPlayer = PlayerUtil.getClosestPlayer(false);
        if (closestPlayer == null) {
            if (this.disable.getValue()) {
                this.disable();
            }
            return;
        }
        for (final Vec3d position : this.positions) {
            final BlockPos block = new BlockPos(position.add(closestPlayer.getPositionVector()));
            if (AutoTrap.mc.world.getBlockState(block).getBlock().equals(Blocks.AIR)) {
                final int oldSlot = AutoTrap.mc.player.inventory.currentItem;
                final int obSlot = PlayerUtil.getHotbarSlot(Blocks.OBSIDIAN);
                if (obSlot == -1) {
                    return;
                }
                AutoTrap.mc.player.inventory.currentItem = obSlot;
                WorldUtil.placeBlock(block);
                AutoTrap.mc.player.inventory.currentItem = oldSlot;
                if (++blocksPlaced == this.blocksPerTick.getValue()) {
                    return;
                }
                continue;
            }
        }
        if (blocksPlaced == 0 && this.disable.getValue()) {
            this.disable();
        }
    }
}
