// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.external.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemPickaxe;
import cat.yoink.xanax.client.module.ModuleManager;
import java.util.List;
import com.google.common.base.Predicate;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityRenderer.class })
public class EntityRendererMixin
{
    @Redirect(method = { "getMouseOver" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List<Entity> getEntitiesInAABBexcluding(final WorldClient worldClient, final Entity entityIn, final AxisAlignedBB boundingBox, final Predicate<? super Entity> predicate) {
        return ModuleManager.INSTANCE.getModule("NoEntityTrace").isEnabled() ? (ModuleManager.INSTANCE.getSetting("NoEntityTrace", "PickaxeOnly").getBoolean() ? ((Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) ? new ArrayList<Entity>() : worldClient.getEntitiesInAABBexcluding(entityIn, boundingBox, (Predicate)predicate)) : new ArrayList<Entity>()) : worldClient.getEntitiesInAABBexcluding(entityIn, boundingBox, (Predicate)predicate);
    }
}
