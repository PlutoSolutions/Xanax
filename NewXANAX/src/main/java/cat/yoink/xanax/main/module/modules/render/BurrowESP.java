package cat.yoink.xanax.main.module.modules.render;

import cat.yoink.eventmanager.Listener;
import cat.yoink.xanax.main.event.events.Render3DEvent;
import cat.yoink.xanax.main.event.events.TickEvent;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.setting.BooleanSetting;
import cat.yoink.xanax.main.setting.NumberSetting;
import cat.yoink.xanax.main.util.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class BurrowESP extends Module
{
    private final NumberSetting red = addSetting(new NumberSetting("Red", 50, 0, 255, 1));
    private final BooleanSetting box = addSetting(new BooleanSetting("Box", true));
    private final NumberSetting green = addSetting(new NumberSetting("Green", 50, 0, 255, 1));
    private final BooleanSetting outline = addSetting(new BooleanSetting("Outline", true));
    private final NumberSetting blue = addSetting(new NumberSetting("Blue", 50, 0, 255, 1));
    private final NumberSetting alpha = addSetting(new NumberSetting("Alpha", 100, 0, 255, 1));

    private final List<BlockPos> posList = new ArrayList<>();

    public BurrowESP()
    {
        super("BurrowESP", Category.RENDER);
    }

    @Listener
    public void onTick(TickEvent event)
    {
        posList.clear();

        for (EntityPlayer player : mc.world.playerEntities)
        {
            BlockPos blockPos = new BlockPos(player.posX, player.posY, player.posZ);
            if (mc.world.getBlockState(blockPos).getBlock().equals(Blocks.OBSIDIAN))
            {
                posList.add(blockPos);
            }
        }
    }

    @Listener
    public void onRender(Render3DEvent event)
    {
        for (BlockPos blockPos : posList)
        {
            RenderUtil.drawBox(blockPos, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), (int) alpha.getValue()), box.getValue(), outline.getValue());
        }
    }
}
