package cat.yoink.xanax.mixin;

import cat.yoink.xanax.core.Main;
import cat.yoink.xanax.main.MinecraftInstance;
import cat.yoink.xanax.main.event.events.EntitiesInAABBEvent;
import com.google.common.base.Predicate;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererPatch implements MinecraftInstance
{
    @SuppressWarnings("ALL")
    @Redirect(method = "getMouseOver", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List<Entity> getEntitiesInAABBexcluding(final WorldClient worldClient, final Entity entityIn, final AxisAlignedBB boundingBox, final Predicate<? super Entity> predicate)
    {
        final EntitiesInAABBEvent event = new EntitiesInAABBEvent(worldClient, entityIn, boundingBox, predicate);

        if (Main.EVENT_BUS.dispatch(event).isCancelled()) return new ArrayList<>();
        else return worldClient.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }
}
