// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.notification;

import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import cat.yoink.xanax.client.MinecraftInstance;

public class Notification implements MinecraftInstance
{
    private final Timer timer;
    private final String text;
    private final long stayTime;
    private float x;
    private float oldX;
    private float y;
    private float oldY;
    private float width;
    private boolean done;
    
    public Notification(final String text, final long stayTime) {
        this.x = (float)(new ScaledResolution(Notification.mc).getScaledWidth() - 2);
        this.y = (float)(new ScaledResolution(Notification.mc).getScaledHeight() - 2);
        this.text = text;
        this.stayTime = stayTime;
        (this.timer = new Timer()).reset();
        this.done = false;
    }
    
    public void renderNotification(final float prevY) {
        final float xSpeed = this.width / (Minecraft.getDebugFPS() / 2.0f);
        final float ySpeed = (new ScaledResolution(Notification.mc).getScaledHeight() - prevY) / (Minecraft.getDebugFPS() / 8.0f);
        if (this.width != Notification.mc.fontRenderer.getStringWidth(this.text) + 10) {
            this.width = (float)(Notification.mc.fontRenderer.getStringWidth(this.text) + 10);
        }
        if (this.done) {
            this.oldX = this.x;
            this.x += xSpeed;
            this.y += ySpeed;
        }
        if (!this.done() && !this.done) {
            this.timer.reset();
            this.oldX = this.x;
            if (this.x <= new ScaledResolution(Notification.mc).getScaledWidth() - this.width + xSpeed) {
                this.x = new ScaledResolution(Notification.mc).getScaledWidth() - this.width;
            }
            else {
                this.x -= xSpeed;
            }
        }
        else if (this.timer.reach(this.stayTime)) {
            this.done = true;
        }
        if (this.x < new ScaledResolution(Notification.mc).getScaledWidth() - this.width) {
            this.oldX = this.x;
            this.x += xSpeed;
        }
        if (this.y != prevY) {
            if (this.y > prevY + ySpeed) {
                this.y -= ySpeed;
            }
            else {
                this.y = prevY;
            }
        }
        else if (this.y < prevY) {
            this.oldY = this.y;
            this.y += ySpeed;
        }
        final int x = (int)(this.oldX + (this.x - this.oldX));
        final int y = (int)(this.oldY + (this.y - this.oldY));
        final int w = (int)this.width;
        final int h = 22;
        final Color border = new Color(250, 250, 250, 226);
        Gui.drawRect(x, y, x + w, y + h, new Color(0, 0, 0, 129).getRGB());
        Gui.drawRect(x, y, x + w, y + 1, border.getRGB());
        Gui.drawRect(x, y, x + 1, y + h, border.getRGB());
        Gui.drawRect(x, y + h - 1, x + w, y + h, border.getRGB());
        Notification.mc.fontRenderer.drawStringWithShadow(this.text, (float)(x + 6), (float)(y + 7), -1);
        if (this.delete()) {
            NotificationManager.INSTANCE.getNotifications().remove(this);
        }
    }
    
    public boolean done() {
        return this.x <= new ScaledResolution(Notification.mc).getScaledWidth() - this.width;
    }
    
    public boolean delete() {
        return this.x >= new ScaledResolution(Notification.mc).getScaledWidth() && this.done;
    }
    
    private static class Timer
    {
        private long time;
        
        private Timer() {
            this.time = System.nanoTime() / 1000000L;
        }
        
        public boolean reach(final long time) {
            return this.time() >= time;
        }
        
        public void reset() {
            this.time = System.nanoTime() / 1000000L;
        }
        
        public long time() {
            return System.nanoTime() / 1000000L - this.time;
        }
    }
}
