package cat.yoink.xanax.main.clickgui.settings;

import cat.yoink.xanax.main.clickgui.SettingButton;
import cat.yoink.xanax.main.font.CFontRenderer;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.module.ModuleManager;
import cat.yoink.xanax.main.setting.BooleanSetting;
import cat.yoink.xanax.main.util.GuiUtil;

import java.awt.*;

public final class BooleanButton extends SettingButton
{
    private final BooleanSetting setting;

    public BooleanButton(final Module module, final int x, final int y, final int w, final int h, final BooleanSetting setting)
    {
        super(module, x, y, w, h);
        this.setting = setting;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final int windowX, final int windowY, final boolean self)
    {
        final boolean outline = ModuleManager.INSTANCE.getSetting("ClickGUI", "Outline").toBoolean().getValue();

        final float[] hue = new float[]{(float) (System.currentTimeMillis() % 11520L) / 11520.0f};
        final Color c = new Color(Color.HSBtoRGB(hue[0], 1.0f, 1.0f));

        GuiUtil.drawSmoothRect(this.x + 5, this.y + 5, 10, 10, 1, new Color(20, 20, 20).getRGB(), outline, c.getRGB());

        if (this.setting.getValue()) GuiUtil.drawSmoothRect(this.x + 7, this.y + 7, 6, 6, 1, c.getRGB());

        CFontRenderer.TEXT.drawString(this.setting.getName(), this.x + 20, this.y + 5.5f, -1);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton, final boolean self)
    {
        if (GuiUtil.isHover(this.x, this.y, this.w, this.h - 1, mouseX, mouseY)) this.setting.toggle();
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state)
    {

    }

    @Override
    public void keyTyped(final char typedChar, final int keyCode)
    {

    }

    @Override
    public void onGuiClosed()
    {

    }
}
