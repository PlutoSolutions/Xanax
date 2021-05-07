// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.misc;

import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import cat.yoink.xanax.client.util.PlayerUtil;
import net.minecraft.init.Items;
import cat.yoink.xanax.client.util.ChatUtil;
import cat.yoink.xanax.client.friend.FriendManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.EnumSetting;
import cat.yoink.xanax.client.module.Module;

public class MiddleClick extends Module
{
    private final EnumSetting mode;
    
    public MiddleClick() {
        super("MiddleClick", Category.MISC);
        this.mode = this.newSetting(new EnumSetting("Mode", "Smart", new String[] { "Smart", "Friend", "Pearl" }));
    }
    
    @SubscribeEvent
    public void onMiddleClick(final InputEvent.MouseInputEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (Mouse.getEventButtonState() && Mouse.getEventButton() == 2) {
            if (this.mode.getValue().equals("Friend") && MiddleClick.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
                this.clickFriend();
            }
            else if (this.mode.getValue().equals("Pearl")) {
                this.throwPearl();
            }
            else if (this.mode.getValue().equals("Smart")) {
                if (MiddleClick.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
                    this.clickFriend();
                }
                else {
                    this.throwPearl();
                }
            }
        }
    }
    
    private void clickFriend() {
        final String name = MiddleClick.mc.objectMouseOver.entityHit.getName();
        if (FriendManager.INSTANCE.isFriend(name)) {
            FriendManager.INSTANCE.removeFriend(name);
            ChatUtil.sendClientMessage("Removed " + name + " as a friend");
        }
        else {
            FriendManager.INSTANCE.addFriend(name);
            ChatUtil.sendClientMessage("Added " + name + " as a friend");
        }
    }
    
    private void throwPearl() {
        final int pearlSlot = PlayerUtil.getHotbarSlot(Items.ENDER_PEARL);
        if (pearlSlot != -1) {
            final int slot = MiddleClick.mc.player.inventory.currentItem;
            MiddleClick.mc.player.inventory.currentItem = pearlSlot;
            MiddleClick.mc.playerController.processRightClick((EntityPlayer)MiddleClick.mc.player, (World)MiddleClick.mc.world, EnumHand.MAIN_HAND);
            MiddleClick.mc.player.inventory.currentItem = slot;
        }
    }
}
