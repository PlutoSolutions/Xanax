// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.handling;

import cat.yoink.xanax.client.command.CommandManager;
import net.minecraftforge.client.event.ClientChatEvent;
import cat.yoink.xanax.client.component.Component;
import cat.yoink.xanax.client.component.ComponentManager;
import cat.yoink.xanax.client.gui.hud.HUDEditor;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.util.ResourceLocation;
import cat.yoink.xanax.client.gui.click.ClickGUI;
import net.minecraft.client.gui.GuiScreen;
import cat.yoink.xanax.client.gui.main.MainMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import cat.yoink.xanax.client.module.Module;
import cat.yoink.xanax.client.module.ModuleManager;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import cat.yoink.xanax.client.MinecraftInstance;

public class EventHandler implements MinecraftInstance
{
    private boolean isInit;
    
    @SubscribeEvent
    public void onInputKeyInput(final InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState() || Keyboard.getEventKey() == 0) {
            return;
        }
        ModuleManager.INSTANCE.getModules().stream().filter(module -> module.getBind() == Keyboard.getEventKey()).forEach(Module::toggle);
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (EventHandler.mc.player == null || EventHandler.mc.world == null) {
            this.isInit = false;
            return;
        }
        try {
            if (!this.isInit) {
                new ConnectionHandler();
                this.isInit = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onGuiOpen(final GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu && ModuleManager.INSTANCE.getModule("MainMenu").isEnabled()) {
            event.setGui((GuiScreen)new MainMenu());
        }
        if (!(event.getGui() instanceof ClickGUI) && event.getGui() != null) {
            return;
        }
        if (!EventHandler.mc.entityRenderer.isShaderActive() && event.getGui() != null && ModuleManager.INSTANCE.getSetting("ClickGUI", "Blur").getBoolean()) {
            EventHandler.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
        else if ((EventHandler.mc.entityRenderer.isShaderActive() && event.getGui() == null) || ModuleManager.INSTANCE.getSetting("ClickGUI", "Blur").getBoolean()) {
            EventHandler.mc.entityRenderer.stopUseShader();
        }
    }
    
    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent event) {
        if (EventHandler.mc.player != null && EventHandler.mc.world != null && !(EventHandler.mc.currentScreen instanceof HUDEditor) && event.getType().equals((Object)RenderGameOverlayEvent.ElementType.TEXT)) {
            ComponentManager.INSTANCE.getComponents().stream().filter(component -> ModuleManager.INSTANCE.getModule(component.getName()).isEnabled()).forEach(Component::render);
        }
    }
    
    @SubscribeEvent
    public void onClientChat(final ClientChatEvent event) {
        if (EventHandler.mc.player == null || EventHandler.mc.world == null) {
            return;
        }
        if (event.getMessage().startsWith(CommandManager.INSTANCE.getPrefix())) {
            event.setCanceled(true);
            EventHandler.mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
            CommandManager.INSTANCE.runCommand(event.getMessage());
        }
    }
}
