// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.world;

import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.Setting;
import cat.yoink.xanax.client.module.Module;

public class NoEntityTrace extends Module
{
    private final Setting pickaxeOnly;
    
    public NoEntityTrace() {
        super("NoEntityTrace", Category.WORLD);
        this.pickaxeOnly = this.newSetting(new BooleanSetting("PickaxeOnly", true));
    }
}
