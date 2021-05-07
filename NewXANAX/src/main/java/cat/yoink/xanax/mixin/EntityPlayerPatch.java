package cat.yoink.xanax.mixin;

import cat.yoink.xanax.core.Main;
import cat.yoink.xanax.main.event.events.CollisionEvent;
import cat.yoink.xanax.main.event.events.WaterPushEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerPatch
{
    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    public void applyEntityCollision(final Entity entity, final CallbackInfo ci)
    {
        final CollisionEvent event = new CollisionEvent(entity);
        Main.EVENT_BUS.dispatch(event);

        if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "isPushedByWater", at = @At("HEAD"), cancellable = true)
    public void isPushedByWater(final CallbackInfoReturnable<Boolean> cir)
    {
        final WaterPushEvent event = new WaterPushEvent();
        Main.EVENT_BUS.dispatch(event);

        if (event.isCancelled()) cir.setReturnValue(false);
    }
}
