// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.gui.main;

import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.glu.Project;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class MainMenu extends GuiScreen
{
    private final List<CustomButton> buttons;
    private float panoramaTimer;
    private final ResourceLocation MINECRAFT_TITLE_TEXTURES;
    private ResourceLocation backgroundTexture;
    private static final ResourceLocation[] TITLE_PANORAMA_PATHS;
    private int showingTime;
    
    public MainMenu() {
        this.buttons = new ArrayList<CustomButton>();
        this.MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
    }
    
    public void initGui() {
        this.showingTime = 0;
        this.buttons.clear();
        this.buttons.add(new CustomButton(0, this.width / 2 - 75, this.height / 2 - 25, 150, 20, "Singleplayer"));
        this.buttons.add(new CustomButton(1, this.width / 2 - 75, this.height / 2, 150, 20, "Multiplayer"));
        this.buttons.add(new CustomButton(2, this.width / 2 - 75, this.height / 2 + 25, 72, 20, "Options"));
        this.buttons.add(new CustomButton(3, this.width / 2 + 3, this.height / 2 + 25, 72, 20, "Quit"));
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", new DynamicTexture(256, 256));
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        ++this.showingTime;
        this.renderBackground(partialTicks);
        Gui.drawRect(this.width / 2 - 100, this.height / 2 - 140, this.width / 2 - 100 + 200, this.height / 2 - 140 + 72, new Color(1711276032, true).getRGB());
        GL11.glPushMatrix();
        GL11.glScalef(6.0f, 6.0f, 0.0f);
        this.mc.fontRenderer.drawStringWithShadow("XANAX", (float)((this.width / 2 - 100 + 15) / 6), (float)((this.height / 2 - 140 + 15) / 6), -1);
        GL11.glPopMatrix();
        for (final CustomButton button : this.buttons) {
            button.drawButton(this.mc, mouseX, mouseY);
        }
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0) {
            for (final CustomButton button : this.buttons) {
                if (this.isHover(button.x, button.y, button.buttonWidth, button.buttonHeight, mouseX, mouseY)) {
                    switch (button.id) {
                        case 0: {
                            this.mc.displayGuiScreen((GuiScreen)new GuiWorldSelection((GuiScreen)this));
                            continue;
                        }
                        case 1: {
                            this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
                            continue;
                        }
                        case 2: {
                            this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
                            continue;
                        }
                        case 3: {
                            this.mc.shutdown();
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    private void renderBackground(final float partialTicks) {
        this.panoramaTimer += partialTicks;
        GlStateManager.disableAlpha();
        this.renderSkybox();
        GlStateManager.enableAlpha();
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        this.mc.getTextureManager().bindTexture(this.MINECRAFT_TITLE_TEXTURES);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderSkybox() {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama();
        for (int i = 0; i < 7; ++i) {
            this.rotateAndBlurSkybox();
        }
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        final float f = 120.0f / Math.max(this.width, this.height);
        final float f2 = this.height * f / 256.0f;
        final float f3 = this.width * f / 256.0f;
        final int j = this.width;
        final int k = this.height;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0, (double)k, (double)this.zLevel).tex((double)(0.5f - f2), (double)(0.5f + f3)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos((double)j, (double)k, (double)this.zLevel).tex((double)(0.5f - f2), (double)(0.5f - f3)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos((double)j, 0.0, (double)this.zLevel).tex((double)(0.5f + f2), (double)(0.5f - f3)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos(0.0, 0.0, (double)this.zLevel).tex((double)(0.5f + f2), (double)(0.5f + f3)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        tessellator.draw();
    }
    
    private void rotateAndBlurSkybox() {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GlStateManager.glTexParameteri(3553, 10241, 9729);
        GlStateManager.glTexParameteri(3553, 10240, 9729);
        GlStateManager.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.colorMask(true, true, true, false);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        for (int j = 0; j < 3; ++j) {
            final float f = 1.0f / (j + 1);
            final int k = this.width;
            final int l = this.height;
            final float f2 = (j - 1) / 256.0f;
            bufferbuilder.pos((double)k, (double)l, (double)this.zLevel).tex((double)(0.0f + f2), 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            bufferbuilder.pos((double)k, 0.0, (double)this.zLevel).tex((double)(1.0f + f2), 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            bufferbuilder.pos(0.0, 0.0, (double)this.zLevel).tex((double)(1.0f + f2), 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            bufferbuilder.pos(0.0, (double)l, (double)this.zLevel).tex((double)(0.0f + f2), 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }
    
    private void drawPanorama() {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0f, 1.0f, 0.05f, 10.0f);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        for (int j = 0; j < 64; ++j) {
            GlStateManager.pushMatrix();
            final float f = (j % 8 / 8.0f - 0.5f) / 64.0f;
            final float f2 = (j / 8 / 8.0f - 0.5f) / 64.0f;
            GlStateManager.translate(f, f2, 0.0f);
            GlStateManager.rotate(MathHelper.sin(this.panoramaTimer / 400.0f) * 25.0f + 20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-this.panoramaTimer * 0.1f, 0.0f, 1.0f, 0.0f);
            for (int k = 0; k < 6; ++k) {
                GlStateManager.pushMatrix();
                if (k == 1) {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (k == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                if (k == 3) {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (k == 4) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (k == 5) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                this.mc.getTextureManager().bindTexture(MainMenu.TITLE_PANORAMA_PATHS[k]);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                final int l = 255 / (j + 1);
                bufferbuilder.pos(-1.0, -1.0, 1.0).tex(0.0, 0.0).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(1.0, -1.0, 1.0).tex(1.0, 0.0).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(-1.0, 1.0, 1.0).tex(0.0, 1.0).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }
        bufferbuilder.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }
    
    private boolean isHover(final int X, final int Y, final int W, final int H, final int mX, final int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
    
    static {
        TITLE_PANORAMA_PATHS = new ResourceLocation[] { new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png") };
    }
}
