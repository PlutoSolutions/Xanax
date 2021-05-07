package cat.yoink.xanax.core;

import cat.yoink.eventmanager.EventManager;
import cat.yoink.xanax.main.MinecraftInstance;
import cat.yoink.xanax.main.config.ConfigManager;
import cat.yoink.xanax.main.event.EventHandler;
import net.minecraftforge.common.MinecraftForge;

public enum Main implements MinecraftInstance
{
    INSTANCE;

    public static final EventManager EVENT_BUS = new EventManager();

    public void startup()
    {
        ConfigManager.loadConfig();

        Runtime.getRuntime().addShutdownHook(new Thread(ConfigManager::saveConfig));
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}