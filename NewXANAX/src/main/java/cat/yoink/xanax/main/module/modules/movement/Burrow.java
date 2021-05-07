package cat.yoink.xanax.main.module.modules.movement;

import cat.yoink.xanax.main.event.events.TickEvent;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.setting.BooleanSetting;
import cat.yoink.xanax.main.setting.EnumSetting;
import cat.yoink.xanax.main.setting.NumberSetting;
import cat.yoink.xanax.main.util.ChatUtil;
import cat.yoink.xanax.main.util.InventoryUtil;
import cat.yoink.xanax.main.util.WorldUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import cat.yoink.eventmanager.Listener;

public final class Burrow extends Module
{
    private final EnumSetting mode = addSetting(new EnumSetting("LagBackMode", "Jump", "Jump", "HighJump", "TPBack", "TP", "Packet"));
    private final NumberSetting height = addSetting(new NumberSetting("Height", 1.2, 1, 1.3, 0.01));
    private final NumberSetting strength = addSetting(new NumberSetting("Strength", 1, 0.05, 2, 0.05));
    private final BooleanSetting announce = addSetting(new BooleanSetting("Announce", false));
    private BlockPos originalPos;

    public Burrow()
    {
        super("Burrow", Category.MOVEMENT);
    }

    @Override
    protected void onEnable()
    {
        this.originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) || WorldUtil.isInterceptedByOther(this.originalPos) || InventoryUtil.getHotbarSlot(Blocks.OBSIDIAN) == -1)
        {
            disable();
            return;
        }

        if (this.announce.getValue()) ChatUtil.sendPrivateMessage("Enabled Burrow");

        mc.player.jump();
    }

    @Listener
    public void onTickClientTick(final TickEvent event)
    {
        if (mc.player.posY > this.originalPos.getY() + this.height.getValue())
        {
            final int oldSlot = mc.player.inventory.currentItem;

            mc.player.inventory.currentItem = InventoryUtil.getHotbarSlot(Blocks.OBSIDIAN);

            WorldUtil.placeBlock(this.originalPos);

            mc.player.inventory.currentItem = oldSlot;

            if (this.mode.is("Jump")) mc.player.jump();
            else if (this.mode.is("HighJump")) mc.player.motionY = strength.getValue();
            else if (this.mode.is("TPBack")) mc.player.posY = this.originalPos.getY();
            else if (this.mode.is("TP")) mc.player.setPosition(0, -1, 0);
            else if (this.mode.is("Packet")) mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, this.originalPos.getY(), mc.player.posZ, true));

            disable();
        }
    }
}
