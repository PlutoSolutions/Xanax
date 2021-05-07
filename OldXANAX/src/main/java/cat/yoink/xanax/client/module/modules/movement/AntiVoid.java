// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import cat.yoink.xanax.client.util.WorldUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.EnumSetting;
import cat.yoink.xanax.client.module.Module;

public class AntiVoid extends Module
{
    private final EnumSetting mode;
    
    public AntiVoid() {
        super("AntiVoid", Category.MOVEMENT);
        this.mode = this.newSetting(new EnumSetting("Mode", "NoFall", new String[] { "NoFall", "Place", "Jump" }));
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.isAboveVoid() && !AntiVoid.mc.player.isSneaking()) {
            if (this.mode.getValue().equals("Place")) {
                final int slot = this.getObsidianSlot();
                if (slot != -1) {
                    final int oldSlot = AntiVoid.mc.player.inventory.currentItem;
                    AntiVoid.mc.player.inventory.currentItem = slot;
                    WorldUtil.placeBlock(new BlockPos(AntiVoid.mc.player.posX, AntiVoid.mc.player.posY - 1.0, AntiVoid.mc.player.posZ));
                    AntiVoid.mc.player.inventory.currentItem = oldSlot;
                }
            }
            else if (this.mode.getValue().equals("NoFall")) {
                AntiVoid.mc.player.motionY = 0.0;
                AntiVoid.mc.player.onGround = true;
            }
            else {
                AntiVoid.mc.player.moveVertical = 5.0f;
                AntiVoid.mc.player.jump();
            }
        }
        else {
            AntiVoid.mc.player.moveVertical = 0.0f;
        }
    }
    
    private boolean isAboveVoid() {
        if (AntiVoid.mc.player.posY <= 3.0) {
            final RayTraceResult trace = AntiVoid.mc.world.rayTraceBlocks(AntiVoid.mc.player.getPositionVector(), new Vec3d(AntiVoid.mc.player.posX, 0.0, AntiVoid.mc.player.posZ), false, false, false);
            return trace == null || trace.typeOfHit != RayTraceResult.Type.BLOCK;
        }
        return false;
    }
    
    public int getObsidianSlot() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AntiVoid.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (block instanceof BlockObsidian) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }
}
