// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.gui.click.settings;

import java.awt.Color;
import cat.yoink.xanax.client.util.font.CFontRenderer;
import cat.yoink.xanax.client.module.Module;
import cat.yoink.xanax.client.gui.click.CategoryButton;
import cat.yoink.xanax.client.gui.click.SettingButton;

public class DrawnButton extends SettingButton
{
    public DrawnButton(final CategoryButton parentCategory, final Module module, final int x, final int y, final int w, final int h) {
        super(parentCategory, module, x, y, w, h);
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        CFontRenderer.INSTANCE.drawString("Drawn", (float)(this.x + 5), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
        CFontRenderer.INSTANCE.drawString(this.module.isDrawn() ? "True" : "False", (float)(this.x + this.w - 5 - CFontRenderer.INSTANCE.getStringWidth(this.module.isDrawn() ? "True" : "False")), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isHover(this.x, this.y, this.w, this.h - 1, mouseX, mouseY)) {
            this.module.setDrawn(!this.module.isDrawn());
        }
    }
}
