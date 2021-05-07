package cat.yoink.xanax.main.config;

import cat.yoink.xanax.core.Main;
import cat.yoink.xanax.main.MinecraftInstance;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.module.ModuleManager;
import cat.yoink.xanax.main.setting.BooleanSetting;
import cat.yoink.xanax.main.setting.EnumSetting;
import cat.yoink.xanax.main.setting.NumberSetting;
import cat.yoink.xanax.main.setting.Setting;
import cat.yoink.xanax.main.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum ConfigManager implements MinecraftInstance
{
    INSTANCE;

    private final File folder = new File(mc.gameDir + File.separator + "xanax");

    public static void loadConfig()
    {
        ConfigManager.INSTANCE.loadConfiguration();
    }

    public static void saveConfig()
    {
        ConfigManager.INSTANCE.saveConfiguration();
    }

    private void loadConfiguration()
    {
        if (!this.folder.exists() && !this.folder.mkdirs()) return;

        for (final String s2 : getFile("ToggledModules.txt"))
        {
            try
            {
                ModuleManager.INSTANCE.getModule(s2).setEnabled(true);
                Main.EVENT_BUS.addSubscriber(ModuleManager.INSTANCE.getModule(s2));
            }
            catch (final Exception ignored)
            {
            }
        }

        for (final String s1 : getFile("Binds.txt"))
        {
            try
            {
                final Module module = ModuleManager.INSTANCE.getModule(s1.split(":")[0]);
                if (module != null) module.setBind(Integer.parseInt(s1.split(":")[1]));
            }
            catch (final Exception ignored)
            {
            }
        }

        for (final String s : getFile("Settings.txt"))
        {
            try
            {
                final Module module = ModuleManager.INSTANCE.getModule(s.split(":")[0]);
                if (module == null) continue;
                final Setting setting = module.getSetting(s.split(":")[1]);
                if (setting == null) continue;
                if (setting instanceof BooleanSetting)
                    ((BooleanSetting) setting).setValue(Boolean.parseBoolean(s.split(":")[2]));
                if (setting instanceof NumberSetting)
                    ((NumberSetting) setting).setValue(Double.parseDouble(s.split(":")[2]));
                if (setting instanceof EnumSetting) ((EnumSetting) setting).setIndex(Integer.parseInt(s.split(":")[2]));
            }
            catch (final Exception ignored)
            {
            }
        }
    }

    private void saveConfiguration()
    {
        saveFile("ToggledModules.txt", ModuleManager.INSTANCE.getModules().stream().filter(Module::isEnabled).filter(module -> !module.getName().equalsIgnoreCase("ClickGUI")).map(Module::getName).collect(Collectors.toList()));
        saveFile("Binds.txt", ModuleManager.INSTANCE.getModules().stream().map(module -> module.getName() + ":" + module.getBind()).collect(Collectors.toList()));
        saveFile("Settings.txt", ModuleManager.INSTANCE.getConfig());
    }

    private void saveFile(final String name, final List<String> lines)
    {
        try
        {
            FileUtil.saveFile(new File(this.folder.getAbsolutePath(), name), lines);
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

    private List<String> getFile(final String name)
    {
        try
        {
            return FileUtil.loadFile(new File(this.folder.getAbsolutePath(), name));
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
