// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.external.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import cat.yoink.xanax.client.module.ModuleManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GameSettings.class })
public class GameSettingsMixin
{
    @Inject(method = { "setOptionValue" }, at = { @At("HEAD") }, cancellable = true)
    public void onSetOptionsValue(final GameSettings.Options settingsOption, final int value, final CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModule("AntiNarrator").isEnabled() && settingsOption.equals((Object)GameSettings.Options.NARRATOR)) {
            ci.cancel();
        }
    }
}
