// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.combat;

import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.module.Module;

public class Reach extends Module
{
    private final NumberSetting distance;
    
    public Reach() {
        super("Reach", Category.COMBAT);
        this.distance = this.newSetting(new NumberSetting("Distance", 6.0, 3.0, 13.0, 1.0));
    }
}
