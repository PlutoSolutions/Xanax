package cat.yoink.xanax.main.module.modules.client;

import cat.yoink.xanax.main.discord.Discord;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;

public final class DiscordRPC extends Module
{
    public DiscordRPC()
    {
        super("DiscordRPC", Category.CLIENT);
    }

    @Override
    protected void onEnable()
    {
        Discord.INSTANCE.start();
    }

    @Override
    protected void onDisable()
    {
        Discord.INSTANCE.stop();
    }
}
