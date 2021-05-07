// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module;

import java.util.Iterator;
import cat.yoink.xanax.client.setting.Setting;
import java.util.stream.Collector;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import java.util.Collection;
import java.util.Arrays;
import cat.yoink.xanax.client.module.modules.client.MainMenu;
import cat.yoink.xanax.client.module.modules.misc.MiddleClick;
import cat.yoink.xanax.client.module.modules.render.Skeleton;
import cat.yoink.xanax.client.module.modules.render.FogColor;
import cat.yoink.xanax.client.module.modules.movement.IceSpeed;
import cat.yoink.xanax.client.module.modules.combat.Quiver;
import cat.yoink.xanax.client.module.modules.movement.Burrow;
import cat.yoink.xanax.client.module.modules.misc.AutoGG;
import cat.yoink.xanax.client.module.modules.world.FastUse;
import cat.yoink.xanax.client.module.modules.world.FakePlayer;
import cat.yoink.xanax.client.module.modules.combat.AutoCrystal;
import cat.yoink.xanax.client.module.modules.world.NoSwing;
import cat.yoink.xanax.client.module.modules.movement.AntiWeb;
import cat.yoink.xanax.client.module.modules.movement.AntiVoid;
import cat.yoink.xanax.client.module.modules.movement.ReverseStep;
import cat.yoink.xanax.client.module.modules.combat.AutoArmor;
import cat.yoink.xanax.client.module.modules.movement.Velocity;
import cat.yoink.xanax.client.module.modules.combat.Reach;
import cat.yoink.xanax.client.module.modules.world.NoMineReset;
import cat.yoink.xanax.client.module.modules.render.Fullbright;
import cat.yoink.xanax.client.module.modules.misc.AutoReplenish;
import cat.yoink.xanax.client.module.modules.world.BuildHeight;
import cat.yoink.xanax.client.module.modules.combat.AntiTrap;
import cat.yoink.xanax.client.module.modules.component.Arraylist;
import cat.yoink.xanax.client.module.modules.world.NoEntityTrace;
import cat.yoink.xanax.client.module.modules.misc.ChatSuffix;
import cat.yoink.xanax.client.module.modules.misc.AntiNarrator;
import cat.yoink.xanax.client.module.modules.movement.Flight;
import cat.yoink.xanax.client.module.modules.combat.Offhand;
import cat.yoink.xanax.client.module.modules.combat.AutoTrap;
import cat.yoink.xanax.client.module.modules.component.Watermark;
import cat.yoink.xanax.client.module.modules.client.HUDEditor;
import cat.yoink.xanax.client.module.modules.movement.Speed;
import cat.yoink.xanax.client.module.modules.movement.LongJump;
import cat.yoink.xanax.client.module.modules.combat.Aura;
import cat.yoink.xanax.client.module.modules.world.PacketMine;
import cat.yoink.xanax.client.module.modules.combat.Surround;
import cat.yoink.xanax.client.module.modules.combat.AutoSelfTrap;
import cat.yoink.xanax.client.module.modules.render.ESP;
import cat.yoink.xanax.client.module.modules.render.HoleESP;
import cat.yoink.xanax.client.module.modules.client.Notifications;
import cat.yoink.xanax.client.module.modules.combat.Criticals;
import cat.yoink.xanax.client.module.modules.movement.Timer;
import cat.yoink.xanax.client.module.modules.client.ClickGUI;
import java.util.ArrayList;
import java.util.List;

public enum ModuleManager
{
    INSTANCE;
    
    private final List<Module> modules;
    
    private ModuleManager() {
        this.modules = new ArrayList<Module>();
        this.addModules(new ClickGUI(), new Timer(), new Criticals(), new Notifications(), new HoleESP(), new ESP(), new AutoSelfTrap(), new Surround(), new PacketMine(), new Aura(), new LongJump(), new Speed(), new HUDEditor(), new Watermark(), new AutoTrap(), new Offhand(), new Flight(), new AntiNarrator(), new ChatSuffix(), new NoEntityTrace(), new Arraylist(), new AntiTrap(), new BuildHeight(), new AutoReplenish(), new Fullbright(), new NoMineReset(), new Reach(), new Velocity(), new AutoArmor(), new ReverseStep(), new AntiVoid(), new AntiWeb(), new NoSwing(), new AutoCrystal(), new FakePlayer(), new FastUse(), new AutoGG(), new Burrow(), new Quiver(), new IceSpeed(), new FogColor(), new Skeleton(), new MiddleClick(), new MainMenu());
    }
    
    private void addModules(final Module... modules) {
        this.modules.addAll(Arrays.asList(modules));
        this.getModules().sort(Comparator.comparing((Function<? super Module, ? extends Comparable>)Module::getName));
    }
    
    public List<Module> getModules() {
        return this.modules;
    }
    
    public List<Module> getEnabledModules() {
        return this.modules.stream().filter(Module::isEnabled).collect(Collectors.toCollection(ArrayList::new));
    }
    
    public Setting getSetting(final String moduleName, final String settingName) {
        return this.modules.stream().filter(m -> m.getName().equalsIgnoreCase(moduleName)).findAny().orElse(null).getSettings().stream().filter(s -> s.getName().equalsIgnoreCase(settingName)).findAny().orElse(null);
    }
    
    public List<Module> getModules(final Category category) {
        return this.modules.stream().filter(module -> module.getCategory().equals(category)).collect(Collectors.toList());
    }
    
    public Module getModule(final String name) {
        for (final Module module : this.modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }
}
