// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import java.lang.reflect.Field;
import cat.yoink.xanax.client.handling.Mapping;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.EnumSetting;
import cat.yoink.xanax.client.module.Module;

public class AntiWeb extends Module
{
    private final EnumSetting mode;
    
    public AntiWeb() {
        super("AntiWeb", Category.MOVEMENT);
        this.mode = this.newSetting(new EnumSetting("Mode", "No", new String[] { "No", "Down" }));
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck() || !this.isInWeb()) {
            return;
        }
        if (this.mode.getValue().equalsIgnoreCase("No")) {
            try {
                final Field isInWeb = Entity.class.getDeclaredField(Mapping.isInWeb);
                isInWeb.setAccessible(true);
                isInWeb.set(AntiWeb.mc.player, false);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (this.mode.getValue().equalsIgnoreCase("Down") && !AntiWeb.mc.player.onGround) {
            final EntityPlayerSP player = AntiWeb.mc.player;
            player.motionY -= 3.0;
        }
    }
    
    private boolean isInWeb() {
        try {
            final Field isInWeb = Entity.class.getDeclaredField(Mapping.isInWeb);
            isInWeb.setAccessible(true);
            return (boolean)isInWeb.get(AntiWeb.mc.player);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
