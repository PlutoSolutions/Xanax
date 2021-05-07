// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.external.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import cat.yoink.xanax.client.event.WalkEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import cat.yoink.xanax.client.event.MoveEvent;
import net.minecraft.entity.MoverType;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.entity.AbstractClientPlayer;

@Mixin({ EntityPlayerSP.class })
public class EntityPlayerSPMixin extends AbstractClientPlayer
{
    public EntityPlayerSPMixin(final World worldIn, final GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }
    
    @Redirect(method = { "move" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(final AbstractClientPlayer player, final MoverType moverType, final double x, final double y, final double z) {
        final MoveEvent event = new MoveEvent(moverType, x, y, z);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            super.move(event.getType(), event.getX(), event.getY(), event.getZ());
        }
    }
    
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("HEAD") }, cancellable = true)
    public void onUpdateWalkingPlayer(final CallbackInfo ci) {
        final WalkEvent event = new WalkEvent();
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
