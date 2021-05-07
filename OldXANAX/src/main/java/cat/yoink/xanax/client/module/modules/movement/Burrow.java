// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import cat.yoink.xanax.client.util.WorldUtil;
import cat.yoink.xanax.client.util.PlayerUtil;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.init.Blocks;
import cat.yoink.xanax.client.module.Category;
import net.minecraft.util.math.BlockPos;
import cat.yoink.xanax.client.module.Module;

public class Burrow extends Module
{
    private BlockPos playerPos;
    
    public Burrow() {
        super("Burrow", Category.MOVEMENT);
    }
    
    @Override
    public void onEnable() {
        this.playerPos = new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ);
        if (Burrow.mc.world.getBlockState(this.playerPos).getBlock().equals(Blocks.OBSIDIAN)) {
            this.disable();
            return;
        }
        Burrow.mc.player.jump();
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (Burrow.mc.player.posY > this.playerPos.getY() + 1.04) {
            WorldUtil.placeBlock(this.playerPos, PlayerUtil.getHotbarSlot(Blocks.OBSIDIAN));
            Burrow.mc.player.jump();
            this.disable();
        }
    }
}
