package cat.yoink.xanax.main.util;

import cat.yoink.xanax.main.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public final class InventoryUtil implements MinecraftInstance
{
    public static int getHotbarSlot(final Item item)
    {
        for (int i = 0; i < 9; i++)
        {
            final Item item1 = mc.player.inventory.getStackInSlot(i).getItem();

            if (item.equals(item1)) return i;
        }
        return -1;
    }

    public static int getHotbarSlot(final Block block)
    {
        for (int i = 0; i < 9; i++)
        {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();

            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(block)) return i;
        }

        return -1;
    }
}
