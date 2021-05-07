// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.render;

import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.module.Module;

public class Fullbright extends Module
{
    private float previousBrightness;
    
    public Fullbright() {
        super("Fullbright", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        this.previousBrightness = Fullbright.mc.gameSettings.gammaSetting;
        Fullbright.mc.gameSettings.gammaSetting = 100.0f;
    }
    
    @Override
    public void onDisable() {
        Fullbright.mc.gameSettings.gammaSetting = this.previousBrightness;
    }
}
