// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.component;

import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.module.Module;

public class Arraylist extends Module
{
    private final BooleanSetting rainbow;
    private final NumberSetting rainbowSpeed;
    
    public Arraylist() {
        super("Arraylist", Category.COMPONENT);
        this.rainbow = this.newSetting(new BooleanSetting("Rainbow", true));
        this.rainbowSpeed = this.newSetting(new NumberSetting("RainbowSpeed", 3.0, 1.0, 10.0, 0.5));
    }
}
