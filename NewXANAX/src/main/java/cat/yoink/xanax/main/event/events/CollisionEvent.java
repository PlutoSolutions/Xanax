package cat.yoink.xanax.main.event.events;

import cat.yoink.xanax.main.event.EventBase;
import net.minecraft.entity.Entity;

public final class CollisionEvent extends EventBase
{
    private final Entity entity;

    public CollisionEvent(final Entity entity)
    {
        this.entity = entity;
    }

    public Entity getEntity()
    {
        return this.entity;
    }
}
