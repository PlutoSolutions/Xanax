// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.misc;

import java.util.Iterator;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.HashMap;
import net.minecraft.item.ItemStack;
import java.util.Map;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.module.Module;

public class AutoReplenish extends Module
{
    private final NumberSetting threshold;
    private final NumberSetting tickDelay;
    private int delayStep;
    
    public AutoReplenish() {
        super("AutoReplenish", Category.MISC);
        this.threshold = this.newSetting(new NumberSetting("Threshold", 1.0, 20.0, 64.0, 1.0));
        this.tickDelay = this.newSetting(new NumberSetting("TickDelay", 4.0, 0.0, 20.0, 1.0));
        this.delayStep = 0;
    }
    
    private Map<Integer, ItemStack> getInventory() {
        return this.getInventorySlots(9, 35);
    }
    
    private Map<Integer, ItemStack> getHotbar() {
        return this.getInventorySlots(36, 44);
    }
    
    private Map<Integer, ItemStack> getInventorySlots(final int current, final int last) {
        int c = current;
        final Map<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        while (c <= last) {
            fullInventorySlots.put(c, (ItemStack)AutoReplenish.mc.player.inventoryContainer.getInventory().get(c));
            ++c;
        }
        return fullInventorySlots;
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck() || AutoReplenish.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.delayStep < this.tickDelay.getValue()) {
            ++this.delayStep;
            return;
        }
        this.delayStep = 0;
        final PairUtil<Integer, Integer> slots = this.findReplenishableHotbarSlot();
        if (slots == null) {
            return;
        }
        final int inventorySlot = slots.getKey();
        final int hotbarSlot = slots.getValue();
        AutoReplenish.mc.playerController.windowClick(0, inventorySlot, 0, ClickType.PICKUP, (EntityPlayer)AutoReplenish.mc.player);
        AutoReplenish.mc.playerController.windowClick(0, hotbarSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoReplenish.mc.player);
        AutoReplenish.mc.playerController.windowClick(0, inventorySlot, 0, ClickType.PICKUP, (EntityPlayer)AutoReplenish.mc.player);
    }
    
    private PairUtil<Integer, Integer> findReplenishableHotbarSlot() {
        PairUtil<Integer, Integer> returnPair = null;
        for (final Map.Entry<Integer, ItemStack> hotbarSlot : this.getHotbar().entrySet()) {
            final ItemStack stack = hotbarSlot.getValue();
            final int inventorySlot = this.findCompatibleInventorySlot(stack);
            if (!stack.isEmpty() && stack.getItem() != Items.AIR && stack.isStackable() && stack.getCount() < stack.getMaxStackSize() && stack.getCount() <= this.threshold.getValue()) {
                if (inventorySlot == -1) {
                    continue;
                }
                returnPair = new PairUtil<Integer, Integer>(inventorySlot, hotbarSlot.getKey());
            }
        }
        return returnPair;
    }
    
    private int findCompatibleInventorySlot(final ItemStack hotbarStack) {
        int inventorySlot = -1;
        int smallestStackSize = 999;
        for (final Map.Entry<Integer, ItemStack> entry : this.getInventory().entrySet()) {
            final ItemStack inventoryStack = entry.getValue();
            if (!inventoryStack.isEmpty() && inventoryStack.getItem() != Items.AIR) {
                if (!this.isCompatibleStacks(hotbarStack, inventoryStack)) {
                    continue;
                }
                final int currentStackSize = ((ItemStack)AutoReplenish.mc.player.inventoryContainer.getInventory().get((int)entry.getKey())).getCount();
                if (smallestStackSize <= currentStackSize) {
                    continue;
                }
                smallestStackSize = currentStackSize;
                inventorySlot = entry.getKey();
            }
        }
        return inventorySlot;
    }
    
    private boolean isCompatibleStacks(final ItemStack stack1, final ItemStack stack2) {
        return stack1.getItem().equals(stack2.getItem()) && stack1.getDisplayName().equals(stack2.getDisplayName()) && stack1.getItemDamage() == stack2.getItemDamage();
    }
    
    private static class PairUtil<T, S>
    {
        private final T key;
        private final S value;
        
        public PairUtil(final T key, final S value) {
            this.key = key;
            this.value = value;
        }
        
        public T getKey() {
            return this.key;
        }
        
        public S getValue() {
            return this.value;
        }
    }
}
