// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.gui.click.settings;

import cat.yoink.xanax.client.util.RenderUtil;
import cat.yoink.xanax.client.util.font.CFontRenderer;
import cat.yoink.xanax.client.module.Module;
import cat.yoink.xanax.client.gui.click.CategoryButton;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.gui.click.SettingButton;

public class NumberSettingButton extends SettingButton
{
    private final NumberSetting setting;
    private boolean dragging;
    private int sliderWidth;
    
    public NumberSettingButton(final CategoryButton parentCategory, final Module module, final int x, final int y, final int w, final int h, final NumberSetting setting) {
        super(parentCategory, module, x, y, w, h);
        this.setting = setting;
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        this.updateSlider(mouseX);
        CFontRenderer.INSTANCE.drawString(this.setting.getName() + ": " + String.valueOf(this.setting.getValue()).replace(".0", ""), (float)(this.x + 5), (float)(this.y + 1), -1);
        RenderUtil.drawRect(this.x + 5, this.y + 14, this.w - 10, 1, -1);
        RenderUtil.drawRect(this.x + this.sliderWidth - 3 + 5, this.y + 13, 6, 3, -1);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isHover(this.x, this.y, this.w, this.h - 1, mouseX, mouseY)) {
            this.dragging = true;
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.dragging = false;
    }
    
    private void updateSlider(final int mouseX) {
        final double diff = Math.min(this.w - 10, Math.max(0, mouseX - this.x - 5));
        final double minimum = this.setting.getMinimum();
        final double maximum = this.setting.getMaximum();
        this.sliderWidth = (int)((this.w - 10) * (this.setting.getValue() - minimum) / (maximum - minimum));
        if (this.dragging) {
            if (diff == 0.0) {
                this.setting.setValue(minimum);
            }
            else if (diff == this.w - 10) {
                this.setting.setValue(maximum);
            }
            else {
                this.setting.setValue(diff / (this.w - 10) * (maximum - minimum) + minimum);
            }
        }
    }
}
