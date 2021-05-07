package cat.yoink.xanax.main.setting;

public abstract class Setting
{
    private final String name;

    public Setting(final String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public BooleanSetting toBoolean()
    {
        return (BooleanSetting) this;
    }

    public NumberSetting toNumber()
    {
        return (NumberSetting) this;
    }

    public EnumSetting toEnum()
    {
        return (EnumSetting) this;
    }
}
