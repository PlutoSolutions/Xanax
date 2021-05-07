package cat.yoink.xanax.main.module.modules.combat;

import cat.yoink.xanax.main.event.events.PacketEvent;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.setting.BooleanSetting;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import org.lwjgl.input.Keyboard;
import cat.yoink.eventmanager.Listener;

public final class Criticals extends Module
{
    private final BooleanSetting crystals = addSetting(new BooleanSetting("Crystals", false));

    public Criticals()
    {
        super("Criticals", Category.COMBAT);
        setBind(Keyboard.KEY_G);
    }

    @Listener
    public void onPacket(final PacketEvent event)
    {
        if (event.getType() == PacketEvent.Type.INCOMING || !(event.getPacket() instanceof CPacketUseEntity) || ((CPacketUseEntity) event.getPacket()).getAction() != CPacketUseEntity.Action.ATTACK || !mc.player.onGround || !this.crystals.getValue() && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world) instanceof EntityEnderCrystal)
            return;

        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, false));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
    }
}
