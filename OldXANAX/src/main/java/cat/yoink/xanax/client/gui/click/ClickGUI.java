// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.gui.click;

import java.awt.Color;
import cat.yoink.xanax.client.util.RenderUtil;
import cat.yoink.xanax.client.module.ModuleManager;
import java.io.IOException;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import cat.yoink.xanax.client.module.Category;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class ClickGUI extends GuiScreen
{
    public static final ClickGUI INSTANCE;
    private int x;
    private int y;
    private final int w;
    private final int h;
    private final List<CategoryButton> buttons;
    private int dragX;
    private int dragY;
    private boolean dragging;
    
    public ClickGUI() {
        this.buttons = new ArrayList<CategoryButton>();
        this.x = 200;
        this.y = 50;
        this.w = 280;
        this.h = 180;
        final Category[] values = Category.values();
        for (int i = 0; i < values.length; ++i) {
            final Category category = values[i];
            ResourceLocation image = null;
            final ResourceLocation combat = new ResourceLocation("clickgui/combat.png");
            final ResourceLocation movement = new ResourceLocation("clickgui/move.png");
            final ResourceLocation render = new ResourceLocation("clickgui/render.png");
            final ResourceLocation world = new ResourceLocation("clickgui/world.png");
            final ResourceLocation misc = new ResourceLocation("clickgui/misc.png");
            final ResourceLocation component = new ResourceLocation("clickgui/component.png");
            final ResourceLocation client = new ResourceLocation("clickgui/client.png");
            switch (values[i]) {
                case COMBAT: {
                    image = combat;
                    break;
                }
                case MISC: {
                    image = misc;
                    break;
                }
                case MOVEMENT: {
                    image = movement;
                    break;
                }
                case RENDER: {
                    image = render;
                    break;
                }
                case WORLD: {
                    image = world;
                    break;
                }
                case COMPONENT: {
                    image = component;
                    break;
                }
                case CLIENT: {
                    image = client;
                    break;
                }
            }
            final CategoryButton button = new CategoryButton(category, this.x + i * 40, this.y, 40, 40, image, this.x, this.y);
            this.buttons.add(button);
            if (button.getCategory().equals(Category.COMBAT)) {
                button.setSelected(true);
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.dragging) {
            final ScaledResolution resolution = new ScaledResolution(this.mc);
            final int screenWidth = resolution.getScaledWidth();
            final int screenHeight = resolution.getScaledHeight();
            this.x = this.dragX + mouseX;
            this.y = this.dragY + mouseY;
            if (this.x < 0) {
                this.x = 0;
            }
            if (this.y < 0) {
                this.y = 0;
            }
            if (this.x + this.w > screenWidth) {
                this.x = screenWidth - this.w;
            }
            if (this.y + this.h > screenHeight) {
                this.y = screenHeight - this.h;
            }
        }
        this.drawFrame();
        for (int i = 0; i < this.buttons.size(); ++i) {
            final CategoryButton button2 = this.buttons.get(i);
            button2.setX(this.x + i * 40);
            button2.setY(this.y);
        }
        final int wheel = Mouse.getDWheel();
        this.buttons.forEach(button -> button.render(mouseX, mouseY, wheel, this.x, this.y));
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isHover(this.x, this.y, this.w, 10, mouseX, mouseY) && mouseButton == 0) {
            this.dragging = true;
            this.dragX = this.x - mouseX;
            this.dragY = this.y - mouseY;
        }
        this.buttons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.dragging = false;
        this.buttons.forEach(button -> button.mouseReleased(mouseX, mouseY));
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.buttons.forEach(button -> button.keyTyped(typedChar, keyCode));
    }
    
    public void onGuiClosed() {
        ModuleManager.INSTANCE.getModule("ClickGUI").disable();
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    private void drawFrame() {
        final int colorLightR = (int)ModuleManager.INSTANCE.getSetting("ClickGUI", "Red").getDouble();
        final int colorLightG = (int)ModuleManager.INSTANCE.getSetting("ClickGUI", "Green").getDouble();
        final int colorLightB = (int)ModuleManager.INSTANCE.getSetting("ClickGUI", "Blue").getDouble();
        final int colorDarkR = (colorLightR < 20) ? 0 : (colorLightR - 20);
        final int colorDarkG = (colorLightG < 20) ? 0 : (colorLightG - 20);
        final int colorDarkB = (colorLightB < 20) ? 0 : (colorLightB - 20);
        RenderUtil.drawRegularPolygon(this.x + 10, this.y + 10, 10, 50, colorLightR / 255.0f, colorLightG / 255.0f, colorLightB / 255.0f, 1.0f);
        RenderUtil.drawRegularPolygon(this.x + this.w - 10, this.y + 10, 10, 50, colorLightR / 255.0f, colorLightG / 255.0f, colorLightB / 255.0f, 1.0f);
        RenderUtil.drawRect(this.x + 10, this.y, this.w - 20, 40, new Color(colorLightR, colorLightG, colorLightB).getRGB());
        RenderUtil.drawRect(this.x, this.y + 10, this.w, 30, new Color(colorLightR, colorLightG, colorLightB).getRGB());
        RenderUtil.drawRegularPolygon(this.x + 10, this.y + this.h - 10, 10, 50, colorDarkR / 255.0f, colorDarkG / 255.0f, colorDarkB / 255.0f, 1.0f);
        RenderUtil.drawRect(this.x, this.y + 40, 80, this.h - 40 - 10, new Color(colorDarkR, colorDarkG, colorDarkB).getRGB());
        RenderUtil.drawRect(this.x + 10, this.y + 40, 70, this.h - 40, new Color(colorDarkR, colorDarkG, colorDarkB).getRGB());
        RenderUtil.drawRegularPolygon(this.x + this.w - 10, this.y + this.h - 10, 10, 50, 0.19607843f, 0.2f, 0.28235295f, 1.0f);
        RenderUtil.drawRect(this.x + 80, this.y + 40, this.w - 80 - 10, this.h - 40, new Color(50, 51, 72).getRGB());
        RenderUtil.drawRect(this.x + 80, this.y + 40, this.w - 80, this.h - 40 - 10, new Color(50, 51, 72).getRGB());
    }
    
    private boolean isHover(final int X, final int Y, final int W, final int H, final int mX, final int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
    
    public List<CategoryButton> getButtons() {
        return this.buttons;
    }
    
    static {
        INSTANCE = new ClickGUI();
    }
}
