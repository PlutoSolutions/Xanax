package cat.yoink.xanax.main.event.events;

import cat.yoink.xanax.main.event.EventBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public final class DamageBlockEvent extends EventBase
{
    private final BlockPos pos;
    private final EnumFacing face;

    public DamageBlockEvent(final BlockPos pos, final EnumFacing face)
    {
        this.pos = pos;
        this.face = face;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    public EnumFacing getFace()
    {
        return this.face;
    }
}
