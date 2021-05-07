// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import cat.yoink.xanax.client.util.WorldUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import cat.yoink.xanax.client.util.PlayerUtil;
import net.minecraft.init.Blocks;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import cat.yoink.xanax.client.module.Category;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.module.Module;

public class AutoSelfTrap extends Module
{
    private final NumberSetting distance;
    private final NumberSetting blocksPerTick;
    private final BooleanSetting disable;
    private final List<Vec3d> positions;
    
    public AutoSelfTrap() {
        super("AutoSelfTrap", Category.COMBAT);
        this.distance = this.newSetting(new NumberSetting("Distance", 4.0, 1.0, 10.0, 0.1));
        this.blocksPerTick = this.newSetting(new NumberSetting("BTP", 1.0, 1.0, 5.0, 1.0));
        this.disable = this.newSetting(new BooleanSetting("Disable", true));
        this.positions = new ArrayList<Vec3d>(Arrays.asList(new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, 0.0)));
    }
    
    @Override
    public void onEnable() {
        if (PlayerUtil.getHotbarSlot(Blocks.OBSIDIAN) == -1) {
            this.disable();
        }
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        final EntityPlayer closestPlayer = PlayerUtil.getClosestPlayer();
        if (closestPlayer == null) {
            return;
        }
        if (AutoSelfTrap.mc.player.getDistance((Entity)closestPlayer) < this.distance.getValue()) {
            int blocksPlaced = 0;
            for (final Vec3d position : this.positions) {
                final BlockPos pos = new BlockPos(position.add(AutoSelfTrap.mc.player.getPositionVector()));
                if (AutoSelfTrap.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                    final int oldSlot = AutoSelfTrap.mc.player.inventory.currentItem;
                    final int obsidianSlot = PlayerUtil.getHotbarSlot(Blocks.OBSIDIAN);
                    if (obsidianSlot == -1) {
                        continue;
                    }
                    AutoSelfTrap.mc.player.inventory.currentItem = obsidianSlot;
                    WorldUtil.placeBlock(pos);
                    AutoSelfTrap.mc.player.inventory.currentItem = oldSlot;
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
}
