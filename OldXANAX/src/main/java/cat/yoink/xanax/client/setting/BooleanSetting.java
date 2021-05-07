// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.setting;

public class BooleanSetting extends Setting
{
    private boolean state;
    
    public BooleanSetting(final String name, final boolean state) {
        super(name, SettingType.BOOLEAN);
        this.state = state;
    }
    
    public boolean getValue() {
        return this.state;
    }
    
    public void enable() {
        this.state = true;
    }
    
    public void disable() {
        this.state = false;
    }
    
    public void toggle() {
        this.state = !this.state;
    }
}
