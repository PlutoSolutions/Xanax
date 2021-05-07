package cat.yoink.xanax.main.module.modules.render;

import cat.yoink.xanax.main.event.events.TickEvent;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;
import net.minecraft.util.EnumHand;
import cat.yoink.eventmanager.Listener;

public final class Animations extends Module
{
    public Animations()
    {
        super("Animations", Category.RENDER);
    }

    @Listener
    public void onTickClientTick(final TickEvent event)
    {
        if (mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9)
        {
            mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1;
            mc.entityRenderer.itemRenderer.itemStackMainHand = mc.player.getHeldItem(EnumHand.MAIN_HAND);
        }
        if (mc.entityRenderer.itemRenderer.prevEquippedProgressOffHand >= 0.9)
        {
            mc.entityRenderer.itemRenderer.equippedProgressOffHand = 1;
            mc.entityRenderer.itemRenderer.itemStackOffHand = mc.player.getHeldItem(EnumHand.OFF_HAND);
        }
    }
}
