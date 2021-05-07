// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.world;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import cat.yoink.xanax.client.util.RenderUtil;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import cat.yoink.xanax.client.util.WorldUtil;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cat.yoink.xanax.client.module.Category;
import net.minecraft.util.math.BlockPos;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.module.Module;

public class PacketMine extends Module
{
    private final BooleanSetting render;
    private final BooleanSetting box;
    private final BooleanSetting fill;
    private final NumberSetting red;
    private final NumberSetting green;
    private final NumberSetting blue;
    private final NumberSetting alpha;
    private int timer;
    private BlockPos renderBlock;
    
    public PacketMine() {
        super("PacketMine", Category.WORLD);
        this.render = this.newSetting(new BooleanSetting("Render", true));
        this.box = this.newSetting(new BooleanSetting("Box", true));
        this.fill = this.newSetting(new BooleanSetting("Fill", true));
        this.red = this.newSetting(new NumberSetting("Red", 0.0, 0.0, 255.0, 1.0));
        this.green = this.newSetting(new NumberSetting("Green", 100.0, 0.0, 255.0, 1.0));
        this.blue = this.newSetting(new NumberSetting("Blue", 0.0, 0.0, 255.0, 1.0));
        this.alpha = this.newSetting(new NumberSetting("Alpha", 50.0, 0.0, 255.0, 1.0));
        this.timer = -1;
        this.renderBlock = null;
    }
    
    @SubscribeEvent
    public void onPlayerInteractLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
        if (this.nullCheck()) {
            return;
        }
        if (WorldUtil.canBreak(event.getPos()) && event.getFace() != null) {
            PacketMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
            PacketMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
            if (this.renderBlock == null) {
                this.renderBlock = event.getPos();
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (this.nullCheck() || this.timer <= 0 || this.renderBlock == null || !this.render.getValue()) {
            return;
        }
        RenderUtil.glSetup();
        final AxisAlignedBB bb = new AxisAlignedBB(this.renderBlock.getX() - PacketMine.mc.getRenderManager().viewerPosX, this.renderBlock.getY() - PacketMine.mc.getRenderManager().viewerPosY, this.renderBlock.getZ() - PacketMine.mc.getRenderManager().viewerPosZ, this.renderBlock.getX() + 1 - PacketMine.mc.getRenderManager().viewerPosX, this.renderBlock.getY() + 1 - PacketMine.mc.getRenderManager().viewerPosY, this.renderBlock.getZ() + 1 - PacketMine.mc.getRenderManager().viewerPosZ);
        if (this.fill.getValue()) {
            RenderGlobal.renderFilledBox(bb, (float)this.red.getValue() / 100.0f, (float)this.green.getValue() / 100.0f, (float)this.blue.getValue() / 100.0f, (float)this.alpha.getValue() / 100.0f);
        }
        if (this.box.getValue()) {
            RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, (float)this.red.getValue() / 100.0f, (float)this.green.getValue() / 100.0f, (float)this.blue.getValue() / 100.0f, ((float)this.alpha.getValue() / 100.0f + 0.3f > 1.0f) ? 1.0f : ((float)this.alpha.getValue() / 100.0f + 0.3f));
        }
        RenderUtil.glCleanup();
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.renderBlock != null && PacketMine.mc.world.getBlockState(this.renderBlock).getBlock() == Blocks.AIR) {
            this.renderBlock = null;
        }
        if (this.renderBlock != null && this.timer == -1) {
            this.timer = 130;
        }
        if (this.timer > 0) {
            --this.timer;
        }
        if (this.timer == 0) {
            this.timer = -1;
            this.renderBlock = null;
        }
    }
}
