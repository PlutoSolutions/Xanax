package cat.yoink.xanax.mixin;

import cat.yoink.xanax.core.Main;
import cat.yoink.xanax.main.event.events.DamageBlockEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public abstract class PlayerControllerMPPatch
{
    @Inject(method = "onPlayerDamageBlock", at = @At("INVOKE"), cancellable = true)
    public void onPlayerDamageBlock(final BlockPos posBlock, final EnumFacing directionFacing, final CallbackInfoReturnable<Boolean> cir)
    {
        final DamageBlockEvent event = new DamageBlockEvent(posBlock, directionFacing);
        if (Main.EVENT_BUS.dispatch(event).isCancelled()) cir.cancel();
    }
}
