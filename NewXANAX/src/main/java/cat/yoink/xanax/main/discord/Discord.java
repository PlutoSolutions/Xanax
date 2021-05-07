package cat.yoink.xanax.main.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public enum Discord
{
    INSTANCE;

    private final DiscordRichPresence presence;
    private final DiscordRPC rpc;
    private final String id;
    private boolean connected;

    Discord()
    {
        this.presence = new DiscordRichPresence();
        this.rpc = DiscordRPC.INSTANCE;
        this.connected = false;
        this.id = "778887499362861067";
    }

    public void start()
    {
        if (this.connected) return;
        this.presence.startTimestamp = System.currentTimeMillis() / 1000L;

        final DiscordEventHandlers handlers = new DiscordEventHandlers();

        this.rpc.Discord_Initialize(this.id, handlers, true, "");
        this.rpc.Discord_UpdatePresence(this.presence);

        this.presence.details = "strong hack";
        this.presence.largeImageKey = "yum";
        this.rpc.Discord_UpdatePresence(this.presence);

        this.connected = true;
        new Thread(this::startThread).start();
    }

    public void stop()
    {
        if (!this.connected) return;
        this.connected = false;
        this.rpc.Discord_Shutdown();
    }

    @SuppressWarnings("ALL")
    private void startThread()
    {
        while (connected && !Thread.currentThread().isInterrupted())
        {
            try
            {
                Thread.sleep(3000);
            }
            catch (final InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
