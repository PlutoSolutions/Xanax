// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.setting;

import java.util.Arrays;
import java.util.List;

public class EnumSetting extends Setting
{
    private int index;
    private final List<String> values;
    
    public EnumSetting(final String name, final String defaultValue, final String... values) {
        super(name, SettingType.ENUM);
        this.values = Arrays.asList(values);
        this.index = this.values.indexOf(defaultValue);
    }
    
    public String getValue() {
        return this.values.get(this.index);
    }
    
    public boolean is(final String mode) {
        return this.index == mode.indexOf(mode);
    }
    
    public void cycleForward() {
        if (this.index < this.values.size() - 1) {
            ++this.index;
        }
        else {
            this.index = 0;
        }
    }
    
    public void cycleBackward() {
        if (this.index > 0) {
            --this.index;
        }
        else {
            this.index = this.values.size() - 1;
        }
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    public List<String> getValues() {
        return this.values;
    }
}
