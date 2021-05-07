// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.world;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import cat.yoink.xanax.client.util.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.module.Module;

public class FastUse extends Module
{
    private final BooleanSetting xp;
    private final BooleanSetting crystals;
    private final BooleanSetting enderChest;
    private final BooleanSetting obsidian;
    
    public FastUse() {
        super("FastUse", Category.WORLD);
        this.xp = this.newSetting(new BooleanSetting("XP", true));
        this.crystals = this.newSetting(new BooleanSetting("Crystals", true));
        this.enderChest = this.newSetting(new BooleanSetting("EnderChest", false));
        this.obsidian = this.newSetting(new BooleanSetting("Obsidian", false));
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck() || ((!FastUse.mc.player.inventory.getCurrentItem().getItem().equals(Items.EXPERIENCE_BOTTLE) || !this.xp.getValue()) && (!FastUse.mc.player.inventory.getCurrentItem().getItem().equals(Items.END_CRYSTAL) || !this.crystals.getValue()) && (!Block.getBlockFromItem(FastUse.mc.player.inventory.getCurrentItem().getItem()).equals(Blocks.ENDER_CHEST) || !this.enderChest.getValue()) && (!Block.getBlockFromItem(FastUse.mc.player.inventory.getCurrentItem().getItem()).equals(Blocks.OBSIDIAN) || !this.obsidian.getValue()))) {
            return;
        }
        PlayerUtil.setRightClickDelayTimer(0);
    }
}
