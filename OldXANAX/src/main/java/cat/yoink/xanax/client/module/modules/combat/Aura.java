// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import cat.yoink.xanax.client.util.PlayerUtil;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.module.Module;

public class Aura extends Module
{
    private final NumberSetting range;
    private final BooleanSetting rotate;
    private final BooleanSetting friends;
    
    public Aura() {
        super("Aura", Category.COMBAT);
        this.range = this.newSetting(new NumberSetting("Range", 4.0, 1.0, 8.0, 0.1));
        this.rotate = this.newSetting(new BooleanSetting("Rotate", false));
        this.friends = this.newSetting(new BooleanSetting("Friends", false));
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        final Entity closest = PlayerUtil.getClosestEntity(this.friends.getValue());
        if (closest != null && !closest.isDead && Aura.mc.player.getDistance(closest) <= this.range.getValue()) {
            if (Aura.mc.player.getCooledAttackStrength(0.0f) < 1.0f) {
                return;
            }
            if (this.rotate.getValue()) {
                PlayerUtil.rotatePacket(closest.posX, closest.posY + 1.0, closest.posZ);
            }
            Aura.mc.playerController.attackEntity((EntityPlayer)Aura.mc.player, closest);
            Aura.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }
}
