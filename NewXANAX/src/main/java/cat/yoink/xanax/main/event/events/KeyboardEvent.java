package cat.yoink.xanax.main.event.events;

import cat.yoink.xanax.main.event.EventBase;

public final class KeyboardEvent extends EventBase
{
    private final int key;
    private final boolean state;

    public KeyboardEvent(final int key, final boolean state)
    {
        this.key = key;
        this.state = state;
    }

    public int getKey()
    {
        return this.key;
    }

    public boolean isState()
    {
        return this.state;
    }
}
