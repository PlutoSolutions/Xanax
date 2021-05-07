// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.component.components;

import java.awt.Color;
import cat.yoink.xanax.client.setting.BooleanSetting;
import org.lwjgl.opengl.GL11;
import cat.yoink.xanax.client.module.ModuleManager;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.component.Component;

public class Watermark extends Component
{
    private float hue;
    
    public Watermark() {
        super("Watermark");
        this.hue = 0.0f;
    }
    
    @Override
    public void render() {
        final float scale = (float)((NumberSetting)ModuleManager.INSTANCE.getSetting("Watermark", "Scale")).getValue();
        GL11.glPushMatrix();
        GL11.glScaled((double)scale, (double)scale, 0.0);
        if (((BooleanSetting)ModuleManager.INSTANCE.getSetting("Watermark", "Rainbow")).getValue()) {
            final int rgb = Color.HSBtoRGB(this.hue, 1.0f, 1.0f);
            this.hue += (float)(((NumberSetting)ModuleManager.INSTANCE.getSetting("Watermark", "RainbowSpeed")).getValue() / 1000.0);
            Watermark.mc.fontRenderer.drawStringWithShadow("XANAX", this.getX() / scale, this.getY() / scale, rgb);
        }
        else {
            final float r = (float)((NumberSetting)ModuleManager.INSTANCE.getSetting("Watermark", "Red")).getValue();
            final float g = (float)((NumberSetting)ModuleManager.INSTANCE.getSetting("Watermark", "Green")).getValue();
            final float b = (float)((NumberSetting)ModuleManager.INSTANCE.getSetting("Watermark", "Blue")).getValue();
            final float a = (float)((NumberSetting)ModuleManager.INSTANCE.getSetting("Watermark", "Alpha")).getValue();
            Watermark.mc.fontRenderer.drawStringWithShadow("XANAX", this.getX() / scale, this.getY() / scale, new Color(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f).getRGB());
        }
        GL11.glPopMatrix();
        this.setW((int)(scale * Watermark.mc.fontRenderer.getStringWidth("XANAX")));
        this.setH((int)(scale * Watermark.mc.fontRenderer.FONT_HEIGHT));
    }
}
