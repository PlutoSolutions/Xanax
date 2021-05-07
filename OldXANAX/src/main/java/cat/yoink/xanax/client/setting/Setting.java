// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.setting;

public class Setting
{
    protected final String name;
    protected final SettingType type;
    
    public Setting(final String name, final SettingType type) {
        this.name = name;
        this.type = type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public SettingType getType() {
        return this.type;
    }
    
    public boolean getBoolean() {
        return ((BooleanSetting)this).getValue();
    }
    
    public double getDouble() {
        return ((NumberSetting)this).getValue();
    }
    
    public String getEnum() {
        return ((EnumSetting)this).getValue();
    }
}
