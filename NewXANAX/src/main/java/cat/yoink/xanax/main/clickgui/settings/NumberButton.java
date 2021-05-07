package cat.yoink.xanax.main.clickgui.settings;

import cat.yoink.xanax.main.clickgui.SettingButton;
import cat.yoink.xanax.main.font.CFontRenderer;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.module.ModuleManager;
import cat.yoink.xanax.main.setting.NumberSetting;
import cat.yoink.xanax.main.util.GuiUtil;

import java.awt.*;

public final class NumberButton extends SettingButton
{
    private final NumberSetting setting;
    private int sliderWidth;
    private boolean dragging;

    public NumberButton(final Module module, final int x, final int y, final int w, final int h, final NumberSetting setting)
    {
        super(module, x, y, w, h);
        this.setting = setting;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final int windowX, final int windowY, final boolean self)
    {
        updateSlider(mouseX);

        final boolean outline = ModuleManager.INSTANCE.getSetting("ClickGUI", "Outline").toBoolean().getValue();

        final float[] hue = new float[]{(float) (System.currentTimeMillis() % 11520L) / 11520.0f};
        final Color c = new Color(Color.HSBtoRGB(hue[0], 1.0f, 1.0f));

        GuiUtil.drawRect(this.x + 5, this.y + 5, 100, 10, new Color(20, 20, 20).getRGB(), outline, c.getRGB());
        GuiUtil.drawRect(this.x + this.sliderWidth - 2 + 8, this.y + 6, 4, 8, c.getRGB());

        CFontRenderer.SMALLTEXT.drawCenteredString(String.valueOf(this.setting.getValue()), this.x + 55, this.y + 6.5f, -1);
        CFontRenderer.TEXT.drawString(this.setting.getName(), this.x + 109, this.y + 5.5f, -1);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton, final boolean self)
    {
        if (GuiUtil.isHover(this.x, this.y, this.w, this.h - 1, mouseX, mouseY)) this.dragging = true;
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state)
    {
        this.dragging = false;
    }

    @Override
    public void keyTyped(final char typedChar, final int keyCode)
    {

    }

    @Override
    public void onGuiClosed()
    {
        this.dragging = false;
    }

    private void updateSlider(final int mouseX)
    {
        final double diff = Math.min(94, Math.max(0, mouseX - this.x - 8));

        final double minimum = this.setting.getMinimum();
        final double maximum = this.setting.getMaximum();

        this.sliderWidth = (int) (94f * (this.setting.getValue() - minimum) / (maximum - minimum));

        if (this.dragging)
        {
            if (diff == 0) this.setting.setValue(minimum);
            else if (diff == 94) this.setting.setValue(maximum);
            else this.setting.setValue(diff / 96f * (maximum - minimum) + minimum);
        }
    }
}
