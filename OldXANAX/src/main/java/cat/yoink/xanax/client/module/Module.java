// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module;

import net.minecraftforge.common.MinecraftForge;
import java.util.ArrayList;
import cat.yoink.xanax.client.setting.Setting;
import java.util.List;
import cat.yoink.xanax.client.MinecraftInstance;

public class Module implements MinecraftInstance
{
    private final String name;
    private final Category category;
    private int bind;
    private boolean enabled;
    private boolean drawn;
    private final List<Setting> settings;
    
    public Module(final String name, final Category category) {
        this.settings = new ArrayList<Setting>();
        this.name = name;
        this.category = category;
        this.drawn = true;
    }
    
    public Module(final String name, final int bind, final Category category) {
        this.settings = new ArrayList<Setting>();
        this.name = name;
        this.bind = bind;
        this.category = category;
        this.drawn = true;
    }
    
    public void enable() {
        this.enabled = true;
        this.onEnable();
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public void disable() {
        this.enabled = false;
        this.onDisable();
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    public void toggle() {
        if (this.isEnabled()) {
            this.disable();
        }
        else {
            this.enable();
        }
    }
    
    public <E extends Setting> E newSetting(final E setting) {
        this.settings.add(setting);
        return setting;
    }
    
    public boolean nullCheck() {
        return Module.mc.player == null || Module.mc.world == null;
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public String getName() {
        return this.name;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public int getBind() {
        return this.bind;
    }
    
    public void setBind(final int bind) {
        this.bind = bind;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isDrawn() {
        return this.drawn;
    }
    
    public void setDrawn(final boolean drawn) {
        this.drawn = drawn;
    }
    
    public List<Setting> getSettings() {
        return this.settings;
    }
}
