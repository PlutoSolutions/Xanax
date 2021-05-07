// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import cat.yoink.xanax.client.util.WorldUtil;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
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

public class AntiTrap extends Module
{
    private final NumberSetting blocksPerTick;
    private final BooleanSetting disable;
    private final BooleanSetting high;
    List<Vec3d> positions;
    
    public AntiTrap() {
        super("AntiTrap", Category.COMBAT);
        this.blocksPerTick = this.newSetting(new NumberSetting("BTP", 1.0, 1.0, 5.0, 1.0));
        this.disable = this.newSetting(new BooleanSetting("Disable", true));
        this.high = this.newSetting(new BooleanSetting("High", false));
        this.positions = new ArrayList<Vec3d>(Arrays.asList(new Vec3d(2.0, 0.0, 0.0), new Vec3d(-2.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 2.0), new Vec3d(0.0, 0.0, -2.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, -1.0)));
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
        int blocksPlaced = 0;
        final List<Vec3d> list = new ArrayList<Vec3d>(this.positions);
        if (this.high.getValue()) {
            for (final Vec3d vec3d : this.positions) {
                final BlockPos block = new BlockPos(vec3d);
                list.add(new Vec3d((Vec3i)block.up()));
            }
        }
        for (final Vec3d position : list) {
            final BlockPos pos = new BlockPos(position.add(AntiTrap.mc.player.getPositionVector()));
            if (AntiTrap.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                final int oldSlot = AntiTrap.mc.player.inventory.currentItem;
                final int obSlot = PlayerUtil.getHotbarSlot(Blocks.OBSIDIAN);
                if (obSlot == -1) {
                    return;
                }
                AntiTrap.mc.player.inventory.currentItem = obSlot;
                WorldUtil.placeBlock(pos);
                AntiTrap.mc.player.inventory.currentItem = oldSlot;
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
