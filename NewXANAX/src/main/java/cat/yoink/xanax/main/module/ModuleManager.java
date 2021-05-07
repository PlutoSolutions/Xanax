package cat.yoink.xanax.main.module;

import cat.yoink.xanax.main.module.modules.client.ClickGUI;
import cat.yoink.xanax.main.module.modules.client.DiscordRPC;
import cat.yoink.xanax.main.module.modules.combat.Criticals;
import cat.yoink.xanax.main.module.modules.combat.Surround;
import cat.yoink.xanax.main.module.modules.misc.ChatSuffix;
import cat.yoink.xanax.main.module.modules.misc.MiddleClick;
import cat.yoink.xanax.main.module.modules.misc.Replenish;
import cat.yoink.xanax.main.module.modules.misc.Swing;
import cat.yoink.xanax.main.module.modules.movement.Burrow;
import cat.yoink.xanax.main.module.modules.movement.FastFall;
import cat.yoink.xanax.main.module.modules.movement.Velocity;
import cat.yoink.xanax.main.module.modules.render.*;
import cat.yoink.xanax.main.module.modules.world.BuildHeight;
import cat.yoink.xanax.main.module.modules.world.GhostEntity;
import cat.yoink.xanax.main.module.modules.world.PacketMine;
import cat.yoink.xanax.main.setting.BooleanSetting;
import cat.yoink.xanax.main.setting.EnumSetting;
import cat.yoink.xanax.main.setting.NumberSetting;
import cat.yoink.xanax.main.setting.Setting;

import java.util.*;

public enum ModuleManager
{
    INSTANCE;

    private final ArrayList<Module> modules = new ArrayList<>();

    ModuleManager()
    {
        addModules(new Criticals(),
                new ClickGUI(),
                new Swing(),
                new Animations(),
                new Surround(),
                new Burrow(),
                new FastFall(),
                new Velocity(),
                new HitMarkers(),
                new FeetHighlight(),
                new PacketMine(),
                new ChatSuffix(),
                new HoleESP(),
                new Replenish(),
                new DiscordRPC(),
                new BlockHighlight(),
                new GhostEntity(),
                new MiddleClick(),
                new BuildHeight(),
                new BurrowESP());
    }

    private void addModules(final Module... modules)
    {
        this.modules.addAll(Arrays.asList(modules));
        this.modules.sort(Comparator.comparing(Module::getName));
    }

    public Setting getSetting(final String moduleName, final String settingName)
    {
        return Objects.requireNonNull(getModules().stream().filter(m -> m.getName().equalsIgnoreCase(moduleName)).findAny().orElse(null)).getSettings().stream().filter(s -> s.getName().equalsIgnoreCase(settingName)).findAny().orElse(null);
    }

    public ArrayList<Module> getModules()
    {
        return this.modules;
    }

    public Module getModule(final String name)
    {
        return this.modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public List<String> getConfig()
    {
        final List<String> config = new ArrayList<>();

        this.modules.forEach(module -> module.getSettings().forEach(setting -> {
            final StringBuilder builder = new StringBuilder(module.getName() + ":" + setting.getName() + ":");
            if (setting instanceof BooleanSetting) builder.append(((BooleanSetting) setting).getValue());
            else if (setting instanceof NumberSetting) builder.append(((NumberSetting) setting).getValue());
            else if (setting instanceof EnumSetting) builder.append(((EnumSetting) setting).getIndex());
            config.add(builder.toString());
        }));

        return config;
    }
}
