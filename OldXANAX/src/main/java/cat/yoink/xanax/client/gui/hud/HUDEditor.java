// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.gui.hud;

import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import cat.yoink.xanax.client.module.ModuleManager;
import cat.yoink.xanax.client.component.ComponentManager;
import cat.yoink.xanax.client.component.Component;
import net.minecraft.client.gui.GuiScreen;

public class HUDEditor extends GuiScreen
{
    public static final HUDEditor INSTANCE;
    private boolean dragging;
    private int dragX;
    private int dragY;
    private Component dragComponent;
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        for (final Component component : ComponentManager.INSTANCE.getComponents()) {
            if (this.dragging && this.dragComponent.equals(component)) {
                component.setX(mouseX - this.dragX);
                component.setY(mouseY - this.dragY);
            }
            if (!ModuleManager.INSTANCE.getModule(component.getName()).isEnabled()) {
                continue;
            }
            Gui.drawRect(component.getX() - 3, component.getY() - 3, component.getX() - 2, component.getY() + component.getH() + 3, new Color(0, 0, 0, 200).getRGB());
            Gui.drawRect(component.getX() - 2, component.getY() - 3, component.getX() + component.getW() + 2, component.getY() - 2, new Color(0, 0, 0, 200).getRGB());
            Gui.drawRect(component.getX() + component.getW() + 2, component.getY() - 3, component.getX() + component.getW() + 3, component.getY() + component.getH() + 3, new Color(0, 0, 0, 200).getRGB());
            Gui.drawRect(component.getX() - 2, component.getY() + component.getH() + 2, component.getX() + component.getW() + 2, component.getY() + component.getH() + 3, new Color(0, 0, 0, 200).getRGB());
            Gui.drawRect(component.getX() - 2, component.getY() - 2, component.getX() + component.getW() + 2, component.getY() + component.getH() + 2, this.isHover(component.getX(), component.getY(), component.getW(), component.getH(), mouseX, mouseY) ? new Color(1912602624, true).getRGB() : new Color(1325400064, true).getRGB());
            component.render();
        }
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        for (final Component component : ComponentManager.INSTANCE.getComponents()) {
            if (ModuleManager.INSTANCE.getModule(component.getName()).isEnabled() && this.isHover(component.getX() - 2, component.getY() - 2, component.getW() + 2, component.getH() + 2, mouseX, mouseY)) {
                this.dragComponent = component;
                this.dragging = true;
                this.dragX = mouseX - component.getX();
                this.dragY = mouseY - component.getY();
            }
        }
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.dragging = false;
        this.dragComponent = null;
    }
    
    public void onGuiClosed() {
        this.dragComponent = null;
        this.dragging = false;
        ModuleManager.INSTANCE.getModule("HUDEditor").disable();
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    private boolean isHover(final int X, final int Y, final int W, final int H, final int mX, final int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
    
    static {
        INSTANCE = new HUDEditor();
    }
}
