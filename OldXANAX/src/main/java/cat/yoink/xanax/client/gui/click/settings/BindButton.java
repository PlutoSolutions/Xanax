// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.gui.click.settings;

import org.lwjgl.input.Keyboard;
import java.awt.Color;
import cat.yoink.xanax.client.util.font.CFontRenderer;
import cat.yoink.xanax.client.module.Module;
import cat.yoink.xanax.client.gui.click.CategoryButton;
import cat.yoink.xanax.client.gui.click.SettingButton;

public class BindButton extends SettingButton
{
    boolean binding;
    
    public BindButton(final CategoryButton parentCategory, final Module module, final int x, final int y, final int w, final int h) {
        super(parentCategory, module, x, y, w, h);
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        CFontRenderer.INSTANCE.drawString("Bind", (float)(this.x + 5), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
        if (this.binding) {
            CFontRenderer.INSTANCE.drawString("...", (float)(this.x + this.w - 5 - CFontRenderer.INSTANCE.getStringWidth("...")), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
        }
        else {
            try {
                CFontRenderer.INSTANCE.drawString(Keyboard.getKeyName(this.module.getBind()), (float)(this.x + this.w - 5 - CFontRenderer.INSTANCE.getStringWidth(Keyboard.getKeyName(this.module.getBind()))), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
            }
            catch (Exception e) {
                CFontRenderer.INSTANCE.drawString("NONE", (float)(this.x + this.w - 10 - CFontRenderer.INSTANCE.getStringWidth("NONE")), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isHover(this.x, this.y, this.w, this.h - 1, mouseX, mouseY)) {
            this.binding = !this.binding;
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.binding) {
            if (keyCode == 211 || keyCode == 14) {
                this.module.setBind(0);
                this.binding = false;
                return;
            }
            this.module.setBind(keyCode);
            this.binding = false;
        }
    }
}
