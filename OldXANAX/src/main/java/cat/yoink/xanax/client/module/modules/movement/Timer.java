// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.movement;

import java.lang.reflect.Field;
import cat.yoink.xanax.client.handling.Mapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import cat.yoink.xanax.client.module.Category;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.setting.EnumSetting;
import cat.yoink.xanax.client.module.Module;

public class Timer extends Module
{
    private final EnumSetting mode;
    private final NumberSetting speedUsual;
    private final NumberSetting fastUsual;
    private final NumberSetting tickToFast;
    private final NumberSetting tickToNoFast;
    private int tickWait;
    
    public Timer() {
        super("Timer", Category.MOVEMENT);
        this.mode = this.newSetting(new EnumSetting("Mode", "Normal", new String[] { "Normal", "Switch" }));
        this.speedUsual = this.newSetting(new NumberSetting("Speed", 4.0, 0.1, 20.0, 0.1));
        this.fastUsual = this.newSetting(new NumberSetting("FastSpeed", 10.0, 1.0, 1000.0, 1.0));
        this.tickToFast = this.newSetting(new NumberSetting("TickToFast", 4.0, 0.0, 20.0, 1.0));
        this.tickToNoFast = this.newSetting(new NumberSetting("TickToDisableFast", 7.0, 0.1, 20.0, 1.0));
        this.tickWait = 0;
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.mode.getValue().equalsIgnoreCase("Normal")) {
            this.setTimer((float)this.speedUsual.getValue());
        }
        else {
            if (this.tickWait == this.tickToFast.getValue()) {
                this.setTimer((float)this.fastUsual.getValue());
            }
            if (this.tickWait >= this.tickToNoFast.getValue()) {
                this.tickWait = 0;
                this.setTimer((float)this.speedUsual.getValue());
            }
            ++this.tickWait;
        }
    }
    
    @Override
    public void onDisable() {
        if (this.nullCheck()) {
            return;
        }
        this.setTimer(1.0f);
    }
    
    private void setTimer(final float value) {
        try {
            final Field timer = Minecraft.class.getDeclaredField(Mapping.timer);
            timer.setAccessible(true);
            final Field tickLength = net.minecraft.util.Timer.class.getDeclaredField(Mapping.tickLength);
            tickLength.setAccessible(true);
            tickLength.setFloat(timer.get(Timer.mc), 50.0f / value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
