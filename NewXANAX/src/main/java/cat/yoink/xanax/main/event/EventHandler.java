package cat.yoink.xanax.main.event;

import cat.yoink.xanax.core.Main;
import cat.yoink.xanax.main.MinecraftInstance;
import cat.yoink.xanax.main.event.events.*;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.module.ModuleManager;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import static net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public final class EventHandler implements MinecraftInstance
{
    @SubscribeEvent
    public void onInputKeyInput(final InputEvent.KeyInputEvent event)
    {
        Main.EVENT_BUS.dispatch(new KeyboardEvent(Keyboard.getEventKey(), Keyboard.getEventKeyState()));

        if (Keyboard.getEventKeyState() && Keyboard.getEventKey() != Keyboard.KEY_NONE)
            ModuleManager.INSTANCE.getModules().stream().filter(module -> module.getBind() == Keyboard.getEventKey()).forEach(Module::toggle);
    }

    @SubscribeEvent
    public void onTickClientTick(final ClientTickEvent event)
    {
        if (isSafe()) Main.EVENT_BUS.dispatch(new TickEvent());
    }

    @SubscribeEvent
    public void onAttackEntity(final net.minecraftforge.event.entity.player.AttackEntityEvent event)
    {
        if (isSafe() && Main.EVENT_BUS.dispatch(new AttackEntityEvent(event.getEntityPlayer())).isCancelled())
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent event)
    {
        if (!event.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) return;
        if (isSafe() && Main.EVENT_BUS.dispatch(new Render2DEvent()).isCancelled())
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onPlayerSPPushOutOfBlocks(final PlayerSPPushOutOfBlocksEvent event)
    {
        if (isSafe() && Main.EVENT_BUS.dispatch(new BlockPushEvent()).isCancelled())
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event)
    {
        if (isSafe()) Main.EVENT_BUS.dispatch(new Render3DEvent());
    }

    @SubscribeEvent
    public void onInputMouseInput(final InputEvent.MouseInputEvent event)
    {
        if (isSafe()) Main.EVENT_BUS.dispatch(new ClickEvent());
    }

    protected final boolean isSafe()
    {
        return mc.player != null && mc.world != null;
    }
}
