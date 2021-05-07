// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.misc;

import cat.yoink.xanax.client.util.ChatUtil;
import cat.yoink.xanax.client.friend.FriendManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import cat.yoink.xanax.client.module.Category;
import java.util.concurrent.ConcurrentHashMap;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.EnumSetting;
import cat.yoink.xanax.client.module.Module;

public class AutoGG extends Module
{
    private final EnumSetting mode;
    private final BooleanSetting withName;
    private final BooleanSetting clientSide;
    private final ConcurrentHashMap<String, Integer> targetedPlayers;
    
    public AutoGG() {
        super("AutoGG", Category.MISC);
        this.mode = this.newSetting(new EnumSetting("Mode", "GG", new String[] { "GG", "EZ" }));
        this.withName = this.newSetting(new BooleanSetting("Name", false));
        this.clientSide = this.newSetting(new BooleanSetting("ClientSide", false));
        this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        for (final Entity entity : AutoGG.mc.world.getLoadedEntityList()) {
            final EntityPlayer player;
            if (entity instanceof EntityPlayer && (player = (EntityPlayer)entity).getHealth() <= 0.0f) {
                final String name2;
                if (!this.shouldAnnounce(name2 = player.getName())) {
                    continue;
                }
                this.doAnnounce(name2);
                break;
            }
        }
        for (final Map.Entry<String, Integer> entry : this.targetedPlayers.entrySet()) {
            final String name3 = entry.getKey();
            final Integer timeout = entry.getValue();
            if (timeout <= 0) {
                this.targetedPlayers.remove(name3);
            }
            else {
                this.targetedPlayers.put(name3, timeout - 1);
            }
        }
    }
    
    @SubscribeEvent
    public void onLeavingDeathEvent(final LivingDeathEvent event) {
        if (this.nullCheck()) {
            return;
        }
        final EntityLivingBase entity;
        if (AutoGG.mc.player == null || (entity = event.getEntityLiving()) == null || !(entity instanceof EntityPlayer)) {
            return;
        }
        final EntityPlayer player = (EntityPlayer)entity;
        if (player.getHealth() > 0.0f) {
            return;
        }
        final String name = player.getName();
        if (this.shouldAnnounce(name)) {
            this.doAnnounce(name);
        }
    }
    
    private boolean shouldAnnounce(final String name) {
        return this.targetedPlayers.containsKey(name) && !FriendManager.INSTANCE.isFriend(name);
    }
    
    private void doAnnounce(final String name) {
        this.targetedPlayers.remove(name);
        final StringBuilder message = new StringBuilder();
        if (this.mode.getValue().equalsIgnoreCase("GG")) {
            message.append("GG!");
        }
        else {
            message.append("EZZZZZZZ");
        }
        if (this.withName.getValue()) {
            message.append(" XANAX owns me and all!");
        }
        if (this.clientSide.getValue()) {
            ChatUtil.sendClientMessage(message.toString());
        }
        else {
            ChatUtil.sendPublicMessage(message.toString());
        }
    }
    
    public void addTargetedPlayer(final String name) {
        if (name.equals(AutoGG.mc.player.getName())) {
            return;
        }
        this.targetedPlayers.put(name, 20);
    }
}
