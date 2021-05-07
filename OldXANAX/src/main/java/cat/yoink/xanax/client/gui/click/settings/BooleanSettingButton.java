// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.gui.click.settings;

import cat.yoink.xanax.client.util.RenderUtil;
import java.awt.Color;
import cat.yoink.xanax.client.util.font.CFontRenderer;
import cat.yoink.xanax.client.module.Module;
import cat.yoink.xanax.client.gui.click.CategoryButton;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.gui.click.SettingButton;

public class BooleanSettingButton extends SettingButton
{
    private final BooleanSetting setting;
    
    public BooleanSettingButton(final CategoryButton parentCategory, final Module module, final int x, final int y, final int w, final int h, final BooleanSetting setting) {
        super(parentCategory, module, x, y, w, h);
        this.setting = setting;
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        CFontRenderer.INSTANCE.drawString(this.setting.getName(), (float)(this.x + 5), (float)(this.y + 5), new Color(255, 255, 255).getRGB());
        RenderUtil.drawRegularPolygon(this.x + 180, this.y + 10, 5, 50, 1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.drawRegularPolygon(this.x + 190, this.y + 10, 5, 50, 0.78431374f, 0.78431374f, 0.78431374f, 1.0f);
        RenderUtil.drawRect(this.x + 180, this.y + 5, 10, 10, this.setting.getValue() ? new Color(255, 255, 255).getRGB() : new Color(200, 200, 200).getRGB());
        RenderUtil.drawRegularPolygon(this.x + (this.setting.getValue() ? 190 : 180), this.y + 10, 6, 60, 0.36078432f, 0.15294118f, 0.8156863f, 1.0f);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isHover(this.x, this.y, this.w, this.h - 1, mouseX, mouseY) && mouseButton == 0) {
            this.setting.toggle();
        }
    }
}
