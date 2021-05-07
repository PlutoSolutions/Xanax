// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.component;

import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import cat.yoink.xanax.client.component.components.Arraylist;
import cat.yoink.xanax.client.component.components.Watermark;
import java.util.ArrayList;
import java.util.List;

public enum ComponentManager
{
    INSTANCE;
    
    private final List<Component> components;
    
    private ComponentManager() {
        this.components = new ArrayList<Component>();
        this.addComponents(new Watermark(), new Arraylist());
    }
    
    public void addComponents(final Component... components) {
        this.components.addAll(Arrays.asList(components));
    }
    
    public List<Component> getComponents() {
        return this.components;
    }
    
    public Component getComponent(final String name) {
        for (final Component component : this.components) {
            if (component.getName().equalsIgnoreCase(name)) {
                return component;
            }
        }
        return null;
    }
}
