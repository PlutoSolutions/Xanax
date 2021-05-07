// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.external.mixin.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import cat.yoink.xanax.client.module.ModuleManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ PlayerControllerMP.class })
public class PlayerControllerMPMixin
{
    @Inject(method = { "resetBlockRemoving" }, at = { @At("HEAD") }, cancellable = true)
    private void resetBlock(final CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModule("NoMineReset").isEnabled()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "getBlockReachDistance" }, at = { @At("RETURN") }, cancellable = true)
    private void getReachDistanceHook(final CallbackInfoReturnable<Float> distance) {
        if (ModuleManager.INSTANCE.getModule("Reach").isEnabled()) {
            distance.setReturnValue((float)ModuleManager.INSTANCE.getSetting("Reach", "Distance").getDouble());
        }
    }
}
