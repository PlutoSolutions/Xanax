package cat.yoink.xanax.main.event;

public abstract class EventBase
{
    private boolean cancelled;

    public boolean isCancelled()
    {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled)
    {
        this.cancelled = cancelled;
    }
}
