package cat.yoink.xanax.main.module.modules.render;

import cat.yoink.eventmanager.Listener;
import cat.yoink.xanax.main.event.events.Render3DEvent;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.setting.BooleanSetting;
import cat.yoink.xanax.main.setting.EnumSetting;
import cat.yoink.xanax.main.setting.NumberSetting;
import cat.yoink.xanax.main.util.RenderUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

public final class BlockHighlight extends Module
{
    private final NumberSetting red = addSetting(new NumberSetting("Red", 50, 0, 255, 1));
    private final EnumSetting mode = addSetting(new EnumSetting("Mode", "Specific", "Specific", "Full"));
    private final NumberSetting green = addSetting(new NumberSetting("Green", 50, 0, 255, 1));
    private final BooleanSetting box = addSetting(new BooleanSetting("Box", true));
    private final NumberSetting blue = addSetting(new NumberSetting("Blue", 50, 0, 255, 1));
    private final BooleanSetting outline = addSetting(new BooleanSetting("Outline", true));
    private final NumberSetting alpha = addSetting(new NumberSetting("Alpha", 100, 0, 255, 1));

    public BlockHighlight()
    {
        super("BlockESP", Category.RENDER);
    }

    @Listener
    public void render3D(final Render3DEvent event)
    {
        final RayTraceResult result = mc.objectMouseOver;

        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            final BlockPos blockPos = result.getBlockPos();

            if (this.mode.is("Specific"))
            {
                AxisAlignedBB box = mc.world.getBlockState(blockPos).getBoundingBox(mc.world, blockPos).offset(blockPos);
                box = RenderUtil.convertBox(box);
                RenderUtil.drawBox(box, (int) this.red.getValue(), (int) this.green.getValue(), (int) this.blue.getValue(), (int) this.alpha.getValue(), this.box.getValue(), this.outline.getValue());
            }
            else
            {
                RenderUtil.drawBox(blockPos, new Color((int) this.red.getValue(), (int) this.green.getValue(), (int) this.blue.getValue(), (int) this.alpha.getValue()), this.box.getValue(), this.outline.getValue());
            }
        }
    }
}
