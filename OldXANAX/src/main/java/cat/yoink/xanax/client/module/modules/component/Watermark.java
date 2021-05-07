// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.component;

import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.module.Module;

public class Watermark extends Module
{
    private final NumberSetting scale;
    private final BooleanSetting rainbow;
    private final NumberSetting red;
    private final NumberSetting green;
    private final NumberSetting blue;
    private final NumberSetting alpha;
    private final NumberSetting rainbowSpeed;
    
    public Watermark() {
        super("Watermark", Category.COMPONENT);
        this.scale = this.newSetting(new NumberSetting("Scale", 2.0, 0.1, 5.0, 0.1));
        this.rainbow = this.newSetting(new BooleanSetting("Rainbow", false));
        this.red = this.newSetting(new NumberSetting("Red", 0.0, 0.0, 255.0, 1.0));
        this.green = this.newSetting(new NumberSetting("Green", 100.0, 0.0, 255.0, 1.0));
        this.blue = this.newSetting(new NumberSetting("Blue", 0.0, 0.0, 255.0, 1.0));
        this.alpha = this.newSetting(new NumberSetting("Alpha", 255.0, 0.0, 255.0, 1.0));
        this.rainbowSpeed = this.newSetting(new NumberSetting("RainbowSpeed", 3.0, 0.1, 10.0, 0.1));
    }
}
