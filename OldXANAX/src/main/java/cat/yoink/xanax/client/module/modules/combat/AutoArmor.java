// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.combat;

import net.minecraft.inventory.ClickType;
import cat.yoink.xanax.client.MinecraftInstance;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemArmor;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemExpBottle;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import java.util.Map;
import cat.yoink.xanax.client.friend.FriendManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import cat.yoink.xanax.client.module.Category;
import java.util.List;
import java.util.Queue;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.module.Module;

public class AutoArmor extends Module
{
    private final NumberSetting delay;
    private final BooleanSetting curse;
    private final BooleanSetting mendingTakeOff;
    private final NumberSetting closestEnemy;
    private final NumberSetting repair;
    private final NumberSetting actions;
    private final Timer timer;
    private final Queue<Task> taskList;
    private final List<Integer> doneSlots;
    private boolean flag;
    
    public AutoArmor() {
        super("AutoArmor", Category.COMBAT);
        this.delay = this.newSetting(new NumberSetting("Delay", 50.0, 0.0, 500.0, 1.0));
        this.curse = this.newSetting(new BooleanSetting("Vanishing", false));
        this.mendingTakeOff = this.newSetting(new BooleanSetting("AutoMend", false));
        this.closestEnemy = this.newSetting(new NumberSetting("Enemy", 8.0, 1.0, 20.0, 0.1));
        this.repair = this.newSetting(new NumberSetting("Repair%", 80.0, 1.0, 100.0, 1.0));
        this.actions = this.newSetting(new NumberSetting("Packets", 3.0, 1.0, 12.0, 1.0));
        this.timer = new Timer();
        this.taskList = new ConcurrentLinkedQueue<Task>();
        this.doneSlots = new ArrayList<Integer>();
    }
    
    @Override
    public void onDisable() {
        this.taskList.clear();
        this.doneSlots.clear();
        this.flag = false;
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck() || (AutoArmor.mc.currentScreen instanceof GuiContainer && !(AutoArmor.mc.currentScreen instanceof GuiInventory))) {
            return;
        }
        if (this.taskList.isEmpty()) {
            if (this.mendingTakeOff.getValue() && this.holdingItem() && AutoArmor.mc.gameSettings.keyBindUseItem.isKeyDown() && AutoArmor.mc.world.playerEntities.stream().noneMatch(e -> e != AutoArmor.mc.player && AutoArmor.mc.player.getDistance((Entity)e) <= (float)this.closestEnemy.getValue() && !FriendManager.INSTANCE.isFriend(((EntityPlayer)e).getName())) && !this.flag) {
                int takeOff = 0;
                for (final Map.Entry<Integer, ItemStack> armorSlot : this.getArmor().entrySet()) {
                    final ItemStack stack = armorSlot.getValue();
                    final float percent = (float)this.repair.getValue() / 100.0f;
                    final int dam = Math.round(stack.getMaxDamage() * percent);
                    if (dam >= stack.getMaxDamage() - stack.getItemDamage()) {
                        continue;
                    }
                    ++takeOff;
                }
                if (takeOff == 4) {
                    this.flag = true;
                }
                if (!this.flag) {
                    final ItemStack itemStack1 = AutoArmor.mc.player.inventoryContainer.getSlot(5).getStack();
                    if (!itemStack1.isEmpty()) {
                        final float percent2 = (float)this.repair.getValue() / 100.0f;
                        final int dam2 = Math.round(itemStack1.getMaxDamage() * percent2);
                        if (dam2 < itemStack1.getMaxDamage() - itemStack1.getItemDamage()) {
                            this.takeOffSlot(5);
                        }
                    }
                    final ItemStack itemStack2 = AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack();
                    if (!itemStack2.isEmpty()) {
                        final float percent3 = (float)this.repair.getValue() / 100.0f;
                        final int dam3 = Math.round(itemStack2.getMaxDamage() * percent3);
                        if (dam3 < itemStack2.getMaxDamage() - itemStack2.getItemDamage()) {
                            this.takeOffSlot(6);
                        }
                    }
                    final ItemStack itemStack3 = AutoArmor.mc.player.inventoryContainer.getSlot(7).getStack();
                    if (!itemStack3.isEmpty()) {
                        final float percent = (float)this.repair.getValue() / 100.0f;
                        final int dam = Math.round(itemStack3.getMaxDamage() * percent);
                        if (dam < itemStack3.getMaxDamage() - itemStack3.getItemDamage()) {
                            this.takeOffSlot(7);
                        }
                    }
                    final ItemStack itemStack4 = AutoArmor.mc.player.inventoryContainer.getSlot(8).getStack();
                    if (!itemStack4.isEmpty()) {
                        final float percent4 = (float)this.repair.getValue() / 100.0f;
                        final int dam4 = Math.round(itemStack4.getMaxDamage() * percent4);
                        if (dam4 < itemStack4.getMaxDamage() - itemStack4.getItemDamage()) {
                            this.takeOffSlot(8);
                        }
                    }
                }
                return;
            }
            this.flag = false;
            final ItemStack helm = AutoArmor.mc.player.inventoryContainer.getSlot(5).getStack();
            final int slot4;
            if (helm.getItem() == Items.AIR && (slot4 = findArmorSlot(EntityEquipmentSlot.HEAD, this.curse.getValue(), true)) != -1) {
                this.getSlotOn(5, slot4);
            }
            final int slot5;
            if (AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack().getItem() == Items.AIR && (slot5 = findArmorSlot(EntityEquipmentSlot.CHEST, this.curse.getValue(), true)) != -1) {
                this.getSlotOn(6, slot5);
            }
            final int slot6;
            if (AutoArmor.mc.player.inventoryContainer.getSlot(7).getStack().getItem() == Items.AIR && (slot6 = findArmorSlot(EntityEquipmentSlot.LEGS, this.curse.getValue(), true)) != -1) {
                this.getSlotOn(7, slot6);
            }
            final int slot7;
            if (AutoArmor.mc.player.inventoryContainer.getSlot(8).getStack().getItem() == Items.AIR && (slot7 = findArmorSlot(EntityEquipmentSlot.FEET, this.curse.getValue(), true)) != -1) {
                this.getSlotOn(8, slot7);
            }
        }
        if (this.timer.passedMs((int)(float)this.delay.getValue())) {
            if (!this.taskList.isEmpty()) {
                for (int i = 0; i < this.actions.getValue(); ++i) {
                    final Task task = this.taskList.poll();
                    if (task != null) {
                        task.run();
                    }
                }
            }
            this.timer.reset();
        }
    }
    
    private boolean holdingItem() {
        final ItemStack stack = AutoArmor.mc.player.getHeldItemMainhand();
        boolean result = this.isInstanceOf(stack);
        if (!result) {
            result = this.isInstanceOf(stack);
        }
        return result;
    }
    
    private boolean isInstanceOf(final ItemStack stack) {
        if (stack == null) {
            return false;
        }
        final Item item = stack.getItem();
        return item instanceof ItemExpBottle;
    }
    
    public static int findArmorSlot(final EntityEquipmentSlot type, final boolean binding, final boolean withXCarry) {
        int slot = findArmorSlot(type, binding);
        if (slot == -1 && withXCarry) {
            float damage = 0.0f;
            for (int i = 1; i < 5; ++i) {
                final Slot craftingSlot = AutoArmor.mc.player.inventoryContainer.inventorySlots.get(i);
                final ItemStack craftingStack = craftingSlot.getStack();
                if (craftingStack.getItem() != Items.AIR && craftingStack.getItem() instanceof ItemArmor) {
                    final ItemArmor armor;
                    if ((armor = (ItemArmor)craftingStack.getItem()).getEquipmentSlot() == type) {
                        final float currentDamage = (float)(armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, craftingStack));
                        final boolean c = binding && EnchantmentHelper.hasBindingCurse(craftingStack);
                        if (currentDamage > damage) {
                            if (!c) {
                                damage = currentDamage;
                                slot = i;
                            }
                        }
                    }
                }
            }
        }
        return slot;
    }
    
    public static int findArmorSlot(final EntityEquipmentSlot type, final boolean binding) {
        int slot = -1;
        float damage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            final ItemStack s = AutoArmor.mc.player.inventoryContainer.getSlot(i).getStack();
            if (s.getItem() != Items.AIR && s.getItem() instanceof ItemArmor) {
                final ItemArmor armor;
                if ((armor = (ItemArmor)s.getItem()).getEquipmentSlot() == type) {
                    final float currentDamage = (float)(armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, s));
                    final boolean c = binding && EnchantmentHelper.hasBindingCurse(s);
                    if (currentDamage > damage) {
                        if (!c) {
                            damage = currentDamage;
                            slot = i;
                        }
                    }
                }
            }
        }
        return slot;
    }
    
    private void takeOffSlot(final int slot) {
        if (this.taskList.isEmpty()) {
            int target = -1;
            for (final int i : this.findEmptySlots()) {
                if (this.doneSlots.contains(target)) {
                    continue;
                }
                target = i;
                this.doneSlots.add(i);
            }
            if (target != -1) {
                this.taskList.add(new Task(slot));
                this.taskList.add(new Task(target));
                this.taskList.add(new Task());
            }
        }
    }
    
    private void getSlotOn(final int slot, final int target) {
        if (this.taskList.isEmpty()) {
            this.doneSlots.remove((Object)target);
            this.taskList.add(new Task(target));
            this.taskList.add(new Task(slot));
            this.taskList.add(new Task());
        }
    }
    
    private Map<Integer, ItemStack> getArmor() {
        return this.getInventorySlots(5, 8);
    }
    
    private List<Integer> findEmptySlots() {
        final ArrayList<Integer> outPut = new ArrayList<Integer>();
        for (final Map.Entry<Integer, ItemStack> entry : this.getInventoryAndHotbarSlots().entrySet()) {
            if (!entry.getValue().isEmpty() && entry.getValue().getItem() != Items.AIR) {
                continue;
            }
            outPut.add(entry.getKey());
        }
        for (int i = 1; i < 5; ++i) {
            final Slot craftingSlot = AutoArmor.mc.player.inventoryContainer.inventorySlots.get(i);
            final ItemStack craftingStack = craftingSlot.getStack();
            if (craftingStack.isEmpty() || craftingStack.getItem() == Items.AIR) {
                outPut.add(i);
            }
        }
        return outPut;
    }
    
    private Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        return this.getInventorySlots(9, 44);
    }
    
    private Map<Integer, ItemStack> getInventorySlots(final int currentI, final int last) {
        final HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = currentI; current <= last; ++current) {
            fullInventorySlots.put(current, (ItemStack)AutoArmor.mc.player.inventoryContainer.getInventory().get(current));
        }
        return fullInventorySlots;
    }
    
    private static class Timer
    {
        private long time;
        
        private Timer() {
            this.time = -1L;
        }
        
        public boolean passedMs(final long ms) {
            return this.getMs(System.nanoTime() - this.time) >= ms;
        }
        
        public long getPassedTimeMs() {
            return this.getMs(System.nanoTime() - this.time);
        }
        
        public void reset() {
            this.time = System.nanoTime();
        }
        
        public long getMs(final long time) {
            return time / 1000000L;
        }
    }
    
    private static class Task
    {
        private final int slot;
        private final boolean update;
        private final boolean quickClick;
        
        public Task() {
            this.update = true;
            this.slot = -1;
            this.quickClick = false;
        }
        
        public Task(final int slot) {
            this.slot = slot;
            this.quickClick = false;
            this.update = false;
        }
        
        public void run() {
            if (this.update) {
                MinecraftInstance.mc.playerController.updateController();
            }
            if (this.slot != -1) {
                MinecraftInstance.mc.playerController.windowClick(0, this.slot, 0, this.quickClick ? ClickType.QUICK_MOVE : ClickType.PICKUP, (EntityPlayer)MinecraftInstance.mc.player);
            }
        }
    }
}
