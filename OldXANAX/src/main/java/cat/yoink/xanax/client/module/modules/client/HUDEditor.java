// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.client;

import net.minecraft.client.gui.GuiScreen;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.module.Module;

public class HUDEditor extends Module
{
    public HUDEditor() {
        super("HUDEditor", Category.CLIENT);
    }
    
    @Override
    public void onEnable() {
        HUDEditor.mc.displayGuiScreen((GuiScreen)cat.yoink.xanax.client.gui.hud.HUDEditor.INSTANCE);
    }
}
