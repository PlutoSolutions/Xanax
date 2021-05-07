package cat.yoink.xanax.main.module.modules.render;

import cat.yoink.xanax.main.event.events.Render3DEvent;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.setting.BooleanSetting;
import cat.yoink.xanax.main.setting.EnumSetting;
import cat.yoink.xanax.main.setting.NumberSetting;
import cat.yoink.xanax.main.util.RenderUtil;
import cat.yoink.xanax.main.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import cat.yoink.eventmanager.Listener;

import java.awt.*;

public final class FeetHighlight extends Module
{
    private final NumberSetting red = addSetting(new NumberSetting("Red", 50, 0, 255, 1));
    private final EnumSetting mode = addSetting(new EnumSetting("Mode", "OutHole", "OutHole", "InHole", "Always"));
    private final NumberSetting green = addSetting(new NumberSetting("Green", 50, 0, 255, 1));
    private final BooleanSetting self = addSetting(new BooleanSetting("Self", false));
    private final NumberSetting blue = addSetting(new NumberSetting("Blue", 50, 0, 255, 1));
    private final BooleanSetting box = addSetting(new BooleanSetting("Box", true));
    private final NumberSetting alpha = addSetting(new NumberSetting("Alpha", 100, 0, 255, 1));
    private final BooleanSetting outline = addSetting(new BooleanSetting("Outline", true));

    public FeetHighlight()
    {
        super("FeetESP", Category.RENDER);
    }

    @Listener
    public void render(final Render3DEvent event)
    {
        for (final EntityPlayer player : mc.world.playerEntities)
        {
            if (!this.self.getValue() && player.equals(mc.player)) continue;

            if (this.mode.is("InHole"))
            {
                if (WorldUtil.isInHole(player))
                    RenderUtil.drawBox(new BlockPos(player.posX, player.posY, player.posZ), new Color((int) this.red.getValue(), (int) this.green.getValue(), (int) this.blue.getValue(), (int) this.alpha.getValue()), this.box.getValue(), this.outline.getValue());
            }
            else if (this.mode.is("OutHole"))
            {
                if (!WorldUtil.isInHole(player))
                    RenderUtil.drawBox(new BlockPos(player.posX, player.posY, player.posZ), new Color((int) this.red.getValue(), (int) this.green.getValue(), (int) this.blue.getValue(), (int) this.alpha.getValue()), this.box.getValue(), this.outline.getValue());
            }
            else
            {
                RenderUtil.drawBox(new BlockPos(player.posX, player.posY, player.posZ), new Color((int) this.red.getValue(), (int) this.green.getValue(), (int) this.blue.getValue(), (int) this.alpha.getValue()), this.box.getValue(), this.outline.getValue());
            }
        }
    }
}
