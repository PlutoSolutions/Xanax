// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.gui.click.settings;

import java.awt.Color;
import cat.yoink.xanax.client.util.font.CFontRenderer;
import cat.yoink.xanax.client.module.Module;
import cat.yoink.xanax.client.gui.click.CategoryButton;
import cat.yoink.xanax.client.setting.EnumSetting;
import cat.yoink.xanax.client.gui.click.SettingButton;

public class EnumSettingButton extends SettingButton
{
    private final EnumSetting setting;
    
    public EnumSettingButton(final CategoryButton parentCategory, final Module module, final int x, final int y, final int w, final int h, final EnumSetting setting) {
        super(parentCategory, module, x, y, w, h);
        this.setting = setting;
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        CFontRenderer.INSTANCE.drawString(this.setting.getName(), (float)(this.x + 5), (float)(this.y + 5), -1);
        CFontRenderer.INSTANCE.drawString(this.setting.getValue(), (float)(this.x + this.w - 5 - CFontRenderer.INSTANCE.getStringWidth(this.setting.getValue())), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isHover(this.x, this.y, this.w, this.h - 1, mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.setting.cycleForward();
            }
            else if (mouseButton == 1) {
                this.setting.cycleBackward();
            }
        }
    }
}
