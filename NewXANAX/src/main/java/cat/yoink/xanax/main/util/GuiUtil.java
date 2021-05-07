package cat.yoink.xanax.main.util;

import cat.yoink.xanax.main.MinecraftInstance;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public final class GuiUtil implements MinecraftInstance
{
    public static void drawRect(final int x, final int y, final int w, final int h, final int c)
    {
        Gui.drawRect(x, y, x + w, y + h, c);
    }

    public static void drawRect(final int x, final int y, final int w, final int h, final int c, final boolean outline, final int outlineC)
    {
        if (outline) drawRect(x - 1, y - 1, w + 2, h + 2, outlineC);
        drawRect(x, y, w, h, c);
    }

    public static void drawSmoothRect(final int x, final int y, final int w, final int h, final int smoothness, final int c)
    {
        final Color color = new Color(c);
        GuiUtil.drawRegularPolygon(x + smoothness, y + smoothness, smoothness, smoothness * 5, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GuiUtil.drawRegularPolygon(x + w - smoothness, y + smoothness, smoothness, smoothness * 5, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GuiUtil.drawRegularPolygon(x + smoothness, y + h - smoothness, smoothness, smoothness * 5, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GuiUtil.drawRegularPolygon(x + w - smoothness, y + h - smoothness, smoothness, smoothness * 5, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        GuiUtil.drawRect(x + smoothness, y, w - (smoothness * 2), h, c);
        GuiUtil.drawRect(x, y + smoothness, w, h - (smoothness * 2), c);
    }

    public static void drawSmoothRect(final int x, final int y, final int w, final int h, final int smoothness, final int c, final boolean outline, final int outlineC)
    {
        if (outline) drawSmoothRect(x - 1, y - 1, w + 2, h + 2, smoothness, outlineC);
        drawSmoothRect(x, y, w, h, smoothness, c);
    }

    public static void drawRegularPolygon(final double x, final double y, final int radius, final int sides, final int r, final int g, final int b, final int a)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(r / 255f, g / 255f, b / 255f, a / 255f);

        Tessellator.getInstance().getBuffer().begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
        Tessellator.getInstance().getBuffer().pos(x, y, 0).endVertex();

        for (int i = 0; i <= sides; i++)
        {
            final double angle = ((Math.PI * 2) * i / sides) + Math.toRadians(180);
            Tessellator.getInstance().getBuffer().pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0).endVertex();
        }
        Tessellator.getInstance().draw();

        GlStateManager.color(1, 1, 1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static boolean isHover(final int X, final int Y, final int W, final int H, final int mX, final int mY)
    {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
}
