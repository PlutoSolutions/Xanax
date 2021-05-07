package cat.yoink.xanax.main.module.modules.world;

import cat.yoink.xanax.main.event.events.DamageBlockEvent;
import cat.yoink.xanax.main.event.events.Render3DEvent;
import cat.yoink.xanax.main.event.events.TickEvent;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.setting.BooleanSetting;
import cat.yoink.xanax.main.setting.EnumSetting;
import cat.yoink.xanax.main.setting.NumberSetting;
import cat.yoink.xanax.main.util.RenderUtil;
import cat.yoink.xanax.main.util.WorldUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import cat.yoink.eventmanager.Listener;

import java.awt.*;

public final class PacketMine extends Module
{
    private final NumberSetting red = addSetting(new NumberSetting("Red", 200, 0, 255, 1));
    private final EnumSetting render = addSetting(new EnumSetting("Render", "Specific", "Off", "Full", "Specific"));
    private final NumberSetting green = addSetting(new NumberSetting("Green", 10, 0, 255, 1));
    private final BooleanSetting box = addSetting(new BooleanSetting("Box", true));
    private final NumberSetting blue = addSetting(new NumberSetting("Blue", 10, 0, 255, 1));
    private final BooleanSetting outline = addSetting(new BooleanSetting("Outline", true));
    private final NumberSetting alpha = addSetting(new NumberSetting("Alpha", 100, 0, 255, 1));
    private final BooleanSetting change = addSetting(new BooleanSetting("Change", false));
    private final BooleanSetting noBreak = addSetting(new BooleanSetting("NoBreak", false));
    private final BooleanSetting swing = addSetting(new BooleanSetting("Swing", false));
    private final NumberSetting time = addSetting(new NumberSetting("Time", 300, 100, 1000, 10));
    private BlockPos breakBlock;
    private int miningTicks;

    public PacketMine()
    {
        super("PacketMine", Category.WORLD);
    }

    @Listener
    public void leftClickBlockEvent(final DamageBlockEvent event)
    {
        if (WorldUtil.isBreakable(event.getPos()))
        {
            if (this.swing.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));

            if (this.render.is("Full") || this.render.is("Specific")) this.breakBlock = event.getPos();
            if (this.noBreak.getValue()) event.setCancelled(true);
        }
    }

    @Listener
    public void tickEvent(final TickEvent event)
    {
        if (this.breakBlock != null) this.miningTicks++;
        if (this.breakBlock != null && this.miningTicks > this.time.getValue())
        {
            this.miningTicks = 0;
            this.breakBlock = null;
        }
    }

    @Listener
    public void render(final Render3DEvent event)
    {
        if (this.breakBlock != null && mc.world.getBlockState(this.breakBlock).getBlock() == Blocks.AIR)
        {
            this.breakBlock = null;
            this.miningTicks = 0;
        }
        else if (this.breakBlock != null && this.miningTicks != 0)
        {
            final Color c;
            if (this.change.getValue())
            {
                if (this.miningTicks < 50) c = new Color(200, 10, 10, 150);
                else c = new Color(10, 200, 10, 150);
            }
            else
            {
                c = new Color((int) this.red.getValue() / 255f, (int) this.green.getValue() / 255f, (int) this.blue.getValue() / 255f, (int) this.alpha.getValue() / 255f);
            }

            if (this.render.is("Specific"))
            {
                final AxisAlignedBB bb = RenderUtil.convertBox(mc.world.getBlockState(this.breakBlock).getBoundingBox(mc.world, this.breakBlock).offset(this.breakBlock));
                RenderUtil.drawBox(bb, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), this.box.getValue(), this.outline.getValue());
            }
            else
            {
                RenderUtil.drawBox(this.breakBlock, c, this.box.getValue(), this.outline.getValue());
            }
        }
    }
}
