package cat.yoink.xanax.main.module;

import cat.yoink.xanax.core.Main;
import cat.yoink.xanax.main.MinecraftInstance;
import cat.yoink.xanax.main.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public abstract class Module implements MinecraftInstance
{
    protected final String name;
    protected final Category category;
    protected final List<Setting> settings = new ArrayList<>();
    protected int bind;
    protected boolean enabled;

    protected Module(final String name, final Category category)
    {
        this.name = name;
        this.category = category;
    }

    protected void onEnable()
    {
    }

    protected void onDisable()
    {
    }

    public final void enable()
    {
        this.enabled = true;
        onEnable();
        Main.EVENT_BUS.addSubscriber(this);
    }

    public final void disable()
    {
        this.enabled = false;
        onDisable();
        Main.EVENT_BUS.removeSubscriber(this);
    }

    public final void toggle()
    {
        if (this.enabled) disable();
        else enable();
    }

    protected <E extends Setting> E addSetting(final E setting)
    {
        this.settings.add(setting);
        return setting;
    }

    public Setting getSetting(final String name)
    {
        return this.settings.stream().filter(setting -> setting.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public final String getName()
    {
        return this.name;
    }

    public final Category getCategory()
    {
        return this.category;
    }

    public final int getBind()
    {
        return this.bind;
    }

    public final void setBind(final int bind)
    {
        this.bind = bind;
    }

    public final boolean isEnabled()
    {
        return this.enabled;
    }

    public final void setEnabled(final boolean enabled)
    {
        this.enabled = enabled;
    }

    public List<Setting> getSettings()
    {
        return this.settings;
    }
}
