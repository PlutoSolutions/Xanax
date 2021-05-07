// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3d;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.module.Module;

public class ReverseStep extends Module
{
    private final BooleanSetting holeOnly;
    
    public ReverseStep() {
        super("ReverseStep", Category.MOVEMENT);
        this.holeOnly = this.newSetting(new BooleanSetting("HoleOnly", true));
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck() || ReverseStep.mc.player.isInLava() || ReverseStep.mc.player.isDead || ReverseStep.mc.player.isInWater()) {
            return;
        }
        if (this.holeOnly.getValue()) {
            if (this.fallingIntoHole() && ReverseStep.mc.player.onGround) {
                final EntityPlayerSP player = ReverseStep.mc.player;
                --player.motionY;
            }
        }
        else if (ReverseStep.mc.player.onGround) {
            final EntityPlayerSP player2 = ReverseStep.mc.player;
            --player2.motionY;
        }
    }
    
    private boolean fallingIntoHole() {
        final Vec3d vec = this.interpolateEntity(ReverseStep.mc.player, ReverseStep.mc.getRenderPartialTicks());
        final BlockPos pos = new BlockPos(vec.x, vec.y - 1.0, vec.z);
        final BlockPos[] posList = { pos.north(), pos.south(), pos.east(), pos.west(), pos.down() };
        int blocks = 0;
        for (final BlockPos blockPos : posList) {
            final Block block = ReverseStep.mc.world.getBlockState(blockPos).getBlock();
            if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
                ++blocks;
            }
        }
        return blocks == 5;
    }
    
    private Vec3d interpolateEntity(final EntityPlayerSP entity, final float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }
}
