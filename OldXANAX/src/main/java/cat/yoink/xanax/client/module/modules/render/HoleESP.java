// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.render;

import net.minecraft.client.renderer.RenderGlobal;
import java.util.Iterator;
import cat.yoink.xanax.client.util.RenderUtil;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.client.renderer.culling.Frustum;
import java.util.ArrayList;
import cat.yoink.xanax.client.module.Category;
import net.minecraft.client.renderer.culling.ICamera;
import java.util.List;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.module.Module;

public class HoleESP extends Module
{
    private final NumberSetting range;
    private final NumberSetting holeHeight;
    private final BooleanSetting box;
    private final BooleanSetting fill;
    private final NumberSetting obsidianRed;
    private final NumberSetting obsidianGreen;
    private final NumberSetting obsidianBlue;
    private final NumberSetting obsidianAlpha;
    private final NumberSetting bedrockRed;
    private final NumberSetting bedrockGreen;
    private final NumberSetting bedrockBlue;
    private final NumberSetting bedrockAlpha;
    private final List<Hole> holes;
    private final ICamera camera;
    
    public HoleESP() {
        super("HoleESP", Category.RENDER);
        this.range = this.newSetting(new NumberSetting("Range", 8.0, 1.0, 30.0, 1.0));
        this.holeHeight = this.newSetting(new NumberSetting("HoleHeight", 0.1, -2.0, 2.0, 0.1));
        this.box = this.newSetting(new BooleanSetting("Box", true));
        this.fill = this.newSetting(new BooleanSetting("Fill", true));
        this.obsidianRed = this.newSetting(new NumberSetting("ObbyRed", 100.0, 0.0, 100.0, 1.0));
        this.obsidianGreen = this.newSetting(new NumberSetting("ObbyGreen", 0.0, 0.0, 100.0, 1.0));
        this.obsidianBlue = this.newSetting(new NumberSetting("ObbyBlue", 0.0, 0.0, 100.0, 1.0));
        this.obsidianAlpha = this.newSetting(new NumberSetting("ObbyAlpha", 0.0, 0.0, 100.0, 1.0));
        this.bedrockRed = this.newSetting(new NumberSetting("BedrockRed", 0.0, 0.0, 100.0, 1.0));
        this.bedrockGreen = this.newSetting(new NumberSetting("BedrockGreen", 100.0, 0.0, 100.0, 1.0));
        this.bedrockBlue = this.newSetting(new NumberSetting("BedrockBlue", 0.0, 0.0, 100.0, 1.0));
        this.bedrockAlpha = this.newSetting(new NumberSetting("BedrockAlpha", 0.0, 0.0, 100.0, 1.0));
        this.holes = new ArrayList<Hole>();
        this.camera = (ICamera)new Frustum();
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent tickEvent) {
        if (HoleESP.mc.player == null || HoleESP.mc.world == null) {
            return;
        }
        this.holes.clear();
        final Vec3i playerPos = new Vec3i(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ);
        for (int x = (int)(playerPos.getX() - this.range.getValue()); x < playerPos.getX() + this.range.getValue(); ++x) {
            for (int z = (int)(playerPos.getZ() - this.range.getValue()); z < playerPos.getZ() + this.range.getValue(); ++z) {
                for (int y = (int)(playerPos.getY() + this.range.getValue()); y > playerPos.getY() - this.range.getValue(); --y) {
                    final BlockPos blockPos = new BlockPos(x, y, z);
                    final IBlockState blockState = HoleESP.mc.world.getBlockState(blockPos);
                    final IBlockState downBlockState = HoleESP.mc.world.getBlockState(blockPos.down());
                    Hole.HoleTypes holeTypes = this.isBlockValid(blockState, blockPos);
                    if (downBlockState.getBlock() == Blocks.AIR) {
                        final BlockPos downPos = blockPos.down();
                        holeTypes = this.isBlockValid(downBlockState, blockPos);
                        this.holes.add(new Hole(downPos.getX(), downPos.getY(), downPos.getZ(), holeTypes));
                    }
                    else {
                        this.holes.add(new Hole(blockPos.getX(), blockPos.getY(), blockPos.getZ(), holeTypes));
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void renderWorldEvent(final RenderWorldLastEvent event) {
        if (HoleESP.mc.player == null || HoleESP.mc.world == null) {
            return;
        }
        if (HoleESP.mc.getRenderManager().options == null) {
            return;
        }
        for (final Hole hole : this.holes) {
            final AxisAlignedBB bb = new AxisAlignedBB(hole.getX() - HoleESP.mc.getRenderManager().viewerPosX, hole.getY() - HoleESP.mc.getRenderManager().viewerPosY, hole.getZ() - HoleESP.mc.getRenderManager().viewerPosZ, hole.getX() + 1 - HoleESP.mc.getRenderManager().viewerPosX, hole.getY() + this.holeHeight.getValue() - HoleESP.mc.getRenderManager().viewerPosY, hole.getZ() + 1 - HoleESP.mc.getRenderManager().viewerPosZ);
            this.camera.setPosition(Objects.requireNonNull(HoleESP.mc.getRenderViewEntity()).posX, HoleESP.mc.getRenderViewEntity().posY, HoleESP.mc.getRenderViewEntity().posZ);
            if (this.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + HoleESP.mc.getRenderManager().viewerPosX, bb.minY + HoleESP.mc.getRenderManager().viewerPosY, bb.minZ + HoleESP.mc.getRenderManager().viewerPosZ, bb.maxX + HoleESP.mc.getRenderManager().viewerPosX, bb.maxY + HoleESP.mc.getRenderManager().viewerPosY, bb.maxZ + HoleESP.mc.getRenderManager().viewerPosZ))) {
                RenderUtil.glSetup();
                if (hole.getHoleTypes() == Hole.HoleTypes.Bedrock) {
                    this.render(bb, (float)this.bedrockRed.getValue() / 100.0f, (float)this.bedrockGreen.getValue() / 100.0f, (float)this.bedrockBlue.getValue() / 100.0f, (float)this.bedrockAlpha.getValue() / 100.0f);
                }
                else if (hole.getHoleTypes() == Hole.HoleTypes.Obsidian) {
                    this.render(bb, (float)this.obsidianRed.getValue() / 100.0f, (float)this.obsidianGreen.getValue() / 100.0f, (float)this.obsidianBlue.getValue() / 100.0f, (float)this.obsidianAlpha.getValue() / 100.0f);
                }
                RenderUtil.glCleanup();
            }
        }
    }
    
    public void render(final AxisAlignedBB bb, final float r, final float g, final float b, final float a) {
        if (this.fill.getValue()) {
            RenderGlobal.renderFilledBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, r, g, b, a);
        }
        if (this.box.getValue()) {
            RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, r, g, b, (a + 0.3f > 1.0f) ? 1.0f : (a + 0.3f));
        }
    }
    
    public Hole.HoleTypes isBlockValid(final IBlockState blockState, final BlockPos blockPos) {
        if (blockState.getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(blockPos.up()).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(blockPos.up(2)).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(blockPos.down()).getBlock() == Blocks.AIR) {
            return Hole.HoleTypes.None;
        }
        final BlockPos[] touchingBlocks = { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west() };
        boolean bedrock = true;
        boolean obsidian = true;
        int validHorizontalBlocks = 0;
        for (final BlockPos touching : touchingBlocks) {
            final IBlockState touchingState = HoleESP.mc.world.getBlockState(touching);
            if (touchingState.getBlock() != Blocks.AIR && touchingState.isFullBlock()) {
                ++validHorizontalBlocks;
                if (touchingState.getBlock() != Blocks.BEDROCK && bedrock) {
                    bedrock = false;
                }
                if (!bedrock && touchingState.getBlock() != Blocks.OBSIDIAN && touchingState.getBlock() != Blocks.BEDROCK) {
                    obsidian = false;
                }
            }
        }
        if (validHorizontalBlocks < 4) {
            return Hole.HoleTypes.None;
        }
        if (bedrock) {
            return Hole.HoleTypes.Bedrock;
        }
        if (obsidian) {
            return Hole.HoleTypes.Obsidian;
        }
        return Hole.HoleTypes.Normal;
    }
    
    private static class Hole extends Vec3i
    {
        private final HoleTypes holeTypes;
        
        public Hole(final int x, final int y, final int z, final HoleTypes type) {
            super(x, y, z);
            this.holeTypes = type;
        }
        
        public HoleTypes getHoleTypes() {
            return this.holeTypes;
        }
        
        public enum HoleTypes
        {
            None, 
            Normal, 
            Obsidian, 
            Bedrock;
        }
    }
}
