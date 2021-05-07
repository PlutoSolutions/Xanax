// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.util;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import cat.yoink.xanax.client.MinecraftInstance;

public class RenderUtil implements MinecraftInstance
{
    public static void drawBox(final AxisAlignedBB box, final float r, final float g, final float b, final float a) {
        try {
            glSetup();
            RenderGlobal.renderFilledBox(box, r, g, b, a);
            RenderGlobal.drawSelectionBoundingBox(box, r, g, b, a * 1.5f);
            glCleanup();
        }
        catch (Exception ex) {}
    }
    
    public static void drawBoxFromBlockpos(final BlockPos blockPos, final float r, final float g, final float b, final float a) {
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, blockPos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, blockPos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, blockPos.getX() + 1 - RenderUtil.mc.getRenderManager().viewerPosX, blockPos.getY() + 1 - RenderUtil.mc.getRenderManager().viewerPosY, blockPos.getZ() + 1 - RenderUtil.mc.getRenderManager().viewerPosZ);
        drawBox(axisAlignedBB, r, g, b, a);
    }
    
    public static void glSetup() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(1.5f);
    }
    
    public static void glCleanup() {
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void drawRect(final int x, final int y, final int w, final int h, final int color) {
        Gui.drawRect(x, y, x + w, y + h, color);
    }
    
    public static void drawRegularPolygon(final double x, final double y, final int radius, final int sides, final float r, final float g, final float b, final float a) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GlStateManager.color(r, g, b, a);
        Tessellator.getInstance().getBuffer().begin(6, DefaultVertexFormats.POSITION);
        Tessellator.getInstance().getBuffer().pos(x, y, 0.0).endVertex();
        for (int i = 0; i <= sides; ++i) {
            final double angle = 6.283185307179586 * i / sides + Math.toRadians(180.0);
            Tessellator.getInstance().getBuffer().pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0.0).endVertex();
        }
        Tessellator.getInstance().draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
}
