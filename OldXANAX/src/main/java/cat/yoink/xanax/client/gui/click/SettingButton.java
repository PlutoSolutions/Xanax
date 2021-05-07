// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.gui.click;

import cat.yoink.xanax.client.module.Module;
import cat.yoink.xanax.client.MinecraftInstance;

public class SettingButton implements MinecraftInstance
{
    protected final CategoryButton parentCategory;
    protected final Module module;
    protected int x;
    protected int y;
    protected final int w;
    protected final int h;
    
    public SettingButton(final CategoryButton parentCategory, final Module module, final int x, final int y, final int w, final int h) {
        this.parentCategory = parentCategory;
        this.module = module;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    public void render(final int mouseX, final int mouseY) {
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    public void mouseReleased(final int mouseX, final int mouseY) {
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    protected boolean isHover(final int X, final int Y, final int W, final int H, final int mX, final int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
}
