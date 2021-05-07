package cat.yoink.xanax.main.event.events;

import cat.yoink.xanax.main.event.EventBase;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.function.Predicate;

public final class EntitiesInAABBEvent extends EventBase
{
    private final WorldClient worldClient;
    private final Entity entityIn;
    private final AxisAlignedBB boundingBox;
    private final Predicate<? super Entity> predicate;

    public EntitiesInAABBEvent(final WorldClient worldClient, final Entity entityIn, final AxisAlignedBB boundingBox, final Predicate<? super Entity> predicate)
    {
        this.worldClient = worldClient;
        this.entityIn = entityIn;
        this.boundingBox = boundingBox;
        this.predicate = predicate;
    }

    public WorldClient getWorldClient()
    {
        return this.worldClient;
    }

    public Entity getEntityIn()
    {
        return this.entityIn;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    public Predicate<? super Entity> getPredicate()
    {
        return this.predicate;
    }
}
