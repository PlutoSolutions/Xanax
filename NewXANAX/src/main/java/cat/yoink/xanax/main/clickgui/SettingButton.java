package cat.yoink.xanax.main.clickgui;

import cat.yoink.xanax.main.module.Module;

public abstract class SettingButton implements GuiBase
{
    protected final Module module;
    protected final int w;
    protected final int h;
    protected int x;
    protected int y;

    public SettingButton(final Module module, final int x, final int y, final int w, final int h)
    {
        this.module = module;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}
