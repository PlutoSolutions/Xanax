package cat.yoink.xanax.main.module.modules.misc;

import cat.yoink.xanax.main.event.events.ClickEvent;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.setting.EnumSetting;
import cat.yoink.xanax.main.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Mouse;
import cat.yoink.eventmanager.Listener;

public final class MiddleClick extends Module
{// TODO: 11/20/2020 add friend option
    private final EnumSetting mode = addSetting(new EnumSetting("Mode", /*"Smart", "Smart", "Friend", */"Pearl", "Pearl"));

    public MiddleClick()
    {
        super("MiddleClick", Category.MISC);
    }

    @Listener
    public void onClick(final ClickEvent event)
    {
        if (Mouse.getEventButtonState() && Mouse.getEventButton() == 2)
        {
            throwPearl();
        }
    }

    private void throwPearl()
    {
        final int pearlSlot = InventoryUtil.getHotbarSlot(Items.ENDER_PEARL);
        if (pearlSlot != -1)
        {
            final int slot = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = pearlSlot;
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
            mc.player.inventory.currentItem = slot;
        }
    }
}
