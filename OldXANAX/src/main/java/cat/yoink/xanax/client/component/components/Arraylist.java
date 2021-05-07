// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.component.components;

import cat.yoink.xanax.client.setting.NumberSetting;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.util.Comparator;
import cat.yoink.xanax.client.module.Module;
import java.util.ArrayList;
import cat.yoink.xanax.client.module.ModuleManager;
import net.minecraft.client.gui.ScaledResolution;
import cat.yoink.xanax.client.component.Component;

public class Arraylist extends Component
{
    float rainbowColor;
    
    public Arraylist() {
        super("Arraylist");
        this.rainbowColor = 0.0f;
    }
    
    @Override
    public void render() {
        if (this.getX() < new ScaledResolution(Arraylist.mc).getScaledWidth() / 2) {
            this.renderLeft();
        }
        else {
            this.renderRight();
        }
    }
    
    private void renderLeft() {
        final boolean rainbow = ModuleManager.INSTANCE.getSetting("Arraylist", "Rainbow").getBoolean();
        int oW = 0;
        final int fH = 13;
        final int X = this.getX();
        int Y = this.getY();
        int C = 0;
        int mW = 0;
        int miW = 10000;
        this.rainbowColor += (float)(Math.floor(ModuleManager.INSTANCE.getSetting("Arraylist", "RainbowSpeed").getDouble()) / 1000.0);
        float r = this.rainbowColor;
        final List<Module> toSort = new ArrayList<Module>();
        for (final Module module2 : ModuleManager.INSTANCE.getModules()) {
            if (module2.isEnabled() && module2.isDrawn()) {
                toSort.add(module2);
            }
        }
        if (toSort.size() == 0) {
            return;
        }
        toSort.sort(Comparator.comparing(module -> this.rRender(module.getName())));
        for (final Module module2 : toSort) {
            final int rgb = Color.HSBtoRGB(r, 1.0f, 1.0f);
            r -= 0.06f;
            final int w = Arraylist.mc.fontRenderer.getStringWidth(module2.getName());
            if (mW < w) {
                mW = w;
            }
            if (miW > w) {
                miW = w;
            }
            final int d = w - oW;
            if (oW != 0) {
                Gui.drawRect(X + w + 3 - d, Y - 1, X + w + 4 - 1, Y, rainbow ? rgb : new Color(-1).getRGB());
            }
            Gui.drawRect(X - 3, Y, X - 4, Y + fH, rainbow ? rgb : new Color(-1).getRGB());
            Gui.drawRect(X + w + 3, Y, X + w + 4, Y + fH, rainbow ? rgb : new Color(-1).getRGB());
            Gui.drawRect(X - 3, Y, X + w + 3, Y + fH, new Color(-1929314047, true).getRGB());
            Arraylist.mc.fontRenderer.drawStringWithShadow(module2.getName(), (float)X, (float)(Y + 1), rainbow ? rgb : -1);
            oW = w;
            Y += fH;
            ++C;
        }
        this.finish(rainbow, fH, X, Y, C, mW, miW, r);
    }
    
    private void renderRight() {
        final boolean rainbow = ModuleManager.INSTANCE.getSetting("Arraylist", "Rainbow").getBoolean();
        int oW = 0;
        final int fH = 13;
        int X = this.getX();
        int Y = this.getY();
        int C = 0;
        int mW = 0;
        int miW = 10000;
        this.rainbowColor += (float)(((NumberSetting)ModuleManager.INSTANCE.getSetting("Arraylist", "RainbowSpeed")).getValue() / 1000.0);
        float r = this.rainbowColor;
        final List<Module> toSort = new ArrayList<Module>();
        for (final Module module2 : ModuleManager.INSTANCE.getModules()) {
            if (module2.isEnabled() && module2.isDrawn()) {
                toSort.add(module2);
            }
        }
        if (toSort.size() == 0) {
            return;
        }
        toSort.sort(Comparator.comparing(module -> this.rRender(module.getName())));
        for (final Module module2 : toSort) {
            final int rgb = Color.HSBtoRGB(r, 1.0f, 1.0f);
            r -= 0.06f;
            final int w = Arraylist.mc.fontRenderer.getStringWidth(module2.getName());
            if (mW < w) {
                mW = w;
            }
            if (miW > w) {
                miW = w;
            }
            final int d = w - oW;
            X = this.getX() + mW - w;
            if (oW != 0) {
                Gui.drawRect(X - 3 + d, Y - 1, X - 3, Y, rainbow ? rgb : new Color(-1).getRGB());
            }
            Gui.drawRect(X - 3, Y, X - 4, Y + fH, rainbow ? rgb : new Color(-1).getRGB());
            Gui.drawRect(X + w + 3, Y, X + w + 4, Y + fH, rainbow ? rgb : new Color(-1).getRGB());
            Gui.drawRect(X - 3, Y, X + w + 3, Y + fH, new Color(-1929314047, true).getRGB());
            Arraylist.mc.fontRenderer.drawStringWithShadow(module2.getName(), (float)X, (float)(Y + 1), rainbow ? rgb : -1);
            oW = w;
            Y += fH;
            ++C;
        }
        this.finish(rainbow, fH, X, Y, C, mW, miW, r);
    }
    
    private void finish(final boolean rainbow, final int fH, final int x, final int y, final int c, final int mW, final int miW, final float r) {
        Gui.drawRect(this.getX() - 4, this.getY() - 1, this.getX() + mW + 4, this.getY(), rainbow ? Color.HSBtoRGB(this.rainbowColor, 1.0f, 1.0f) : new Color(-1).getRGB());
        Gui.drawRect(x - 4, y, x + miW + 4, y + 1, rainbow ? Color.HSBtoRGB(r, 1.0f, 1.0f) : new Color(-1).getRGB());
        this.setW(mW);
        this.setH(c * fH);
    }
    
    private int rRender(final String text) {
        return 1000 - Arraylist.mc.fontRenderer.getStringWidth(text);
    }
}
