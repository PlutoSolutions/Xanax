// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.awt.Color;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.module.Module;

public class FogColor extends Module
{
    private final NumberSetting red;
    private final NumberSetting green;
    private final NumberSetting blue;
    private final BooleanSetting rainbow;
    
    public FogColor() {
        super("FogColor", Category.RENDER);
        this.red = this.newSetting(new NumberSetting("Red", 100.0, 0.0, 255.0, 1.0));
        this.green = this.newSetting(new NumberSetting("Green", 0.0, 0.0, 255.0, 1.0));
        this.blue = this.newSetting(new NumberSetting("Blue", 0.0, 0.0, 255.0, 1.0));
        this.rainbow = this.newSetting(new BooleanSetting("Rainbow", false));
    }
    
    @SubscribeEvent
    public void onEntityViewRenderFogColors(final EntityViewRenderEvent.FogColors event) {
        if (this.nullCheck()) {
            return;
        }
        final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
        final int r = rgb >> 16 & 0xFF;
        final int g = rgb >> 8 & 0xFF;
        final int b = rgb & 0xFF;
        if (this.rainbow.getValue()) {
            event.setRed(r / 255.0f);
            event.setGreen(g / 255.0f);
            event.setBlue(b / 255.0f);
        }
        else {
            event.setRed((float)this.red.getValue() / 255.0f);
            event.setGreen((float)this.green.getValue() / 255.0f);
            event.setBlue((float)this.blue.getValue() / 255.0f);
        }
    }
    
    @SubscribeEvent
    public void onEntityViewRenderFogDensity(final EntityViewRenderEvent.FogDensity event) {
        if (this.nullCheck()) {
            return;
        }
        event.setDensity(0.0f);
    }
}
