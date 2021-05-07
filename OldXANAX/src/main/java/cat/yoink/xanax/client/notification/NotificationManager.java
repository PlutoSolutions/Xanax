// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.notification;

import net.minecraft.client.gui.ScaledResolution;
import java.util.ArrayList;
import java.util.List;
import cat.yoink.xanax.client.MinecraftInstance;

public enum NotificationManager implements MinecraftInstance
{
    INSTANCE;
    
    private final List<Notification> notifications;
    
    private NotificationManager() {
        this.notifications = new ArrayList<Notification>();
    }
    
    public List<Notification> getNotifications() {
        return this.notifications;
    }
    
    public void addNotification(final String text, final long duration) {
        this.notifications.add(new Notification(text, duration));
    }
    
    public void renderNotifications() {
        final ScaledResolution scaledResolution = new ScaledResolution(NotificationManager.mc);
        int neededY = scaledResolution.getScaledHeight() - 12;
        for (int i = 0; i < this.notifications.size(); ++i) {
            final Notification notification = this.notifications.get(i);
            neededY -= 29;
            notification.renderNotification((float)neededY);
        }
    }
}
