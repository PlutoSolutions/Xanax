package cat.yoink.xanax.main.setting;

public final class BooleanSetting extends Setting
{
    private boolean value;

    public BooleanSetting(final String name, final boolean value)
    {
        super(name);
        this.value = value;
    }

    public boolean getValue()
    {
        return this.value;
    }

    public void setValue(final boolean value)
    {
        this.value = value;
    }

    public void enable()
    {
        this.value = true;
    }

    public void disable()
    {
        this.value = false;
    }

    public void toggle()
    {
        this.value = !this.value;
    }
}
