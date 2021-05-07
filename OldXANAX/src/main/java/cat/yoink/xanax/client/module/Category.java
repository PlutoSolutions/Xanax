// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module;

public enum Category
{
    COMBAT("Combat"), 
    MOVEMENT("Movement"), 
    WORLD("World"), 
    RENDER("Render"), 
    MISC("Miscellaneous"), 
    COMPONENT("Component"), 
    CLIENT("Client");
    
    private final String name;
    
    private Category(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
