// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client;

import cat.yoink.xanax.client.config.Config;
import cat.yoink.xanax.client.handling.EventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
		modid = "xanax",
		name = "Xanax",
		version = "1"
)
public class Client
{
    //INSTANCE;
	public final static Client INSTANCE = new Client( );
    
    private final Logger logger;
    
    public Client() {
        this.logger = LogManager.getLogger("XANAX");
    }
    
    @net.minecraftforge.fml.common.Mod.EventHandler
    public void initialize( FMLInitializationEvent event ) {
        this.logger.info("Starting initialization...");
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        Runtime.getRuntime().addShutdownHook(new Config());
        Config.loadConfig();
        this.logger.info("Initialization complete!");
    }
}
