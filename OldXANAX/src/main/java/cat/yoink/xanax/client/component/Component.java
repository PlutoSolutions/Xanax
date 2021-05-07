// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.component;

import cat.yoink.xanax.client.MinecraftInstance;

public class Component implements MinecraftInstance
{
    private String name;
    private int x;
    private int y;
    private int w;
    private int h;
    
    public Component(final String name) {
        this.name = name;
    }
    
    public void render() {
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getW() {
        return this.w;
    }
    
    public void setW(final int w) {
        this.w = w;
    }
    
    public int getH() {
        return this.h;
    }
    
    public void setH(final int h) {
        this.h = h;
    }
}
