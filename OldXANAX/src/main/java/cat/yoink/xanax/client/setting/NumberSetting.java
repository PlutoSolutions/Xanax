// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.setting;

public class NumberSetting extends Setting
{
    private double value;
    private final double minimum;
    private final double maximum;
    private final double increment;
    
    public NumberSetting(final String name, final double value, final double minimum, final double maximum, final double increment) {
        super(name, SettingType.NUMBER);
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }
    
    public double getValue() {
        return this.value;
    }
    
    public void setValue(final double value) {
        final double precision = 1.0 / this.increment;
        this.value = Math.round(Math.max(this.minimum, Math.min(this.maximum, value)) * precision) / precision;
    }
    
    public void increment() {
        this.setValue(this.value + this.increment);
    }
    
    public void decrement() {
        this.setValue(this.value - this.increment);
    }
    
    public double getMinimum() {
        return this.minimum;
    }
    
    public double getMaximum() {
        return this.maximum;
    }
    
    public double getIncrement() {
        return this.increment;
    }
}
