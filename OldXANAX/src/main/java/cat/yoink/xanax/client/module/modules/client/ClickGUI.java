// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.client;

import net.minecraft.client.gui.GuiScreen;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.setting.Setting;
import cat.yoink.xanax.client.module.Module;

public class ClickGUI extends Module
{
    private final Setting blur;
    private final NumberSetting red;
    private final NumberSetting green;
    private final NumberSetting blue;
    
    public ClickGUI() {
        super("ClickGUI", Category.CLIENT);
        this.blur = this.newSetting(new BooleanSetting("Blur", false));
        this.red = this.newSetting(new NumberSetting("Red", 92.0, 0.0, 255.0, 1.0));
        this.green = this.newSetting(new NumberSetting("Green", 39.0, 0.0, 255.0, 1.0));
        this.blue = this.newSetting(new NumberSetting("Blue", 208.0, 0.0, 255.0, 1.0));
        this.setBind(54);
    }
    
    @Override
    public void onEnable() {
        ClickGUI.mc.displayGuiScreen((GuiScreen)cat.yoink.xanax.client.gui.click.ClickGUI.INSTANCE);
    }
}
